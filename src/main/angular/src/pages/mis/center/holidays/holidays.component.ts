import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, Validators } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';

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
  mode: string;
  show: boolean;
  viewPanel = true;
  showtable = false;
  downloadinData: boolean;
  hollidayForm = this.fb.group({
    name: ['', Validators.required],
    date: ['', Validators.required],
    centers: ['', Validators.required]
  });
  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private alertService: AlertService
  ) { }


  ngOnInit() {
    this.getCenter();
    this.getZone();
  }


  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        this.centers = res;
      });
  }

  getZone() {
    this.adminService.getZones()
      .subscribe((res: any) => {
        this.zones = res;
        console.log(res);
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  getSelectedZoneStates(zoneId) {
    this.adminService.getStatesByZone(zoneId)
      .subscribe((res: any) => {
        this.states = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  getSelectedStateCities(stateId) {
    this.adminService.getCitiesByStateByZone(stateId)
      .subscribe((res: any) => {
        this.cities = res;
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }


  getCenterStudentFeeDetails() {
    this.downloadinData = false;
    this.alertService.loading.next(true);
    this.adminService.getStudentFeeList({
      'centerCode': this.hollidayForm.value.center,
      'period': 'Quarterly',
      'quarter': this.hollidayForm.value.quater,
      'year': this.hollidayForm.value.year
    })
      .subscribe((res) => {
        this.alertService.loading.next(false);

        this.showtable = true;
        this.downloadinData = true;

      }, (err) => {
        this.downloadinData = true;
        this.showtable = true;
        this.alertService.loading.next(false);
        this.alertService.errorAlert(err);
      });



  }


  showHoliday(x, y) {

  }
  editHoliday(f) {

  }
  delHoliday(g) {
  }

  removeCenter(s) {

  }

  saveHoliday() {
  }

  cancelHoliday() {

  }
}
