import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';

declare let $: any;

@Component({
  selector: 'app-student-approvals',
  templateUrl: './student-approvals.component.html',
  styleUrls: ['./student-approvals.component.css']
})
export class StudentApprovalsComponent implements OnInit {
  comment: string;
  selectedCenterId: any;
  centers: Array<any>;
  studentAprrovelList: Array<any> = [];
  selectedStudent: any = {};
  viewPanel = false;
  update  = false;
  showTable = false;
  studentLoader: boolean;
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
    this.centers.sort((ai: any , bi: any) => {
      return bi.count - ai.count;
    });
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
  }


  studentApprove(student) {
    this.adminService.aproveStudent(student.id)
      .subscribe((res: any) => {
        this.getCenterStudentApprovelList();
        this.studentAprrovelList = this.studentAprrovelList.filter(element => element.id !== student.id);
          this.alertService.successAlert('Student Approved');
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  studentReject(student: any, comment: any) {
    this.studentLoader = true;
    this.adminService.rejectStudent(student.id, comment)
      .subscribe((res: any) => {
        this.studentLoader = false;
        this.getCenterStudentApprovelList();
        this.studentAprrovelList = this.studentAprrovelList.filter(element => element.id !== student.id);
        this.alertService.successAlert('Student Rejected');
        $('#myModal').modal('hide');
      }, (err) => {
        this.studentLoader = false;
        this.alertService.errorAlert(err);
      });
  }




  subscribSidePanel = () => {
    this.adminService.viewPanel.subscribe(value => {
      this.viewPanel = value;
      console.log(value);
    });
  }

}
