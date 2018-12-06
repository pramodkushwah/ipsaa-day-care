import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-student-attendance-report',
  templateUrl: './student-attendance-report.component.html',
  styleUrls: ['./student-attendance-report.component.css']
})
export class StudentAttendanceReportComponent implements OnInit {


  centerList: Array<any>;
  SelectedCenterId: any;
  formDate: Date;
  toDate: Date;
  studentAttendanceFor = {};
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
  studentAttendanceReportDownload() {
    this.downloadData = true;

      this.studentAttendanceFor['centerId'] = this.SelectedCenterId;
      this.studentAttendanceFor['from'] = this.formDate;
      this.studentAttendanceFor['to'] = this.toDate;
console.log(this.studentAttendanceFor);

this.adminService.studentsAttendanceReportDownload(this.studentAttendanceFor)
.subscribe((res) => {
  const blob = new Blob([res.body], {
  });
  FileSaver.saveAs(blob, res.headers.get('fileName'));

  this.downloadData = false;
  this.studentAttendanceFor = {};
      }, (err) => {
        this.downloadData = false;
      });
  }


}
