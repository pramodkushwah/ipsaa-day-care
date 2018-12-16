import { Injectable } from '@angular/core';
import { Api } from '../api/api';

@Injectable({
  providedIn: 'root'
})
export class ParentService {

  constructor(public api: Api) { }

  getStudentDetails() {
    return this.api.get('api/pp/student/me');
  }


  getDetailsByStudentId(id) {
    return this.api.get('/api/pp/student/' + id);
  }

  updateStudentDetails(details) {
    return this.api.post('/api/pp/student/', details);

  }


  udatePicture(img) {
    return this.api.put('/api/pp/student/profile', img);

  }

  smsNotification(parent) {
    return this.api.get('/api/pp/student/sms/' + parent.id + '/' + parent.smsEnabled);

  }

  emailNotification(parent) {
    return this.api.get('/api/pp/student/sms/' + parent.id + '/' + parent.smsEnabled);
  }


  // fee

  getStudentFee(student_Id) {
    return this.api.get('/api/pp/student/fee/' + student_Id);
  }

  getStudentFeeledger(student_Id) {
    return this.api.get('/api/pp/student/feeledger/' + student_Id);
  }

  hdfcCheckout(feeledger_id, mydetails_id) {
    return this.api.get('/hdfc/checkout/' + feeledger_id + '/' + mydetails_id);

  }


  ipsaaClubhdfcCheckout(feeledger_id, mydetails_id) {
    return this.api.get('/hdfc/checkout/ipsaaclub/' + feeledger_id + '/' + mydetails_id);

  }
  getMyDetails() {
    return this.api.get('/api/pp/student/mydetails');
  }

  downloadFeeSlip(student_Id) {
    return this.api.get('/api/pp/student/feeslip/' + student_Id);
  }

  downloadFeeReceipt(student_Id) {
    return this.api.get('/api/pp/student/feereceipt/' + student_Id);
  }

  getSuccessDetail(paymentId) {
  return this.api.get('hdfc/payment/' + paymentId);
  }




  // support

  getQueries() {
    return this.api.get('/api/pp/student/support');
  }


  getQuery(id) {
    return this.api.get('/api/pp/student/support/' + id);
  }


  replyToQuery(reply_msg) {
    return this.api.post('/api/pp/student/support/' + reply_msg.id + '/reply' , reply_msg);

  }

  newInQuery(inqueryDetails) {
    return this.api.post('/api/pp/student/support' , inqueryDetails);
  }
}
