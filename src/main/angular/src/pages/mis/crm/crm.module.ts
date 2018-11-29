import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrmRoutingModule } from './/crm-routing.module';
import { ReportComponent } from './report/report.component';
import { InquiryComponent } from './inquiry/inquiry.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReportModule } from '../report/report.module';
import { InquiryDetailsComponent } from './inquiryDetails/inquiryDetails.component';
import { FollowUpComponent } from './followUp/followUp.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    ReportModule,
    CrmRoutingModule
  ],
  declarations: [ReportComponent,
    InquiryDetailsComponent,
    FollowUpComponent,
     InquiryComponent]
})
export class CrmModule { }
