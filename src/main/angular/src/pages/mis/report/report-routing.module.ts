import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InquiryReportComponent } from './inquiry-report/inquiry-report.component';
import { StudentFeeReportComponent } from './student-fee-report/student-fee-report.component';
import { StaffSalaryMonthlyReportComponent } from './staff-salary-monthly-report/staff-salary-monthly-report.component';
import { StudentAttendanceReportComponent } from './student-attendance-report/student-attendance-report.component';
import { StaffAttendanceReportComponent } from './staff-attendance-report/staff-attendance-report.component';
import { FeeCollectionReportComponent } from './fee-collection-report/fee-collection-report.component';
import { HdfcGatewayFeeReportComponent } from './hdfc-gateway-fee-report/hdfc-gateway-fee-report.component';
import { IpsaaClubReportComponent } from './ipsaa-club-report/ipsaa-club-report.component';
import { StaffReportComponent } from './staff-report/staff-report.component';

const reportRoutes: Routes = [
  {
    path: 'stdattendancereport',
    component: StudentAttendanceReportComponent
  },
  {
    path: 'staffattendancereport',
    component: StaffAttendanceReportComponent
  },
  {
    path: 'ipsaaclubreport',
    component: IpsaaClubReportComponent
  },
  {
    path: 'stdfeereport',
    component: StudentFeeReportComponent
  },
  {
    path: 'inquiryreport',
    component: InquiryReportComponent
  },
  {
    path: 'collectionfeereport',
    component: FeeCollectionReportComponent
  },
  {
    path: 'hdfcgatewayfeereport',
    component: HdfcGatewayFeeReportComponent
  },
  {
    path: 'staffsalarymonthlyreport',
    component: StaffSalaryMonthlyReportComponent
  },
  {
    path: 'staffreport',
    component: StaffReportComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(reportRoutes)],
  exports: [RouterModule]
})
export class ReportRoutingModule { }
