import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AuthGuard} from "./auth.guard";
import {ReadUsersComponent} from "./components/read-users/read-users.component";
import {CreateUserComponent} from "./components/create-user/create-user.component";
import {UpdateUserComponent} from "./components/update-user/update-user.component";

const routes: Routes = [
  {
    path: "",
    component: LoginComponent
  },
  {
    path: "users",
    component: ReadUsersComponent,
    canActivate:[AuthGuard],
    canDeactivate:[AuthGuard]
  },
  {
    path: "createuser",
    component: CreateUserComponent,
    canActivate:[AuthGuard],
    canDeactivate:[AuthGuard]
  },
  {
    path: "updateuser/:email",
    component: UpdateUserComponent,
    canActivate:[AuthGuard],
    canDeactivate:[AuthGuard]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
