import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';

@Component({
  selector: 'app-holidays',
  templateUrl: './holidays.component.html',
  styleUrls: ['./holidays.component.css']
})
export class HolidaysComponent implements OnInit {


  zones: Array<any>;
  selectedZone: any;
  states: Array<any>;
  selectedState: any;
  cities: Array<any>;
  selectedCity: any;
  centers: Array<any>;
  selectedCenter: any;
  holidays: Array<any> = [];
  holidaysCopy: Array<any> = [];
  mode: string;
  centerCopy: any = [];
  formCenters: any = [];
  formCenterCopy: any = [];

  show: boolean;
  viewPanel: boolean;
  showtable = false;
  downloadinData: boolean;
  currentMonth: any;
  currentYear: any;
  selectCenter: any;
  hollidayForm: FormGroup;
  saved: boolean;

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) { }


  ngOnInit() {
    this.currentMonth = (new Date().getMonth() + 1);
    this.currentYear = (new Date().getFullYear());
    this.getCenter();
    this.getZone();
    this.getHoliday();
  }

  getHoliday() {
    this.adminService.getHolidaysList({
      month: '1',
      year: this.currentYear
    })
      .subscribe((res: any) => {
        this.holidays = res;
        this.holidaysCopy = res;
      });
  }

  getHolidayForm() {
    return this.fb.group({
      name: [''],
      holidayDate: [''],
      centers: [''],
      floating: [false],
      id: [''],
      optional: [false]
    });
  }

  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;
        this.centerCopy = res.slice(0);
      });
  }

  getZone() {
    this.adminService.getZones()
      .subscribe((res: any) => {
        this.zones = res;
      });
  }

  getSelectedZoneStates(zone) {
    this.states = [];
    this.cities = [];
    this.adminService.getStatesByZone(zone.id)
      .subscribe((res: any) => {
        this.states = res;
        this.filterCenter(zone.name, 'zone');
      });
  }

  getSelectedStateCities(state) {
    this.cities = [];
    this.adminService.getCitiesByStateByZone(state.id)
      .subscribe((res: any) => {
        this.cities = res;
        this.filterCenter(state.name, 'state');
      });
  }


  filterCenter(filterName, filterBy) {
    this.centers = this.centerCopy.filter(center => {
      return (center[filterBy] === filterName);
    });
    // this.holidays = this.holidaysCopy.filter(elem => {
    //   elem.centers.forEach(element => {
    //     this.centers.forEach(el => {
    //       console.log(el.id + '  ' + element.id);
    //       return el.id === element.id;
    //     });
    //   });
    // });
    // console.log(this.centers);

  }


  add() {
    this.viewPanel = true;
    this.mode = 'New';
    this.show = true;
    this.formCenters = [];
    this.hollidayForm = this.getHolidayForm();

  }
  showHoliday(x, y) {

    // this.show = true;
  }
  editHoliday(holiday, show) {
    this.show = show;
    this.viewPanel = true;
    this.hollidayForm = this.getHolidayForm();
    this.hollidayForm.patchValue(holiday);
    this.formCenters = holiday.centers;
    this.mode = (show) ? 'Edit' : 'show';
  }
  delHoliday(holidayId) {
    this.adminService.deleteHoliday(holidayId)
      .subscribe((res: any) => {
        for (let i = 0; i < this.holidays.length; i++) {
          if (this.holidays[i].id === holidayId) {
            this.holidays.splice(i, 1);
          }
        }
        this.alertService.successAlert('Delete successfuly');
      });
  }

  addCenter(center) {
    if (center === 'All') {
      this.centers.forEach(element => {
        this.formCenters.push(element);
      });
    } else {
      this.formCenters.push(center);
    }
    this.formCenters = this.formCenters.filter(function (elem, index, self) {
      return index === self.indexOf(elem);
    });

  }

  removeCenter(center) {

    for (let i = 0; i < this.formCenters.length; i++) {
      if (this.formCenters[i].id === center.id) {
        this.formCenters.splice(i, 1);
      }
    }

  }

  saveHoliday() {
    this.saved = true;
    this.formCenterCopy = [];
    this.formCenters.forEach(element => {
      this.formCenterCopy.push(element.id);
    });
    this.hollidayForm.get('centers').setValue(this.formCenterCopy);
    this.adminService.AddNewHolliday(this.hollidayForm.value)
      .subscribe((res: any) => {
        this.hollidayForm.reset();
        this.saved = false;
        this.alertService.successAlert('Holliday Add');
        this.holidays.push(res);
      });
  }

  hideSidePanel() {
    this.viewPanel = false;
    this.formCenters = [];
  }
}
