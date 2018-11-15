import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { PayrollService } from '../../../../providers/payroll/payroll.service';

@Component({
  selector: 'app-staff-leaves',
  templateUrl: './staff-leaves.component.html',
  styleUrls: ['./staff-leaves.component.css']
})
export class StaffLeavesComponent implements OnInit {


  months = [{ 'month': 'January', 'id': 1 },
  { 'month': 'February', 'id': 2 },
  { 'month': 'March', 'id': 3 },
  { 'month': 'April', 'id': 4 },
  { 'month': 'May', 'id': 5 },
  { 'month': 'June', 'id': 6 },
  { 'month': 'July', 'id': 7 },
  { 'month': 'August', 'id': 8 },
  { 'month': 'September', 'id': 9 },
  { 'month': 'October', 'id': 10 },
  { 'month': 'November', 'id': 11 },
  { 'month': 'December', 'id': 12 }, ];
  leaveMonth: string;
  currentYear: number;
  years = [];
  toDate: any;
  fromDate: any;
  selectedLeaveType: any;
  leaveList = ['SICK', 'CASUAL', 'PAID', 'UNPAID', 'ADOPTION', 'MATERNITY', 'BEREAVEMENT'];
  selectedId: number;
  rowColour: string;
  selectedMonth: any;
  selectedYear: any;
  halfDay: any;
  eId: string;
  reason: string;
  monthlyLeaveHistory: Array<any>;
  yearlyLeaveHistory: Array<any>;
  applyLeavDetails = {};
  showDetails = false;
  employeeList: Array<any>;
  currentDate: Date;
  totalLeave = 0;
  selectedEmployeeAttendanceDetails: Array<any>;
  leaveSummary: any = {};
  leaveType: string;
  totalMonthly = 0;
  disableApprove = false;

  constructor(
    private payrollService: PayrollService, private alertService: AlertService
  ) {
    this.currentDate = new Date();
    this.currentDate.setDate(this.currentDate.getDate());
    this.selectedMonth = new Date().getMonth() + 1;
    this.selectedYear = (new Date()).getFullYear();
    this.years.push((new Date()).getFullYear() - 1);
    this.years.push((new Date()).getFullYear());
    this.LeaveMonthTable();
  }

  ngOnInit() {
    this.getEmployees();
  }

  getEmployees() {
    this.payrollService.getEmployee()
      .subscribe((res: any) => {
        this.employeeList = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }
  getEmployeeAttendance() {
    this.applyLeavDetails = {};
    this.payrollService.getAttendance({
      empId: this.selectedId,
      month: this.selectedMonth,
      year: this.selectedYear,
    })
      .subscribe((res: any) => {
        this.LeaveMonthTable();
        this.showDetails = true;
        this.eId = res[0].eid;
        this.selectedEmployeeAttendanceDetails = res.sort((val1, val2) => {
          return new Date(val1.date).getTime() - new Date(val2.date).getTime();
        });
        console.log(this.selectedEmployeeAttendanceDetails);

        for (let i = 0; i < this.selectedEmployeeAttendanceDetails.length; i++) {
          switch (this.selectedEmployeeAttendanceDetails[i].status) {
            case 'Absent':
              this.selectedEmployeeAttendanceDetails[i].class = 'table-danger';
              break;

            case 'Present':
              this.selectedEmployeeAttendanceDetails[i].class = 'table-active';
              break;

            case 'Leave':
              this.selectedEmployeeAttendanceDetails[i].class = 'table-secondary';
              break;

            case 'Holiday':
              this.selectedEmployeeAttendanceDetails[i].class = 'table-primary';
              break;

          }
        }

      }, (err) => {
        this.showDetails = true;
        this.alertService.errorAlert(err);
      });

    this.MonthlyLeaveSummry();
    this.yearlyLeaveSummry();
  }

  apllyLeave() {
    this.applyLeavDetails['eid'] = this.eId;
    this.applyLeavDetails['fromDate'] = this.fromDate;
    this.applyLeavDetails['leaveType'] = this.selectedLeaveType;
    this.applyLeavDetails['reason'] = this.reason;
    this.applyLeavDetails['toDate'] = this.toDate;
    if (this.halfDay) {
      this.applyLeavDetails['halfLeave'] = true;
    }
    this.payrollService.leaveApplication(this.applyLeavDetails)
      .subscribe((res) => {
        this.applyLeavDetails = {};
        this.alertService.successAlert('Leave operation successful');

      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  approveLeave(details) {
    this.disableApprove = true;
    this.payrollService.approveLeave(details.leaveId)
      .subscribe((res) => {
        this.getEmployeeAttendance();
        this.disableApprove = false;
this.alertService.successAlert('Leave Approve');
      }, (err) => {
        this.disableApprove = false;
        this.alertService.errorAlert(err);
      });

  }

  rejectLeave(details) {
    this.disableApprove = true;
    this.payrollService.rejectLeave(details.leaveId)
    .subscribe((res) => {
      this.alertService.successAlert('Leave Reject');
this.getEmployeeAttendance();
      this.disableApprove = false;
      console.log(res);

    }, (err) => {
      this.disableApprove = false;
      this.alertService.errorAlert(err);
    });
  }

  MonthlyLeaveSummry() {
    this.totalMonthly = 0;
    this.payrollService.leaveSummry({
      empId: this.selectedId,
      month: this.selectedMonth,
      year: this.selectedYear,
    })
      .subscribe((res) => {
        this.monthlyLeaveHistory = res;


        for (let i = 0; i < this.leaveList.length; i++) {
          this.leaveSummary[this.leaveList[i]] = 0;
        }

        for (let i = 0; i < res.length; i++) {
          this.leaveSummary[res[i].type] = res[i].count;
          this.totalMonthly += res[i].count;
        }




      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  yearlyLeaveSummry() {
    this.totalLeave = 0;
    this.payrollService.leaveSummry({
      empId: this.selectedId,
      year: this.selectedYear,
    })
      .subscribe((res) => {
        this.yearlyLeaveHistory = res;
        res.forEach(element => {
          this.totalLeave = this.totalLeave + element.count;
        });
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }



  LeaveMonthTable() {
    console.log(typeof this.selectedMonth);
    this.months.forEach(element => {
      if (this.selectedMonth === element.id) {

        this.leaveMonth = element.month;
      }
    });
    console.log(this.leaveMonth);
  }

}
