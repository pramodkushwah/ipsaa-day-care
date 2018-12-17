import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReportComponent } from './report/report.component';
import { InquiryComponent } from './inquiry/inquiry.component';

const attendanceRoutes: Routes = [
  {
    path: 'inquiry',
    component: InquiryComponent
  },
  {
    path: 'inquiryreport',
    component: ReportComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(attendanceRoutes)],
  exports: [RouterModule]
})
export class CrmRoutingModule { }
