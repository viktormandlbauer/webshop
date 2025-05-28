import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

export interface DecodedToken {
  exp: number;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'jwt';

  // Save token to localStorage
  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  // Get token from localStorage
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Remove token (e.g., on logout)
  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  // Check if the token is expired
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      const now = Math.floor(Date.now() / 1000); // in seconds
      return decoded.exp < now;
    } catch {
      return true; // Invalid token = treat as expired
    }
  }

  // Optional: decode the token and return payload
  getDecodedToken(): DecodedToken | null {
    const token = this.getToken();
    try {
      return token ? jwtDecode<DecodedToken>(token) : null;
    } catch {
      return null;
    }
  }
}