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
      });
  }
  staffSalaryMonthlyReportForDownload() {
    this.downloadData = true;
    if (this.selectedEmployee === 'All') {

      this.staffSalaryMonthlyReportFor['employerCode'] = 'ALL';
    } else {
      this.staffSalaryMonthlyReportFor['employerCode'] = this.selectedEmployee;
    }
    this.staffSalaryMonthlyReportFor['year'] = this.selectedYear;
    this.staffSalaryMonthlyReportFor['month'] = this.selectedMonth;

    this.adminService.staffSalaryMonthlyReportDownload(this.staffSalaryMonthlyReportFor)
    .subscribe((res) => {
      const blob = new Blob([res.body], {
      });
      FileSaver.saveAs(blob, res.headers.get('fileName'));

        this.downloadData = false;
      }, (err) => {
        this.downloadData = false;
      });
  }

}
