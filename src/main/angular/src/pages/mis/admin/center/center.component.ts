import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DashboardService } from '../../../../providers/dashboard/dashboard.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';

@Component({
  selector: 'app-center',
  templateUrl: './center.component.html',
  styleUrls: ['./center.component.css']
})
export class CenterComponent implements OnInit {
  tabs = ['Center', 'Zone', 'City'];
  zones: any[] = [];
  cities: any[] = [];
  centers: any[] = [];
  tableData: any[] = [];
  tableColumn: string[] = [];
  tableTitle = 'Centers';
  viewPanel: boolean;
  centerForm: any;
  zoneForm: any;
  cityForm: any;
  editable: boolean;
  selectedTab = 'Center';
  selectedCenter: any;
  selectedZone: any;
  selectedCity: any;
  centersCopy: any = [];
  states: any = [];
  stateForm: FormGroup;
  selectedState: any;

  constructor(
    private dashboardService: DashboardService,
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
    this.getCenters();
    this.getZones();
    this.getCities();
    this.getStates();
    this.changeTab('Center');
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

  getCenters() {
    this.dashboardService.getCenters().subscribe((response: any[]) => {
      this.centers = response;
      this.centersCopy = JSON.parse(JSON.stringify(response));
      this.tableTitle = 'Centers';
      this.tableData = this.centers;
      this.tableColumn = [
        'code',
        'capacity',
        'name',
        'type',
        'address',
        'city',
        'zone'
      ];
    });
  }

  getStates() {
    this.dashboardService.getStates().subscribe(response => {
      this.states = response;
    });
  }

  changeTab(tab: string) {
    this.selectedTab = tab;
    this.viewPanel = false;
    this.tableData = [];
    this.tableColumn = [];
    switch (tab) {
      case 'Center':
        this.tableTitle = 'Centers';
        this.tableData = this.centers;
        this.tableColumn = [
          'code',
          'capacity',
          'name',
          'type',
          'address',
          'city',
          'zone'
        ];
        break;
      case 'Zone':
        this.tableTitle = 'Zones';
        this.tableData = this.zones;
        this.tableColumn = ['name', 'cities'];
        break;
      case 'City':
        this.tableTitle = 'Cities';
        this.tableData = this.cities;
        this.tableColumn = ['name', 'zone', 'state'];
        break;
      case 'State':
      this.tableTitle = 'States';
      this.tableData = this.states;
      this.tableColumn = ['name'];
      break;
    }
  }

  showSidePanel(update: boolean, data: any) {
    this.viewPanel = true;
    this.subscribeViewPanelChange();
    this.editable = update;
    if (update) {
      switch (this.tableTitle) {
        case 'Centers':
          this.selectedCenter = data;
          this.centerForm = this.getCenterForm().patchValue(data);
          break;
        case 'Zones':
          this.selectedZone = data;
          this.zoneForm = this.getZoneForm();
          this.zoneForm.patchValue(data);
          break;
        case 'Cities':
          this.selectedCity = data;
          this.cityForm = this.getCityForm();
          this.cityForm.patchValue(data);
          break;
        case 'States':
          this.selectedState = data;
          this.stateForm = this.getStateForm();
          this.stateForm.patchValue(data);
        break;
      }
    } else {
      switch (this.tableTitle) {
        case 'Centers':
          this.selectedCenter = null;
          this.centerForm = this.getCenterForm();
          break;
        case 'Zones':
          this.zoneForm = this.getZoneForm();
          break;
        case 'Cities':
          this.cityForm = this.getCityForm();
          break;
        case 'States':
          this.stateForm = this.getStateForm();
        break;
      }
    }
  }

  hideViewPanel() {
    this.viewPanel = false;
  }

  getCenterForm() {
    return this.fb.group({
      active: [],
      address: [],
      addressType: [],
      capacity: [],
      city: [],
      code: [],
      id: [],
      mode: [],
      name: [],
      phone: [],
      state: [],
      type: [],
      zipcode: [],
      zone: []
    });
  }

  getZoneForm() {
    return this.fb.group({
      id: [null],
      mode: ['New'], /** 'Edit' in the case of update */
      name: [''],
      cities: []
    });
  }

  getCityForm() {
    return this.fb.group({
      id: [null],
      mode: ['New'], /** 'Edit' in the case of update*/
      name: [''],
      zone: [''],
      state: ['']
    });
  }

  getStateForm() {
    return this.fb.group({
      id: [null],
      name: ['']
    });
  }

  subscribeViewPanelChange() {
    if (this.viewPanel && this.tableTitle === 'Centers') {
      for (let i = this.tableColumn.length - 1; i--; ) {
        if (this.tableColumn[i] === 'address') {
          this.tableColumn.splice(i, 1);
        }
      }
    }
  }

  saveZone() {
    if (this.editable) {
      this.alertService.confirm('').then((isConfirm) => {
        if (isConfirm) {
          this.adminService.updateZone(this.zoneForm.value).subscribe((response: any) => {
            _.extend(this.selectedZone, response);
            this.alertService.successAlert('You have updated zone!');
            this.adminService.viewPanel.next(false);
          });
        }
      });
    } else {
      this.adminService.saveZone(this.zoneForm.value).subscribe((response: any) => {
        this.zones.push(response);
        this.alertService.successAlert('You have added new zone!');
      });
    }
  }

  saveCity() {
    if (this.editable) {
      this.alertService.confirm('').then((isConfirm) => {
        if (isConfirm) {
          this.adminService.updateCity(this.cityForm.value).subscribe((response: any) => {
            _.extend(this.selectedCity, response);
            this.alertService.successAlert('You have updated city!');
            this.adminService.viewPanel.next(false);
          });
        }
      });
    } else {
      this.adminService.saveCity(this.cityForm.value).subscribe((response: any) => {
        this.cities.push(response);
        this.alertService.successAlert('You have added new city!');
        this.adminService.viewPanel.next(false);
      });
    }
  }

  saveState() {
    if (this.editable) {
      this.alertService.confirm('').then((isConfirm) => {
        if (isConfirm) {
          this.adminService.updateState(this.stateForm.value).subscribe((response: any) => {
            _.extend(this.selectedState, response);
            this.alertService.successAlert('You have updated state!');
            this.adminService.viewPanel.next(false);
          });
        }
      });
    } else {
      this.adminService.saveState(this.stateForm.value).subscribe((response: any) => {
        this.states.push(response);
        this.alertService.successAlert('You have added new State!');
        this.adminService.viewPanel.next(false);
      });
    }
  }

  deleteZone(zone: any) {
    this.alertService.confirm('Once deleted, you will not be able to recover this Zone').then(isConfirm => {
      if (isConfirm) {
        this.adminService.deleteZone(zone.id).subscribe((response: any) => {
          this.zones.splice(this.zones.indexOf(this.selectedZone), 1);
        });
      }
    });
  }

  deleteCity(city: any) {
    this.alertService.confirm('Once deleted, you will not be able to recover this City').then(isConfirm => {
      if (isConfirm) {
        this.adminService.deleteCity(city.id).subscribe((response: any) => {
          this.cities.splice(this.cities.indexOf(this.selectedCity), 1);
        });
      }
    });
  }

  deleteState(state: any) {
    this.alertService.confirm('Once deleted, you will not be able to recover this state').then(isConfirm => {
      if (isConfirm) {
        this.adminService.deleteState(state.id).subscribe((response: any) => {
          this.cities.splice(this.cities.indexOf(this.selectedState), 1);
        });
      }
    });
  }

  deleteCenter(center: any) {
    this.alertService.confirm('Once deleted, you will not be able to recover this Center').then(isConfirm => {
      if (isConfirm) {
        this.adminService.deleteCenter(center.id).subscribe((response: any) => {
          this.centers.splice(this.centers.indexOf(this.selectedCenter), 1);
          this.alertService.successAlert('You have successfully deleted ' + center.name + 'Center');
        });
      }
    });
  }

  filterCenter(searchKey) {
    this.tableData = this.centersCopy;
    const val = searchKey.toLowerCase();
    if (val && val.trim() !== '') {
      this.tableData = this.centersCopy.filter((center: any) => {
        return center.code.toLowerCase().startsWith(val);
      });
    }
  }

  pushCenter(center) {
    console.log(center);
    if (center) {
      this.centers.push(center);
    }
  }
}
