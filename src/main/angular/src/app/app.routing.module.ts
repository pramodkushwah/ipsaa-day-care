import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGaurd } from '../providers/gaurd/auth.gaurd';
import { LoginGaurd } from '../providers/gaurd/login.gaurd';
import { Page404Component } from '../pages/404/page404.component';
import { LoginComponent } from '../pages/login/login.component';
import { PpauthGuard } from '../providers/gaurd/ppauth.guard';
import { ResponseComponent } from '../pages/pp/response/response.component';
import { CheckoutComponent } from '../pages/pp/checkout/checkout.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    // loadChildren: 'pages/login/login.module#LoginModule',
    component: LoginComponent, canActivate: [LoginGaurd]
  },
  { path: 'pp', loadChildren: 'pages/pp/pp.module#PpModule', canActivate: [PpauthGuard] },

  { path: 'mis', loadChildren: 'pages/mis/mis.module#MISModule', canActivate: [AuthGaurd] },
  {
    path: 'pp/checkout.html!/app/checkoutdetails/:p1/:p2',
    component: CheckoutComponent
  },
  {
    path: 'pp/checkout.html!/app/ipsaaclubcheckoutdetails/:p1/:p2',
    component: CheckoutComponent
  },
  {
    path: 'pp/checkout-failure/:paymentId',
    component: ResponseComponent
  },
  {
    path: 'pp/checkout-success/:paymentId',
    component: ResponseComponent
  },
  {
    path: '**',
    redirectTo: '/page404'
  },
  {
    path: 'page404',
    component: Page404Component
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule],
  providers: [AuthGaurd, PpauthGuard, LoginGaurd]
})
export class AppRoutingModule {
  constructor() { }
}
