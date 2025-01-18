import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Dish, Order, OrderFilter} from "../model";
import {Observable} from "rxjs";
import {PageableResponse} from "../pageable-response.model";

@Injectable({
  providedIn: 'root'
})
export class DishService {
  private readonly apiUrl = environment.postApi + 'dishes'

  constructor(private http: HttpClient) { }

  getDishes(): Observable<Dish[]> {

    const headers = this.getHeaders();

    return this.http.get<Dish[]>(`${this.apiUrl}`,  { headers: headers });
  }



  private getHeaders() {
    const jwt = localStorage.getItem('jwt');
    return  new HttpHeaders().set('Authorization', `Bearer ${jwt}`);
  }
}
