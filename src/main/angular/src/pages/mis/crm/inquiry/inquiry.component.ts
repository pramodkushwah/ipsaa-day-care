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
  followUpsFor = {
    date: '2018-11-13',
    dispositions: ['Callback', 'Followup', 'NewInquiry', 'Revisit']
  };

  centers: Array<any>;
  selectedCenterId: Array<any>;
  InquiresDteailsShow = false;
  selectedInquiryDetialsId: any;
  induiryForm: FormGroup;
  induiryForm1: FormGroup;
  currentDate: any;
  leadSources = [
    'BUILDING',
    'CORPORATE',
    'ADVERTISEMENT',
    'WEBSITE',
    'REFERENCE',
    'NEWSPAPER',
    'SIGNBOARDS',
    'FACEBOOK',
    'ADWORD',
    'ORGANIC',
    'OTHERS'];
  inquiryTypes = [
    'Web',
    'Walkin',
    'Call',
    'Email',
    'Newspaper'];
  dispositions = [
    'NewInquiry',
    'Followup',
    'Callback',
    'ParentMessage',
    'Enrolled',
    'Drop',
    'NotInterested',
    'Revisit'
  ];
  filterFollowUps = [ {
    date: 'jj',
    name: 'ALL',
    value: ''
  },
  // {
  //   date: (this.currentDate | date: 'y-MM-dd'" ),
  //   name: 'DUE',
  //   value: ''
  // },
  {
    date: 'jj',
    name: 'OPEN',
    value: ''
  },
  {
    date: 'jj',
    name: 'TODAY',
    value: ''
  },
  'ALL', 'Due', 'Open', 'Today'];
  programs: Array<any>;
  groups = [];
  selectedCenter = {};
  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private alertService: AlertService
  ) {
    this.currentDate = (new Date());
   }
  ngOnInit() {
    console.log(this.selectedTab);
// this.currentDate = new Date();
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
  getInquiries(CenterId?: number) {
    this.adminService.getInquiry(CenterId)
      .subscribe((res: any) => {
        this.inquiries = res;
        this.inquiryTable = true;
      }, (err) => {
        this.alertService.errorAlert(err);

      });
  }

  getFollowUps(CenterId?: number) {

    if (CenterId) {
      this.followUpsFor['centerCodes'].push(CenterId);
      //  = CenterId;

  }
        // today- date 0
  // centerCodes= []
  // 	due- to -1
  // 	open- from +1
  // 	all- "kuch nhi "

    //   this.followUpsFor['centerCodes'] = [selectedCenterId];
    //   this.followUpsFor['centerCodes'] = [selectedCenterId];
    //   this.followUpsFor['centerCodes'] = [selectedCenterId];
    //   this.followUpsFor['centerCodes'] = [selectedCenterId];
    // }
    this.adminService.getFollowUps(this.followUpsFor)
      .subscribe((res: any) => {
        this.followUps = res;
        this.inquiryTable = false;
        this.followUpsTable = true;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  filterFeeByCenter(selectedCenterId) {
    if (selectedCenterId) {
      this.getInquiries(selectedCenterId);
      this.getFollowUps(selectedCenterId);
    } else {
      this.getInquiries();
      this.getFollowUps();

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
}
