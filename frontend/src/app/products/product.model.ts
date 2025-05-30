export interface Product {
  id: number;
  name: string;
  description: string;
  imageURL: string;
  categoryName: string;
  stock: number;
  price: number;
  avgRating: number | null;
}

export interface Category {
  id: number;
  name: string;
}

export interface PageInfo {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

export interface ProductPageResponse {
  content: Product[];
  page: PageInfo;
}