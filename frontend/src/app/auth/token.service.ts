import { Injectable } from '@angular/core';
import { jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private readonly tokenKey = 'jwtToken';

  // Save token
  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  // Get token
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Remove token
  removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  // Decode token
  decodeToken(): any | null {
    const token = this.getToken();
    try {
      return token ? jwtDecode(token) : null;
    } catch (error) {
      return null;
    }
  }

  // Check if token is expired
  isTokenExpired(): boolean {
    const decoded: any = this.decodeToken();
    if (!decoded || !decoded.exp) return true;
    const exp = decoded.exp;
    const now = Math.floor(Date.now() / 1000);
    return exp < now;
  }
}