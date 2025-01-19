import {Component, OnInit} from '@angular/core';
import {ErrorService} from "../../service/error.service";
import {Error} from "../../model";
@Component({
  selector: 'app-errors',
  templateUrl: './errors.component.html',
  styleUrls: ['./errors.component.css']
})
export class ErrorsComponent implements OnInit{
  pageIndex: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  errors: Error[] = [];
  constructor(private errorService: ErrorService) {}

  ngOnInit(): void {
    this.loadErrors();
  }

  loadErrors(): void {
    this.errorService.getErrors(this.pageIndex, this.pageSize).subscribe({
      next: (response) => {
        this.errors = response.content;
        this.totalPages = response.totalPages;
      },
      error: (err) => {
        console.error('Error fetching errors:', err);
      }
    });
  }

  previousPage(): void {
    if (this.pageIndex > 0) {
      this.pageIndex--;
      this.loadErrors();
    }
  }

  nextPage(): void {
    if (this.pageIndex < this.totalPages - 1) {
      this.pageIndex++;
      this.loadErrors();
    }
  }

}
