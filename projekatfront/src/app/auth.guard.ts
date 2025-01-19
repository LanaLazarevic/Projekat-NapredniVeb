import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanDeactivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable } from 'rxjs';
import {LoginService} from "./service/loginservice.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanDeactivate<unknown> {

  constructor(private router: Router, private loginservice: LoginService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const url = state.url;

    if (url === '/createuser' && !this.loginservice.hasPermission("can_create_users")) {
      alert('Nemate dozvolu za kreiranje korisnika');
      this.router.navigate(['']);
    } else if (state.url.startsWith('/updateuser/') && !this.loginservice.hasPermission("can_update_users")) {
      alert('Nemate dozvolu za izmenu korisnika');
      this.router.navigate(['']);
    } else if( !this.loginservice.isAuthorized() ){
      alert('You dont have read permission.');
      this.router.navigate(['']);
    } if (url === '/search' && !this.loginservice.hasPermission("can_search_order")) {
      alert('Nemate dozvolu za gledanje porudzbina');
      this.router.navigate(['']);
    }if (url === '/place' && !this.loginservice.hasPermission("can_place_order")) {
      alert('Nemate dozvolu za pravljenje porudzbina');
      this.router.navigate(['']);
    }if (url === '/errors' && !this.loginservice.hasPermission("can_place_order")) {
      alert('Nemate dozvolu za pravljenje porudzbina time ni ne mate greske');
      this.router.navigate(['']);
    }


    return true

  }



  canDeactivate(
    component: unknown,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return true;
  }

}
