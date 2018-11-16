import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminRoutingModule } from './admin.routing';
import { StudentComponent } from './student/student.component';
import { AdminService } from '../../../providers/admin/admin.service';
import { StudentInfoComponent } from './student-info/info.component';
import { ComponentsModule } from '../../components/components.module';
import { AppStaffComponent } from './staff/staff.component';
import { StaffInfoComponent } from './staff-info/staffInfo.component';
import { CenterComponent } from './center/center.component';
import { CenterInfoComponent } from './center-info/info.component';
import { ProgramComponent } from './program/program.component';
import { RoleComponent } from './role/role.component';
import { UserComponent } from './user/user.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { ImportDataComponent } from './import-data/import-data.component';
import { PayrollModule } from '../payroll/payroll.module';

@NgModule({
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ComponentsModule,
    PayrollModule
  ],
  declarations: [
    StudentComponent,
    StudentInfoComponent,
    AppStaffComponent,
    StaffInfoComponent,
    CenterComponent,
    CenterInfoComponent,
    ProgramComponent,
    RoleComponent,
    UserComponent,
    UserInfoComponent,
    ImportDataComponent
  ],
  exports: [StudentInfoComponent, StaffInfoComponent, CenterInfoComponent],
  providers: [AdminService]
})
export class AdminModule { }
