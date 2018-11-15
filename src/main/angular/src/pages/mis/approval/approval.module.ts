import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StaffApprovalsComponent } from './staff-approvals/staff-approvals.component';
import { StudentApprovalsComponent } from './student-approvals/student-approvals.component';
import { ApprovalRoutingModule } from './approval-routing.module';
import { AdminModule } from '../admin/admin.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    AdminModule,
    FormsModule,
    ApprovalRoutingModule
  ],
  declarations: [StaffApprovalsComponent, StudentApprovalsComponent]
})
export class ApprovalModule { }
