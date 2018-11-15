import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';

@Component({
  selector: 'app-fee-collection-report',
  templateUrl: './fee-collection-report.component.html',
  styleUrls: ['./fee-collection-report.component.css']
})
export class FeeCollectionReportComponent implements OnInit {
  centerList: Array<any>;
  SelectedCenter: any;
  formDate: Date;
  toDate: Date;
  feeCollectionfor = {};
  currentDate: string;
  downloadData = false;
  constructor(
    private alertService: AlertService,
    private adminService: AdminService,
  ) {
    this.currentDate = (new Date()).toISOString().split('T')[0];
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
  downloadFeeCollectionReport() {
    this.downloadData = true;
    if (this.SelectedCenter === 'All') {

      this.feeCollectionfor['centerCode'] = 'ALl';
      this.feeCollectionfor['centerId'] = 'ALl';
    } else {
      this.feeCollectionfor['centerCode'] = this.SelectedCenter.code;
      this.feeCollectionfor['centerId'] = this.SelectedCenter.id;
    }
    this.adminService.feeCollectionReportDownload(this.feeCollectionfor)
      .subscribe((res) => {
        console.log(res);
        this.downloadData = false;
      }, (err) => {
        this.alertService.errorAlert(err);
        this.downloadData = false;
      });
  }
}
