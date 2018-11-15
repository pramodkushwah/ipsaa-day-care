import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-student-fee-report',
  templateUrl: './student-fee-report.component.html',
  styleUrls: ['./student-fee-report.component.css']
})
export class StudentFeeReportComponent implements OnInit {


  centerList: Array<any>;
  SelectedCenter: any;
  quaters = [{ Qtype: 'FYQ1', id: 2 }, { Qtype: 'FYQ2', id: 3 }, { Qtype: 'FYQ3', id: 4 }, { Qtype: 'FYQ4', id: 1 }];
  currentYear: number;
  years = [];
selectedYear: any;
selectedQuater: any;

  studentFeeReport = {};
  currentDate: Date;
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
    this.getCenter();
  }

  getCenter() {
    this.adminService.getCenters()
      .subscribe((res: any) => {
        this.centerList = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }
  studentFeeReportdownload() {
    this.downloadData = true;
      this.studentFeeReport['centerCode'] = this.SelectedCenter;
    this.studentFeeReport['quarter'] = this.selectedQuater;
      this.studentFeeReport['year'] = this.selectedYear;
      console.log(this.studentFeeReport);

    this.adminService.studentsFeeReportdownload(this.studentFeeReport)
      // .subscribe((res) => {
      //   const blob = new Blob([res.data], {
      //     type: 'application/octet-stream'
      // });

      // FileSaver.saveAs(blob, res.headers('fileName'));
      .subscribe((res: ArrayBuffer) => {
        // const headers = res.headers;
        const blob = new Blob([res], {
        });
        FileSaver.saveAs(blob, 'Fee_Report.pdf');

        this.downloadData = false;
      }, (err) => {
        this.alertService.errorAlert(err);
        this.downloadData = false;
      });
  }

}
