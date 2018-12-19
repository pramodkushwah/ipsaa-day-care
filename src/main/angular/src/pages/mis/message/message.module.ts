import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentMessageComponent } from './student-message/student-message.component';
import { StaffMessageComponent } from './staff-message/staff-message.component';
import { MessageRoutingModule } from './/message-routing.module';
import { FormsModule, ReactiveFormsModule } from '../../../../node_modules/@angular/forms';
import { EmailMessageDirectiveComponent } from './email-message-directive/email-message-directive.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MessageRoutingModule
  ],
  declarations: [StudentMessageComponent, StaffMessageComponent, EmailMessageDirectiveComponent]
})
export class MessageModule { }
