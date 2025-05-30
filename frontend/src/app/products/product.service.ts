import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from './product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  constructor(private http: HttpClient) {}

  getProducts(page: number, size: number): Observable<any> {
    return this.http.get(`/api/products?page=${page}&size=${size}`);
  } 

  searchProducts(params: any): Observable<any> {
    return this.http.get('/api/products/filter', { params });
  }
}