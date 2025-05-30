import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { ProductListComponent } from './products/product-list/product-list.component';
import { ProductViewComponent } from './products/product-view/product-view.component';
import { WelcomeComponent } from './home/welcome/welcome.component';
import { ImpressumComponent } from './home/impressum/impressum.component';
import { HelpComponent } from './home/help/help.component';


export const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'impressum', component: ImpressumComponent },
  { path: 'help', component: HelpComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'products', component: ProductListComponent },
  { path : 'products/:id', component: ProductViewComponent },
];