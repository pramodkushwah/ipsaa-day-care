import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PpComponent } from './pp.component';
import { FeeComponent } from './fee/fee.component';
import { AttendanceComponent } from './attendance/attendance.component';
import { ProfileComponent } from './profile/profile.component';
import { SupportComponent } from './support/support.component';

const routes: Routes = [
  {
    path: '',
    component: PpComponent,
    // children: [
    //   {
    //     path: '',
    //     redirectTo: 'dashboard',
    //     pathMatch: 'full'
    //   },
    //   {
    //     path: 'dashboard',
    //     component: FeeComponent
    //   },
    //   {
    //     path: 'fee',
    //     component: FeeComponent
    //   },
    //   {
    //     path: 'attendance',
    //     component: AttendanceComponent
    //   },
    //   {
    //     path: 'profile',
    //     component: ProfileComponent
    //   },
    //   {
    //     path: 'support',
    //     component: SupportComponent
    //   },
    // ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PpRoutingModule { }
