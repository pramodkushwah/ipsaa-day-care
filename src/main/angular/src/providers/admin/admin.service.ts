import 'rxjs/add/operator/toPromise';
import { Injectable } from '@angular/core';
import { Api } from '../api/api';
import { Student } from '../../modal/student';
import {  Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminService {
  students: Student[];
  public viewPanel = new Subject<boolean>();
  public viewPanelForFee = new Subject<boolean>();

  constructor(public api: Api) { }

  getPrograms() {
    return this.api.get('api/program/');
  }

  getProgramsByCenterId(centerId: number) {
    return this.api.get('api/center/programs/' + centerId);
  }

  getCenters() {
    return this.api.get('api/center/');
  }

  getCostCenter() {
    return this.api.get('api/le/');
  }

  getGroups() {
    return this.api.get('api/group/');
  }

  getStudents(body: any) {
    return this.api.post('api/student/filter', body);
  }

  getStudentById(studentId: number) {
    return this.api.get('api/student/' + studentId);
  }

  deleteStudentById(studentId: number) {
    return this.api.delete('api/student/' + studentId);
  }

  getStudentPaymentHistory(studentId: number) {
    return this.api.get('api/student/payments/history/' + studentId);
  }

  // Used by staffComponent
  getStaff() {
    return this.api.get('api/staff/all/');
  }
  getStaffById(staffId: number) {
    return this.api.get('api/staff/' + staffId);
  }

  getPaySlipByEmoployee(employeeId) {
    return this.api.post('api/employee/payslip/' + employeeId, {});
  }

  filterStaff(filter) {
    return this.api.post('api/staff/filter', filter);
  }
  deleteStaffById(staffId) {
    return this.api.delete('api/staff/' + staffId);
  }
  addStaff(reqBody) {
    return this.api.post('api/staff/', reqBody);
  }
  updateStaff(reqbody) {
    return this.api.put('api/staff/', reqbody);
  }
  updateStaffProfilePic(staffId: number, formData: FormData) {
    return this.api.post('api/staff/' + staffId + '/profile-pic', formData);
  }

  getPrivileges() {
    return this.api.get('api/user/my_privileges');
  }

  uploadStudentProfilePic(studentId: number, formData: FormData) {
    return this.api.post('api/student/' + studentId + '/profile-pic', formData);
  }

  updateStudent(student: any) {
    return this.api.put('api/student/', student);
  }

  addStudent(student: any) {
    return this.api.post('api/student/', student);
  }

  saveZone(zone: any) {
    return this.api.post('api/zone/', zone);
  }

  saveCity(city: any) {
    return this.api.post('api/city/', city);
  }

  updateZone(zone: any) {
    return this.api.put('api/zone/', zone);
  }

  updateCity(city: any) {
    return this.api.put('api/city/', city);
  }

  deleteZone(zoneId: any) {
    return this.api.delete('api/zone/' + zoneId);
  }

  deleteCenter(centerId: any) {
    return this.api.delete('api/center/' + centerId);
  }

  deleteCity(cityId: any) {
    return this.api.delete('api/city/' + cityId);
  }

  saveCenter(center) {
    return this.api.post('api/center/', center);
  }

  updateCenter(center) {
    return this.api.put('api/center/', center);
  }

  saveProgram(program: any) {
    return this.api.post('api/program/', program);
  }

  updateProgram(program: any) {
    return this.api.put('api/program/', program);
  }

  saveGroup(group: any) {
    return this.api.post('api/group/', group);
  }

  updateGroup(group: any) {
    return this.api.put('api/group/', group);
  }

  getRoles() {
    return this.api.get('api/user/roles/');
  }

  getAllPrivileges() {
    return this.api.get('api/user/privileges/');
  }

  saveRole(role: any) {
    return this.api.post('api/user/role/', role);
  }

  updateRole(role: any) {
    return this.api.put('api/user/role/', role);
  }

  getUsers() {
    return this.api.get('api/user/');
  }

  getAllCenters() {
    return this.api.get('api/center/all');
  }

  getEmployee() {
    return this.api.post('api/staff/filter', { 'active': true });
  }

  saveUser(user: any) {
    return this.api.post('api/user/', user);
  }

  updateUser(user: any) {
    return this.api.put('api/user/', user);
  }

  resetUserPassword(userIdAndPassword: any) {
    return this.api.post('api/user/resetpwd', userIdAndPassword);
  }

  updateSalary(salary) {
    return this.api.post('api/employee/salary/update', salary);
  }

  getNewEmployees() {
    return this.api.get('api/staff/new');
  }

  getPaySlips(object: any) {
    return this.api.post('api/employee/payslip?month=' + object.month + '&year=' + object.year + '&employerId=' + object.employer, {});
  }

  updatePaySlip(salary: any) {
    return this.api.put('api/employee/payslip/', salary);
  }

  downloadPaySlip(salary: any) {
    return this.api.get('api/employee/payslip/pdf/' + salary.id);
  }

  regeneratePaySlip(salary: any) {
    return this.api.put('api/employee/payslip/regenerate/', salary);
  }

  lockPayslip(paySlip: any) {
    return this.api.put('api/employee/payslip/lock', {
      id: paySlip.id,
      lock: true
    });
  }

  getStudentAttendance() {
    return this.api.get('api/attendance/student/');
  }

  clockInStudent(student: any) {
    return this.api.post('api/attendance/student/clockin/', { studentId: student.id });
  }

  clockOutStudent(student: any) {
    return this.api.post('api/attendance/student/clockout/', { studentId: student.id });
  }

  markPresent(studentId: number) {
    return this.api.put('api/attendance/student/markAbsents/' + studentId, {});
  }

  markPresents(centerId: number, programId) {
    return this.api.post('api/attendance/student/markPresents?centerId=' + centerId + '&programId=' + programId, {});
  }

  getStaffAttendance() {
    return this.api.get('api/attendance/staff/');
  }

  clockInStaff(staff: any) {
    return this.api.post('api/attendance/staff/clockin/', { staffId: staff.id });
  }

  clockOutStaff(staff: any) {
    return this.api.post('api/attendance/staff/clockout/', { staffId: staff.id });
  }

  staffFullLeave(eid: any) {
    return this.api.post('api/staff/leave/single-day-leave?eid=' + eid, {});
  }

  staffHalfLeave(eid: any, halfLeave: boolean) {
    return this.api.post('api/staff/leave/single-day-leave?eid=' + eid + '&halfLeave=' + halfLeave, {});
  }

  // student fee service
  loadStudentFeeByCenterId(centerId) {
    return this.api.get('api/student/fee?centerId=' + centerId);
  }
  getProgramFee(programNgroup: any) {
    return this.api.post('api/center/fee/', programNgroup);
  }



  // generate  fee slip
  getProgramCenter() {
    return this.api.get('api/center/');
  }

  getCenterFee(centerId) {
    return this.api.get('api/center/' + centerId + '/fee/');
  }

  getProgramsList() {
    return this.api.get('api/program/');
  }

  getChargesList() {
    return this.api.get('api/charge/');
  }

  getcharge(centerId) {
    return this.api.get('api/center/' + centerId + '/charge/');
  }

  addNewCharge(fd) {
    return this.api.post('api/center/charge', fd);
  }

  editCharge(fd) {
    return this.api.put('api/center/charge', fd);

  }

  editProgramFee(fd) {
    return this.api.put('api/center/program/fee/', fd);
  }

  addProgramFee(fd) {
    return this.api.post('api/center/program/fee/', fd);

  }

  deleteProgramFee(programId) {
    return this.api.delete('api/center/program/fee/' + programId + '/');
  }

  deleteAdditionalCharge(chargeId) {
    return this.api.delete('api/center/charge/' + chargeId + '/');
  }

  editChargeList(fd) {
    return this.api.put('api/charge/', fd);
  }

  addNewChargeInList(fd) {
    return this.api.post('api/charge/', fd);
  }

  generateStudentFeeSlip(fd) {
    return this.api.post('api/student/feeslip/generate/', fd);

  }

  regenerateStudentsFeeSlips(fd, value) {
    return this.api.post('api/student/feeslip/' + value + '/', fd);

  }

  lockStudentsFeeSlips(fd) {
    return this.api.post('api/student/feeslip/generate-all/', fd);

  }

  saveFeeSlipChanges(fd) {
    return this.api.post('api/student/feeslip/', fd);

  }

  downloadFeeSlips(fd) {
    return this.api.getPDF('api/student/feeslips/pdf', fd);
  }


  sendEmails(fd) {
    return this.api.post('api/student/paymentLink/', fd);

  }


  // generate fee receipt

  getStudentFeeList(fd) {
    return this.api.post('api/student/feeslip/list/', fd);

  }

  payStudentFee(fd) {
    return this.api.post('api/student/payfee/', fd);

  }

  downloadReceipt(id) {
    return this.api.getPDFByGetMethod('api/student/download/receipt/' + id);

  }


  studentFeeUpdate(feeDetails) {
    return this.api.put('api/student/fee/', feeDetails);

  }

  inquiryReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/inquiry/', centerId_and_range);
  }

  feeCollectionReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/inquiry/', centerId_and_range);

  }

  studentsFeeReportdownload(centerId_and_range) {
    return this.api.getPDF('api/report/studentfee/excel/', centerId_and_range);

  }

  studentsAttendanceReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/studentattendance/', centerId_and_range);

  }

  getEmployeeForSalaryReport() {
    return this.api.get('api/le/');

  }

  staffSalaryMonthlyReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/staffCollection/excel/', centerId_and_range);

  }

  staffsAttendanceReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/staffattendance/', centerId_and_range);

  }



  getParentsQueries(ForAll?: string) {
    if (ForAll) {

      return this.api.get('api/support/all/' );
    } else {
      return this.api.get('api/support/' );

    }
  }

  getSelectedParentQueries(ParentId) {
    return this.api.get('api/support/' + ParentId );

  }

  closeQuery(queryId) {
    return this.api.post('api/support/' + queryId + '/close' , {});
  }

  replyToQuery(queryId , replyText) {
    return this.api.post('api/support/' + queryId + '/reply' , replyText);

  }

getAllCenterStaffApprovalCount() {
  return this.api.get('api/staff/approvals/count');
}

getSelectedCenterStaffApprovalCount(centerId: number) {
  return this.api.get('api/staff/approvals/' + centerId);
}

getAllCenterStudentsApprovalCount() {
  return this.api.get('api/student/approvals/count');
}

getSelectedCenterStudentsApprovalCount(centerId: number) {
  return this.api.get('api/student/approvals/' + centerId);
}



}
