import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-hdfc-gateway-fee-report',
  templateUrl: './hdfc-gateway-fee-report.component.html',
  styleUrls: ['./hdfc-gateway-fee-report.component.css']
})
export class HdfcGatewayFeeReportComponent implements OnInit {

  quaters = [{ Qtype: 'FYQ1', id: 2 }, { Qtype: 'FYQ2', id: 3 }, { Qtype: 'FYQ3', id: 4 }, { Qtype: 'FYQ4', id: 1 }];
  currentYear: number;
  years = [];
selectedYear: any;
selectedQuater: any;

  hdfcGatewayFeeReport = {};
  currentDate: Date;
  downloadData = false;
  constructor(
    private adminService: AdminService,
  ) {

    this.currentYear = (new Date()).getFullYear();

    this.years.push(this.currentYear - 1);
    this.years.push(this.currentYear);
    this.years.push(this.currentYear + 1);

  }

  ngOnInit() {
  }

  hdfcGatewayFeeReportdownload() {
    this.downloadData = true;
    this.hdfcGatewayFeeReport['period'] = 'Quarterly';
    this.hdfcGatewayFeeReport['quarter'] = this.selectedQuater;
    this.hdfcGatewayFeeReport['reportType'] = 'Paid';
      this.hdfcGatewayFeeReport['year'] = this.selectedYear;
      console.log(this.hdfcGatewayFeeReport);

    this.adminService.hdfcGatewayReportdownload(this.hdfcGatewayFeeReport)
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
