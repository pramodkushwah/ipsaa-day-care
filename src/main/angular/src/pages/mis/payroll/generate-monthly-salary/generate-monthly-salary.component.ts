import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import * as FileSaver from 'file-saver';
import * as _ from 'underscore';

declare let $: any;

@Component({
  selector: 'app-generate-monthly-salary',
  templateUrl: './generate-monthly-salary.component.html',
  styleUrls: ['./generate-monthly-salary.component.css']
})
export class GenerateMonthlySalaryComponent implements OnInit {
  employers: any;
  months: any[] = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];
  years: any[] = [new Date().getFullYear() - 1, new Date().getFullYear(), new Date().getFullYear() + 1];
  paySlipForm: FormGroup;
  viewPanel: boolean;
  salaryList: any[] = [];
  salaryListCopy: any[] = [];

  selectedSalary: any;
  salaryPayslipForm: FormGroup;
  saving: boolean;
  regenerating: boolean;
  genrateSalaryLoader: boolean;
  salaryListCopy1: any;
  showTable = false;
  ungeneratedSalary: any = [];
  loader: boolean;
  constructor(private adminService: AdminService,
    private alertService: AlertService,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.paySlipForm = this.fb.group({
      employer: [],
      year: [],
      month: []
    });
    this.getEmployers();
  }

  getSalaryPAySlipForm() {
    return this.fb.group({
      aadharNumber: [],
      advance: [],
      arrears: [],
      autoComment: [],
      basic: [],
      bonus: [],
      centerAccountName: [],
      centerAccountNumber: [],
      centerCode: [],
      centerName: [],
      comment: [],
      conveyance: [],
      ctc: [],
      disableDownloadBtn: [],
      disableUpdateBtn: [],
      doj: [],
      eid: [],
      empDesignation: [],
      empName: [],
      employerAddress: [],
      employerCode: [],
      employerName: [],
      employerPhone: [],
      entertainment: [],
      esi: [],
      esin: [],
      extraMonthlyAllowance: [],
      grossSalary: [],
      hra: [],
      id: [],
      islock: [],
      master: [],
      medical: [],
      mode: [],
      month: [],
      netSalary: [],
      otherAllowances: [],
      otherDeductions: [],
      pan: [],
      pfan: [],
      pfe: [],
      pfr: [],
      pran: [],
      presents: [],
      professionalTax: [],
      retention: [],
      serial: [],
      shoes: [],
      special: [],
      tds: [],
      tiffin: [],
      totalDays: [],
      totalDeduction: [],
      totalEarning: [],
      uan: [],
      uniform: [],
      washing: [],
      year: [],
    });
  }

  getEmployers() {
    this.adminService.getCostCenter().subscribe((response: any) => {
      this.employers = response;
      this.employers.unshift({ id: 'ALL', code: 'ALL', name: 'ALL' });
    });
  }

  getPayslips() {
    this.genrateSalaryLoader = true;
    this.adminService.getPaySlips(this.paySlipForm.value).subscribe((response: any) => {
      this.salaryList = response;
      this.salaryListCopy = response;
      this.salaryListCopy1 = response;
      this.genrateSalaryLoader = false;
      this.showTable = true;
    }, (err) => {
      this.genrateSalaryLoader = false;

    });
  }

  showSidePanel(salary: any) {
    this.selectedSalary = salary;
    this.viewPanel = true;
    this.salaryPayslipForm = this.getSalaryPAySlipForm();
    this.salaryPayslipForm.patchValue(salary);
  }

  hideViewPanel() {
    this.viewPanel = false;
  }

  updatePaySlip() {
    this.saving = true;
    this.adminService.updatePaySlip(this.salaryPayslipForm.value).subscribe((response: any) => {
      this.saving = false;
      _.extend(this.selectedSalary, response);
      this.alertService.successAlert('Payslip updated successfully.');
      this.salaryPayslipForm.reset();
      this.hideViewPanel();
    });
  }

  downloadPaySlip(salary: any) {
    this.adminService.downloadPaySlip(salary).subscribe((response: any) => {
      const blob = new Blob([response.body], {
        // type: 'application/octet-stream'
      });
      FileSaver.saveAs(blob, response.headers.get('fileName'));

    });
  }

  lockControls() {
    this.adminService.lockPayslip(this.selectedSalary).subscribe((response: any) => {
      this.selectedSalary.islock = true;
      _.extend(this.selectedSalary, response);

      this.alertService.successAlert('Salary Slip locked');
    });
  }

  regeneratePaySlip() {
    this.regenerating = true;
    this.adminService.regeneratePaySlip(this.selectedSalary).subscribe((response: any) => {
      this.regenerating = false;
      _.extend(this.selectedSalary, response);

      this.alertService.successAlert('Payslip regenerated successfully.');
    });
  }

  getExcelFile(event) {
    const formData = new FormData();
    if (event.srcElement.files[0]) {
      formData.append('file', event.srcElement.files[0]);
      formData.append('employerId', this.paySlipForm.controls['employer'].value);
      formData.append('year', this.paySlipForm.controls['year'].value);
      formData.append('month', this.paySlipForm.controls['month'].value);
      this.loader = true;
      this.adminService.uploadEmployeeSalaryFile(formData).subscribe(response => {
        this.ungeneratedSalary = response;
        this.loader = false;
        $('#errorPageResponse').modal('show');
        this.getEmployers();
      }, (err) => {

      });
    }
  }


  searchEmployee(event: any) {
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.salaryList = this.salaryListCopy.filter(employee => {
        return employee.empName.toLowerCase().startsWith(val);
      });
    } else {
      this.salaryList = this.salaryListCopy1;
    }
  }


}
