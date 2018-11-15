import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { PagerService } from '../../../../providers/pagination/pager.service';
import { SmsService } from '../../../../providers/sms/sms.service';
import { AlertService } from '../../../../providers/alert/alert.service';

@Component({
  selector: 'app-student-message',
  templateUrl: './student-message.component.html',
  styleUrls: ['./student-message.component.css']
})
export class StudentMessageComponent implements OnInit {
  centers: any[];
  groups: any[];
  programs: any[];
  students: any[];
  studentIds: any = {};
  loader: boolean;
  allItems: any;
  studentsCopy: any;
  pageSize: any;
  pageNumber: any;
  searchKey: any;
  pager: any =  {};
  pagedItems: any;
  viewPanel: boolean;
  smsCard: boolean;
  emailCard: boolean;
  ids: any[] = [];
  smsContent = '';
  emailsubject: any;
  emailcontent: any = '';
  attachments: any[] = [];
  files: any[] = [];
  selectAllStudent: boolean;
  sending: boolean;
  emailData: any;

  constructor(
    private adminService: AdminService,
    private pagerService: PagerService,
    private smsService: SmsService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.getStudents();
    this.getCenters();
    this.getPrograms();
    this.getGroups();
    this.subscribeViewPanelChange();
  }

  hideViewPanel() {
    this.adminService.viewPanel.next(false);
    this.smsContent = '';
    this.emailcontent = '';
    this.emailsubject = '';
    this.files = [];
  }

  getCenters() {
    this.adminService.getCenters().subscribe((response: any[]) => {
      this.centers = response;
    });
  }

  getGroups() {
    this.adminService.getGroups().subscribe((response: any[]) => {
      this.groups = response;
    });
  }

  getPrograms() {
    this.adminService.getPrograms().subscribe((response: any[]) => {
      this.programs = response;
    });
  }

  getStudents() {
    this.students = [];
    this.loader = true;
    this.adminService.getStudents({}).subscribe((response: any) => {
      this.loader = false;
      this.allItems = response.students;
      this.students = response.students;
      this.studentsCopy = JSON.parse(JSON.stringify(this.students));
      this.pageSize = response.pageSize;
      this.pageNumber = response.pageNumber;
      // initialize to page 1
      this.setPage(1);
      // checked if searchKey entered before
      if (this.searchKey) {
        this.searchStudent(this.searchKey);
      }
    });
  }

  searchStudent(event: any) {
    this.searchKey = event;
    const val = event.target.value;
    if (val && val.trim() !== '') {
      this.allItems = this.students.filter(student => {
        return student.fullName.startsWith(val);
      });
      this.setPage(1);
    } else {
      this.allItems = this.studentsCopy;
      this.setPage(1);
    }
  }

  setPage(page: number) {
    // get pager object from service
    this.pager = this.pagerService.getPager(this.allItems.length, page);

    // get current page of items
    this.pagedItems = this.allItems.slice(
      this.pager.startIndex,
      this.pager.endIndex + 1
    );
  }

  subscribeViewPanelChange() {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
  }

  selectAll(isChecked: boolean) {
    if ( isChecked ) {
      this.students.forEach(student => {
        this.studentIds[student.id] = true;
      });
    } else {
      this.students.forEach(student => {
        this.studentIds[student.id] = false;
      });
      this.selectAllStudent = false;
    }
    this.selectStudents();
  }

  sendSms() {
    this.adminService.viewPanel.next(true);
    this.smsCard = true;
    this.emailCard = false;
  }

  smsApi() {
    const object = {ids: this.ids, smscontent: this.smsContent};
    this.sending = true;
    this.smsService.sendStudentSMS(object).subscribe((response: any) => {
      this.sending = false;
      this.alertService.successAlert('Succesfully sent');
      this.hideViewPanel();
      this.selectAll(false);
    }, (error: any) => {
      this.alertService.errorAlert(error);
      this.sending = false;
    });
  }

  emailApi() {
    const object = {
    'ids': this.ids,
    'subject': this.emailsubject,
    'emailcontent': this.emailcontent
    };
    // this.files.forEach(element => {
    //   object['files'] = element;
    // });
    const formData = new FormData();
    Object.keys(object).forEach(key => {
      formData.append(key, object[key]);
    });
    for (const file of this.files) {
      formData.append('files', file);
    }

    this.emailData.files.forEach(file => {
      formData.append('files', file);
    });
    this.emailData.images.forEach(image => {
      formData.append('images', image);
    });
    
    this.sending = true;
    this.smsService.sendStudentEmail(formData).subscribe((response: any) => {
      this.sending = false;
      this.alertService.successAlert('Succesfully sent');
      this.hideViewPanel();
      this.selectAll(false);
    }, (error: any) => {
      this.alertService.errorAlert(error);
      this.sending = false;
    });
  }

  sendEmail() {
    this.adminService.viewPanel.next(true);
    this.smsCard = false;
    this.emailCard = true;
    console.log(this.ids);
  }

  selectStudents() {
    this.ids = [];
    Object.keys(this.studentIds).forEach(id => {
      if ( this.studentIds[id] ) {
        this.ids.push(id);
      }
    });
    if (!this.ids.length) {
      this.hideViewPanel();
    }
  }

  getFiles(event: any) {
    this.files = event.srcElement.files;
  }

  removeAttachment(index: number) {
    this.files.splice(index, 1);
  }

  dropped(event) {
    this.emailData = event;
    this.emailcontent = event.textContent;
  }
}
