import { Component, OnInit, Input } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import {
  FormBuilder,
  FormGroup
} from '../../../../../node_modules/@angular/forms';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';
import { PayrollService } from '../../../../providers/payroll/payroll.service';
@Component({
  selector: 'app-salary-info',
  templateUrl: './salary-info.component.html',
  styleUrls: ['./salary-info.component.css']
})
export class SalaryInfoComponent implements OnInit {
  selectedSalary: any;
  editable: boolean;
  employees: any;
  employee: any = {};
  salaryForm: FormGroup;
  saving: boolean;

  @Input()
  set salary(salary: any) {
    this.selectedSalary = salary;
    this.salaryForm = this.getSalaryForm();
    salary
      ? this.salaryForm.patchValue(salary)
      : this.salaryForm.patchValue({});
  }

  @Input()
  set update(update: boolean) {
    this.editable = update;
  }

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private alertService: AlertService,
    private payrollService: PayrollService
  ) {}

  ngOnInit() {
    this.getEmployee();
  }

  getEmployee() {
    this.adminService.getNewEmployees().subscribe((response: any) => {
      this.employees = response.stafflist;
    });
  }

  saveSalary() {
    if (this.editable) {
      this.alertService
      .confirm('You want to update Salary')
      .then((isConfirm: any) => {
        if (isConfirm) {
          this.saving = true;
          this.adminService
            .updateSalary(this.salaryForm.value)
            .subscribe((response: any) => {
              this.saving = false;
              _.extend(this.selectedSalary, response);
              this.alertService.successAlert('Salary updated Successfully');
              this.adminService.viewPanel.next(false);
            });
        }
      });
    } else {
      this.saving = true;
      this.adminService.updateSalary(this.salaryForm.value).subscribe((response: any) => {
        this.saving = false;
        this.alertService.successAlert('New Employee salary added');
        this.adminService.viewPanel.next(false);
      });
    }
  }

  onChange() {
    this.salaryForm.controls.basic.patchValue(
      this.calculateBasic(this.salaryForm.value)
    );
    this.salaryForm.controls.hra.patchValue(
      this.calculateHRA(this.salaryForm.value)
    );
    this.salaryForm.controls.special.patchValue(
      this.calculateSpecial(this.salaryForm.value)
    );
    this.salaryForm.controls.pfr.patchValue(
      this.calculatePFR(this.salaryForm.value)
    );
    this.salaryForm.controls.pfe.patchValue(
      this.calculatePFE(this.salaryForm.value)
    );
    this.salaryForm.controls.grossSalary.patchValue(
      this.calculateGross(this.salaryForm.value)
    );
    this.salaryForm.controls.esi.patchValue(
      this.calculateESI(this.salaryForm.value)
    );

    let totalDeduction: any = 0;
    let totalEarning: any = 0;

    if (this.salaryForm.controls.ctc.value) {
      this.salaryForm.controls.conveyance.patchValue(1600);
      this.salaryForm.controls.bonus.patchValue(584);
    }

    if (this.salaryForm.controls.pfd.value) {
      totalDeduction = totalDeduction + this.salaryForm.controls.pfe.value;
      totalDeduction = totalDeduction + this.salaryForm.controls.pfr.value;
    }

    if (this.salaryForm.controls.esid.value) {
      totalDeduction = totalDeduction + this.salaryForm.controls.esi.value;
    }

    if (this.salaryForm.controls.profd.value) {
      this.salaryForm.controls.professionalTax.patchValue(200);
      totalDeduction =
        totalDeduction + this.salaryForm.controls.professionalTax.value;
    } else {
      this.salaryForm.controls.professionalTax.patchValue(0);
    }
    totalEarning =
      this.salaryForm.controls.ctc.value +
      this.salaryForm.controls.extraMonthlyAllowance.value;
    totalEarning = Math.round(totalEarning);
    totalDeduction = Math.round(totalDeduction);
    this.salaryForm.controls.netSalary.patchValue(
      totalEarning - totalDeduction
    );
  }

  calculateBasic(salary) {
    const ctc = salary.ctc ? salary.ctc : 0;
    return (ctc * 40) / 100;
  }

  calculateHRA(salary) {
    const basic = salary.basic ? salary.basic : 0;
    return (basic * 40) / 100;
  }
  // conveyance fixed 1600
  // bonus fixed 584
  calculateSpecial(salary) {
    return (
      salary.ctc - salary.basic - salary.hra - salary.conveyance - salary.bonus
    );
  }

  calculatePFR(salary) {
    if (salary.pfd) {
      if (salary.basic <= 15000) {
        return Math.floor(salary.basic * 0.12);
      } else {
        return 1800;
      }
    } else {
      return 0;
    }
  }

  calculatePFE(salary) {
    if (salary.pfd) {
      if (salary.basic <= 15000) {
        return Math.floor(salary.basic * 0.12);
      } else {
        return 1800;
      }
    } else {
      return 0;
    }
  }

  calculateGross(salary) {
    return salary.ctc - salary.bonus - salary.pfr;
  }

  calculateESI(salary) {
    if (!salary.esid || salary.grossSalary > 21000) {
      return 0;
    }
    return Math.floor(salary.grossSalary * .0175);
  }

  hideViewPanel() {
    // this.viewPanel = false;
    this.adminService.viewPanel.next(false);
  }

  getSalaryForm() {
    return this.fb.group({
      advance: [0],
      arrears: [],
      basic: [],
      bonus: [],
      conveyance: [],
      ctc: [],
      employer: [],
      eid: [],
      entertainment: [],
      esi: [],
      esid: [],
      extraMonthlyAllowance: [],
      hra: [],
      medical: [],
      pfd: [],
      pfe: [],
      pfr: [],
      profd: [],
      professionalTax: [],
      retention: [0],
      shoes: [],
      special: [],
      tds: [0],
      tiffin: [],
      uniform: [],
      washing: [],
      grossSalary: [] /** extra field */,
      netSalary: [] /* extra field*/
    });
  }
}
