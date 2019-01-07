import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';
declare const $: any;

@Component({
  selector: 'app-generate-fee-slip',
  templateUrl: './generate-fee-slip.component.html',
  styleUrls: ['./generate-fee-slip.component.css']
})
export class GenerateFeeSlipComponent implements OnInit {

  centers: Array<any>;
  quaters = [{ Qtype: 'FYQ1', id: 2 }, { Qtype: 'FYQ2', id: 3 }, { Qtype: 'FYQ3', id: 4 }, { Qtype: 'FYQ4', id: 1 }];
  currentYear: number;
  years = [];
  studentDetails = [];
  viewPanel = false;
  mailPanel = false;
  saveFeeSlip = false;
  regenerateSlip = false;
  studentIds: any = {};
  selectAllStudent: boolean;
  downloadingSlips = false;
  lockSlip = false;
  regeneratingSlip = false;
  selectedStudentDetails: any = {};
  regenerateSlipForm: FormGroup;
  eMailForm: FormGroup;
  ids: any[] = [];
  unlockIds: any[] = [];
  allItems: any;
  showtable = false;
  downloadinData: boolean;
  regenrateSpecificDate: any;
  studentDetailsCopy: any;
  statusFilter = 'All';
  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) {
    this.currentYear = (new Date).getFullYear();
    this.years.push(this.currentYear - 1);
    this.years.push(this.currentYear);
    this.years.push(this.currentYear + 1);


  }


  generateSlipForm = this.fb.group({
    center: ['', Validators.required],
    quater: ['', Validators.required],
    year: ['', Validators.required],
  });

  getEmailForm() {
    return this.fb.group({
      subject: ['', Validators.required],
      body: ['', Validators.required],
    });
  }


  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;
      });

  }


  generate() {
    this.studentIds = {};
    this.downloadinData = false;
    this.alertService.loading.next(true);
    this.adminService.generateStudentFeeSlip({
      'centerCode': this.generateSlipForm.value.center,
      'period': 'Quarterly',
      'quarter': this.generateSlipForm.value.quater,
      'year': this.generateSlipForm.value.year
    }).subscribe((res) => {
      this.alertService.loading.next(false);
      this.showtable = true;
      this.downloadinData = true;
      this.ids = [];
      this.selectAllStudent = false;
      this.studentDetails = res;
      this.studentDetailsCopy = res;
      this.allItems = res.slice(0);
    }, (err) => {
      this.alertService.loading.next(false);
      this.downloadinData = true;
      this.showtable = true;
      this.ids = [];
      this.selectAllStudent = false;

    });

  }

  filterByPaymentStatus(status: string) {
    this.studentDetails = this.studentDetailsCopy;
    if (status === 'All') {
      this.studentDetails = this.studentDetailsCopy;
      return;
    }
    this.studentDetails = this.studentDetails.filter(slip => {
      return slip.status === status;
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




  showSidePanel(value: boolean, student: object) {
    this.viewPanel = value;
    this.mailPanel = false;
    this.selectedStudentDetails = (student) ? student : {};
    this.regenerateSlipForm = this.getRegenerateSlipForm();
    console.log(this.regenerateSlipForm.value);

    this.regenerateSlipForm.patchValue(this.selectedStudentDetails);

    this.subscribeToCalculateFinalFee();
  }

  subscribeToCalculateFinalFee() {
    this.regenerateSlipForm.get('balance').valueChanges
      .subscribe((val) => { this.updateFinalFee(); });

    this.regenerateSlipForm.get('latePaymentCharge').valueChanges
      .subscribe((val) => { this.updateFinalFee(); });

    this.regenerateSlipForm.get('adjust').valueChanges
      .subscribe((val) => { this.updateFinalFee(); });

    this.regenerateSlipForm.get('extraCharge').valueChanges
      .subscribe((val) => { this.updateFinalFee(); });

    this.regenerateSlipForm.get('stationary').valueChanges
      .subscribe((val) => { this.updateFinalFee(); });

    this.regenerateSlipForm.get('uniformCharges').valueChanges
      .subscribe((val) => { this.updateFinalFee(); });
  }

  updateFinalFee() {
    const val = this.regenerateSlipForm.get('balance').value + this.regenerateSlipForm.get('adjust').value +
      this.regenerateSlipForm.get('latePaymentCharge').value + this.regenerateSlipForm.get('extraCharge').value +
      this.regenerateSlipForm.get('stationary').value + this.regenerateSlipForm.get('uniformCharges').value +
      this.regenerateSlipForm.get('finalAnnualCharges').value + this.regenerateSlipForm.get('finalAdmissionFee').value +
      this.regenerateSlipForm.get('finalBaseFee').value + this.regenerateSlipForm.get('finalDepositFee').value +
      this.regenerateSlipForm.get('transportFee').value + this.regenerateSlipForm.get('gstAmount').value;

    this.regenerateSlipForm.get('totalFee').setValue(val);
  }

  getRegenerateSlipForm() {
    return this.fb.group({

      addmissionFeeDiscount: [''],
      addmissionPaidAmountTotal: [''],
      adjust: [''],
      admissionFee: [''],
      annualFee: [''],
      annualFeeDiscount: [''],
      annualPaidAmountTotal: [''],
      autoComments: [''],
      balance: [''],
      baseFee: [''],
      baseFeeDiscount: [''],
      cgst: [''],
      comments: ['', Validators.required],
      deposit: [''],
      depositFeeDiscount: [''],
      depositPaidAmountTotal: [''],
      extraCharge: [''],
      fee: [''],
      feeDuration: [''],
      feeRatio: [''],
      finalAdmissionFee: [''],
      finalAnnualCharges: [''],
      finalBaseFee: [''],
      finalFee: [''],
      finalDepositFee: [''],
      finalTransportFee: [''],
      fullName: [''],
      generateActive: [''],
      group: [''],
      gstAmount: [''],
      id: [''],
      igst: [''],
      invoiceDate: [''],
      latePaymentCharge: [''],
      month: [''],
      payableAmount: [''],
      payments: [''],
      program: [''],
      programPaidAmountTotal: [''],
      quarter: [''],
      sgst: [''],
      stationary: [''],
      stationaryPaidAmountTotal: [''],
      status: [''],
      totalFee: [''],
      totalOtherPaidAmount: [''],
      totalOtherRemainningAmount: [''],
      totalPaidAmount: [''],
      transportFee: [''],
      transportPaidAmountTotal: [''],
      uniformCharges: [''],
      uniformPaidAmountTotal: [''],
      year: [''],
    });
  }
  ngOnInit() {
    this.getCenter();
  }


  selectStudents() {
    this.ids = [];
    this.unlockIds = [];
    Object.keys(this.studentIds).forEach(id => {
      if (this.studentIds[id]) {
        this.ids.push(id);
        this.studentDetails.forEach(element => {
          if (element.id === Number(id) && !element.generateActive) {
            this.unlockIds.push(id);
          }
        });
      }
    });

  }

  selectAll(isChecked: boolean) {
    if (isChecked) {
      this.allItems.forEach(student => {
        this.studentIds[student.id] = true;
      });
    } else {
      this.allItems.forEach(student => {
        this.studentIds[student.id] = false;
      });
      this.selectAllStudent = false;
    }
    this.selectStudents();
  }



  saveSlip(value) {

    if (value === 'save') {
      this.saveFeeSlip = true;


      this.adminService.saveFeeSlipChanges(this.regenerateSlipForm.value)
        .subscribe((res: any) => {
          this.studentDetails = this.studentDetails.filter(element => element.id !== this.regenerateSlipForm.value.id);
          this.studentDetails.push(res);
          this.saveFeeSlip = false;
          this.alertService.successAlert('');
        }, (err) => {
          this.saveFeeSlip = false;
        });
    }
    if (value === 'Regenerate') {
      this.regenerateSlip = true;

      this.adminService.regenerateStudentsFeeSlips({ 'id': this.selectedStudentDetails.id }, 'regenerate')
        .subscribe((res: any) => {
          $('#regenerateDatePopUp').modal('hide');

          this.regenerateSlip = false;
          this.alertService.successAlert('');
        }, (err) => {
          this.regenerateSlip = false;
        });
    }

    if (value === 'SpecificDate') {
      this.regenerateSlip = true;

      this.adminService.regenerateStudentsFeeSlips({
        'id': this.selectedStudentDetails.id,
        'spaceifyRegenrationDate': this.regenrateSpecificDate
      }, 'regenerate')
        .subscribe((res: any) => {
          this.regenerateSlip = false;
          $('#SpecificDate').modal('hide');

          this.alertService.successAlert('');
        }, (err) => {
          this.regenerateSlip = false;
        });
    }



  }

  downloadSlips() {
    this.downloadingSlips = true;

    this.adminService.downloadFeeSlips(this.ids)
      .subscribe((res) => {
        const blob = new Blob([res.body], {
        });
        FileSaver.saveAs(blob, res.headers.get('fileName'));

        this.downloadingSlips = false;
        this.alertService.successAlert('');
      }, (err) => {
        this.downloadingSlips = false;
      }
      );
  }

  lockFee() {
    this.lockSlip = true;

    this.adminService.lockStudentsFeeSlips(this.unlockIds)
      .subscribe((res: any) => {

        this.lockSlip = false;
        this.alertService.successAlert('');
      }, (err) => {
        this.lockSlip = false;
      });
  }

  regenerateSlips() {
    this.regeneratingSlip = true;


    this.adminService.regenerateStudentsFeeSlips(this.unlockIds, 'regenerateAll')
      .subscribe((res: any) => {
        this.regeneratingSlip = false;

        this.alertService.successAlert('');
        this.generate();

      }, (err) => {
        this.regeneratingSlip = false;
      });
  }


  sendMailPanel(value) {
    this.viewPanel = value;
    this.mailPanel = value;

    this.eMailForm = this.getEmailForm();
  }

  sendMail() {
    this.adminService.sendEmails({ 'subject': this.eMailForm.value.subject, 'body': this.eMailForm.value.body, 'slipIds': this.ids })
      .subscribe((res: any) => {
        this.ids = [];
        this.eMailForm.reset();
        this.alertService.successAlert('');
        this.showSidePanel(false, null);
      });
  }

  dropped(event) {
    console.log(event);
    this.eMailForm.controls['body'].patchValue(event.emailcontent || '');
  }
}
