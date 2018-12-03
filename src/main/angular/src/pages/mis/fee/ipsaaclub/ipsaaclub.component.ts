import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';

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

  constructor(private adminService: AdminService, private fb: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
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
};

toggleOneSlip(slip) {
    if (slip.selected) {
        this.checkedSlipCount++;
    } else {
        this.checkedSlipCount--;
    }
};


}
