import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../product.service';
import { CategoryService } from '../category.service';
import { Product, Category, ProductPageResponse } from '../product.model';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [RouterModule, CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  page = 0;
  pageSize = 6;
  totalPages = 1;
  totalElements = 0;
  isLoading = false;

  // Filters
  filterForm: FormGroup;
  selectedMinRating = 0;
  isFilterActive = false;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService
  ) {
    this.filterForm = this.fb.group({
      categoryName: [''],
      minPrice: [''],
      maxPrice: ['']
    });
  }

  ngOnInit() {
    this.loadCategories();
    this.fetchProducts();
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: cats => (this.categories = cats),
      error: () => (this.categories = [])
    });
  }

  fetchProducts(page: number = 0) {
    this.isLoading = true;
    this.productService.getProducts(page, this.pageSize).subscribe({
      next: (data: ProductPageResponse) => {
        this.products = data.content;
        this.page = data.page.number;
        this.totalPages = data.page.totalPages;
        this.totalElements = data.page.totalElements;
        this.isLoading = false;
        this.isFilterActive = false;
      },
      error: () => {
        this.products = [];
        this.isLoading = false;
      }
    });
  }

  applyFilters(page: number = 0) {
    this.isLoading = true;
    this.isFilterActive = true;
    const params: any = {
      ...this.filterForm.value,
      minRating: this.selectedMinRating > 0 ? this.selectedMinRating : undefined,
      page,
      size: this.pageSize
    };

    this.productService.searchProducts(params).subscribe({
      next: (data: ProductPageResponse) => {
        this.products = data.content;
        this.page = data.page.number;
        this.totalPages = data.page.totalPages;
        this.totalElements = data.page.totalElements;
        this.isLoading = false;
      },
      error: () => {
        this.products = [];
        this.isLoading = false;
      }
    });
  }

  resetFilters() {
    this.filterForm.reset();
    this.selectedMinRating = 0;
    this.fetchProducts(0);
  }

  setRating(rating: number) {
    this.selectedMinRating = rating;
    // Optionally, call applyFilters(0) to auto-filter on rating change
  }

  goToPage(page: number) {
    if (this.isFilterActive) {
      this.applyFilters(page);
    } else {
      this.fetchProducts(page);
    }
  }

  // For displaying rating as stars:
  getStars(rating: number | null = 0): string[] {
    rating = rating ?? 0;
    const full = Math.floor(rating);
    const half = rating % 1 >= 0.5 ? 1 : 0;
    const empty = 5 - full - half;
    return [
      ...Array(full).fill('full'),
      ...Array(half).fill('half'),
      ...Array(empty).fill('empty')
    ];
  }
}