import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';

@Component({
  selector: 'app-inquiry',
  templateUrl: './inquiry.component.html',
  styleUrls: ['./inquiry.component.css']
})
export class InquiryComponent implements OnInit {


  inquiries: Array<any>;
  followUps: Array<any>;
  viewPanel: boolean;
  selectedTab: string;
  inquiryTable = false;
  followUpsTable = false;
  editable: boolean;
  followUpsFor = {
    date: '2018-11-13',
    dispositions: ['Callback', 'Followup', 'NewInquiry', 'Revisit']
  };
  centers: Array<any>;
  selectedCenterId: Array<any>;

  leadSources = [
    'Building',
    'Corporate',
    'Advertisement',
    'Reference',
    'Website',
    'Newspaper',
    'Signboards',
    'Facebook',
    'Adword',
    'Organic',
    'Others'];
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
workingInquiry: any;
inquiryNumbers = [];
    programs: Array<any>;
    groups = [];
    selectedCenter = {};
  constructor(
    private adminService: AdminService,
    private alertService: AlertService
  ) { }
  ngOnInit() {
    this.getCenter();
    this.getFollowUps();
    this.getInquiries();
    this.getPrograms();
  }

  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;
      });
  }

  getPrograms() {
    this.adminService.getPrograms()
      .subscribe((res) => {
        this.programs = res;
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
    if (val === 'Inquiry') {
      this.inquiryTable = true;
      this.followUpsTable = false;
    } else {

      this.inquiryTable = false;
      this.followUpsTable = true;
    }
  }


  addInquiry() {

  }

  loadInquiry(InquiryId, val) {
    this.viewPanel = true;
  }


  loadFollowup(followUpInquiryId) {
    this.viewPanel = false;
  }
  centerChanged(selectedCenterId) {

  }
}
