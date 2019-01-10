import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormGroup, FormBuilder, FormControl, Validators, ValidatorFn, AbstractControl } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as _ from 'underscore';
import { AlertService } from '../../../../providers/alert/alert.service';
import { PayrollService } from '../../../../providers/payroll/payroll.service';
declare let $: any;

@Component({
  selector: 'app-staff-info',
  templateUrl: './staffInfo.component.html',
  styleUrls: ['./staffInfo.component.css']
})
export class StaffInfoComponent implements OnInit {
  @Output() addStaff: EventEmitter<any> = new EventEmitter<any>();
  paySlips: any = [];
  staff: any = {};
  editable: boolean;
  staffForm: FormGroup;
  newStaff: boolean;
  centers = [];
  picFile: any;
  formSave: boolean;
  costCenters = [];
  allReportingManagers = [];
  filterReportingManager = [];
  searchEmpName: string;
  martialStatusOptions = ['Married', 'Unmarried', 'Widowed', 'Divorced'];
  months: any[] = [
    'Jan',
    'Feb',
    'March',
    'April',
    'May',
    'June',
    'July',
    'Aug',
    'Sept',
    'Oct',
    'Nov',
    'Dec'
  ];
  salary: any;
  states: any;
  constructor(
    private adminService: AdminService,
    private payrollService: PayrollService,
    private fb: FormBuilder,
    private alertService: AlertService,
    private datePipe: DatePipe
  ) { }

  ngOnInit() {
    this.staffForm = this.getStaffForm();
    this.getCenters();
    this.getStates();
    this.getCostCenters();
    this.getReportingManagers();
  }

  @Input()
  set id(id: number) {
    if (id) {
      // if id exist then show staff details
      this.newStaff = false;
      this.getStaff(id);
      this.getPaySlipByEmployee(id);
    } else {
      // no staff id so add new staff
      this.staff = {};
      this.newStaff = true;
      this.staffForm = this.getStaffForm();
    }
  }

  @Input()
  set update(update: boolean) {
    this.editable = update;
    const action = update ? 'enable' : 'disable';
  }

  // get staff data
  getStaff(id) {
    this.adminService.getStaffById(id).subscribe((staff: any) => {
      this.staff = staff;
      this.staffForm.patchValue(staff);
      this.searchEmpName = this.staffForm.value.reportingManagerName;
      this.staffForm.controls['costCenterId'].patchValue(
        staff.costCenter ? staff.costCenter.id : ''
      );
      this.payrollService.getSalaryByEmployee(staff.eid).subscribe(response => {
        this.salary = response;
      });
    });
  }

  getCenters() {
    this.adminService.getCenters().subscribe(res => {
      this.centers = res;
    });
  }

  getStates() {
    this.adminService.getStates().subscribe(response => {
      this.states = response;
    });
  }

  getCostCenters() {
    this.adminService.getCostCenter().subscribe(res => {
      this.costCenters = res;
    });
  }

  getReportingManagers() {
    this.adminService.getStaff().subscribe(res => {
      res.forEach(staff => {

        // this.staffList = this.allItems.filter(staff => {
        //   console.log(status);

        //   return staff.active === status;
        if (staff.active === true) {
          this.allReportingManagers.push({ id: staff.id, name: staff.name, designation: staff.designation });

        }
      });
      console.log(this.allReportingManagers);

      this.filterReportingManager = this.allReportingManagers;
    });
  }
  setReportingManagerId(reportingManagerName) {
    this.searchEmpName = reportingManagerName.name;
    this.allReportingManagers.filter(staff => {
      if (reportingManagerName.name === staff.name) {
        this.staffForm.controls['reportingManagerId'].patchValue(staff.id);
        this.staffForm.controls['reportingManagerName'].patchValue(staff.name);

      }
    });

  }

  hideViewPanel() {
    this.adminService.viewPanel.next(false);
  }
  uploadProfilePic(staff: any, file: any) {
    this.picFile = file;
    const formData = new FormData();
    formData.append('file', file);
    if (!staff.id) {

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = function (e: any) {
        $('#new-Staff-profile').attr('src', e.target.result);
      };
    } else {
      this.adminService
        .updateStaffProfilePic(staff.id, formData)
        .subscribe((response: any) => {
          this.getStaff(staff.id);
          // const reader = new FileReader();
          // reader.readAsDataURL(this.picFile);
          // reader.onload = function (e: any) {
          //   $('#staff-profile').attr('src', e.target.result);
          //   staff.staffImageData = e.target.result;
          // };
        });
    }
  }

  getStaffForm() {
    return this.fb.group({
      id: [''],
      eid: [''],
      costCenter: this.fb.group({
        id: [null],
        name: [''],
        code: [''],
        type: [''],
        active: ['']
      }),
      employerId: [null, Validators.required],
      employerName: [''],
      reportingManagerId: [''],
      reportingManagerName: [null, Validators.required],
      name: [''],
      firstName: ['', Validators.required],
      lastName: [''],
      mobile: ['', [Validators.required]],
      secondaryNumbers: [''],
      email: ['', [Validators.email]],
      payrollEnabled: [''],
      attendanceEnabled: [''],
      designation: ['', Validators.required],
      type: [null, Validators.required],
      active: [''],
      expectedIn: [''],
      expectedOut: [''],
      maritalStatus: [null],
      profile: this.getProfile(),
      biometricId: [''],
      approvalStatus: [''],
      expectedHours: [''],
      aadharNumber: [''],
      costCenterId: [null, Validators.required],
      mode: [''],
    });
  }

  getProfile() {
    return this.fb.group({
      imagePath: [''],
      gender: [null, Validators.required],
      doj: ['', Validators.required],
      dob: ['', Validators.required],
      dol: [''],
      address: this.fb.group({
        address: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        zipcode: ['', Validators.required],
        phone: [''],
        addressType: ['']
      }),
      permanentAddress: this.fb.group({
        address: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        zipcode: ['', Validators.required],
        phone: [''],
        addressType: ['']
      }),
      pan: [''],
      uan: [''],
      pfan: [''],
      esin: [''],
      pran: [''],
      ban: [''],
      ifscCode: [''],
      bankName: [''],
      branchName: [''],
      fatherName: [''],
      spouseName: [''],
      holderName: [''],
      pState: ['']
    });
  }

  // getter methods for template
  // get email() { return this.staffForm.get('email'); }
  get firstName() { return this.staffForm.get('firstName'); }
  get lastName() { return this.staffForm.get('lastName'); }
  get dob() { return this.staffForm.controls['profile'].get('dob'); }
  get doj() { return this.staffForm.controls['profile'].get('doj'); }
  get costCenterId() { return this.staffForm.get('costCenterId'); }
  get maritalStatus() { return this.staffForm.get('maritalStatus'); }
  get gender() { return this.staffForm.controls['profile'].get('gender'); }
  get type() { return this.staffForm.get('type'); }
  get designation() { return this.staffForm.get('designation'); }
  get mobile() { return this.staffForm.get('mobile'); }
  get reportingManagerName() { return this.staffForm.get('reportingManagerName'); }
  // get expectedIn() { return this.staffForm.get('expectedIn'); }
  // get expectedOut() { return this.staffForm.get('expectedOut'); }
  // get expectedHours() { return this.staffForm.get('expectedHours'); }
  get employerId() { return this.staffForm.get('employerId'); }
  // get biometricId() { return this.staffForm.get('biometricId'); }
  get address() { return this.staffForm.get('profile').get('address').get('address'); }
  get city() { return this.staffForm.get('profile').get('address').get('city'); }
  get state() { return this.staffForm.get('profile').get('address').get('state'); }
  get zipcode() { return this.staffForm.get('profile').get('address').get('zipcode'); }
  get phone() { return this.staffForm.get('profile').get('address').get('phone'); }
  get address2() { return this.staffForm.get('profile').get('permanentAddress').get('address'); }
  get city2() { return this.staffForm.get('profile').get('permanentAddress').get('city'); }
  get state2() { return this.staffForm.get('profile').get('permanentAddress').get('state'); }
  get zipcode2() { return this.staffForm.get('profile').get('permanentAddress').get('zipcode'); }
  get phone2() { return this.staffForm.get('profile').get('permanentAddress').get('phone'); }

  saveStaff() {
    this.formSave = true;
    if (this.newStaff) {
      // for new staff add request
      console.log(this.staffForm.value);
      // delete this.staffForm.value['costCenter'];
      // delete this.staffForm.value['aadharNumber'];
      // delete this.staffForm.value['active'];
      // delete this.staffForm.value['approvalStatus'];
      // delete this.staffForm.value['attendanceEnabled'];
      // delete this.staffForm.value['biometricId'];
      // delete this.staffForm.value['eid'];
      // delete this.staffForm.value['employerName'];
      // delete this.staffForm.value['expectedHours'];
      // delete this.staffForm.value['expectedIn'];
      // delete this.staffForm.value['expectedOut'];
      // delete this.staffForm.value['reportingManagerName'];
      // delete this.staffForm.value['payrollEnabled'];
      // delete this.staffForm.value['name'];
      // delete this.staffForm.value['mode'];
      // delete this.staffForm.value['id'];

      this.adminService.addStaff(this.staffForm.value).subscribe(res => {
          this.formSave = false;
          this.staffForm.reset();
          this.addStaff.emit(res);
          this.alertService.successAlert('New Staff Added');
          this.hideViewPanel();
          if (this.picFile) {
            this.uploadProfilePic(res, this.picFile);
          }
      }, error => {
        this.formSave = false;
      });
    } else {
      this.staffForm.controls['mode'].patchValue('Edit');
      this.adminService.updateStaff(this.staffForm.value).subscribe(res => {
        this.formSave = false;
        this.alertService.successAlert('Staff Details Updated');
        this.id = null;
        this.staff = {};
        this.adminService.viewPanel.next(false);
      }, error => {
        this.formSave = false;
      });
    }
  }





  getPaySlipByEmployee(employeeId) {
    this.adminService.getPaySlipByEmoployee(employeeId).subscribe(response => {
      this.paySlips = response;
    });
  }




  searchManager(event) {
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.allReportingManagers = this.filterReportingManager.filter(employee => {
        return employee.name.toLowerCase().startsWith(val);
      });
    } else {
      this.allReportingManagers = this.filterReportingManager;
    }
  }

}
