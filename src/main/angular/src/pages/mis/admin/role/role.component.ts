import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';


@Component({
  selector: 'app-role',
  templateUrl: './role.component.html',
  styleUrls: ['./role.component.css']
})
export class RoleComponent implements OnInit {
  roles: any[] = [];
  editable: boolean;
  roleForm: FormGroup;
  viewPanel: boolean;
  selectedPrivileges: any[] = [];
  selectedPrivilegeModel: string;
  privileges: any[];
  saving: boolean;
  selectedRole: any;

  constructor(private adminService: AdminService, private fb: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.getRoles();
    this.getPrivileges();
  }

  getRoles() {
    this.adminService.getRoles().subscribe((response: any[]) => {
      this.roles = response;
    });
  }

  getPrivileges() {
    this.adminService.getAllPrivileges().subscribe((response: any[]) => {
      this.privileges = response;
    });
  }

  showSidePanel(update: boolean, role: any) {
    this.editable = update;
    this.viewPanel = true;
    this.selectedRole = role;
    this.selectedPrivileges = [];
    this.roleForm = this.getRoleForm();
    if (this.editable) {
      this.roleForm.patchValue(role);
      this.selectedPrivileges = JSON.parse(JSON.stringify(role.privileges));
    } else {
      this.roleForm.reset();
    }
  }

  hideViewPanel() {
    this.viewPanel = false;
  }

  getRoleForm() {
    return this.fb.group({
      id: [''],
      mode: [''],
      name: [''],
      privileges: ['']
    });
  }

  addRolePrivilege(privilege) {
    if (!this.selectedPrivileges.length) {
      this.selectedPrivileges = [];
      this.selectedPrivileges.push(privilege);
    } else {
      if (this.selectedPrivileges.findIndex(element =>  privilege.id === element.id) === -1) {
        this.selectedPrivileges.push(privilege);
      }
    }
  }

  removeRolePrivilege(index: number) {
    this.selectedPrivileges.splice(index, 1);
    this.selectedPrivilegeModel = '';
  }

  saveRole() {
    this.roleForm.controls['privileges'].patchValue(this.selectedPrivileges);
    if (this.editable) {
      this.alertService.confirm('You want to update role and privileges').then(isConfirm => {
        if (isConfirm) {
          this.saving = true;
          this.adminService.updateRole(this.roleForm.value).subscribe((response) => {
            _.extend(this.selectedRole, response);
            this.saving = false;
            this.roleForm.reset();
            this.viewPanel = false;
            this.selectedPrivileges = [];
            this.alertService.successAlert('Role and Privileges updated');
          });
        }
      });
    } else {
      this.saving = true;
      this.adminService.saveRole(this.roleForm.value).subscribe((response) => {
        this.roles.push(response);
        this.saving = false;
        this.roleForm.reset();
        this.viewPanel = false;
        this.selectedPrivileges = [];
        this.alertService.successAlert('New Role added');
      });
    }
  }

  deleteRole(role: any) {
    this.alertService.confirm('you want to delete ' + role.name + ' role').then(() => {
      this.adminService.deleteRole(role.id).subscribe(response => {
        this.alertService.successAlert(role.name + 'role Deleted Successfully');
        this.roles.splice(this.roles.indexOf(role, 1));
      });
    });
  }
}
