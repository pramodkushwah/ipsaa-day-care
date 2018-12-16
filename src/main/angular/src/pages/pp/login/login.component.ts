import { Component, OnInit, OnDestroy  } from '@angular/core';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { ActivatedRoute } from '@angular/router';
// import { Route } from '@angular/router';

declare let $: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy  {
  checkoutDetails: any;
  myDetailId: number;
  sub: any;
feeledgeId: number;

  constructor(
    private parentService: ParentService,
    private route: ActivatedRoute  ) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.feeledgeId = +params['p1']; // (+) converts string 'id' to a number
this.myDetailId = +params['p2'];
      // In a real app: dispatch action to load the details here.
   });

    this.getFullBillingDetails();
  }

//   getMyParentDeatil() {
//     this.parentService.getMyDetails()
//       .subscribe((res: any) => {
// this.myDetailId = res.id;
// this.getFullBillingDetails();
//       });
//   }

  getFullBillingDetails() {
    this.parentService.hdfcCheckout(this.feeledgeId, this.myDetailId)
      .subscribe((res: any) => {
        this.checkoutDetails = res;
      });
  }

  checkout() {
    $('#checkout-form').attr('action', this.checkoutDetails.transactionUrl).submit();
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
