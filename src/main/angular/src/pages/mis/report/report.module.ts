import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportRoutingModule } from './/report-routing.module';
import { InquiryReportComponent } from './inquiry-report/inquiry-report.component';
import { StudentFeeReportComponent } from './student-fee-report/student-fee-report.component';
import { StaffSalaryMonthlyReportComponent } from './staff-salary-monthly-report/staff-salary-monthly-report.component';
import { StudentAttendanceReportComponent } from './student-attendance-report/student-attendance-report.component';
import { StaffAttendanceReportComponent } from './staff-attendance-report/staff-attendance-report.component';
import { FeeCollectionReportComponent } from './fee-collection-report/fee-collection-report.component';
import { FormsModule } from '@angular/forms';
import { HdfcGatewayFeeReportComponent } from './hdfc-gateway-fee-report/hdfc-gateway-fee-report.component';
import { IpsaaClubReportComponent } from './ipsaa-club-report/ipsaa-club-report.component';
import { StaffReportComponent } from './staff-report/staff-report.component';

@NgModule({
  imports: [CommonModule,    FormsModule,
    ReportRoutingModule],
  declarations: [
    InquiryReportComponent,
    StudentFeeReportComponent,
    StaffSalaryMonthlyReportComponent,
    StudentAttendanceReportComponent,
    StaffAttendanceReportComponent,
    FeeCollectionReportComponent,
    HdfcGatewayFeeReportComponent,
    IpsaaClubReportComponent,
    StaffReportComponent
  ],
  exports: [InquiryReportComponent],
})
export class ReportModule {}
