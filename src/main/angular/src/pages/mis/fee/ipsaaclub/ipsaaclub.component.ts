import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';

declare let $: any;

@Component({
  selector: 'app-ipsaaclub',
  templateUrl: './ipsaaclub.component.html',
  styleUrls: ['./ipsaaclub.component.css']
})
export class IpsaaclubComponent implements OnInit {
  centers: any;
  generatedFeeSlips: any;
  allchecked = false;
  checkedSlipCount: any;
  selectedStudentFee: any;
  showPanel: boolean;
  selectedCenter: any;
  selected: any;
  disabledSlipDownload: boolean;

  constructor(private adminService: AdminService, private fb: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    // this.STUDENTFEE_WRITE = 
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
    this.showPanel = false;
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

    }, error => {
      this.disabledSlipDownload = false;
    });
  }

  downloadReceipt(selectedStudentFee) {

  }

  showCommentField(payment) {

  }



}
