import {Component, OnInit} from '@angular/core';
import {LoginService} from "../../service/loginservice.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Projekat';
  availableTabs: {label: string, route: string, canShow: () => boolean}[] = [];

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loginService.permissions$.subscribe(permissions => {
      this.updateTabs(permissions);
    });
  }

  logout() {
    this.loginService.clearAuthData();
    this.router.navigate(['']);
  }

  canLogout() {
    return this.loginService.isLogin();
  }

  private updateTabs(permissions: string[]) {
    this.availableTabs = [
      {
        label: 'Read users',
        route: '/users',
        canShow: () => permissions.includes('can_read_users')
      },
      {
        label: 'Add user',
        route: '/createuser',
        canShow: () => permissions.includes('can_create_users')
      },
      {
        label: 'Search orders',
        route: '/search',
        canShow: () => permissions.includes('can_search_order')
      },
      {
        label: 'Place order',
        route: '/place',
        canShow: () => permissions.includes('can_place_order')
      },
      {
        label: 'Errors',
        route: '/errors',
        canShow: () => permissions.includes('can_place_order')
      }
    ].filter(tab => tab.canShow());
  }
}
