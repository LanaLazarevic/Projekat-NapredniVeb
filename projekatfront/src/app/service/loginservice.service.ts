import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginRequest, LoginResponse} from "../model";
import {BehaviorSubject, Observable} from "rxjs";
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private readonly apiUrl = environment.postApi + 'auth/login';
  private permissionsSubject = new BehaviorSubject(this.getUserPermissions());
  public permissions$ = this.permissionsSubject.asObservable();
  constructor(private httpClient: HttpClient) { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(this.apiUrl, {email:credentials.email, password:credentials.password},
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      });
  }

  setToken(jwt: string): void {
    localStorage.setItem("jwt", jwt)
    const decoded: any  = jwtDecode(jwt);
    this.setUserPermissions(decoded.permissions);
    localStorage.setItem("isAdmin", decoded.isAdmin);
  }

  setUserPermissions(permissions: string[]): void {
    localStorage.setItem('permissions', JSON.stringify(permissions));
    this.permissionsSubject.next(permissions);
  }

  getUserPermissions(): string[] {
    const permissions = localStorage.getItem('permissions');
    return permissions ? JSON.parse(permissions) : [];
  }

  getToken(): string {
    if(localStorage.getItem("jwt") == null)
      return  " "
    else
    { // @ts-ignore
      return localStorage.getItem("jwt")
    }
  }

  hasPermission(permission: string): boolean {
    const permissions = this.getUserPermissions();
    return permissions.includes(permission);
  }

  clearAuthData(): void {
    localStorage.removeItem('jwt');
    localStorage.removeItem('permissions');
    this.permissionsSubject.next(this.getUserPermissions());
  }

  isAuthorized(): boolean {
    const token = this.getToken();
    const permissions = this.getUserPermissions();
    return !token.match(" ") && (this.hasPermission("can_read_user") || this.hasPermission("can_search_order"));
  }
  isLogin(): boolean {
    const token = this.getToken();
    return !token.match(" ");
  }

}
