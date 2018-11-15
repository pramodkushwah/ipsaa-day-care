import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';

@Component({
  selector: 'app-staff-attendance',
  templateUrl: './staff-attendance.component.html',
  styleUrls: ['./staff-attendance.component.css']
})
export class StaffAttendanceComponent implements OnInit {

  attendance: any[] = [];
  imgLaod: boolean[] = [];

  constructor(private adminService: AdminService, private alertService: AlertService) { }

  ngOnInit() {
    this.getStaffAttendance();
  }

  getStaffAttendance() {
    this.alertService.loading.next(true);
    this.adminService.getStaffAttendance().subscribe((response: any[]) => {
      this.attendance = response;
      this.alertService.loading.next(false);
    }, error => {
      this.alertService.loading.next(false);
    });
  }

  clockin(staff: any) {
    staff.clockinDisabled = true;
    this.adminService.clockInStaff(staff).subscribe((response: any) => {
      staff.clockinDisabled = false;
      this.alertService.successAlert('Clock in OK');
    }, error => {
      staff.clockinDisabled = false;
    });
  }

  clockOut(staff: any) {
    staff.clockoutDisabled = true;
    this.adminService.clockOutStaff(staff).subscribe((response: any) => {
      staff.clockoutDisabled = false;
      this.alertService.successAlert('Clock out OK');
    }, error => {
      staff.clockoutDisabled = false;
    });
  }

  singleLeave(staff, halfLeave) {
    if (halfLeave) {
        staff.halfLeave = true;
        this.adminService.staffHalfLeave(staff.eid, halfLeave).subscribe(response => {
          staff.halfLeave = false;
          staff.onLeave = true;
          staff.status = 'Leave';
          this.alertService.successAlert('Leave operation successful');
        }, error => {
          staff.halfLeave = false;
        });
    } else {
      staff.fullLeave = true;
      this.adminService.staffFullLeave(staff.eid).subscribe(response => {
        staff.fullLeave = false;
        staff.onLeave = true;
        staff.status = 'Leave';
        this.alertService.successAlert('Leave operation successful');
      }, error => {
        staff.fullLeave = false;
      });
    }
  }
}
