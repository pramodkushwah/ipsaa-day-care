import 'rxjs/add/operator/toPromise';
import { Injectable } from '@angular/core';
import { Api } from '../api/api';
import { Student } from '../../modal/student';
import { Subject } from 'rxjs';
import { StorageService } from '../localstorage/storage';

@Injectable({ providedIn: 'root' })
export class AdminService {
  students: Student[];
  public viewPanel = new Subject<boolean>();
  public viewPanelForFee = new Subject<boolean>();

  constructor(public api: Api, public storage: StorageService) { }

  getPrograms() {
    return this.api.get('api/program/');
  }

  getProgramsByCenterId(centerId: number) {
    return this.api.get('api/center/programs/' + centerId);
  }

  deleteProgram(programId) {
    return this.api.delete('api/program/' + programId);
  }
  deleteGroup(groupId) {
    return this.api.delete('api/group/' + groupId);
  }
  getCenters() {
    return this.api.get('api/center/');
  }

  getStates() {
    return this.api.get('api/state/all');
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

  isFeePanding(studentId: number) {
    return this.api.get('api/student/checkPending/' + studentId);
  }

  deleteStudentById(studentId: number) {
    return this.api.delete('api/student/' + studentId);
  }

  deleteStudentForcefully(studentId: number) {
    return this.api.delete('api/student/' + studentId + '/forced');
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

  saveState(state: any) {
    return this.api.post('api/state/', state);
  }

  updateZone(zone: any) {
    return this.api.put('api/zone/', zone);
  }

  updateCity(city: any) {
    return this.api.put('api/city/', city);
  }

  updateState(state: any) {
    return this.api.put('api/state/', state);
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

  deleteState(stateId: any) {
    return this.api.delete('api/state/' + stateId);
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

  deleteRole(roleId) {
    return this.api.delete('api/role/' + roleId);
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

  deleteUser(userId: any) {
    return this.api.delete('api/user/' + userId);
  }
  resetUserPassword(userIdAndPassword: any) {
    return this.api.post('api/user/resetpwd', userIdAndPassword);
  }

  updateSalary(salary) {
    return this.api.post('api/employee/salary/update', salary);
  }

  getProfessionalTax(data) {
    return this.api.post('api/employee/stateTax', data);
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
    return this.api.getPDFByGetMethod('api/employee/payslip/pdf/' + salary.id);
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
    return this.api.post('api/attendance/staff/clockin/', { employeeId: staff.id });
  }

  clockOutStaff(staff: any) {
    return this.api.post('api/attendance/staff/clockout/', { employeeId: staff.id });
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

  uploadEmployeeSalaryFile(data: any) {
    return this.api.put('api/employee/payslip/upload', data);
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

  studentPaymentConfirm(paymentDetails) {
    return this.api.put('api/student/payfee', paymentDetails);
  }

  inquiryReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/inquiry/', centerId_and_range);
  }

  feeCollectionReportDownload(centerId_and_range) {
    return this.api.getPDF('api/report/collectionfee/excel/', centerId_and_range);

  }

  ipsaaClubFeeReportDownload(data) {
    return this.api.getPDF('api/report/ipsaaclub/studentfee/excel', data);
  }

  ipsaaClubCollectionFeeDownload(data) {
    return this.api.getPDF('api/report/ipsaaclub/collectionfee/excel', data);
  }

  staffReportDownload(data) {
    return this.api.getPDF('api/report/staff/excel', data);
  }

  studentsFeeReportdownload(centerId_and_range) {
    return this.api.getPDF('api/report/studentfee/excel/', centerId_and_range);

  }

  hdfcGatewayReportdownload(centerId_and_range) {
    return this.api.getPDF('api/report/collectionfee/hdfc', centerId_and_range);

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

      return this.api.get('api/support/all/');
    } else {
      return this.api.get('api/support/');

    }
  }

  getSelectedParentQueries(ParentId) {
    return this.api.get('api/support/' + ParentId);

  }

  closeQuery(queryId) {
    return this.api.post('api/support/' + queryId + '/close', {});
  }

  replyToQuery(queryId, replyText) {
    return this.api.post('api/support/' + queryId + '/reply', replyText);

  }

  getAllCenterStaffApprovalCount() {
    return this.api.get('api/staff/approvals/count');
  }

  getSelectedCenterStaffApprovalCount(centerId: number) {
    return this.api.get('api/staff/approvals/' + centerId);
  }

  aproveStaff(staff_id) {
    return this.api.get('api/staff/approve/' + staff_id);
  }

  rejectStaff(staff_id) {
    return this.api.get('api/staff/reject/' + staff_id);
  }
  getAllCenterStudentsApprovalCount() {
    return this.api.get('api/student/approvals/count');
  }

  getSelectedCenterStudentsApprovalCount(centerId: number) {
    return this.api.get('api/student/approvals/' + centerId);
  }
  aproveStudent(student_id) {
    return this.api.get('api/student/approve/' + student_id);
  }
  rejectStudent(student_id, comment) {
    return this.api.post('api/student/reject/' + student_id, { 'comment': comment });
  }

  // center

  getZones() {
    return this.api.get('api/zone/');
  }


  getStatesByZone(zoneId) {
    return this.api.get('api/state/zone/' + zoneId + '/');
  }
  getCitiesByStateByZone(stateId) {
    return this.api.get('api/city/state/' + stateId + '/');
  }

  getHolidaysList(currentYearMonth) {
    return this.api.post('api/holiday/', currentYearMonth);
  }

  AddNewHolliday(holliday_Details) {
    return this.api.post('api/holiday/save', holliday_Details);
  }

  deleteHoliday(holidayId) {
    return this.api.delete('api/holiday/' + holidayId);

  }


  // crm


  getInquiry(centerId?) {
    if (centerId) {
      return this.api.get('api/inquiry/?centerId=' + centerId);
    } else {
      return this.api.get('api/inquiry/');
    }
  }

  getFollowUps(date_Dispositions) {
    return this.api.post('api/inquiry/followUps/', date_Dispositions);
  }
  loadInquiryDetials(inquiry_Id) {
    return this.api.get('api/inquiry/' + inquiry_Id);
  }

  addNewInquiry(inquiry_Details) {
    return this.api.post('api/inquiry/', inquiry_Details);
  }

  updateInquiry(updated_Inquiry_Details) {
    return this.api.put('api/inquiry/', updated_Inquiry_Details);
  }
  // website inquiry

  getWebInquiry() {
    return this.api.get('api/inquiry/website');
  }





  getIpsaaClubFeeSlips(code: string) {
    return this.api.post('api/student/ipsaaclub/feeslip/list', { 'centerCode': code });
  }

  updateIpsaaClubSlip(data) {
    return this.api.post('api/student/ipsaaclub/slip/update', data);
  }

  downloadIpsaaClubSlip(data) {
    return this.api.getPDF('api/student/ipsaaclub/feeslips/pdf', data);
  }

  downloadIpsaaClubReceipt(receiptId) {
    return this.api.getPDFByGetMethod('api/student/download/ipsaaclub/receipt/' + receiptId);
  }

  emailToParentsOfIpsaaClubStudents(emailObject) {
    return this.api.post('api/student/ipsaaclub/paymentLink/', emailObject);
  }

  payIpsaaClubFee(data) {
    return this.api.post('api/student/ipsaaclub/payfee', data);
  }

  confirmIpsaaClubFee(data) {
    return this.api.put('api/student/ipsaaclub/record/update', data);
  }

  resetParentAccount(parentId) {
    return this.api.put('api/student/parent/resetPassword/', { 'id': parentId });
  }

  createAccount(parentId) {
    return this.api.get('api/student/createParentAccount/' + parentId);
  }

  rejectIpsaaClubFee(data) {
    return this.api.put('api/student/ipsaaclub/record/update', data);
  }

  generateIpsaaclubStudentFee(studentId, data) {
    return this.api.post('api/student/ipsaaclub/generate/' + studentId, data);
  }

  hasPrivilage(privilage: any) {
    const privilages: string[] = this.storage.getData('ngStorage-privileges');
    return privilages.includes(privilage);
  }


}
