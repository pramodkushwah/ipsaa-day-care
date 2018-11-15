import { Component, OnInit, Input } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';

import * as _ from 'underscore';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  selectedUser: any;
  selectedRoles: any[];
  selectedCenters: any[];
  userForm: FormGroup;
  editable: boolean;
  roles: any[];
  centers: any;
  employees: any[] = [];
  selectedRoleModel: string;
  selectedCenterModel: string;
  saving: boolean;

  @Input()
  set update(val: boolean) {
    this.editable = val;
  }

  @Input()
  set user(user: any) {
    this.selectedUser = user;
    this.selectedRoles = [];
    this.selectedCenters = [];
    this.userForm = this.getUserForm();
    if (this.editable) {
      this.userForm.patchValue(user);
      this.selectedRoles = JSON.parse(JSON.stringify(user.roles));
      this.selectedCenters = JSON.parse(JSON.stringify(user.centers));
    } else {
      this.selectedUser = {};
      this.userForm.reset();
    }
  }

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.selectedUser.employee
        ? this.userForm.controls['empId'].patchValue(this.selectedUser.employee.id)
        : this.userForm.controls['empId'].patchValue(null);
    this.getEmployee();
    this.getCenters();
    this.getRoles();
  }

  getRoles() {
    this.adminService.getRoles().subscribe((response: any[]) => {
      this.roles = response;
    });
  }

  getCenters() {
    this.adminService.getAllCenters().subscribe((response: any) => {
      this.centers = response;
    });
  }

  getEmployee() {
    this.adminService.getEmployee().subscribe((response: any) => {
      this.employees = response.stafflist;
    });
  }

  getUserForm() {
    return this.fb.group({
      active: [''],
      centers: [''],
      email: [''],
      empId: [''],
      employee: [''],
      firstname: [''],
      fullName: [''],
      id: [''],
      lastname: [''],
      mode: [''],
      name: [''],
      phone: [''],
      profileImage: [''],
      profileImageData: [''],
      roles: [[]],
      type: ['']
    });
  }

  hideViewPanel() {
    // this.viewPanel = false;
    this.adminService.viewPanel.next(false);
  }

  addRole(role: any) {
    if (!this.selectedRoles.length) {
      this.selectedRoles = [];
      this.selectedRoles.push(role);
    } else {
      if (this.selectedRoles.findIndex(element => role === element) === -1) {
        this.selectedRoles.push(role);
      }
    }
  }

  addCenter(center: any) {
    if (!this.selectedCenters.length) {
      this.selectedCenters = [];
      this.selectedCenters.push(center);
    } else {
      if (
        this.selectedCenters.findIndex(element => center === element) === -1
      ) {
        this.selectedCenters.push(center);
      }
    }
  }

  removeUserRole(index) {
    this.selectedRoles.splice(index, 1);
    this.selectedRoleModel = '';
  }

  removeUserCenter(index) {
    this.selectedCenters.splice(index, 1);
    this.selectedCenterModel = '';
  }

  resetPassword() {
    swal({
      title: 'New Password',
      buttons: ['Cancel', 'Ok'],
      content: {
        element: 'input',
        attributes: {
          placeholder: 'Type your password',
          type: 'password',
        },
      },
    }).then((value) => {
      if (value) {
        this.alertService.confirm('You want to change Password').then(isConfirm => {
          if (isConfirm) {
            this.adminService.resetUserPassword({id: this.selectedUser.id, password: value}).subscribe(success => {
              this.alertService.successAlert('Password Changed Successfully');
            });
          }
        });
      }
    });
  }

  removeUser() {}

  saveUser() {
    this.userForm.controls['roles'].patchValue(this.selectedRoles);
    this.userForm.controls['centers'].patchValue(this.selectedCenters);
    if (this.editable) {
      this.alertService.confirm('You want to update user').then(isConfirm => {
        if (isConfirm) {
          this.saving = true;
          this.adminService
            .updateUser(this.userForm.value)
            .subscribe(response => {
              _.extend(this.selectedUser, response);
              this.saving = false;
              this.userForm.reset();
              this.adminService.viewPanel.next(false);
              this.selectedRoles = [];
              this.selectedCenters = [];
              this.alertService.successAlert('User updated');
            });
        }
      });
    } else {
      this.saving = true;
      this.adminService.saveUser(this.userForm.value).subscribe(response => {
        // this.users.push(response);
        this.saving = false;
        this.userForm.reset();
        this.adminService.viewPanel.next(false);
        this.selectedRoles = [];
        this.selectedCenters = [];
        this.alertService.successAlert('New User added');
      });
    }
  }
}
