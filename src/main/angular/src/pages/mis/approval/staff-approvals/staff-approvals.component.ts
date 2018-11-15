import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';

@Component({
  selector: 'app-staff-approvals',
  templateUrl: './staff-approvals.component.html',
  styleUrls: ['./staff-approvals.component.css']
})
export class StaffApprovalsComponent implements OnInit {
  selectedCenterId: any;
  centers: Array<any>;
  staffAprrovelList: Array<any> = [];
  selectedStaff: any = {};
  viewPanel = false;
  update = false;
  constructor(
    private alertService: AlertService,
    private adminService: AdminService,
  ) { }

  ngOnInit() {
    this.getCenterStaffApprovelList();
    this.subscribSidePanel();
  }

  getCenterStaffApprovelList() {
    this.adminService.getAllCenterStaffApprovalCount()
      .subscribe((res: any) => {
        this.centers = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }
  getStaffList() {
    console.log(this.selectedCenterId);
    this.adminService.getSelectedCenterStaffApprovalCount(this.selectedCenterId)
      .subscribe((res: any) => {
        this.staffAprrovelList = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }


  getSelectedStaffDetail(staff) {
    console.log(staff);
    this.selectedStaff = staff;
    this.showSidePanel();
  }



  showSidePanel() {

    this.adminService.viewPanel.next(true);
    console.log(this.viewPanel);
  }


  subscribSidePanel = () => {
    this.adminService.viewPanel.subscribe(value => {
      this.viewPanel = value;
      console.log(value);
    });
  }

}
