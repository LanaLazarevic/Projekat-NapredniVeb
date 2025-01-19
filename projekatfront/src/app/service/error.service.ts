import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {PageableResponse} from "../pageable-response.model";
import {Error} from "../model";
@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  private readonly apiUrl = environment.postApi + 'errors'

  constructor(private http: HttpClient) { }

  getErrors(page: number, size: number): Observable<PageableResponse<Error[]>> {

    const headers = this.getHeaders();
    const params = {
      page: page.toString(),
      size: size.toString(),
    };
    return this.http.get<PageableResponse<Error[]>>(`${this.apiUrl}`,  { headers: headers,
      params, });
  }



  private getHeaders() {
    const jwt = localStorage.getItem('jwt');
    return  new HttpHeaders().set('Authorization', `Bearer ${jwt}`);
  }

}
