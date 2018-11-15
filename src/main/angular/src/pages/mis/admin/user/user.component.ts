import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  users: any[] = [];
  editable: boolean;
  viewPanel: boolean;
  selectedUser: any;
  usersCopy: any[];

  constructor(private adminService: AdminService) { }

  ngOnInit() {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
    this.getUsers();
  }

  getUsers() {
    this.adminService.getUsers().subscribe((response: any[]) => {
      this.users = response;
      this.usersCopy = JSON.parse(JSON.stringify(response));
    });
  }

  showSidePanel(update: boolean, user: any) {
    this.editable = update;
    this.selectedUser = user;
    // this.viewPanel = true;
    this.adminService.viewPanel.next(true);
    this.users = JSON.parse(JSON.stringify(this.usersCopy));
  }

}
