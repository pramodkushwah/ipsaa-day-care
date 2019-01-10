import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as FileSaver from 'file-saver';
import { AlertService } from '../../../../providers/alert/alert.service';

@Component({
  selector: 'app-staff-report',
  templateUrl: './staff-report.component.html',
  styleUrls: ['./staff-report.component.css']
})
export class StaffReportComponent implements OnInit {
  costCenters: any;
  months = [{ 'name': 'January', 'moy': 1 },
  { 'name': 'February', 'moy': 2 },
  { 'name': 'March', 'moy': 3 },
  { 'name': 'April', 'moy': 4 },
  { 'name': 'May', 'moy': 5 },
  { 'name': 'June', 'moy': 6 },
  { 'name': 'July', 'moy': 7 },
  { 'name': 'August', 'moy': 8 },
  { 'name': 'September', 'moy': 9 },
  { 'name': 'October', 'moy': 10 },
  { 'name': 'November', 'moy': 11 },
  { 'name': 'December', 'moy': 12 }];
  selectedEmployer: any;
  disableGenerateButton: boolean;
  selectedMonth: any;
  salaries: any[];

  constructor(public adminService: AdminService, public alertService: AlertService) { }

  ngOnInit() {
    this.adminService.getCostCenter().subscribe(response => {
      this.costCenters = response;
      this.costCenters.unshift({ id: 'ALL', code: 'ALL', name: 'ALL' });
    });
  }

  generateReport() {
    if (!this.selectedEmployer) {
      this.alertService.errorAlert('Select Employer');
      return false;
    }

    // if (!this.selectedMonth) {
    //   this.alertService.errorAlert('Select Month');
    //   return false;
    // }

    this.disableGenerateButton = true;
    this.salaries = [];

    const req_body: any = {
      employerCode: this.selectedEmployer.code,
      month: 0
    };
    if (this.selectedMonth) {
      req_body['month'] = this.selectedMonth.moy;
    }

    this.adminService.staffReportDownload(req_body).subscribe(response => {
      this.disableGenerateButton = false;
      const blob = new Blob([response.body], {
        type: 'application/octet-stream'
      });
      FileSaver.saveAs(blob, response.headers.get('fileName'));
      this.alertService.successAlert('Staff Report Generated');
    }, (err) => {
      this.disableGenerateButton = false;
    });
  }

}
