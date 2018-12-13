import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PpRoutingModule } from './pp-routing.module';
import { PpComponent } from './pp.component';
import { AttendanceComponent } from './attendance/attendance.component';
import { FeeComponent } from './fee/fee.component';
import { ProfileComponent } from './profile/profile.component';
import { SupportComponent } from './support/support.component';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    PpRoutingModule
  ],
  declarations: [PpComponent, AttendanceComponent, FeeComponent, ProfileComponent, SupportComponent, LoginComponent]
})
export class PpModule { }
