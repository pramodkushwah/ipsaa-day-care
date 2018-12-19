import { Injectable } from '@angular/core';
import { Api } from '../api/api';

@Injectable({
  providedIn: 'root'
})
export class SmsService {

  constructor(public api: Api) { }

  public sendStudentSMS(sms: any) {
    return this.api.post('api/send/student/sms/', sms);
  }

  public sendStudentEmail(email) {
    return this.api.post('api/send/student/email', email);
  }

  public sendStaffSMS(sms: any) {
    return this.api.post('api/send/staff/sms/', sms);
  }

  public sendStaffEmail(email) {
    return this.api.post('api/send/staff/email', email);
  }
}
