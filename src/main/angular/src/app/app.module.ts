import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { Api } from '../providers/api/api';
import { User } from '../providers/user/user';
import { StorageService } from '../providers/localstorage/storage';
import { AppRoutingModule } from './app.routing.module';
import { HttpClientModule } from '@angular/common/http';
import { Page404Component } from '../pages/404/page404.component';
import { LoginComponent } from '../pages/login/login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DatePipe } from '@angular/common';
import { AlertService } from '../providers/alert/alert.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    Page404Component
  ],
  providers: [Api, User, StorageService, DatePipe, AlertService],
  bootstrap: [AppComponent]
})
export class AppModule {}
