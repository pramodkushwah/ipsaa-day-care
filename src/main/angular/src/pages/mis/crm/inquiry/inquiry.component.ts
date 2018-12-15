import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-inquiry',
  templateUrl: './inquiry.component.html',
  styleUrls: ['./inquiry.component.css']
})
export class InquiryComponent implements OnInit {


  inquiries: Array<any>;
  followUps: Array<any>;
  viewPanel: boolean;
  selectedTab = 'Inquiry';
  inquiryTable = false;
  followUpsTable = false;
  editable: boolean;
  followUpsFor: any = {};


  centers: Array<any>;
  currentCenter: any = {};
  InquiresDteailsShow = false;
  selectedInquiryDetialsId: any;
  induiryForm: FormGroup;
  today: Date;
  yesterday: Date;
  tomorrow: Date;

  filterFollowUps = [{
    name: 'ALL',
    value: 'ALL'
  },
  {
    name: 'DUE',
    value: 'to'
  },
  {
    name: 'OPEN',
    value: 'from'
  },
  {
    name: 'TODAY',
    value: 'date'
  }];

  filterBy: any = {};
  programs: Array<any>;
  groups = [];
  selectedCenter = {};
  followUpsCoppy: any;
  inquiriesCoppy: any;
  FOLLOWUP_NOTIFICATION: boolean;
  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private alertService: AlertService
  ) {
  }

  ngOnInit() {
    this.FOLLOWUP_NOTIFICATION = this.adminService.hasPrivilage('FOLLOWUP_NOTIFICATION');
    this.alertService.loading.next(true);
    console.log(this.selectedTab);
    this.today = new Date();
    this.today.setDate(this.today.getDate());
    this.yesterday = new Date();
    this.yesterday.setDate(this.yesterday.getDate() - 1);
    this.tomorrow = new Date();
    this.tomorrow.setDate(this.tomorrow.getDate() + 1);
    this.getCenter();
    this.getFollowUps();
    this.getInquiries();
  }

  getCenter() {

    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;

      });
  }
  getInquiries(centerId?) {
    this.adminService.getInquiry(centerId)
      .subscribe((res: any) => {
        this.alertService.loading.next(false);

        this.inquiries = res;
        this.inquiriesCoppy = res;
        this.inquiryTable = true;
      }, (err) => {
        this.alertService.loading.next(false);


      });
  }

  getFollowUps() {

    this.followUpsFor['dispositions'] = ['Callback', 'Followup', 'NewInquiry', 'Revisit'];

    if (this.currentCenter && this.currentCenter.code) {
      this.followUpsFor['centerCodes'] = [this.currentCenter.code];
    }

    // if (filter) {


if (this.filterBy.name === 'TODAY') {
  this.followUpsFor[this.filterBy.value] = this.today.toJSON().slice(0, 10);

}
if (this.filterBy.name === 'OPEN') {
  this.followUpsFor[this.filterBy.value] = this.tomorrow.toJSON().slice(0, 10);

}
if (this.filterBy.name === 'DUE') {
  this.followUpsFor[this.filterBy.value] = this.yesterday.toJSON().slice(0, 10);

}
console.log(this.filterBy);
    // }
    this.adminService.getFollowUps(this.followUpsFor)
      .subscribe((res: any) => {
        this.alertService.loading.next(false);

        this.followUpsFor = {};
        this.followUps = res;
        this.followUpsCoppy = res;

        this.inquiryTable = false;
        this.followUpsTable = true;
      }, (err) => {
        this.alertService.loading.next(false);

      });
  }

  filterFeeByCenter(selectedCenter) {
    // this.currentCenter = {};
    this.alertService.loading.next(true);

    this.currentCenter = selectedCenter;
    this.getFollowUps();

    if (selectedCenter) {
      this.getInquiries(selectedCenter.id);
    } else {
      this.getInquiries();
    }
  }


  changeTab(val) {
    this.selectedTab = val;
    this.InquiresDteailsShow = false;
    console.log(this.selectedTab);
  }


  loadInquiry(id) {
    this.selectedInquiryDetialsId = id;
    this.InquiresDteailsShow = true;
  }



  searchStudent(event: any) {
    const val = event.target.value.toLowerCase();
    if (this.selectedTab === 'Inquiry') {
      if (val && val.trim() !== '') {
        this.inquiries = this.inquiriesCoppy.filter(inquiry => {
          return inquiry.childName.toLowerCase().startsWith(val);
        });
    }  else {
      this.inquiries = this.inquiriesCoppy;
    }
  } else {
      if (val && val.trim() !== '') {
        this.followUps = this.followUpsCoppy.filter(follow => {
          return follow.inquiryNumber.toLowerCase().startsWith(val);
        });
         }  else {
          this.followUps = this.followUpsCoppy;
         }
        }
  }


}
