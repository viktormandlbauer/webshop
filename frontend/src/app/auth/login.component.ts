import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TokenService } from '../auth/token.service';  // adjust path as needed

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenService: TokenService
  ) {}

  login() {
    this.http.post<{ token: string }>('/api/auth/login', {
      username: this.username,
      password: this.password
    }).subscribe({
      next: (res) => {
        this.tokenService.saveToken(res.token);  // ✅ use the service
        this.router.navigate(['/products']);
      },
      error: () => {
        this.error = 'Invalid username or password';
      }
    });
  }
}