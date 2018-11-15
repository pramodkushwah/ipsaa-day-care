import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import * as FileSaver from 'file-saver';
import * as _ from 'underscore';

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
  years: any[] = [ new Date().getFullYear() - 1, new Date().getFullYear() ];
  paySlipForm: FormGroup;
  viewPanel: boolean;
  salaryList: any[] = [];
  selectedSalary: any;
  salaryPayslipForm: FormGroup;
  saving: boolean;
  regenerating: boolean;
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
    });
  }

  getPayslips() {
    this.adminService.getPaySlips(this.paySlipForm.value).subscribe((response: any) => {
      this.salaryList = response;
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
    this.adminService.updatePaySlip(this.selectedSalary).subscribe((response: any) => {
      this.saving = false;
      _.extend(this.selectedSalary, response);
      this.alertService.successAlert('Payslip updated successfully.');
      this.salaryPayslipForm.reset();
      this.hideViewPanel();
    });
  }

  downloadPaySlip(salary: any) {
    this.adminService.downloadPaySlip(salary).subscribe((response: any) => {
      const blob = new Blob([response.data], {
        type: 'application/octet-stream'
    });
    FileSaver.saveAs(blob, response.headers('fileName'));
    });
  }

  lockControls() {
    this.adminService.lockPayslip(this.selectedSalary).subscribe((response: any) => {
      this.selectedSalary.islock = true;
      this.alertService.successAlert('Salary Slip locked');
    });
  }

  regeneratePaySlip() {
    this.regenerating = true;
    this.adminService.regeneratePaySlip(this.selectedSalary).subscribe((response: any) => {
      this.regenerating = false;
      this.alertService.successAlert('Payslip regenerated successfully.');
    });
  }

}
