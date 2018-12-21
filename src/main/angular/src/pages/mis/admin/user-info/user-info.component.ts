import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
  EmpName: string;
  centers: any[] = [];
  employees: any[] = [];
  employeesCopy: any[] = [];
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
      if (user.employee) {
        this.EmpName = user.employee.name;
        this.userForm.controls['empId'].patchValue(user.employee.id);
      }
      this.selectedRoles = JSON.parse(JSON.stringify(user.roles));
      this.selectedCenters = user.centers;
    } else {
      this.selectedUser = {};
      this.userForm.reset();
    }
  }
  @Output() addUser: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) { }

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
      this.centers = this.centers.filter(cen => {
        return !this.selectedCenters.includes(cen.name);
      });
    });
  }

  getEmployee() {
    this.adminService.getEmployee().subscribe((response: any) => {
      this.employees = response.stafflist;
      this.employeesCopy = response.stafflist;
    });
  }

  getUserForm() {
    return this.fb.group({
      active: [''],
      centers: [''],
      email: ['', [Validators.required]],
      empId: [''],
      employee: [''],
      firstname: ['', [Validators.required]],
      fullName: [''],
      id: [''],
      lastname: [''],
      mode: [''],
      name: [''],
      phone: ['', [Validators.required]],
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
      if (!this.selectedCenters.includes(center)) {
        this.selectedCenters.push(center);
        this.centers.splice(this.centers.findIndex((cen) => {
          return cen.name === center;
        }), 1);
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
            this.adminService.resetUserPassword({ id: this.selectedUser.id, password: value }).subscribe(success => {
              this.alertService.successAlert('Password Changed Successfully');
            });
          }
        });
      }
    });
  }

  removeUser() {

    this.adminService.deleteUser(this.selectedUser.id)
      .subscribe((res: any) => {

        this.alertService.successAlert('User Dalete Successfuly');
      });
  }

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
            }, error => {
              this.saving = false;
            });
        }
      });
    } else {
      this.saving = true;
      this.userForm.controls['active'].patchValue(true);
      this.adminService.saveUser(this.userForm.value).subscribe(response => {
        // this.users.push(response);
        this.saving = false;
        this.userForm.reset();
        this.adminService.viewPanel.next(false);
        this.selectedRoles = [];
        this.selectedCenters = [];
        this.alertService.successAlert('New User added');
        this.addUser.emit(response);
      }, error => {
        this.saving = false;
      });
    }
  }

  getSelectedEmployee(employe) {
    this.EmpName = employe.name;
    const employee = this.employees.find(emp => {
      return emp.id === employe.id;
    });
    if (employee) {
      // this.userForm.patchValue(employee);
      this.userForm.controls['empId'].patchValue(employee.id);
      this.userForm.controls['firstname'].patchValue(employee.name);
      this.userForm.controls['phone'].patchValue(employee.mobile);
      this.userForm.controls['email'].patchValue(employee.email);
    }
  }

  searchStudent(event) {
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.employees = this.employeesCopy.filter(employee => {
        return employee.name.toLowerCase().startsWith(val);
      });
    } else {
      this.employees = this.employeesCopy;
    }
  }
}
