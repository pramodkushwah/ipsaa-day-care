import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PayrollRoutingModule } from './payroll-routing.module';
import { SalaryComponent } from './salary/salary.component';
import { SalaryInfoComponent } from './salary-info/salary-info.component';
import { PayrollService } from '../../../providers/payroll/payroll.service';
import { GenerateMonthlySalaryComponent } from './generate-monthly-salary/generate-monthly-salary.component';
import { StaffLeavesComponent } from './staff-leaves/staff-leaves.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PayrollRoutingModule
  ],
  exports: [SalaryInfoComponent],
  declarations: [SalaryComponent, SalaryInfoComponent, GenerateMonthlySalaryComponent, StaffLeavesComponent],
  providers: [PayrollService]
})
export class PayrollModule { }
