import {Component, OnInit} from '@angular/core';
import {User} from "../../model";
import {UserService} from "../../service/user.service";
import {LoginService} from "../../service/loginservice.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-read-users',
  templateUrl: './read-users.component.html',
  styleUrls: ['./read-users.component.css']
})
export class ReadUsersComponent implements OnInit{
  users:User[]=[]

  constructor(private userService: UserService, private loginService: LoginService, private router:Router) {}

  ngOnInit(): void{
    this.userService.users$.subscribe(users => {
      this.users = users.map(user => ({
        ...user,
        permissions: new Set(user.permissions) // Konvertovanje permissions u Set
      }));
    });

    this.userService.getUsers().subscribe();
  }


  canDeleteUser(): boolean {
    return this.loginService.hasPermission("can_delete_users");
  }

  canUpdateUser(): boolean {
    return  this.loginService.hasPermission("can_update_users");
  }

  deleteUser(email: string): void {
    if (confirm(`Are you sure you want to delete user ${email}?`)) {
      this.userService.deleteUser(email).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.email !== email);
        },
        error: (error) => {
          console.error('There was an error deleting the user!', error);
        }
      });
    }
  }

  updateUser(email: string) {
    this.router.navigate(['/updateuser', email]);
  }
}
