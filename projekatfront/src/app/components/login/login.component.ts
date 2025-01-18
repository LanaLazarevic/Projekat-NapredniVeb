import { Component } from '@angular/core';
import {LoginService} from "../../service/loginservice.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = "";
  password:string = "";
  constructor(private loginService: LoginService, private router: Router) {}
  onLogin() {
    const cleanCredentials = {
      email: this.email.trim(),
      password: this.password.trim()
    };
    if(this.loginService.isLogin())
      this.loginService.clearAuthData();
    this.loginService.login(cleanCredentials).subscribe({
      next: (response) => {
        this.loginService.setToken(response.jwt);
        this.router.navigate(['/users']);
      },
      error: (error) => {
        if (error.status === 404) {
          alert("Your credentials are not valid.");
        } else {
          alert("Your login failed.")
        }

      }
    });
  }

}
