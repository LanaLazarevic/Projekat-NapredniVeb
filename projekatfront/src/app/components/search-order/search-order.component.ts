import { Component } from '@angular/core';
import {Subscription} from "rxjs";
import {Order, OrderFilter} from "../../model";
import {OrderServiceService} from "../../service/order-service.service";
import {LoginService} from "../../service/loginservice.service";
import {PageableResponse} from "../../pageable-response.model";

@Component({
  selector: 'app-search-order',
  templateUrl: './search-order.component.html',
  styleUrls: ['./search-order.component.css']
})
export class SearchOrderComponent {
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
  totalOrders: number = 0;
  totalPages:number=0;
  private subscriptions: Subscription[] = [];
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
    if (this.orderFilter.from) {
      this.orderFilter.from = this.formatToLocalDateTime(new Date(this.orderFilter.from));
    }
    if (this.orderFilter.to) {
      this.orderFilter.to = this.formatToLocalDateTime(new Date(this.orderFilter.to));
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

