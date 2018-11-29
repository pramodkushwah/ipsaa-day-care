import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-followup',
  templateUrl: './followUp.component.html',
  styleUrls: ['./followUp.component.css']
})
export class FollowUpComponent implements OnInit {

inquiryLogs: any = {};
  @Input() set inquiryInfo(inquiryInfo: any) {
this.inquiryLogs = (inquiryInfo) ? inquiryInfo : { };
  }
  constructor() { }


  ngOnInit() {
  }

}
