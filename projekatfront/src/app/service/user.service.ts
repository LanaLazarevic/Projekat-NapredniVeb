import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {User, UserCreate} from "../model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usersSubject = new BehaviorSubject<User[]>([]);
  public users$ = this.usersSubject.asObservable();

  private readonly apiUrl = environment.postApi + 'users'

  constructor(private httpClient: HttpClient) { }

  getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.apiUrl, {headers: this.getHeaders()}).pipe(
      tap(users => {
        this.usersSubject.next(users);
      }));
  }

  getUserByEmail(email:string): Observable<User> {
    return this.httpClient.get<User>(`${this.apiUrl}/${email}`, {headers: this.getHeaders()});
  }

  deleteUser(email: string): Observable<any> {
    return this.httpClient.put(`${this.apiUrl}/delete/${email}`, {}, {headers: this.getHeaders()});
  }

  updateUser(user: User): Observable<User> {
    const userPayload = {
      ...user,
      permissions: Array.from(user.permissions)
    };
    return this.httpClient.put<User>(`${this.apiUrl}`, userPayload, { headers: this.getHeaders() }).pipe(
      tap(() => this.refreshUsers())
    );
  }

  createUser(user:UserCreate): Observable<User>{
    const userPayload = {
      ...user,
      permissions: Array.from(user.permissions)
    };
    return this.httpClient.post<User>(`${this.apiUrl}`, userPayload, {headers:this.getHeaders()}).pipe(
      tap(() => this.refreshUsers())
    );
  }

  private getHeaders() {
    const jwt = localStorage.getItem('jwt');
    return  new HttpHeaders().set('Authorization', `Bearer ${jwt}`);
  }

  private refreshUsers(): void {
    this.getUsers().subscribe();
  }

}
