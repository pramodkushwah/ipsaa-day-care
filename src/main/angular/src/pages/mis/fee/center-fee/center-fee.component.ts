import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { FormBuilder, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';

@Component({
  selector: 'app-center-fee',
  templateUrl: './center-fee.component.html',
  styleUrls: ['./center-fee.component.css']
})
export class CenterFeeComponent implements OnInit {

  viewPanel = false;
  selectedCenter: any;
  tableShow: boolean;
  editable: boolean;
  saving: boolean;
  loader: boolean;
  programFeeForm: FormGroup;
  additionalChargeForm: FormGroup;
  chargeListForm: FormGroup;
  selectedDetails: any = {};
  selectedTab = 'Program Fee';
  selectedCenterDetails: Array<any>;
  centers: Array<any>;
  charges: Array<any>;
  filterCharge = [];
  additionCharge: Array<any>;
  programList: Array<any>;
  FilterProgramList: Array<any>;
  heading = ['Program', 'Fee', ' Annual Charges', 'Security Deposit', 'Admission Fee', 'Actions'];
  constructor(private adminService: AdminService, private fb: FormBuilder, private alertService: AlertService) { }

  ngOnInit() {
    this.getCenter();
    this.getProgramList();
    this.getChargeList();
  }



  changeTab(value) {
    this.selectedTab = value;
    this.viewPanel = false;
  }



  getCenter() {
    this.adminService.getProgramCenter()
      .subscribe((res) => {
        console.log(res);
        this.centers = res;

      });
  }


  getProgramList() {
    this.adminService.getProgramsList()
      .subscribe((res) => {
        console.log(res);
        this.programList = res;
        console.log(res);

      });
  }


  getChargeList() {
    this.adminService.getChargesList()
      .subscribe((res: any) => {
        this.charges = res;
        this.filterCharge = this.charges.slice(0);
      });
  }


  showSidePanel(value: boolean, object: any) {
    this.viewPanel = value;
    if (!(object)) {
      this.filterProgramList();



      this.editable = false;
    } else {
      this.FilterProgramList = this.programList.slice(0);

      this.editable = true;
    }


    if (this.selectedTab === 'Program Fee') {
      this.selectedDetails = (object) ? object : {};
      this.programFeeForm = this.getProgramForm();
      this.programFeeForm.patchValue(this.selectedDetails);

      console.log();

    }

    if (this.selectedTab === 'Charge') {
      this.selectedDetails = (object) ? object : {};
      this.chargeListForm = this.getChargeListForm();
      this.chargeListForm.patchValue(this.selectedDetails);
      console.log(this.selectedDetails);

    }



    if (this.selectedTab === 'Additional Charges') {
      this.selectedDetails = (object) ? object : {};
      this.additionalChargeForm = this.getAdditionalChargesForm();
      this.additionalChargeForm.patchValue(this.selectedDetails);
      console.log(this.selectedDetails);
      this.filterCharges(object);


    }


  }



  filterCharges(object) {

    if (object) {

      this.filterCharge = this.charges.filter(charge => charge.id === object.charge.id);
    } else {

      this.filterCharge = this.charges.filter(charge => charge.id !== this.additionCharge);
    }

  }

  filterProgramList() {

    this.FilterProgramList = this.programList.slice(0);

    for (let i = 0; i < this.selectedCenterDetails.length; i++) {

      for (let j = 0; j < this.programList.length; j++) {

        if (this.selectedCenterDetails[i].program.id === this.programList[j].id) {
          this.FilterProgramList.splice(j, 1);
        }
      }
    }




  }

  filterFeeByCenter() {


    if (this.selectedCenter !== 'All') {

      this.loader = true;
      this.adminService.getCenterFee(this.selectedCenter.id)
        .subscribe((res) => {
          this.tableShow = true;
          this.loader = false;
          console.log(res);

          this.selectedCenterDetails = res;
        }, (err) => {
          this.loader = false;

          this.tableShow = false;
        });




      this.adminService.getcharge(this.selectedCenter.id)
        .subscribe((res) => {

          this.additionCharge = res;


        });

    }

    if (this.selectedCenter === 'All') {
      this.tableShow = false;
    }



  }


  getProgramForm() {
    return this.fb.group({
      fee: ['', Validators.required],
      annualFee: ['', Validators.required],
      deposit: ['', Validators.required],
      admissionFee: ['', Validators.required],
      id: [null],
      program: this.fb.group({
        id: [null, Validators.required],
      }),
      center: this.fb.group({
        id: [null],
      })
    });

  }

  get fee() { return this.programFeeForm.get('fee'); }
  get annualFee() { return this.programFeeForm.get('annualFee'); }
  // get programId() { return this.programFeeForm.controls['program'].get('id'); }
  get deposit() { return this.programFeeForm.get('deposit'); }
  get admissionFee() { return this.programFeeForm.get('admissionFee'); }




  getChargeListForm() {
    return this.fb.group({

      name: ['', Validators.required],

    });
  }





  getAdditionalChargesForm() {
    return this.fb.group({
      amount: ['', Validators.required],
      id: [''],
      charge: this.fb.group({
        id: [null, Validators.required],
        name: ['']
      })
    });
  }

  get amount() { return this.additionalChargeForm.get('amount'); }
  get id() { return this.additionalChargeForm.controls['charge'].get('id'); }




  saveProgramFee() {

    this.saving = true;
    if (this.selectedTab === 'Program Fee') {

      // const fd = {
      //   'annualFee': parseInt(this.programFeeForm.value.annualFee),
      //   'admissionFee': parseInt(this.programFeeForm.value.admissionFee),
      //   'centerId': this.selectedCenter.id,
      //   'deposit': parseInt(this.programFeeForm.value.deposit),
      //   'fee': parseInt(this.programFeeForm.value.fee),
      //   'program': this.programFeeForm.value.program.id,
      //   'programId': this.programFeeForm.value.program.id,
      // }

      const fd = {
        'annualFee': this.programFeeForm.value.annualFee,
        'admissionFee': this.programFeeForm.value.admissionFee,
        'centerId': this.selectedCenter.id,
        'deposit': this.programFeeForm.value.deposit,
        'fee': this.programFeeForm.value.fee,
        'program': this.programFeeForm.value.program.id,
        'programId': this.programFeeForm.value.program.id,
      };








      if (this.editable) {

        fd['mode'] = 'Edit';
        fd['center'] = this.selectedCenter.id;
        fd['id'] = this.programFeeForm.value.id;
        this.adminService.editProgramFee(fd)
          .subscribe((res: any) => {
            this.saving = false;

            this.selectedCenterDetails = this.selectedCenterDetails.filter(center  => center.id !== this.programFeeForm.value.id);
            res['id'] = this.programFeeForm.value.id;
            this.selectedCenterDetails.push(res);
            this.alertService.successAlert('Program Fee Updated');
            this.viewPanel = false;
            this.programFeeForm.reset();
          }, (err) => {

            this.saving = false;

          });


      } else {

        fd['mode'] = 'New';

        this.adminService.addProgramFee(fd)
          .subscribe((res: any) => {
            this.saving = false;

            this.selectedCenterDetails.push(res);
            this.alertService.successAlert('Program Fee Updated');
            this.viewPanel = false;
            this.programFeeForm.reset();
          }, (err) => {

            this.saving = false;

          });


      }


    }




    if (this.selectedTab === 'Additional Charges') {

      if (this.editable) {

        this.adminService.editCharge(
          {
            'amount': this.additionalChargeForm.value.amount,
            'charge': this.additionalChargeForm.value.charge.id,
            'id': this.additionalChargeForm.value.id,
            'chargeId': this.additionalChargeForm.value.charge.id,
            'mode': 'Edit', 'center': this.selectedCenter.id,
            'centerId': this.selectedCenter.id,
          }
        )
          .subscribe((res: any) => {
            this.additionCharge = this.additionCharge.filter(adCharge => adCharge.id !== res.id);
            this.additionCharge.push(res);
            this.saving = false;

            this.additionalChargeForm.reset();
            this.viewPanel = false;

          }, (err) => {
            this.saving = false;
          });
      } else {





        this.adminService.addNewCharge(
          {
            'amount': this.additionalChargeForm.value.amount,
            'charge': this.additionalChargeForm.value.charge.id,
            'chargeId': this.additionalChargeForm.value.charge.id,
            'mode': 'New', 'center': this.selectedCenter.id,
            'centerId': this.selectedCenter.id,
          })
          .subscribe((res: any) => {
            this.saving = false;

            this.additionCharge.push(res);
            this.additionalChargeForm.reset();
            this.viewPanel = false;
            this.alertService.successAlert('successfuly add');

          }, (err) => {
            this.saving = false;
          });
      }



    }


    if (this.selectedTab === 'Charge') {

      if (this.editable) {

        this.adminService.editChargeList({ 'mode': 'Edit', 'name': this.chargeListForm.value.name, 'id': this.selectedDetails.id })
          .subscribe((res: any) => {
            this.saving = false;

            this.charges = this.charges.filter(charge => charge.id !== this.selectedDetails.id);
            this.charges.push(res);
            this.viewPanel = false;
            this.alertService.successAlert('Edit successfuly');
            this.chargeListForm.reset();

          }, (err) => {
            this.saving = false;

          });

      } else {


        this.adminService.addNewChargeInList({ 'mode': 'New', 'name': this.chargeListForm.value.name, })
          .subscribe((res: any) => {
            this.saving = false;

            this.charges.push(res);
            this.viewPanel = false;
            this.alertService.successAlert('Edit successfuly');

            this.chargeListForm.reset();
          });

      }


    }


  }





  delete(id) {

    if (this.selectedTab === 'Program Fee') {

      this.adminService.deleteProgramFee(id)
        .subscribe((res: any) => {
          this.selectedCenterDetails = this.selectedCenterDetails.filter(center => center.id !== id);
          this.alertService.successAlert('delete successfuly');
        });
    }


    if (this.selectedTab === 'Additional Charges') {
      this.adminService.deleteAdditionalCharge(id)
        .subscribe((res: any) => {
          this.additionCharge = this.additionCharge.filter(addCharge => addCharge.id !== id);
          this.alertService.successAlert('delete successfuly');
        });
    }
  }


  cancel(form) {
    this.viewPanel = false;
    form.reset();
  }





}
