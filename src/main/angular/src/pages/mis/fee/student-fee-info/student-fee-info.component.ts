import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';

@Component({
  selector: 'app-student-fee-info',
  templateUrl: './student-fee-info.component.html',
  styleUrls: ['./student-fee-info.component.css']
})
export class StudentFeeInfoComponent implements OnInit {

  annualGST: number;
  quarterlyGST: number;
  searchedStudent = '';
  selectedCenter = { id: 0 };
  centers = [];
  selectedStudentDetails: any = {};
  studentFeeDetails = [];
  loadingFeeList = false;
  studentFeeForm: FormGroup;
  updateFee: any;

  @Input() set studentFee(studentFee: any) {
    this.updateFee = studentFee;

    this.getStudentFee(studentFee);
  }

  constructor(
    private fb: FormBuilder,
    private alertService: AlertService,
    private adminService: AdminService,
  ) { }

  ngOnInit() {
  }


  getStudentFee(student) {
    this.selectedStudentDetails = (student) ? student : {};
    this.studentFeeForm = this.getStudentFeeForm();
    this.studentFeeForm.patchValue(this.selectedStudentDetails);
    this.modifyResponse(student);
    this.subscribeToCalculateDiscount();
  }

  getStudentFeeForm() {

    return this.fb.group({
      active: [''],
      adjust: [''],
      admissionCharges: [''],
      admissionNumber: [''],
      annualFee: [''],
      baseFee: [0],
      baseFeeGst: [''],
      center: this.fb.group({
        active: [''],
        capacity: [''],
        code: [''],
        id: [''],
        name: [''],
        type: [''],
      }),
      cgst: [''],
      comment: [''],
      corporate: [''],
      discount: [''],
      discountAdmissionCharges: [''],
      discountAnnualCharges: [''],
      discountBaseFee: [''],
      discountSecurityDeposit: [''],
      feeDuration: [''],
      finalAdmissionCharges: [''],
      finalAnnualFee: [''],
      finalBaseFee: [''],
      finalFee: [''],
      finalSecurityDeposit: [''],
      firstName: [''],
      formalSchool: [''],
      found: [''],
      fullName: [''],
      group: this.fb.group({
        description: [''],
        id: [''],
        name: [''],
      }),

      gstAmount: [''],
      gstFee: [0],
      id: [''],
      igst: [''],
      lastName: [''],
      leavingDate: [''],
      program: this.fb.group({
        code: [''],
        description: [''],
        groups: [''],
        id: [''],
        name: [''],
      }),
      schoolName: [''],
      securityDeposit: [''],
      sgst: [''],
      studentId: [''],
      transportFee: [''],


    });
  }

  modifyResponse(student) {
    this.studentFeeForm.get('finalBaseFee').setValue(student.finalBaseFee / 3);

    if (student.program.id === 622614691413790 || student.formalSchool) {
      this.studentFeeForm.get('gstFee').setValue((this.studentFeeForm.get('finalAnnualFee').value * .18).toFixed(2)); // annual-fee-gst
      this.studentFeeForm.get('baseFeeGst').setValue((this.studentFeeForm.get('finalBaseFee').value * 3 * .18).toFixed(2));
    }
  }

  saveStudentFee() {

    this.adminService.studentFeeUpdate(this.studentFeeForm.value)
      .subscribe((res: any) => {
        _.extend(this.updateFee, res);
        this.alertService.successAlert(' ');
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }



  subscribeToCalculateDiscount() {
    this.studentFeeForm.get('finalAnnualFee').valueChanges
      .subscribe((val) => { this.updateDiscount(); });

    this.studentFeeForm.get('finalAdmissionCharges').valueChanges
      .subscribe((val) => { this.updateDiscount(); });

    this.studentFeeForm.get('finalSecurityDeposit').valueChanges
      .subscribe((val) => { this.updateDiscount(); });

    this.studentFeeForm.get('finalBaseFee').valueChanges
      .subscribe((val) => { this.updateDiscount(); });

    this.studentFeeForm.get('transportFee').valueChanges
      .subscribe((val) => { this.updateDiscount(); });

  }


  updateDiscount() {
    this.studentFeeForm.get('baseFeeGst').setValue((this.studentFeeForm.get('finalBaseFee').value * 3 * .18).toFixed(2));
    this.studentFeeForm.get('gstFee').setValue((this.studentFeeForm.get('finalAnnualFee').value * .18).toFixed(2)); // annual-fee-gst

    const val = this.studentFeeForm.get('finalAnnualFee').value + this.studentFeeForm.get('finalAdmissionCharges').value +
      this.studentFeeForm.get('finalSecurityDeposit').value + (this.studentFeeForm.get('finalBaseFee').value * 3) +
      (this.studentFeeForm.get('transportFee').value * 3 ) + Number(this.studentFeeForm.get('gstFee').value) +
      Number(this.studentFeeForm.get('baseFeeGst').value);

    this.studentFeeForm.get('finalFee').setValue(Number(val).toFixed(2));
  }

  setDiscount(base, targetDiscount, final) {



    if (this.studentFeeForm.get(final).value === this.studentFeeForm.get(base).value) {
      if (this.studentFeeForm.get(base).value === 0) {
        return;
      } else {
        this.studentFeeForm.get(targetDiscount).setValue(0);
        this.studentFeeForm.get(final).setValue(this.studentFeeForm.get(base).value);
        return;
      }

    }

    const finalChange = this.studentFeeForm.get(final).value || 0;

    if (this.studentFeeForm.get(base).value - finalChange > 0) {

      const disc = Number((((this.studentFeeForm.get(base).value - finalChange)
        / this.studentFeeForm.get(base).value) * 100)
        .toFixed(2));

      this.studentFeeForm.get(targetDiscount).setValue(disc);
    } else {
      this.studentFeeForm.get(targetDiscount).setValue(0);
      this.studentFeeForm.get(final).setValue(this.studentFeeForm.get(base).value);
    }






  }

  hideSidePanel() {
    this.adminService.viewPanel.next(false);

  }



}
