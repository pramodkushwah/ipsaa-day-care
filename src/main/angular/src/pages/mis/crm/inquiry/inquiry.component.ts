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
  selectedInquiryDetials: any = {};
  induiryForm: FormGroup;
  induiryForm1: FormGroup;
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
filterFollowUps = ['All', 'Due', 'Open', 'Today'];
    programs: Array<any>;
    groups = [];
    selectedCenter = {};
  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private alertService: AlertService
  ) { }
  ngOnInit() {
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

  getFollowUps() {

    // if (selectedCenterId) {
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
    this.getInquiries(selectedCenterId);
  }


  changeTab(val) {
    this.selectedTab = val;
    this.InquiresDteailsShow = false;
  }


    loadInquiry(id) {
      this.selectedInquiryDetials = id;
          this.InquiresDteailsShow = true;
  }

  centerChanged(selectedCenterId) {

  }
}
