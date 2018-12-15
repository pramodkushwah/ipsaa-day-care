import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { PagerService } from '../../../../providers/pagination/pager.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';

@Component({
  selector: 'app-student-fee',
  templateUrl: './student-fee.component.html',
  styleUrls: ['./student-fee.component.css']
})

export class StudentFeeComponent implements OnInit {

  showTable = false;
  searchedStudent = '';
  selectedCenter: number;
  centers = [];
  selectedStudentDetails: any = {};
  studentFeeDetails = [];
  viewPanel = false;
  loadingFeeList = false;
  allItems = [];
  update = true;
  STUDENTFEE_WRITE: boolean;

  constructor(
    private adminService: AdminService,
    private alertService: AlertService,

  ) { }

  ngOnInit() {
    this.STUDENTFEE_WRITE = this.adminService.hasPrivilage('STUDENTFEE_WRITE');
    this.initiallize();
    this.subscribSidePanel();
  }

  initiallize() {
    this.adminService
      .getCenters()
      .subscribe(centers => (this.centers = centers));
  }

  loadStudentFeeByCenter() {
    this.loadingFeeList = true;
    this.alertService.loading.next(true);
    this.adminService.loadStudentFeeByCenterId(this.selectedCenter)
      .subscribe(res => {
        this.alertService.loading.next(false);
        this.studentFeeDetails = res;
        this.allItems = res;
        this.showTable = true;
        this.loadingFeeList = false;
      }, (err) => {
        this.alertService.loading.next(false);
      });
  }

  searchStudent(event: any) {
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.studentFeeDetails = this.allItems.filter(student => {
        return student.fullName.toLowerCase().startsWith(val);
      });
    } else {
      this.studentFeeDetails = this.allItems;
    }
  }

  getStudentFee(student) {
    console.log(student);

    this.selectedStudentDetails = (student) ? student : {};
    this.showSidePanel();

  }




  showSidePanel() {

    this.adminService.viewPanel.next(true);
  }


  subscribSidePanel = () => {
    this.adminService.viewPanel.subscribe(value => {
      this.viewPanel = value;
    });
  }

}
