import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';
declare let $: any;

@Component({
  selector: 'app-ipsaaclub',
  templateUrl: './ipsaaclub.component.html',
  styleUrls: ['./ipsaaclub.component.css']
})
export class IpsaaclubComponent implements OnInit {
  centers: any;
  comment: string;
  generatedFeeSlips: any[] = [];
  allchecked = false;
  checkedSlipCount: any = 0;
  selectedStudentFee: any;
  showPanel = '';
  selectedCenter: any;
  selected: any;
  disabledSlipDownload: boolean;
  disabledReceiptDownload: boolean;
  slipEmail: any = {};
  sendPaymentLinkDisable: boolean;
  disabledRecordPayment: boolean;
  selectedPayment: any;
  STUDENTFEE_WRITE: boolean;
  STUDENTFEE_RECEIPT_CONFIRM: boolean;

  constructor(private adminService: AdminService, private fb: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.STUDENTFEE_WRITE = this.adminService.hasPrivilage('STUDENTFEE_WRITE');
    this.STUDENTFEE_RECEIPT_CONFIRM = this.adminService.hasPrivilage('STUDENTFEE_RECEIPT_CONFIRM');
    this.getCenter();
  }

  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;
      });
  }

  getGeneratedFeeSlips(center) {
    if (center) {
      this.adminService.getIpsaaClubFeeSlips(center.code).subscribe(response => {
        this.generatedFeeSlips = response;
        this.selectedStudentFee = null;
        this.selected = {};
      });
    }
  }

  toggleAll(allchecked) {
    this.allchecked = allchecked;
    this.checkedSlipCount = 0;
    if (this.generatedFeeSlips) {
      for (let i = 0; i < this.generatedFeeSlips.length; i++) {
        this.generatedFeeSlips[i].selected = allchecked;
        if (allchecked) {
          this.checkedSlipCount++;
        }
      }
    }
  }

  toggleOneSlip(slip) {
    if (slip.selected) {
      this.checkedSlipCount++;
    } else {
      this.checkedSlipCount--;
    }
  }

  loadStudentFee(studentFee) {
    this.selectedStudentFee = studentFee;
    this.showPanel = 'studentFee';
  }

  addBalance() {
    this.selectedStudentFee.totalFee = this.selectedStudentFee.finalFee + this.selectedStudentFee.extraCharge;
    this.selectedStudentFee.totalFee += this.selectedStudentFee.balance;
  }

  addExtraCharges() {
    this.selectedStudentFee.totalFee = this.selectedStudentFee.finalFee + this.selectedStudentFee.balance;
    this.selectedStudentFee.totalFee += this.selectedStudentFee.extraCharge;
  }

  saveSlip(slip) {
    if (this.selectedCenter) {
      const object = {
        extraCharge: this.selectedStudentFee.extraCharge,
        balance: this.selectedStudentFee.balance,
        comments: this.selectedStudentFee.comments,
        id: slip.id
      };
      this.adminService.updateIpsaaClubSlip(object).subscribe(response => {
        $.extend(this.selectedStudentFee, response);
        this.alertService.successAlert('Extra Charges saved, Now you have to pay');
      }, error => {

      });
    }
  }

  showPayNow(studentFee) {
    this.selected = studentFee;
    this.selected.paymentDate = formatDate(new Date());
    function formatDate(date) {
      const d = new Date(date);
      const year = d.getFullYear();
      let month = '' + (d.getMonth() + 1);
      let day = '' + d.getDate();
      if (month.length < 2) { month = '0' + month; }
      if (day.length < 2) { day = '0' + day; }
      return [year, month, day].join('-');
    }
  }

  downloadSlip(slip) {
    this.disabledSlipDownload = true;
    this.adminService.downloadIpsaaClubSlip([slip.id]).subscribe(response => {
      this.disabledSlipDownload = false;
      const blob = new Blob([response.body], {
      });
      FileSaver.saveAs(blob, response.headers.get('fileName'));
    }, error => {
      this.disabledSlipDownload = false;
      this.alertService.errorAlert(error.error.message);
    });
  }

  downloadReceipt(receipt) {
    if (receipt && receipt.id) {
      this.disabledReceiptDownload = true;
      this.adminService.downloadIpsaaClubReceipt(receipt.id).subscribe(response => {
        this.disabledReceiptDownload = false;
        const blob = new Blob([response.body], {
        });
        FileSaver.saveAs(blob, response.headers.get('fileName'));
      }, error => {
        this.disabledReceiptDownload = false;
        this.alertService.errorAlert(error.error.message);
      });
    }
  }

  cancelSlipEmail() {
    this.slipEmail = {};
    this.showPanel = '';
  }

  sendSlipEmail(generatedFeeSlips, slipEmail) {
    const list = [];
    for (let i = 0; i < generatedFeeSlips.length; i++) {
        const slip = generatedFeeSlips[i];
        if (slip.selected) {
            list.push(slip.id);
        }
    }

    slipEmail.body = $('#slipEmailMessage').clone().html();

    if (list.length === 0) {
        this.alertService.errorAlert('Please select al least on slip.');
        return;
    }
    if (slipEmail.body === '') {
      this.alertService.errorAlert('Please write something to message.');
      return;
    }

    slipEmail.slipIds = list;

    this.sendPaymentLinkDisable = true;

    this.adminService.emailToParentsOfIpsaaClubStudents(slipEmail).subscribe(response => {
      this.sendPaymentLinkDisable = false;
      this.showPanel = '';
      this.toggleAll(false);
      this.alertService.successAlert('Emails send successfully');
    }, error => {
      this.sendPaymentLinkDisable = false;
      this.alertService.errorAlert(error.error.message);
    });
  }

  hideShowPanel() {
    this.showPanel = '';
    this.selectedStudentFee = null;
  }

  payStudentFee() {
    const fee_diff = this.selected.paidAmount - this.selected.payableAmount;
    if (!(this.selected && this.selected.paidAmount)) {
      this.alertService.errorAlert('Please enter paid amount.');
      return ;
    }
    if (fee_diff > 0) {
      this.alertService.confirm('Your are about to pay extra amount of Ruppes ' + fee_diff + '.').then( ok => {
        if(ok ===true) {
        this.feePaymentRequest();
}
      }).catch(err => {

      });
    } else {
      this.feePaymentRequest();
    }
  }

  confirm(payment) {
    this.adminService.confirmIpsaaClubFee({confirmed: true, id: payment.id}).subscribe(response => {
      $.extend(payment, response);
      this.alertService.successAlert('Payment Confirmed');
    });
  }

  feePaymentRequest() {
    this.disabledRecordPayment = true;
    this.adminService.payIpsaaClubFee(this.selected).subscribe(response => {
      $('#myModal').modal('toggle');
      this.selected.payableAmount = this.selected.payableAmount - this.selected.paidAmount;
      this.selected.paidAmount = 0;
      this.selectedStudentFee.payments.push(response);
      this.disabledRecordPayment = false;
      this.alertService.successAlert('Successfully applied payment');
      this.getGeneratedFeeSlips(this.selectedCenter);
    }, error => {
      this.disabledRecordPayment = false;
      this.alertService.errorAlert(error.error.message);
    });
  }

  cancelStudentSlip() {
    // this.showPanel = '';
    this.selected = {};
  }

  showCommentField(payment) {
    this.selectedPayment = payment;
  }

  rejectFeeSlip(comment) {
    const object = {
      id: this.selectedPayment.id,
      confirmed: false,
      comments: comment
    };
    this.adminService.rejectIpsaaClubFee(object).subscribe(response => {
      this.selectedStudentFee = null;
      $.extend(this.selectedPayment, response);
      this.getGeneratedFeeSlips(this.selectedCenter);
      this.alertService.successAlert('Payment Rejected');
      $('#commentModal').modal('hide');
    }, error => {
      this.alertService.errorAlert(error.error.message);
    });
  }

}
