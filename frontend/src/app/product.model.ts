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

export interface PageInfo {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

export interface ProductResponse {
  content: Product[];
  page: PageInfo;
}