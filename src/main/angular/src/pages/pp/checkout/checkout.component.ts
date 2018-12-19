import { Component, OnInit, OnDestroy } from '@angular/core';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
declare const $: any;

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {

  checkoutDetails: any;
  myDetailId: number;
  sub: any;
  feeledgeId: number;
  chec: any;

  constructor(
    private parentService: ParentService,
    private route: ActivatedRoute, public http: HttpClient) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.feeledgeId = params['p1'];
      this.myDetailId = params['p2'];
    });

    this.route.url.subscribe( UrlSegment => {
      this.chec = UrlSegment[0].path;
    });

    if (this.chec === 'ipsaaclubcheckoutdetails') {
      this.parentService.ipsaaClubhdfcCheckout(this.feeledgeId, this.myDetailId)
        .subscribe((res: any) => {
          this.checkoutDetails = res;
        });
    } else {
      this.getFullBillingDetails();

    }
    console.log(this.chec);
  }
  getFullBillingDetails() {
    this.parentService.hdfcCheckout(this.feeledgeId, this.myDetailId)
      .subscribe((res: any) => {
        this.checkoutDetails = res;
      });
  }

  checkout() {
    $('#checkout-form').submit();
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}

