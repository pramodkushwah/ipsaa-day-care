import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InquiryReportComponent } from './inquiry-report/inquiry-report.component';
import { StudentFeeReportComponent } from './student-fee-report/student-fee-report.component';
import { StaffSalaryMonthlyReportComponent } from './staff-salary-monthly-report/staff-salary-monthly-report.component';
import { StudentAttendanceReportComponent } from './student-attendance-report/student-attendance-report.component';
import { StaffAttendanceReportComponent } from './staff-attendance-report/staff-attendance-report.component';
import { FeeCollectionReportComponent } from './fee-collection-report/fee-collection-report.component';

const reportRoutes: Routes = [
  {
    path: 'inquiryreport',
    component: InquiryReportComponent
  },
  {
    path: 'stdfeereport',
    component: StudentFeeReportComponent
  },
  {
    path: 'staffsalarymonthlyreport',
    component: StaffSalaryMonthlyReportComponent
  },
  {
    path: 'stdattendancereport',
    component: StudentAttendanceReportComponent
  },
  {
    path: 'staffattendancereport',
    component: StaffAttendanceReportComponent
  },
  {
    path: 'collectionfeereport',
    component: FeeCollectionReportComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(reportRoutes)],
  exports: [RouterModule]
})
export class ReportRoutingModule { }
