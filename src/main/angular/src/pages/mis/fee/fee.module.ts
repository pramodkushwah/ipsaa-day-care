import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentFeeComponent } from './student-fee/student-fee.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FeeRoutingModule } from './fee.routing.module';
import { MatSelectModule, MatInputModule, MatSpinner } from '@angular/material';
import { CenterFeeComponent } from './center-fee/center-fee.component';
import { GenerateFeeSlipComponent } from './generate-fee-slip/generate-fee-slip.component';
import { GenerateFeeReceiptComponent } from './generate-fee-receipt/generate-fee-receipt.component';
import { StudentFeeInfoComponent } from './student-fee-info/student-fee-info.component';
import { FeeReceiptInfoComponent } from './fee-receipt-info/fee-receipt-info.component';
import { IpsaaclubComponent } from './ipsaaclub/ipsaaclub.component';
import { MessageModule } from '../message/message.module';
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FeeRoutingModule,
    MatSelectModule,
    MatInputModule,
    MessageModule
  ],
  declarations: [StudentFeeComponent,
    CenterFeeComponent, GenerateFeeSlipComponent,
    GenerateFeeReceiptComponent, StudentFeeInfoComponent,
    FeeReceiptInfoComponent,
    IpsaaclubComponent],
  exports: [FeeReceiptInfoComponent]

})
export class FeeModule { }
