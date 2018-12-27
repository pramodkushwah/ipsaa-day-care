import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-ipsaa-club-report',
  templateUrl: './ipsaa-club-report.component.html',
  styleUrls: ['./ipsaa-club-report.component.css']
})
export class IpsaaClubReportComponent implements OnInit {

  years = [new Date().getFullYear() - 1, new Date().getFullYear(), new Date().getFullYear() + 1];
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
  selectedCenter = null;
  selectedMonth = null;
  selectedYear = null;
  centers: any = [];
  disableDownloadFee: boolean;
  disableDownloadCoolection: boolean;

  constructor(public adminService: AdminService, public alertService: AlertService) { }

  ngOnInit() {
    this.adminService.getCenters().subscribe(response => {
      this.centers = response;
      this.centers.unshift({ code: 'All', name: 'All', id: 'All' });
    });
  }


  downloadFee = function () {
    if (this.validate()) {
      const request = {
        centerCode: this.selectedCenter.code,
        month: this.selectedMonth.moy,
        year: this.selectedYear
      };
      console.log(request);
      this.disableDownloadFee = true;

      this.adminService.ipsaaClubFeeReportDownload(request).subscribe(response => {
        this.disableDownloadFee = false;
        const blob = new Blob([response.body], {
        });
        FileSaver.saveAs(blob, response.headers.get('fileName'));
      }, error => {
        this.disableDownloadFee = false;
      });
    }
  };

  downloadCollection = function () {
    if (this.validate()) {
      const request = {
        centerCode: this.selectedCenter.code,
        month: this.selectedMonth.moy,
        year: this.selectedYear
      };
      console.log(request);
      this.disableDownloadCollection = true;
      this.adminService.ipsaaClubCollectionFeeDownload(request).subscribe(response => {
        const blob = new Blob([response.body], {
          type: 'application/octet-stream'
        });
        FileSaver.saveAs(blob, response.headers.get('fileName'));
        this.disableDownloadCollection = false;
      }, error => {
        this.disableDownloadCollection = false;
      });
    }
  };

  validate() {
    if (!this.selectedCenter) {
      this.alertService.errorAlert('Select Center !!');
      return false;
    }
    if (!this.selectedMonth) {
      this.alertService.errorAlert('Select Month !!');
      return false;
    }
    if (!this.selectedYear) {
      this.alertService.errorAlert('Select Year !!');
      return false;
    }
    if (this.selectedCenter && this.selectedMonth && this.selectedYear) {
      return true;
    } else {
      return false;
    }
  }

}
