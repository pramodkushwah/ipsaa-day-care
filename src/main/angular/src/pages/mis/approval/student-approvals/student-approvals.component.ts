import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';

@Component({
  selector: 'app-student-approvals',
  templateUrl: './student-approvals.component.html',
  styleUrls: ['./student-approvals.component.css']
})
export class StudentApprovalsComponent implements OnInit {

  selectedCenterId: any;
  centers: Array<any>;
  studentAprrovelList: Array<any> = [];
  selectedStudent: any = {};
  viewPanel = false;
  update  = false;
  showTable = false;
  constructor(
    private alertService: AlertService,
    private adminService: AdminService,
  ) { }

  ngOnInit() {
    this.getCenterStudentApprovelList();
    this.subscribSidePanel();
  }

getCenterStudentApprovelList() {
  this.adminService.getAllCenterStudentsApprovalCount()
  .subscribe((res: any) => {
this.centers = res;
  }, (err) => {
    this.alertService.errorAlert(err);
  });
}
  getStudentsList() {
    this.alertService.loading.next(true);
this.adminService.getSelectedCenterStudentsApprovalCount(this.selectedCenterId)
.subscribe((res: any) => {
  this.studentAprrovelList = res;
  this.alertService.loading.next(false);
  this.showTable = true;

    }, (err) => {
      this.alertService.loading.next(false);
      this.alertService.errorAlert(err);
      this.showTable = true;

    });
  }


  getSelectedStudentsDetail(student) {
 console.log(student);
this.selectedStudent = student;
this.showSidePanel();
  }



  showSidePanel() {

    this.adminService.viewPanel.next(true);
    console.log(this.viewPanel);
  }


  subscribSidePanel = () => {
    this.adminService.viewPanel.subscribe(value => {
      this.viewPanel = value;
      console.log(value);
    });
  }

}
