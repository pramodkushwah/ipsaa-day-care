import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-staff-attendance-report',
  templateUrl: './staff-attendance-report.component.html',
  styleUrls: ['./staff-attendance-report.component.css']
})
export class StaffAttendanceReportComponent implements OnInit {

  centerList: Array<any>;
  SelectedCenterId: any;
  formDate: Date;
  toDate: Date;
  staffAttendanceFor = {};
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
      });
  }
  staffAttendanceReportDownload() {
    this.downloadData = true;

      this.staffAttendanceFor['centerId'] = this.SelectedCenterId;
      this.staffAttendanceFor['from'] = this.formDate;
      this.staffAttendanceFor['to'] = this.toDate;
console.log(this.staffAttendanceFor);

    this.adminService.staffsAttendanceReportDownload(this.staffAttendanceFor)
      .subscribe((res) => {
          const blob = new Blob([res.body], {
          });
          FileSaver.saveAs(blob, res.headers.get('fileName'));

        this.downloadData = false;
        this.staffAttendanceFor = {};
      }, (err) => {
        this.downloadData = false;
      });
  }
}
