import { Component, OnInit } from '@angular/core';
import * as FileSaver from 'file-saver';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';

@Component({
  selector: 'app-inquiry-report',
  templateUrl: './inquiry-report.component.html',
  styleUrls: ['./inquiry-report.component.css']
})
export class InquiryReportComponent implements OnInit {

  centerList: Array<any>;
  SelectedCenter: any;
  formDate: Date;
  toDate: Date;
  inquiryFor = {};
  currentDate: Date;
  downloadData = false;
  constructor(
    private alertService: AlertService,
    private adminService: AdminService,
  ) {
    this.currentDate = new Date();
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
  downloadReport() {
    this.downloadData = true;
    if (this.SelectedCenter === 'All') {

      this.inquiryFor['centerCode'] = 'ALL';
      this.inquiryFor['centerId'] = 1;
    } else {
      this.inquiryFor['centerCode'] = this.SelectedCenter.code;
      this.inquiryFor['centerId'] = this.SelectedCenter.id;
    }
    this.inquiryFor['from'] = this.formDate;
      this.inquiryFor['to'] = this.toDate;
      this.adminService.inquiryReportDownload(this.inquiryFor)
      // .subscribe((res) => {
        //   const blob = new Blob([res.data], {
          //     type: 'application/octet-stream'
          // });
          // FileSaver.saveAs(blob, res.headers('fileName'));
          .subscribe((res) => {
            this.downloadData = false;
            const blob = new Blob([res.data], {
            });
            const headers = res.headers('fileName');
            FileSaver.saveAs(blob, headers);

      }, (err) => {
        this.alertService.errorAlert(err);
        this.downloadData = false;
      });
  }
}
