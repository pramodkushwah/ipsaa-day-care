import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { StudentAttendanceComponent } from './student-attendance/student-attendance.component';
import { StaffAttendanceComponent } from './staff-attendance/staff-attendance.component';
import { StaffAttendanceLogsComponent } from './staff-attendance-logs/staff-attendance-logs.component';

const attendanceRoutes: Routes = [
  {
    path: 'student-attendance',
    component: StudentAttendanceComponent
  },
  {
    path: 'staff-attendance',
    component: StaffAttendanceComponent
  },
  {
    path: 'staff-attendance-log',
    component: StaffAttendanceLogsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(attendanceRoutes)],
  exports: [RouterModule]
})
export class AttendanceRoutingModule { }
