import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import * as _ from 'underscore';
import { AlertService } from '../../../../providers/alert/alert.service';

@Component({
  selector: 'app-program',
  templateUrl: './program.component.html',
  styleUrls: ['./program.component.css']
})
export class ProgramComponent implements OnInit {
  programs: any[] = [];
  groups: any[];
  groupCopy: any[];
  viewPanel: boolean;
  editable: boolean;
  selectedProgram: any = {};
  programForm: FormGroup;
  selectedGroup: any = {};
  selectedGroups: any;
  groupForm: FormGroup;
  selectedGroupModel: string;
  selectedTab = 'Program';
  saving: boolean;
  constructor(private adminService: AdminService, private fb: FormBuilder, private alertService: AlertService) {

  }

  ngOnInit() {
    this.getPrograms();
    this.getGroups();
  }

  getPrograms() {
    this.adminService.getPrograms().subscribe((response: any[]) => {
      this.programs = response;
    });
  }

  getGroups() {
    this.adminService.getGroups().subscribe((response: any[]) => {
      this.groups = response;
      this.groupCopy = response;
    });
  }

  changeTab(tab: string) {
    this.selectedTab = tab;
    this.viewPanel = false;
  }

  showSidePanel(update: boolean, object: any) {
    this.editable = update;
    this.viewPanel = true;
    this.selectedGroups = [];
    if (this.selectedTab === 'Program') {
      this.selectedProgram = (object) ? object : {};
      this.programForm = this.getProgramForm();
      this.programForm.patchValue(this.selectedProgram);
      (object) ? this.selectedGroups = JSON.parse(JSON.stringify(object.groups)) : this.selectedGroups = [];
    } else {
      this.selectedGroup = (object) ? object : {};
      this.groupForm = this.getGroupForm();
      this.groupForm.patchValue(this.selectedGroup);
    }
  }

  getProgramForm() {
    return this.fb.group({
      code: [''],
      description: [''],
      groups: [],
      id: [''],
      mode: ['New'],
      name: ['']
    });
  }

  getGroupForm() {
    return this.fb.group({
      description: [''],
      id: [''],
      mode: ['New'],
      name: ['']
    });
  }

  hideViewPanel() {
    this.viewPanel = false;
  }

  deleteProgram(program: any) {
    this.alertService.confirm('you want to delete ' + program.name + 'program').then(() => {
      this.adminService.deleteProgram(program.id).subscribe(response => {
        this.alertService.successAlert(program.name + 'Program Deleted Successfully');
        this.programs.splice(this.programs.indexOf(program, 1));
      });
    }).catch((error) => {

    });
  }

  addProgramGroup(program) {
    if (!this.selectedGroups.length) {
      this.selectedGroups = [];
      this.selectedGroups.push(program);
    } else {
      if (this.selectedGroups.findIndex(element => program.id === element.id) === -1) {
        this.selectedGroups.push(program);
      }
    }
  }

  removeProgramGroup(index: number) {
    this.selectedGroups.splice(index, 1);
    this.selectedGroupModel = '';
  }

  saveProgram() {
    if (this.editable) {
      this.alertService.confirm('You want to update program').then(isConfirm => {
        if (isConfirm) {
          this.saving = true;
          this.programForm.controls['groups'].patchValue(this.selectedGroups);
          this.adminService.updateProgram(this.programForm.value).subscribe((response: any) => {
            this.saving = false;
            _.extend(this.selectedProgram, response);
            this.alertService.successAlert('Program Updated');
            this.viewPanel = false;
            this.programForm.reset();
            this.selectedGroups = [];
          });
        }
      });
    } else {
      this.saving = true;
      this.programForm.controls['groups'].patchValue(this.selectedGroups);
      this.adminService.saveProgram(this.programForm.value).subscribe((response: any) => {
        this.saving = false;
        this.programs.push(response);
        this.viewPanel = false;
        this.programForm.reset();
        this.selectedGroups = [];
        this.alertService.successAlert('New Program Added.');
      });
    }
  }

  saveGroup() {
    if (this.editable) {
      this.alertService.confirm('You want to update group').then(res => {
        this.saving = true;
        this.adminService.updateGroup(this.groupForm.value).subscribe((response: any) => {
          this.saving = false;
          _.extend(this.selectedGroup, response);
          this.alertService.successAlert('Group Updated');
          this.viewPanel = false;
          this.groupForm.reset();
        });
      });
    } else {
      this.saving = true;
      this.adminService.saveGroup(this.groupForm.value).subscribe((response: any) => {
        this.saving = false;
        this.groups.push(response);
        this.viewPanel = false;
        this.groupForm.reset();
        this.alertService.successAlert('New Group Added.');
      });
    }
  }

  deleteGroup(group) {
    this.adminService.deleteGroup(group.id)
      .subscribe((res: any) => {
this.alertService.successAlert('Group Delete Successfuly');
this.groups = this.groupCopy.filter( element => {
  return element.id === group.id;
});
      });
  }
}
