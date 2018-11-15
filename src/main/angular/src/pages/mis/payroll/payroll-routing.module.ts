import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SalaryComponent } from './salary/salary.component';
import { GenerateMonthlySalaryComponent } from './generate-monthly-salary/generate-monthly-salary.component';
import { StaffLeavesComponent } from './staff-leaves/staff-leaves.component';

export const payrollRoutes: Routes = [
  {
    path: 'salary',
    component: SalaryComponent
  },
  {
    path: 'generate-monthly-salary',
    component: GenerateMonthlySalaryComponent
  },
  {
    path: 'staffleaves',
    component: StaffLeavesComponent
  }];

@NgModule({
  imports: [RouterModule.forChild(payrollRoutes)],
  exports: [RouterModule]
})
export class PayrollRoutingModule { }
