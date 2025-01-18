import {Component, OnInit} from '@angular/core';
import {User} from "../../model";
import {UserService} from "../../service/user.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit{
  userEmail:string='';
  user:User={
    firstname:'',
    lastname:'',
    email: '',
    id: 0,
    permissions: new Set<string>()
  };

  constructor(private userService: UserService, private router:Router, private route:ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.userEmail = params.get('email') || '';
      if (this.userEmail) {
        this.loadUser();
      }
    });
  }

  loadUser(): void {
    // @ts-ignore
    this.userService.getUserByEmail(this.userEmail).subscribe({
      next: (user) => {
        this.user =  {
          ...user,
          permissions: new Set(user.permissions)
        };
      },
      error: (error) => {
        console.error('Error loading user:', error);
      }
    });
  }

  updateUser(){
    if(this.check(this.user.firstname)&& this.check(this.user.lastname)){
      alert("You cant leave first name and last name empty!")
    }else{
      this.userService.updateUser(this.user).subscribe({
        next: (data) => {
          this.router.navigate(['/users'])
        },
        error: (error) => {
          console.log('There was an error fetching the users!', error);
        }
      });
    }
  }

  togglePermission(permission: string): void {
    if (!this.user.permissions.has(permission)) {
      this.user.permissions.add(permission);

      if (
        permission === 'can_place_order' ||
        permission === 'can_cancel_order' ||
        permission === 'can_track_order' ||
        permission === 'can_schedule_order'
      ) {
        this.user.permissions.add('can_search_order');
      }
      if (
        permission === 'can_create_users' ||
        permission === 'can_update_users' ||
        permission === 'can_delete_users'
      ) {
        this.user.permissions.add('can_read_users');
      }
    } else {
      this.user.permissions.delete(permission);
    }
  }

  isPermissionDisabled(permission: string): boolean {
    if (permission === 'can_read_users') {
      return (
        this.user.permissions.has('can_create_users') ||
        this.user.permissions.has('can_update_users') ||
        this.user.permissions.has('can_delete_users')
      );
    }
    if (permission === 'can_search_order') {
      return (
        this.user.permissions.has('can_place_order') ||
        this.user.permissions.has('can_cancel_order') ||
        this.user.permissions.has('can_track_order') ||
        this.user.permissions.has('can_schedule_order')
      );
    }
    return false;
  }

  check(field:string){
    return (field == '' || field == ' ');
  }



}
