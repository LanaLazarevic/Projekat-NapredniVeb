import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './components/app/app.component';
import { LoginComponent } from './components/login/login.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {LoginService} from "./service/loginservice.service";
import { ReadUsersComponent } from './components/read-users/read-users.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { UpdateUserComponent } from './components/update-user/update-user.component';
import { SearchOrderComponent } from './components/search-order/search-order.component';
import {MatPaginatorModule} from "@angular/material/paginator";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { PlaceorderComponent } from './components/placeorder/placeorder.component';
import { ErrorsComponent } from './components/errors/errors.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ReadUsersComponent,
    CreateUserComponent,
    UpdateUserComponent,
    SearchOrderComponent,
    PlaceorderComponent,
    ErrorsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule, // Ovo je neophodno za Angular Material
    MatPaginatorModule
  ],
  providers: [LoginService],
  bootstrap: [AppComponent]
})
export class AppModule { }
