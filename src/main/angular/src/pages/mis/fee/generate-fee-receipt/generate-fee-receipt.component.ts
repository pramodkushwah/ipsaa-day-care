import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';


@Component({
  selector: 'app-generate-fee-receipt',
  templateUrl: './generate-fee-receipt.component.html',
  styleUrls: ['./generate-fee-receipt.component.css']
})
export class GenerateFeeReceiptComponent implements OnInit {

  centers: Array<any>;
  quaters = [{ Qtype: 'FYQ1', id: 2 }, { Qtype: 'FYQ2', id: 3 }, { Qtype: 'FYQ3', id: 4 }, { Qtype: 'FYQ4', id: 1 }];
  currentYear: number;

  years = [];

  studentDetails: Array<any>;
  viewPanel = false;
  selectedStudentDetails: any = {};
  feePaymentForm: FormGroup;
  allItems: any;
  showtable = false;
  downloadinData: boolean;
  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) {
    this.currentYear = (new Date()).getFullYear();

    this.years.push(this.currentYear - 1);
    this.years.push(this.currentYear);
  }

  generateSlipForm = this.fb.group({
    center: ['', Validators.required],
    quater: ['', Validators.required],
    year: ['', Validators.required]
  });




  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;
      });
  }




  getCenterStudentFeeDetails() {
    this.downloadinData = false;
    this.alertService.loading.next(true);
    this.adminService.getStudentFeeList({
      'centerCode': this.generateSlipForm.value.center,
      'period': 'Quarterly',
      'quarter': this.generateSlipForm.value.quater,
      'year': this.generateSlipForm.value.year
    })
      .subscribe((res) => {
        this.alertService.loading.next(false);

        this.showtable = true;
        this.downloadinData = true;

        this.studentDetails = res;
        this.allItems = res.slice(0);
      }, (err) => {
        this.downloadinData = true;
        this.showtable = true;
        this.alertService.loading.next(false);
        this.alertService.errorAlert(err);
      });

  }

  searchStudent(event: any) {
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.studentDetails = this.allItems.filter(student => {
        return student.fullName.toLowerCase().startsWith(val);
      });
    } else {
      this.studentDetails = this.allItems;
    }
  }



getStudentReceipt(student) {
  this.selectedStudentDetails = (student) ? student : {};
this.showSidePanel();
}


  showSidePanel() {
    this.adminService.viewPanelForFee.next(true);


  }

  subscribSidePanel = () => {
    this.adminService.viewPanelForFee.subscribe(value => {
      this.viewPanel = value;
    });
  }
  ngOnInit() {
    this.getCenter();
    this.subscribSidePanel();
  }








}
