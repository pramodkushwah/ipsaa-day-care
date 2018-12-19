import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';
import * as _ from 'underscore';

declare const $: any;

@Component({
  selector: 'app-fee-receipt-info',
  templateUrl: './fee-receipt-info.component.html',
  styleUrls: ['./fee-receipt-info.component.css']
})
export class FeeReceiptInfoComponent implements OnInit {
  currentYear: number;

  years = [];
  paymentMode = ['Cash', 'Cheque', 'NEFT', 'Card'];

  studentDetails: Array<any>;
  viewPanel = false;
  mailPanel = false;
  recordPayment = false;
  downloadReceipt = false;
  currentDate: Date;
  selectedStudentDetails: any = {};
  feePaymentForm: FormGroup;
  allItems: any;
  showtable = false;
  downloadinData: boolean;
  replyText: string;
  updateReceipt: any = {};
  paymentUpdate: any = {};
  selectedPayment: any;
  STUDENTFEE_RECEIPT_CONFIRM = this.adminService.hasPrivilage('STUDENTFEE_RECEIPT_CONFIRM');
  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) {
    this.currentDate = new Date();
    this.feePaymentForm = this.getReceiptForm();
  }


  @Input() set studentReceipt(studentReceipt: any) {
    this.updateReceipt = studentReceipt;
    this.selectedStudentDetails = (studentReceipt) ? studentReceipt : {};
    this.feePaymentForm = this.getReceiptForm();
    this.feePaymentForm.patchValue(this.selectedStudentDetails);
    this.feePaymentForm.controls['paymentDate'].patchValue(studentReceipt.invoiceDate);
  }
  ngOnInit() {
    console.log(this.selectedStudentDetails);

  }


  getReceiptForm() {
    return this.fb.group({

      addmissionFeeDiscount: [{ value: '', disabled: false }],
      addmissionPaidAmountTotal: [{ value: '', disabled: false }],
      adjust: [{ value: '', disabled: false }],
      admissionFee: [{ value: '', disabled: false }],
      annualFee: [{ value: '', disabled: false }],
      annualFeeDiscount: [{ value: '', disabled: false }],
      annualPaidAmountTotal: [{ value: '', disabled: false }],
      autoComments: [{ value: '', disabled: false }],
      balance: [{ value: '', disabled: false }],
      baseFee: [{ value: '', disabled: false }],
      baseFeeDiscount: [{ value: '', disabled: false }],
      cgst: [{ value: '', disabled: false }],
      comments: [{ value: '', disabled: false }],
      deposit: [{ value: '', disabled: false }],
      depositFeeDiscount: [{ value: '', disabled: false }],
      depositPaidAmountTotal: [{ value: '', disabled: false }],
      extraCharge: [{ value: '', disabled: false }],
      fee: [{ value: '', disabled: false }],
      feeDuration: [{ value: '', disabled: false }],
      feeRatio: [{ value: '', disabled: false }],
      finalAdmissionFee: [{ value: '', disabled: false }],
      finalAnnualCharges: [{ value: '', disabled: false }],
      finalBaseFee: [{ value: '', disabled: false }],
      finalDepositFee: [{ value: '', disabled: false }],
      finalTransportFee: [{ value: '', disabled: false }],
      fullName: [{ value: '', disabled: false }],
      generateActive: [{ value: '', disabled: false }],
      group: [{ value: '', disabled: false }],
      gstAmount: [{ value: '', disabled: false }],
      id: [{ value: '', disabled: false }],
      igst: [{ value: '', disabled: false }],
      invoiceDate: [{ value: '', disabled: false }],
      latePaymentCharge: [{ value: '', disabled: false }],
      month: [{ value: '', disabled: false }],
      payableAmount: [{ value: '', disabled: true }],
      payments: [{ value: '', disabled: false }],
      program: [{ value: '', disabled: false }],
      programPaidAmountTotal: [{ value: '', disabled: false }],
      quarter: [{ value: '', disabled: false }],
      sgst: [{ value: '', disabled: false }],
      stationary: [{ value: '', disabled: false }],
      stationaryPaidAmountTotal: [{ value: '', disabled: false }],
      status: [{ value: '', disabled: false }],
      paymentDate: [{ value: this.currentDate.toISOString().slice(0, 10), disabled: false }],
      totalFee: [{ value: '', disabled: true }],
      totalOtherPaidAmount: [{ value: '', disabled: false }],
      totalOtherRemainningAmount: [{ value: '', disabled: false }],
      totalPaidAmount: [{ value: '', disabled: false }],
      transportFee: [{ value: '', disabled: false }],
      transportPaidAmountTotal: [{ value: '', disabled: false }],
      uniformCharges: [{ value: '', disabled: false }],
      uniformPaidAmountTotal: [{ value: '', disabled: false }],
      year: [{ value: '', disabled: false }],
      finalFee: [{ value: '', disabled: false }],
      paymentMode: [{ value: '', disabled: false }],
      paidAmount: [{ value: '', disabled: false }],
      txnid: [{ value: '', disabled: false }],
      // confirmed: [{ value: false, disabled: false }]

    });
  }

  paymentRecord() {
    this.recordPayment = true;
    this.adminService.payStudentFee(this.feePaymentForm.value)
      .subscribe((res) => {
        _.extend(this.updateReceipt, res);
        this.feePaymentForm.get('paymentMode').reset();
        this.feePaymentForm.get('txnid').reset();
        this.feePaymentForm.get('paidAmount').reset();

        this.recordPayment = false;
        if (this.feePaymentForm.get('paidAmount').value > this.feePaymentForm.get('payableAmount').value) {
          this.alertService.successAlert('thank you for paying in advance');
        } else {
          this.alertService.successAlert('');
        }
        this.feePaymentForm.patchValue(this.selectedStudentDetails);
        // this.hideSidePanel();
      }, (err) => {
        this.recordPayment = false;
      });

  }

  receiptDownload() {

    this.downloadReceipt = true;
    this.adminService.downloadReceipt(this.selectedStudentDetails.id)
    .subscribe((res) => {
      const blob = new Blob([res.body], {
      });
      FileSaver.saveAs(blob, res.headers.get('fileName'));

        this.alertService.successAlert('');
        this.downloadReceipt = false;
      }, (err) => {
        this.downloadReceipt = false;
      });
  }




  sendMailPanel(value) {
    this.mailPanel = value;

  }
  hideSidePanel() {
    this.adminService.viewPanelForFee.next(false);
  }

  upDatePayment(payment, val) {
    this.paymentUpdate = {};
    this.paymentUpdate['id'] = payment.id;

    if (val === 'confirm') {
      this.paymentUpdate['confirmed'] = true;

    } else {
      this.paymentUpdate['confirmed'] = false;
      this.paymentUpdate['comments'] = this.replyText;

    }

    if (payment && payment.id) {
      payment.disabled = true;
      this.adminService.studentPaymentConfirm(this.paymentUpdate)
        .subscribe((res: any) => {
          payment.disabled = false;
          $('#rejectReply').modal('hide');
          for (let i = 0; i < this.selectedStudentDetails.payments.length; i++) {
            if (this.selectedStudentDetails.payments[i].id === res.id) {
              _.extend(this.updateReceipt.payments[i], res);
              this.selectedStudentDetails.payments[i] = res;

            }
          }
          this.paymentUpdate = {};
        }, (err) => {
          payment.disabled = false;
        });
    }
  }

  rejectPayment(payments) {
    this.selectedPayment = payments;
  }
}
