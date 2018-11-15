import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { StudentMessageComponent } from './student-message/student-message.component';
import { StaffMessageComponent } from './staff-message/staff-message.component';

export const messageRoutes: Routes = [
  {path: 'studentmessage', component: StudentMessageComponent},
  {path: 'staffmessage', component: StaffMessageComponent}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(messageRoutes)
  ],
  exports: [RouterModule],
  declarations: []
})
export class MessageRoutingModule { }
