import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MISComponent } from './mis.component';
import { DashboardComponent } from './dashboard/dashboard.component';

export const MISRoutes: Routes = [
  {
    path: '',
    component: MISComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'admin',
        loadChildren: 'pages/mis/admin/admin.module#AdminModule'
      },
      {
        path: 'payroll',
        loadChildren: 'pages/mis/payroll/payroll.module#PayrollModule'
      },
      {
        path: 'attendance',
        loadChildren: 'pages/mis/attendance/attendance.module#AttendanceModule'
      },
      {
        path: 'fee',
        loadChildren: 'pages/mis/fee/fee.module#FeeModule'
      },
      {
        path: 'comms',
        loadChildren: 'pages/mis/message/message.module#MessageModule'
      },
      {
        path: 'approval',
        loadChildren: 'pages/mis/approval/approval.module#ApprovalModule'
      },
      {
        path: 'report',
        loadChildren: 'pages/mis/report/report.module#ReportModule'
      },
      {
        path: 'crm',
        loadChildren: 'pages/mis/crm/crm.module#CrmModule'
      },
      {
        path: 'center',
        loadChildren: 'pages/mis/center/center.module#CenterModule'
      },
      {
        path: 'support',
        loadChildren: 'pages/mis/support/support.module#SupportModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(MISRoutes)],
  exports: [RouterModule]
})
export class MISRoutingModule {
  constructor() {}
}
