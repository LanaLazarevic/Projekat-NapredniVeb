import {Component, OnInit} from '@angular/core';
import {OrderServiceService} from "../../service/order-service.service";
import {Dish} from "../../model";
import {DishService} from "../../service/dish.service";
import {Router} from "@angular/router";
import {LoginService} from "../../service/loginservice.service";

@Component({
  selector: 'app-placeorder',
  templateUrl: './placeorder.component.html',
  styleUrls: ['./placeorder.component.css']
})
export class PlaceorderComponent implements OnInit{
  dishes: Dish[] = [];
  selectedDishes: string[] = [];
  isScheduled: boolean = false;
  orderTime: string = '';
  constructor(private orderService: OrderServiceService,private dishService: DishService, private router:Router, private loginService:LoginService) {}

  ngOnInit(): void {
    this.fetchDishes();
  }

  fetchDishes(): void {
    this.dishService.getDishes().subscribe({
      next: (response: Dish[]) => {
        this.dishes = response;
      },
      error: (err) => {
        console.error('Error fetching dishes:', err);
      }
    });
  }

  toggleDish(dish: string): void {
    const index = this.selectedDishes.indexOf(dish);
    if (index > -1) {
      this.selectedDishes.splice(index, 1);
    } else {
      this.selectedDishes.push(dish);
    }
  }

  submitOrder(): void {
    if (this.selectedDishes.length === 0) {
      alert('Please select at least one dish to create an order.');
      return;
    }
    if (this.isScheduled && this.orderTime) {
      const scheduleDto = {
        dishes: this.selectedDishes,
        orderTime: this.orderTime
      };
      this.orderService.scheduleOrder(scheduleDto).subscribe({
        next: () => {
          this.router.navigate(['/search'])
        },
        error: (err) => {
          console.error('Error scheduling order:', err);
          alert('Failed to schedule order.');
        }
      });
    } else {
      this.orderService.createOrder(this.selectedDishes).subscribe({
        next: () => {
          this.router.navigate(['/search'])
        },
        error: (err) => {
          console.error('Error placing order:', err);
          alert('Failed to place order.');
        }
      });
    }
  }

  canSchedule():boolean{
    return this.loginService.hasPermission("can_schedule_order");
  }
}
