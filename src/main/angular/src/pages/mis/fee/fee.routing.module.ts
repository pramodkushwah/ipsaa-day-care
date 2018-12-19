import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StudentFeeComponent } from './student-fee/student-fee.component';
import { CenterFeeComponent } from './center-fee/center-fee.component';
import { GenerateFeeSlipComponent } from './generate-fee-slip/generate-fee-slip.component';
import { GenerateFeeReceiptComponent } from './generate-fee-receipt/generate-fee-receipt.component';
import { IpsaaclubComponent } from './ipsaaclub/ipsaaclub.component';
const feeRoutes: Routes = [
  {
    path: 'centerfeemanagement',
    component: CenterFeeComponent
  },
  {
    path: 'ipsaaclub',
    component: IpsaaclubComponent
  },
  {
    path: 'studentfeemanagement',
    component: StudentFeeComponent
  },
  {
    path: 'feeslip',
    component: GenerateFeeSlipComponent
  },
  {
    path: 'feepayment',
    component: GenerateFeeReceiptComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(feeRoutes)],
  exports: [RouterModule],
  declarations: []
})
export class FeeRoutingModule {}
