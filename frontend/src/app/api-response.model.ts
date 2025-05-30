export interface Data {
  content: [];
  page: PageInfo;
}

export interface PageInfo {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
}

export interface ApiResponse {
  data: Data;
  status: string;
}