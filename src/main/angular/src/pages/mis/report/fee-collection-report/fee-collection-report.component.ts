import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';
@Component({
  selector: 'app-fee-collection-report',
  templateUrl: './fee-collection-report.component.html',
  styleUrls: ['./fee-collection-report.component.css']
})
export class FeeCollectionReportComponent implements OnInit {

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
  STUDENTFEE_RECEIPT_CONFIRM: boolean;
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
    this.STUDENTFEE_RECEIPT_CONFIRM = this.adminService.hasPrivilage('STUDENTFEE_RECEIPT_CONFIRM');
    this.getCenter();
  }

  getCenter() {
    this.adminService.getCenters()
      .subscribe((res: any) => {
        this.centerList = res;
      });
  }
  studentFeeCollectionReportdownload() {
    this.downloadData = true;
      this.studentFeeReport['centerCode'] = this.SelectedCenter;
    this.studentFeeReport['quarter'] = this.selectedQuater;
      this.studentFeeReport['year'] = this.selectedYear;
      this.studentFeeReport['period'] = 'Quarterly';
      this.studentFeeReport['reportType'] = 'Paid';
    this.adminService.feeCollectionReportDownload(this.studentFeeReport)
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
