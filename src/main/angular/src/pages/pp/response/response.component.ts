import { Component, OnInit } from '@angular/core';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { Route, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-response',
  templateUrl: './response.component.html',
  styleUrls: ['./response.component.css']
})
export class ResponseComponent implements OnInit {
  responseDetails: any = {};
  param: number;
  constructor(private parentservice: ParentService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(parms => {
      this.param = parms['paymentId'];
      this.parentservice.getSuccessDetail(this.param)
      .subscribe((res: any) => {
        this.responseDetails = res;
      });
    });


  }
}
