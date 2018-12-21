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
  selectedCenter: any = 'all';
  selectedProgram: any = 'all';
  selectedGroup: any = 'all';
  students: any[];
  studentIds: any = {};
  loader: boolean;
  allItems: any;
  studentsCopy: any;
  pageSize: any;
  pageNumber: any;
  searchKey: any;
  pager: any = {};
  pagedItems: any;
  viewPanel: boolean;
  smsCard: boolean;
  emailCard: boolean;
  ids: any[] = [];
  smsContent = '';
  emailsubject: any;
  emailcontent = '';
  attachments: any[] = [];
  files: any[] = [];
  selectAllStudent: boolean;
  sending: boolean;
  emailData: any;
  ccEmail: string;
  emailList: any = [];
  searchArray: any = [];
  selectedStudents: any[] = [];
  constructor(
    private adminService: AdminService,
    private pagerService: PagerService,
    private smsService: SmsService,
    private alertService: AlertService
  ) { }

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
    this.allItems = [];
    this.loader = true;
    this.adminService.getStudents({ active: true }).subscribe((response: any) => {
      this.loader = false;
      this.allItems = response.students.filter(stud => {
        return stud.active === true;
      });
      console.log(this.allItems);

      this.students = this.allItems;
      this.studentsCopy = JSON.parse(JSON.stringify(this.allItems));
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



  filterByCenter() {
    this.selectedProgram = 'all';
    this.selectedGroup = 'all';
    if (this.selectedCenter === 'all') {
      this.allItems = this.students;
    } else {
      this.allItems = this.students.filter(student => {
        return student.center.name === this.selectedCenter.name;
      });
    }

    this.searchArray = this.allItems.slice();
    this.setPage(1);
    this.selectAll(false);
  }

  filterByProgram() {
    this.selectedGroup = 'all';
    this.filterProgram();
    this.searchArray = this.allItems.slice();

    this.setPage(1);

  }

  filterProgram() {
    if (this.selectedCenter === 'all') {
      this.allItems = this.students.filter(student => {
        return student.program.code === this.selectedProgram.code;
      });
    } else {
      if (this.selectedProgram === 'all') {
        this.allItems = this.students.filter(student => {
          return student.center.name === this.selectedCenter.name;
        });
      } else {
        this.allItems = this.students.filter(student => {
          return student.center.name === this.selectedCenter.name && student.program.code === this.selectedProgram.code;
        });
      }

    }
    return this.allItems;

  }
  filterByGroup() {


    if (this.selectedGroup === 'all') {
      this.allItems = this.filterProgram();
    } else {

      this.allItems = this.filterProgram().filter(student => {
        return student.group.name === this.selectedGroup.name;
      });
    }
    this.searchArray = this.allItems.slice();
    this.setPage(1);

  }

  searchStudent(event: any) {
    this.searchKey = event;
    const val = event.target.value.toLowerCase();

    if ((this.selectedCenter !== 'all' || this.selectedProgram !== 'all' || this.selectedGroup !== 'all')) {
      if (val && val.trim() !== '') {
        this.allItems = this.searchArray.filter(student => {
          return student.fullName.toLowerCase().startsWith(val);
        });
        this.setPage(1);
      } else {
        this.allItems = this.searchArray;
        this.setPage(1);
      }
    } else {
      if (val && val.trim() !== '') {
        this.allItems = this.students.filter(student => {
          return student.fullName.toLowerCase().startsWith(val);
        });
        this.setPage(1);
      } else {
        this.allItems = this.studentsCopy;
        this.setPage(1);
      }
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
    if (isChecked) {
      this.allItems.forEach(student => {
        this.studentIds[student.id] = true;
      });
    } else {
      this.allItems.forEach(student => {
        this.studentIds[student.id] = false;
      });
      this.selectAllStudent = false;
      this.hideViewPanel();
    }
    this.selectStudents();
  }

  sendSms() {
    this.adminService.viewPanel.next(true);
    this.smsCard = true;
    this.emailCard = false;
  }

  smsApi() {
    const object = { ids: this.ids, smscontent: this.smsContent };
    this.sending = true;
    this.smsService.sendStudentSMS(object).subscribe((response: any) => {
      this.sending = false;
      this.studentIds = {};

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
      'cids': this.emailData.cids,
      'ids': this.ids,
      'subject': this.emailsubject,
      'emailcontent': this.emailData.emailcontent
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

    // this.emailData.files.forEach(file => {
    //   formData.append('files', file);
    // });
    this.emailData.images.forEach(image => {
      formData.append('images', image);
    });
    this.emailList.forEach(element => {
      formData.append('cc', element);
    });
    this.sending = true;
    this.smsService.sendStudentEmail(formData).subscribe((response: any) => {
      this.studentIds = {};
      this.sending = false;
      this.emailList = [];
      this.ccEmail = '';
      this.alertService.successAlert('Succesfully sent');
      this.hideViewPanel();
      this.selectAll(false);
    }, (error: any) => {
      this.alertService.errorAlert(error);
      this.sending = false;
    });
  }

  sendEmail() {
    this.selectedStudents = [];
    this.ids.forEach((id: number) => {
      const student: any = this.allItems.find(s => {
        return s.id == id;
      });
      if (student) {
        this.selectedStudents.push(student);
      }
    });
    this.adminService.viewPanel.next(true);
    this.smsCard = false;
    this.emailCard = true;
    console.log(this.ids);
  }

  selectStudents() {
    this.ids = [];
    Object.keys(this.studentIds).forEach(id => {
      if (this.studentIds[id] === true) {
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
    console.log(event);

    // this.emailcontent = event.textContent || '';
  }


  addCcEmail() {
    this.emailList.push(this.ccEmail);
    this.ccEmail = '';
  }
  removeCcEmail(i) {
    this.emailList.splice(i, 1);
  }
}
