export interface Product {
  id: number;
  name: string;
  description: string;
  imageURL: string;
  categoryName: string;
  stock: number;
  price: number;
  avgRating: number;
}

export interface Category {
  id: number;
  name: string;
}
