import { Component } from '@angular/core';
import {OrderServiceService} from "../../service/order-service.service";
import {Dish} from "../../model";
import {DishService} from "../../service/dish.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-placeorder',
  templateUrl: './placeorder.component.html',
  styleUrls: ['./placeorder.component.css']
})
export class PlaceorderComponent {
  dishes: Dish[] = [];
  selectedDishes: string[] = [];

  constructor(private orderService: OrderServiceService,private dishService: DishService, private router:Router) {}

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

    this.orderService.createOrder(this.selectedDishes).subscribe({
      next: () => {
        this.router.navigate(['/search'])
      },
      error: (err) => {
        console.error('Error creating order:', err);
        alert('Failed to create order.');
      }
    });
  }
}
