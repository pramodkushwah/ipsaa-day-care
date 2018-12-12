import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { PayrollService } from '../../../../providers/payroll/payroll.service';
import { AdminService } from '../../../../providers/admin/admin.service';
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
  { 'month': 'December', 'id': 12 }];
  leaveMonth: string;
  currentYear: number;
  years = [];
  toDate: any;
  fromDate: any;
  selectedLeaveType: any;
  leaveList = ['SICK', 'CASUAL', 'PAID', 'UNPAID', 'ADOPTION', 'MATERNITY', 'BEREAVEMENT'];
  selectedId: number;
  summaryMonth: number;
  rowColour: string;
  selectedMonth: any;
  selectedYear: any;
  halfDay: any;
  eId: string;
  reason: string;
  monthlyLeaveHistory: Array<any>;
  yearlyLeaveHistory: Array<any>;
  applyLeavDetails = {};
  apllyLeaveSuccessful = true;
  showDetails = false;
  employeeList: Array<any>;
  employeeListCopy: Array<any>;

  currentDate: Date;
  totalLeave = 0;
  selectedEmployeeAttendanceDetails: Array<any>;
  leaveSummary: any = {};
  leaveType: string;
  totalMonthly = 0;
  disableApprove = false;
  disableDelete = false;
  disableSave = false;

  attenClockin: string;
  attenClockOut: string;

  attenDetails: any = {};
  centers: any;
  employeeLeaveSummary: any = [];
  employeeLeaveSummaryCopy: any = [];
  monthlyLeaves: any = [];
  filterCenter = 'All';
  constructor(
    private payrollService: PayrollService,
    private alertService: AlertService,
    private adminService: AdminService,
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
    this.getCenters();
  }

  getCenters() {
    this.adminService.getCenters()
      .subscribe((res: any) => {
        this.centers = res;
      });
  }
  getEmployees() {
    this.payrollService.getEmployee()
      .subscribe((res: any) => {
        this.employeeList = res;

        this.employeeListCopy = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  selctedEmpId(id) {
    this.selectedId = id;
    this.getEmployeeAttendance();
  }

  getEmployeeAttendance() {
    this.alertService.loading.next(true);
    this.applyLeavDetails = {};
    this.payrollService.getAttendance({
      empId: this.selectedId,
      month: this.selectedMonth,
      year: this.selectedYear,
    })
      .subscribe((res: any) => {
        this.LeaveMonthTable();
        this.alertService.loading.next(false);

        this.showDetails = true;
        this.eId = res[0].eid;
        this.selectedEmployeeAttendanceDetails = res.sort((val1, val2) => {
          return new Date(val1.date).getTime() - new Date(val2.date).getTime();
        });
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
        this.alertService.loading.next(false);
        this.alertService.errorAlert(err);
      });

    this.MonthlyLeaveSummry();
    this.yearlyLeaveSummry();
  }

  apllyLeave() {
    this.apllyLeaveSuccessful = false;
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
        this.apllyLeaveSuccessful = true;
        this.applyLeavDetails = {};
        this.fromDate = '';
        this.selectedLeaveType = '';
        this.reason = '';
        this.toDate = '';
        this.halfDay = '';
        this.alertService.successAlert('Leave operation successful');

      }, (err) => {
        this.apllyLeaveSuccessful = false;

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




  delete(attendanceDetails) {
    this.disableDelete = true;
    this.payrollService.deleteLeave(attendanceDetails.attendanceId)
      .subscribe((res: any) => {
        this.disableDelete = false;
        this.getEmployeeAttendance();
        // this.selectedEmployeeAttendanceDetails.forEach(element => {
        //   if (element.id === attendanceDetails.id) {
        //     element.status = 'Absent';
        //   }
        // });
      }, (err) => {
        this.disableDelete = false;
      });
  }


  saveAttendance(attendanceDetails) {
    this.disableSave = true;
    this.attenDetails['clockin'] = attendanceDetails.attenClockin;
    this.attenDetails['clockout'] = attendanceDetails.attenClockOut;
    this.attenDetails['date'] = attendanceDetails.date;
    this.attenDetails['employeeId'] = attendanceDetails.id;
    this.payrollService.saveLeave(this.attenDetails)
      .subscribe((res: any) => {
        this.disableSave = false;
        this.getEmployeeAttendance();
      }, (err) => {
        this.disableSave = false;
      });
  }


  getMonthlyEmployee(id) {
    this.payrollService.getEmployeeAttendance(id)
      .subscribe((res: any) => {
        console.log(res);
        this.employeeLeaveSummary = res;
        this.employeeLeaveSummaryCopy = res;
        this.filterEmployeeList();
      });
  }


  filterEmployeeList() {
    console.log();

    if (this.filterCenter === 'All') {
      this.employeeLeaveSummary = this.employeeLeaveSummaryCopy;
    } else {

      this.employeeLeaveSummary = this.employeeLeaveSummaryCopy.filter(elem => {
        return (elem.center === this.filterCenter);
      });
    }

  }

  searchStudent(event: any) {
    const val = event.target.value.toLowerCase();
      if (val && val.trim() !== '') {
        this.employeeList = this.employeeListCopy.filter(employee => {
          return employee.name.toLowerCase().startsWith(val);
        });
    }  else {
      this.employeeList = this.employeeListCopy;
    }
  }
  getEmployeeLeaves(eid) {
    this.monthlyLeaves = [];

    this.payrollService.getAttendanvceSummry(eid, this.summaryMonth)
      .subscribe((res: any) => {
        console.log(res);
this.monthlyLeaves = res;
      });
  }
}
