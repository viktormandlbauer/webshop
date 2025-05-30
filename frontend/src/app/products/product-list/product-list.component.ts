import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../product.service';
import { CategoryService } from '../category.service';
import { Product } from '../product.model';
import { Category } from '../product.model';
import { ApiResponse } from '../../api-response.model';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [RouterModule, CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  environment = environment;
  page = 0;
  pageSize = 6;
  totalPages = 1;
  totalElements = 0;
  isLoading = false;

  // Filters
  filterForm: FormGroup;
  isFilterActive = false;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService
  ) {
    this.filterForm = this.fb.group({
      categoryName: [''],
      minPrice: [''],
      maxPrice: [''],
      minRating: [0]
    });
  }

  get minRating(): number {
    return this.filterForm.get('minRating')?.value ?? 0;
  }

  setRating(rating: number) {
    this.filterForm.patchValue({ minRating: rating });
  }

  get categoryName(): string {
    return this.filterForm.get('categoryName')?.value ?? '';
  }
  
  setCategory(category: Category) {
    this.filterForm.patchValue({ categoryName: category.name });
  }

  get minPrice(): number {
    return this.filterForm.get('minPrice')?.value ?? 0;
  }

  setMinPrice(price: number) {
    this.filterForm.patchValue({ minPrice: price });
  }

  get maxPrice(): number {
    return this.filterForm.get('maxPrice')?.value ?? 0;
  }

  setMaxPrice(price: number) {
    this.filterForm.patchValue({ maxPrice: price });
  }

  ngOnInit() {
    this.loadCategories();
    this.fetchProducts();
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: (cats: Category[]) => (this.categories = cats),
      error: () => (this.categories = [])
    });
  }

  fetchProducts(page: number = 0) {
    this.isLoading = true;
    this.productService.getProducts(page, this.pageSize).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        // Type assertion as Product[] because your ApiResponse.Data.content is []
        this.products = data.content as Product[];
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
      page,
      size: this.pageSize
    };

    if (!params.categoryName) {
      delete params.categoryName;
    }

    if (!params.minPrice) {
      delete params.minPrice;
    }

    if (!params.maxPrice) {
      delete params.maxPrice;
    }

    if (!params.minRating) {
      delete params.minRating;
    }

    this.productService.searchProducts(params).subscribe({
      next: (response: ApiResponse) => {
        const data = response.data;
        this.products = data.content as Product[];
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
    this.fetchProducts(0);
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
