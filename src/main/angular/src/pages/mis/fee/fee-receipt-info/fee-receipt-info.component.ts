import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';
import * as _ from 'underscore';
import { DatePipe } from '@angular/common';

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
  // currentDate: Date;
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
    private alertService: AlertService,
    private datePipe: DatePipe
  ) {
    // this.currentDate = new Date();
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

      addmissionFeeDiscount: [{ value: 0, disabled: false }],
      addmissionPaidAmountTotal: [{ value: 0, disabled: false }],
      adjust: [{ value: 0, disabled: false }],
      admissionFee: [{ value: 0, disabled: false }],
      annualFee: [{ value: 0, disabled: false }],
      annualFeeDiscount: [{ value: 0, disabled: false }],
      annualPaidAmountTotal: [{ value: 0, disabled: false }],
      autoComments: [{ value: 0, disabled: false }],
      balance: [{ value: 0, disabled: false }],
      baseFee: [{ value: 0, disabled: false }],
      baseFeeDiscount: [{ value: 0, disabled: false }],
      cgst: [{ value: 0, disabled: false }],
      comments: [{ value: 0, disabled: false }],
      deposit: [{ value: 0, disabled: false }],
      depositFeeDiscount: [{ value: 0, disabled: false }],
      depositPaidAmountTotal: [{ value: 0, disabled: false }],
      extraCharge: [{ value: 0, disabled: false }],
      fee: [{ value: 0, disabled: false }],
      feeDuration: [{ value: 0, disabled: false }],
      feeRatio: [{ value: 0, disabled: false }],
      finalAdmissionFee: [{ value: 0, disabled: false }],
      finalAnnualCharges: [{ value: 0, disabled: false }],
      finalBaseFee: [{ value: 0, disabled: false }],
      finalDepositFee: [{ value: 0, disabled: false }],
      finalTransportFee: [{ value: 0, disabled: false }],
      fullName: [{ value: 0, disabled: false }],
      generateActive: [{ value: 0, disabled: false }],
      group: [{ value: 0, disabled: false }],
      gstAmount: [{ value: 0, disabled: false }],
      id: [{ value: 0, disabled: false }],
      igst: [{ value: 0, disabled: false }],
      invoiceDate: [{ value: 0, disabled: false }],
      latePaymentCharge: [{ value: 0, disabled: false }],
      month: [{ value: 0, disabled: false }],
      payableAmount: [{ value: 0, disabled: true }],
      payments: [{ value: 0, disabled: false }],
      program: [{ value: 0, disabled: false }],
      programPaidAmountTotal: [{ value: 0, disabled: false }],
      quarter: [{ value: 0, disabled: false }],
      sgst: [{ value: 0, disabled: false }],
      stationary: [{ value: 0, disabled: false }],
      stationaryPaidAmountTotal: [{ value: 0, disabled: false }],
      status: [{ value: 0, disabled: false }],
      paymentDate: [this.datePipe.transform(new Date(), 'yyyy-MM-dd')],
      totalFee: [{ value: 0, disabled: true }],
      totalOtherPaidAmount: [{ value: 0, disabled: false }],
      totalOtherRemainningAmount: [{ value: 0, disabled: false }],
      totalPaidAmount: [{ value: 0, disabled: false }],
      transportFee: [{ value: 0, disabled: false }],
      transportPaidAmountTotal: [{ value: 0, disabled: false }],
      uniformCharges: [{ value: 0, disabled: false }],
      uniformPaidAmountTotal: [{ value: 0, disabled: false }],
      year: [{ value: 0, disabled: false }],
      finalFee: [{ value: 0, disabled: false }],
      paymentMode: [{ value: 0, disabled: false }],
      paidAmount: [{ value: 0, disabled: false }],
      txnid: [{ value: 0, disabled: false }],
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
