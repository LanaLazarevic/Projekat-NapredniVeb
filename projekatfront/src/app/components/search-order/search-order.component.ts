import {Component, OnInit} from '@angular/core';
import {Order, OrderFilter} from "../../model";
import {OrderServiceService} from "../../service/order-service.service";
import {LoginService} from "../../service/loginservice.service";
import {PageableResponse} from "../../pageable-response.model";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

@Component({
  selector: 'app-search-order',
  templateUrl: './search-order.component.html',
  styleUrls: ['./search-order.component.css']
})
export class SearchOrderComponent implements OnInit{
  stompClient: Client | null = null;
  isWebSocketConnected: boolean = false;
  orders: Order[] = [];
  orderFilter: OrderFilter = {
    from: '',
    to: '',
    email: '',
    statuses: []
  };
  paginatedOrders: PageableResponse<Order[]> = {
    content: [],
    pageable: null,
    last: false,
    totalElements: 0,
    totalPages: 0,
    size: 10,
    number: 0,
    sort: null,
    first: true,
    numberOfElements: 0,
    empty: true,
  };
  pageIndex: number = 0;
  pageSize: number = 10;
  availableStatuses: string[] = ['ORDERED', 'PREPARING', 'IN_DELIVERY', 'DELIVERED', 'CANCELED', 'SCHEDULED'];

  constructor(private orderService: OrderServiceService, private loginservice: LoginService) {

  }

  ngOnInit(): void {
    this.orderService.orders$.subscribe({
      next: (paginatedOrders) => {
        this.paginatedOrders = paginatedOrders;
        this.orders = paginatedOrders.content;
      },
      error: (err) => {
        console.error('Error receiving orders:', err);
      }
    });

    this.getOrders();
    if(this.canTrack()) {
      this.onConnect(() => {
        console.log("connected");
        //this.sendMessage();
      });
    }
  }

  onConnect(callback: () => void) {
    const token = localStorage.getItem('jwt');
    const socket = new SockJS(`http://localhost:8080/ws?jwt=${token}`);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        this.isWebSocketConnected = true;
        this.stompClient?.subscribe('/topic/orders', (message) => {
          console.log("message ", message);
          this.getOrders();
        });
        callback();
      }
    });
    this.stompClient.activate();
  }


  getOrders(): void {
    this.orderService.getOrders(this.pageIndex, this.pageSize, this.orderFilter).subscribe({
      next: (paginatedOrders) => {
        this.paginatedOrders = paginatedOrders;
        this.orders = paginatedOrders.content;
      },
      error: (err) => {
        console.error('Error fetching orders:', err);
      }
    });
  }


  isAdmin():boolean | null{
    // @ts-ignore
    return localStorage.getItem("isAdmin").startsWith("true");
  }

  previousPage(): void {
    if (this.paginatedOrders.first) return;

    this.pageIndex = this.paginatedOrders.number - 1;
    this.getOrders();
  }

  nextPage(): void {
    if (this.paginatedOrders.last) return;

    this.pageIndex = this.paginatedOrders.number + 1;
    this.getOrders();
  }

  onStatusChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const value = checkbox.value;

    if (checkbox.checked) {
      this.orderFilter.statuses.push(value);
    } else {
      const index = this.orderFilter.statuses.indexOf(value);
      if (index > -1) {
        this.orderFilter.statuses.splice(index, 1);
      }
    }
  }

  onSubmit(): void {
    this.pageIndex = 0;

    if (!this.orderFilter.from || !this.orderFilter.to) {
      alert('Please enter both start and end date');
      return;
    }

    this.orderFilter.from = this.formatToLocalDateTime(new Date(this.orderFilter.from));
    this.orderFilter.to = this.formatToLocalDateTime(new Date(this.orderFilter.to));

    const fromDate = new Date(this.orderFilter.from);
    const toDate = new Date(this.orderFilter.to);

    if (fromDate > toDate) {
      alert('Start date must be before end date');
      return;
    }

    this.getOrders();

  }

  private formatToLocalDateTime(date: Date): string {
    if (!date) return '';

    const pad = (n: number) => (n < 10 ? '0' : '') + n;

    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}` +
      `T${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
  }

  canTrack(): boolean {
    return  this.loginservice.hasPermission("can_track_order");
  }

  canCancel(): boolean {
    return  this.loginservice.hasPermission("can_cancel_order");
  }

  cancelOrder(orderId: number): void {

      this.orderService.cancelOrder(orderId).subscribe({
        next: () => {
          this.getOrders();
        },
        error: (err) => {
          console.error('Error canceling order:', err);
          alert('Failed to cancel order');
        }
      });
  }

}

