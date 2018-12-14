import { Injectable } from '@angular/core';
import { Api } from '../api/api';

@Injectable({
  providedIn: 'root'
})
export class PayrollService {

  constructor(public api: Api) { }

  getSalary() {
    return this.api.get('api/employee/salary');
  }

  getSalaryByEmployee(eid: any) {
    return this.api.get('api/employee/' + eid + '/salary');
  }
  getEmployee() {
    return this.api.get('api/staff/reporting/');
  }

  getAttendance(employeeId) {
    return this.api.post('api/attendance/staff/', employeeId);
  }
  leaveApplication(leaveDetails) {
    return this.api.post('api/staff/leave/multi-day-leave/', leaveDetails);
  }

  leaveSummry(emId_range) {
    return this.api.post('api/staff/leave/summary/', emId_range);
  }
  approveLeave(leaveId) {
    return this.api.get('api/staff/leave/approve/' + leaveId + '/');
  }

  rejectLeave(leaveId) {
    return this.api.get('api/staff/leave/reject/' + leaveId + '/');
  }

  deleteLeave(leaveId) {
    return this.api.delete('api/attendance/staff/hradmin/' + leaveId + '/');

  }

  saveLeave(timingDetails) {
    return this.api.post('api/attendance/staff/hradmin' , timingDetails);

  }

  getEmployeeAttendance(id) {
    return this.api.post('/api/staff/leave/' + id , {} );

  }

  getAttendanvceSummry( id, month) {
    return this.api.post('api/staff/leave/employeeMonthly?eid=' + id + '&month=' + month , {});
  }
}
