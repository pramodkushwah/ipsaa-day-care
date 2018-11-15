import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrmRoutingModule } from './/crm-routing.module';
import { ReportComponent } from './report/report.component';
import { InquiryComponent } from './inquiry/inquiry.component';
import { FormsModule } from '@angular/forms';
import { ReportModule } from '../report/report.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReportModule,
    CrmRoutingModule
  ],
  declarations: [ReportComponent, InquiryComponent]
})
export class CrmModule { }
