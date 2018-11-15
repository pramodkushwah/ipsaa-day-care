import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReportComponent } from './report/report.component';
import { InquiryComponent } from './inquiry/inquiry.component';

const attendanceRoutes: Routes = [
  {
    path: 'inquiryreport',
    component: ReportComponent
  },
  {
    path: 'inquiry',
    component: InquiryComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(attendanceRoutes)],
  exports: [RouterModule]
})
export class CrmRoutingModule { }
