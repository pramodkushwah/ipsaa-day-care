import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-staff-salary-monthly-report',
  templateUrl: './staff-salary-monthly-report.component.html',
  styleUrls: ['./staff-salary-monthly-report.component.css']
})
export class StaffSalaryMonthlyReportComponent implements OnInit {

  months = ['January', 'February ', 'March',
   'April ', 'May ', 'June ', 'July ', 'August ', 'September ', 'October ', 'November ', 'December'] ;
  currentYear: number;
  years = [];
  employeeList: Array<any>;
  selectedEmployee: any;
 selectedMonth: number;
 selectedYear: number;
  staffSalaryMonthlyReportFor = {};
  downloadData = false;
  constructor(
    private alertService: AlertService,
    private adminService: AdminService,
  ) {
    this.currentYear = (new Date()).getFullYear();

    this.years.push(this.currentYear - 1);
    this.years.push(this.currentYear);
    this.years.push(this.currentYear + 1);
  }

  ngOnInit() {
  this.getEmployee();
  }

  getEmployee() {
    this.adminService.getEmployeeForSalaryReport()
      .subscribe((res: any) => {
        this.employeeList = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }
  staffSalaryMonthlyReportForDownload() {
    this.downloadData = true;
    if (this.selectedEmployee === 'All') {

      this.staffSalaryMonthlyReportFor['employerCode'] = 'ALl';
    } else {
      this.staffSalaryMonthlyReportFor['employerCode'] = this.selectedEmployee;
    }
    this.staffSalaryMonthlyReportFor['year'] = this.selectedYear;
    this.staffSalaryMonthlyReportFor['month'] = this.selectedMonth;

    this.adminService.staffSalaryMonthlyReportDownload(this.staffSalaryMonthlyReportFor)
      // .subscribe((res) => {
      //   const blob = new Blob([res.data], {
      //     type: 'application/octet-stream'
      // });
      // FileSaver.saveAs(blob, res.headers('fileName'));
      .subscribe((res: ArrayBuffer) => {
        // const headers = res.headers;
        const blob = new Blob([res], {
        });
        FileSaver.saveAs(blob, 'Staff_Salary_Monthly_Report.pdf');

        this.downloadData = false;
      }, (err) => {
        this.alertService.errorAlert(err);
        this.downloadData = false;
      });
  }

}
