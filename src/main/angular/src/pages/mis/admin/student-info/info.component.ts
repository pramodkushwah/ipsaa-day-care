import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import * as _ from 'underscore';
import { AlertService } from '../../../../providers/alert/alert.service';
import { DatePipe } from '@angular/common';
declare let $: any;

@Component({
  selector: 'app-student-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class StudentInfoComponent implements OnInit {
  student: any = {};
  editable: boolean;
  studentForm: FormGroup;
  private programs: any[];
  centers: any[];
  groups: any[];
  bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-', 'NA'];
  quarters: any[] = ['FYQ4', 'FYQ1', 'FYQ2', 'FYQ3'];
  newStudent: boolean;
  sibling: any = {};
  programFee: any;
  students: any;
  siblingCenter: any = {};
  siblingProgram: any = {};
  siblingGroup: any = {};
  paymentHistory: any[] = [];
  @Input()
  set id(id: number) {
    if (id) {
      this.newStudent = false;
      this.adminService.getStudentById(id).subscribe((student: any) => {
        this.student = student;
        this.studentForm.patchValue(student);
        this.studentForm.controls['centerId'].patchValue(student.center.id);
        this.studentForm.controls['groupId'].patchValue(student.group.id);
        this.studentForm.controls['programId'].patchValue(student.program.id);
        this.getPaymentHistory(student);
      });
    } else {
      this.newStudent = true;
      this.studentForm = this.getStudentForm();
    }
  }

  @Input()
  set update(update: boolean) {
    this.editable = update;
  }

  @Output() getPayReceiptHistory: EventEmitter<any> = new EventEmitter<any>();
  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService,
    private datePipe: DatePipe
  ) { }

  ngOnInit() {
    this.getStudents();
    this.getCenters();
    this.getPrograms();
    this.getGroups();
    this.studentForm = this.getStudentForm();
  }

  getStudentForm() {
    return this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      nickName: [''],
      centerId: ['', [Validators.required]],
      programId: ['', [Validators.required]],
      groupId: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      bloodGroup: [''],
      familyType: [''],
      nationality: ['', [Validators.required]],
      expectedIn: ['', [Validators.required]],
      expectedOut: ['', [Validators.required]],
      corporate: [false],
      formalSchool: [false],
      schoolName: [''],
      siblingId: [null],
      active: [''],
      admissionDate: [this.datePipe.transform(new Date(), 'yyyy-MM-dd')],
      admissionNumber: [''],
      approvalStatus: [''],
      dob: [''],
      fullName: [''],
      id: [''],
      imagePath: [''],
      mode: [''],
      profile: [''],
      parents: this.fb.array([this.getParentData(), this.getParentData()]),
      fee: this.getFeeField()
    });
  }

  getProgramsByCenter(centerId: number) {
    this.programs = [];
    this.adminService.getProgramsByCenterId(centerId).subscribe((response: any) => {
      this.programs = response;
    });
    this.studentForm.controls['programId'].reset();
  }

  getParentData() {
    return this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      mobile: ['', [Validators.required]],
      email: ['', [Validators.required]],
      smsEnabled: [''],
      emailEnabled: [''],
      educationalQualification: ['', [Validators.required]],
      occupation: ['', [Validators.required]],
      designation: ['', [Validators.required]],
      residentialAddress: this.getAddressField(),
      officeAddress: this.getAddressField(),
      account: [''],
      emergencyContact: [''],
      fullName: [''],
      id: [''],
      organisation: [''],
      relationship: [''],
      secondaryNumbers: ['']
    });
  }

  getAddressField() {
    return this.fb.group({
      address: ['', [Validators.required]],
      city: ['', [Validators.required]],
      state: ['', [Validators.required]],
      zipcode: ['', [Validators.required]],
      addressType: [''],
      phone: ['']
    });
  }

  getFeeField() {
    return this.fb.group({
      annualFee: [0],
      discountAnnualCharges: [0],
      finalAnnualFee: [0],
      admissionFee: [0],
      discountAdmissionCharges: [0],
      finalAdmissionCharges: [0],
      baseFee: [0],
      discountBaseFee: [0],
      finalBaseFee: [0],
      deposit: [0],
      discountSecurityDeposit: [0],
      finalSecurityDeposit: [0],
      transportFee: [0],
      uniformCharges: [0],
      stationary: [0],
      comment: [''],
      gstFee: [0],
      baseFeeGst: [0],
      finalFee: [0],
      formalSchool: [false]
    });
  }

  hideViewPanel() {
    this.adminService.viewPanel.next(false);
    this.adminService.viewPanelForFee.next(false);
  }

  getCenters() {
    this.adminService.getCenters().subscribe((response: any[]) => {
      this.centers = response;
    });
  }

  getGroups() {
    this.adminService.getGroups().subscribe((response: any[]) => {
      this.groups = response;
    });
  }

  getPrograms() {
    this.adminService.getPrograms().subscribe((response: any[]) => {
      this.programs = response;
    });
  }

  getFee(programId: number) {
    if (programId && this.studentForm.controls['centerId'].value) {
    this.adminService
      .getProgramFee({
        centerId: this.studentForm.controls['centerId'].value,
        programId: programId
      })
      .subscribe((response: any) => {
        this.programFee = response;
        if (this.studentForm.contains('fee')) {
          const feeControlForm = <FormGroup>this.studentForm.controls['fee'];
          feeControlForm.patchValue(response);
          feeControlForm.controls['baseFee'].patchValue(response.fee); // Monthly Fees
          feeControlForm.controls['finalBaseFee'].patchValue(response.fee); // Final Monthly Fees
          feeControlForm.controls['finalAnnualFee'].patchValue(
            response.annualFee
          );
          feeControlForm.controls['finalAdmissionCharges'].patchValue(
            response.admissionFee
          );
          feeControlForm.controls['finalSecurityDeposit'].patchValue(
            response.deposit
          );
          const sprogram = this.programs.find(program => program.id === programId);
          this.groups = (sprogram) ? sprogram.groups : [];
        }
      });
    }
  }

  uploadProfilePic(student: any, file: any) {
    console.log('asdfdsf', file);
    const formData = new FormData();
    formData.append('file', file);
    if (file) {
      this.adminService.uploadStudentProfilePic(student.id, formData).subscribe(
        (response: any) => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = function (e: any) {
            $('#student-profile').attr('src', e.target.result);
          };
        },
        (error: any) => {
          this.alertService.errorAlert(error);
        }
      );
    }
  }

  saveStudent() {
    this.studentForm.value['dob'] = this.datePipe.transform(this.studentForm.controls['dob'].value, 'yyyy-MM-dd');
    if (this.editable) {
      this.adminService
        .updateStudent(this.studentForm.value)
        .subscribe((response: any) => {
          _.extend(this.student, response);
          this.alertService.successAlert('Student Info Successfully updated.');
          this.adminService.viewPanel.next(false);
        });
    } else {
      this.adminService
        .addStudent(this.studentForm.value)
        .subscribe((response: any) => {
          this.alertService.successAlert('Student Info Successfully added.');
          this.adminService.viewPanel.next(false);
        });
    }
  }

  removeFee(isCorporative: boolean) {
    if (isCorporative) {
      this.studentForm.removeControl('fee');
    } else {
      if (this.studentForm.contains('fee')) {
        this.studentForm.controls['fee'].patchValue(this.student.fee);
      } else {
        this.studentForm.addControl('fee', this.getFeeField());
        this.student.fee
          ? this.studentForm.controls['fee'].patchValue(this.student.fee)
          : this.getFee(this.studentForm.controls['programId'].value);
      }
    }
  }

  calculateDiscount(base: string, final: string, targetDiscount: string) {
    const feeControlForm = <FormGroup>this.studentForm.controls['fee'];
    const fee = feeControlForm.value;
    if (fee[base] > 0 && fee[final]) {
      if (fee[base] - fee[final] > 0) {
        feeControlForm.controls[targetDiscount].patchValue(
          Number((((fee[base] - fee[final]) / fee[base]) * 100).toFixed(2))
        );
      } else {
        feeControlForm.controls[targetDiscount].patchValue(0);
      }
    }

    this.calculateGstFee(fee, this.studentForm.value);
    this.calculateFinalFee(fee);
  }

  calculateGstFee(fee, student) {
    const feeControlForm = <FormGroup>this.studentForm.controls['fee'];
    if (typeof student !== 'undefined' && student.formalSchool) {
      feeControlForm.controls['gstFee'].patchValue(
        Number((Number(fee.finalAnnualFee) * 0.18).toFixed(2))
      ); // annual-fee-gst
      feeControlForm.controls['baseFeeGst'].patchValue(
        Number((Number(fee.finalBaseFee) * 3 * 0.18).toFixed(2))
      );
    } else if (fee.formalSchool) {
      feeControlForm.controls['gstFee'].patchValue(
        Number((Number(fee.finalAnnualFee) * 0.18).toFixed(2))
      );
      feeControlForm.controls['baseFeeGst'].patchValue(
        Number((Number(fee.finalBaseFee) * 3 * 0.18).toFixed(2))
      );
    } else {
      feeControlForm.controls['gstFee'].patchValue(0);
      feeControlForm.controls['baseFeeGst'].patchValue(0);
    }
  }

  calculateFinalFee(fee) {
    const feeControlForm = <FormGroup>this.studentForm.controls['fee'];
    fee.finalTransportFees = fee.transportFee ? fee.transportFee * 3 : 0;
    let final = 0;
    if (fee.finalAnnualFee > 0) {
      final += Number(fee.finalAnnualFee);
    }
    if (fee.finalAdmissionCharges > 0) {
      final += Number(fee.finalAdmissionCharges);
    }
    if (fee.finalBaseFee > 0) {
      final += Number(fee.finalBaseFee * 3);
    }
    if (fee.finalSecurityDeposit > 0) {
      final += Number(fee.finalSecurityDeposit);
    }
    if (fee.finalTransportFees > 0) {
      final += Number(fee.finalTransportFees);
    }

    if (fee.uniformCharges > 0) {
      final += Number(fee.uniformCharges);
    }

    if (fee.stationary > 0) {
      final += Number(fee.stationary);
    }

    if (fee.gstFee > 0) {
      final += Number(fee.gstFee);
    }

    if (fee.baseFeeGst > 0) {
      final += Number(fee.baseFeeGst);
    }

    feeControlForm.controls['finalFee'].patchValue(Number(final.toFixed(2)));
  }

  monthlyTransportFeesChanged(fee) {
    if (fee.transportFee > 0) {
      fee.finalTransportFees = fee.transportFee * 3;
    } else {
      fee.transportFee = 0;
      fee.finalTransportFees = 0;
    }
    this.calculateFinalFee(fee);
  }

  monthlyUniformChargesChanged(fee) {
    if (fee.uniformCharges < 0) {
      fee.uniformCharges = 0;
    }
    this.calculateFinalFee(fee);
  }

  monthlyStationeryChargesChanged(fee) {
    if (fee.satationary < 0) {
      fee.satationary = 0;
    }
    this.calculateFinalFee(fee);
  }

  formalClicked(formalSchool: boolean) {
    if (this.studentForm.contains('fee')) {
      const feeConrol = <FormGroup>this.studentForm.controls['fee'];
      feeConrol.controls['formalSchool'].patchValue(formalSchool);
      this.calculateGstFee(feeConrol.value, this.studentForm.value);
      this.calculateFinalFee(feeConrol.value);
    }
  }

  getStudents() {
    const object = {
      active: true,
      pageNumber: 0,
      pageSize: 0,
      programCode: 'ALL'
    };

    this.students = [];
    this.adminService.getStudents(object).subscribe((response: any) => {
      this.students = response.students;
    });
  }

  siblingSelected(siblingStudent: any) {
    this.adminService.getStudentById(siblingStudent.id).subscribe((student: any) => {
      this.studentForm.controls['parents'].patchValue(student.parents);
      this.studentForm.controls['siblingId'].patchValue(student.id);
    });
  }

  getPaymentHistory(student) {
    this.adminService.getStudentPaymentHistory(student.id).subscribe((response: any) => {
      this.paymentHistory = response.payments;
      console.log(response);
    });
  }


  selectedPaymentHistoryDetails(history) {
   this.getPayReceiptHistory.emit(history);
   this.adminService.viewPanelForFee.next(true);
  }
}
