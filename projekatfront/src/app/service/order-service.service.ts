import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {Order, OrderFilter} from "../model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {PageableResponse} from "../pageable-response.model";

@Injectable({
  providedIn: 'root'
})
export class OrderServiceService {
  private ordersSubject = new BehaviorSubject<PageableResponse<Order[]>>({
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
  });
  public orders$ = this.ordersSubject.asObservable();

  private readonly apiUrl = environment.postApi + 'orders'

  constructor(private http: HttpClient) { }

  getOrders(page: number, size: number, filters: OrderFilter): Observable<PageableResponse<Order[]>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    const headers = this.getHeaders();
    console.log('API URL:', `${this.apiUrl}/search`);
    console.log('Filters:', filters);
    console.log('Headers', headers )

    const body: any = {};

    if (filters.from) {
      body.from = filters.from;
    }

    if (filters.to) {
      body.to = filters.to;
    }

    if (filters.email) {
      body.email = filters.email;
    }

    if (filters.statuses && filters.statuses.length > 0) {
      body.statuses = filters.statuses;
    }

    return this.http.post<PageableResponse<Order[]>>(`${this.apiUrl}/search`, body, { params, headers }).pipe(
      tap((orders) => {
        this.ordersSubject.next(orders);
      })
    );
  }

  cancelOrder(orderId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.put<void>(`${this.apiUrl}`, {'id':orderId},{ headers });
  }
  createOrder(dishes: string[]): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}`, { dishes : dishes }, {headers:this.getHeaders()}).pipe(
      tap(() => this.refreshOrders())
    );
  }
  scheduleOrder(scheduleDto: { dishes: string[], orderTime: string }): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/schedule`, scheduleDto, {headers:this.getHeaders()}).pipe(
      tap(() => this.refreshOrders())
    );
  }

  private getHeaders() {
    const jwt = localStorage.getItem('jwt');
    return  new HttpHeaders().set('Authorization', `Bearer ${jwt}`);
  }
  private refreshOrders(): void {
    const currentPage = this.ordersSubject.value.number;
    const currentSize = this.ordersSubject.value.size;
    const  orderf = {
      from: '',
      to: '',
      email: '',
      statuses: []
    };
    this.getOrders(currentPage, currentSize, orderf).subscribe();
  }
}
