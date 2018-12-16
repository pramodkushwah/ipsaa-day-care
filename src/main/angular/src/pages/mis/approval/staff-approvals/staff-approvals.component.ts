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
  showTable = false;
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
    this.alertService.loading.next(true);

    this.adminService.getSelectedCenterStaffApprovalCount(this.selectedCenterId)
      .subscribe((res: any) => {
        this.alertService.loading.next(false);
        this.showTable = true;
        this.staffAprrovelList = res;
      }, (err) => {
        this.alertService.loading.next(false);
        this.showTable = true;

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



  staffApprove(staff) {
    this.adminService.aproveStaff(staff.id)
      .subscribe((res: any) => {
        this.getCenterStaffApprovelList();

        this.staffAprrovelList = this.staffAprrovelList.filter(element => element.id !== staff.id);
          this.alertService.successAlert('Staff Approved');
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  staffReject(staff) {
    this.adminService.rejectStaff(staff.id)
      .subscribe((res: any) => {
        this.getCenterStaffApprovelList();
        this.staffAprrovelList = this.staffAprrovelList.filter(element => element.id !== staff.id);
        this.alertService.successAlert('Staff Rejected');

      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }


  subscribSidePanel = () => {
    this.adminService.viewPanel.subscribe(value => {
      this.viewPanel = value;
      console.log(value);
    });
  }

}
