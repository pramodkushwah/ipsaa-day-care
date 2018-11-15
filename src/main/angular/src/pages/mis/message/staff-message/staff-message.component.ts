import { Component, OnInit, NgZone, Renderer, ViewChild } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { PagerService } from '../../../../providers/pagination/pager.service';
import { SmsService } from '../../../../providers/sms/sms.service';
import { AlertService } from '../../../../providers/alert/alert.service';

export interface FileSystemEntry {
  name: string;
  isDirectory: boolean;
  isFile: boolean;
}

export interface FileSystemFileEntry extends FileSystemEntry {
  isDirectory: false;
  isFile: true;
  file(callback: (file: File) => void): void;
}

export class UploadFile {
  constructor(
      public relativePath: string,
      public fileEntry: FileSystemEntry) {
  }
}

@Component({
  selector: 'app-staff-message',
  templateUrl: './staff-message.component.html',
  styleUrls: ['./staff-message.component.css']
})
export class StaffMessageComponent implements OnInit {
  centers: any[];
  groups: any[];
  programs: any[];
  staffs: any[];
  staffIds: any = {};
  loader: boolean;
  allItems: any;
  staffsCopy: any;
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
  selectAllStaff: boolean;
  sending: boolean;
  emailData: any;

  constructor(
    private adminService: AdminService,
    private pagerService: PagerService,
    private smsService: SmsService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.getStaffs();
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

  getStaffs() {
    this.staffs = [];
    this.loader = true;
    this.adminService.getEmployee().subscribe((response: any) => {
      this.loader = false;
      this.allItems = response.stafflist;
      this.staffs = response.stafflist;
      this.staffsCopy = JSON.parse(JSON.stringify(this.staffs));
      this.pageSize = response.pageSize;
      this.pageNumber = response.pageNumber;
      // initialize to page 1
      this.setPage(1);
      // checked if searchKey entered before
      if (this.searchKey) {
        this.searchStaff(this.searchKey);
      }
    });
  }

  searchStaff(event: any) {
    this.searchKey = event;
    const val = event.target.value;
    if (val && val.trim() !== '') {
      this.allItems = this.staffs.filter(staff => {
        return staff.fullName.startsWith(val);
      });
      this.setPage(1);
    } else {
      this.allItems = this.staffsCopy;
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
      this.staffs.forEach(staff => {
        this.staffIds[staff.id] = true;
      });
    } else {
      this.staffs.forEach(staff => {
        this.staffIds[staff.id] = false;
      });
      this.selectAllStaff = false;
    }
    this.selectStaffs();
  }

  showSmsPanel() {
    this.adminService.viewPanel.next(true);
    this.smsCard = true;
    this.emailCard = false;
  }

  smsApi() {
    const object = {ids: this.ids, smscontent: this.smsContent};
    this.sending = true;
    this.smsService.sendStaffSMS(object).subscribe((response: any) => {
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
    this.smsService.sendStaffEmail(formData).subscribe((response: any) => {
      this.sending = false;
      this.alertService.successAlert('Succesfully sent');
      this.hideViewPanel();
      this.selectAll(false);
    }, (error: any) => {
      this.alertService.errorAlert(error);
      this.sending = false;
    });
  }

  showEmailPanel() {
    this.adminService.viewPanel.next(true);
    this.smsCard = false;
    this.emailCard = true;
  }

  selectStaffs() {
    this.ids = [];
    Object.keys(this.staffIds).forEach(id => {
      if ( this.staffIds[id] ) {
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
