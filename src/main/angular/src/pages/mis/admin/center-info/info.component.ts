import { Component, Input } from '@angular/core';
import { DashboardService } from '../../../../providers/dashboard/dashboard.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import {FormBuilder, FormGroup } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';

@Component({
  selector: 'app-center-info',
  templateUrl: './info.component.html'
})
export class CenterInfoComponent {
  editable: boolean;
  zones: any[];
  cities: any[];
  selectedCenter: any;
  centerForm: FormGroup;
  saving: boolean;

  @Input()
  set center(center: any) {
    if (center) {
      this.selectedCenter = center;
      this.centerForm = this.getCenterForm();
      this.centerForm.patchValue(center);
    } else {
      this.centerForm = this.getCenterForm();
    }
  }

  @Input()
  set update(val: boolean) {
    this.editable = val;
  }
  constructor(
    private dashboardService: DashboardService,
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) {
    this.getZones();
    this.getCities();
    this.centerForm = this.getCenterForm();
  }

  getCenterForm() {
    return this.fb.group({
      active: [''],
      address: [''],
      addressType: [''],
      capacity: [''],
      city: [''],
      code: [''],
      id: [''],
      mode: [''],
      name: [''],
      phone: [''],
      state: [''],
      type: [''],
      zipcode: [''],
      zone: ['']
    });
  }

  hideViewPanel() {
    this.adminService.viewPanel.next(false);
  }

  getZones() {
    this.dashboardService.getZones().subscribe((response: any[]) => {
      this.zones = response;
    });
  }

  getCities() {
    this.dashboardService.getCities().subscribe((response: any[]) => {
      this.cities = response;
    });
  }

  getCitiesByZone(zone: any) {
    if (zone) {
      this.cities = this.zones.find(element => {
        return element.name === zone;
      }).cities;
    }
  }

  saveCenter() {
    console.log(this.centerForm.value);
    if (this.editable) {
      this.alertService.confirm('You want to update center').then( res => {
        this.saving = true;
        this.adminService.updateCenter(this.centerForm.value).subscribe((response: any) => {
          _.extend(this.selectedCenter, response);
          this.alertService.successAlert('Center Updated');
          this.saving = false;
          this.adminService.viewPanel.next(false);
          this.centerForm.reset();
        }, error => {
          this.saving = false;
        });
      });
    } else {
      this.saving = true;
        this.adminService.saveCenter(this.centerForm.value).subscribe((response: any) => {
          this.alertService.successAlert('New Center added');
          this.saving = false;
          this.adminService.viewPanel.next(false);
          this.centerForm.reset();
        }, error => {
          this.saving = false;
        });
    }
  }
}
