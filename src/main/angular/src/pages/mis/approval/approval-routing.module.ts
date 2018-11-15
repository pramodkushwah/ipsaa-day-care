import { NgModule } from '@angular/core';
import { StudentApprovalsComponent } from './student-approvals/student-approvals.component';
import { StaffApprovalsComponent } from './staff-approvals/staff-approvals.component';
import { Routes, RouterModule } from '@angular/router';

const approvalRoutes: Routes = [
  {
    path: 'student-approval',
    component: StudentApprovalsComponent
  },
  {
    path: 'staff-approval',
    component: StaffApprovalsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(approvalRoutes)],
  exports: [RouterModule]
})
export class ApprovalRoutingModule { }
