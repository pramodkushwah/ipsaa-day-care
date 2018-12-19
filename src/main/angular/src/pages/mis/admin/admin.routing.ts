import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudentComponent } from './student/student.component';
import { AppStaffComponent } from './staff/staff.component';
import { CenterComponent } from './center/center.component';
import { ProgramComponent } from './program/program.component';
import { RoleComponent } from './role/role.component';
import { UserComponent } from './user/user.component';
import { ImportDataComponent } from './import-data/import-data.component';

export const adminRoutes: Routes = [
  {
    path: 'center',
    component: CenterComponent
  },
  {
    path: 'program',
    component: ProgramComponent
  },
  {
    path: 'student',
    component: StudentComponent
  },
  {
    path: 'staff',
    component: AppStaffComponent
  },
  {
    path: 'user',
    component: UserComponent
  },
  {
    path: 'role',
    component: RoleComponent
  },
  {
    path: 'importdata',
    component: ImportDataComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(adminRoutes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
  constructor() {}
}
