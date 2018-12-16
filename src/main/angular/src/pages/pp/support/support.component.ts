import { Component, OnInit } from '@angular/core';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { AlertService } from '../../../providers/alert/alert.service';
declare const $: any;
@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})
export class SupportComponent implements OnInit {
  query: any;
  queries: any = [];
  parent: any;
  studentId: any;
  soQuiry: boolean;
  replyText: any;
  QueryTittle: string;
  newQueryMsg: string;
  // studentId: number;
  constructor(
    private parentService: ParentService,
    private alertService: AlertService,
  ) { }

  ngOnInit() {
    this.getStudents();
  }
  getStudents() {
    this.parentService.getStudentDetails()
      .subscribe((res: any) => {
        console.log(res);
        this.parent = res;
        this.studentId = this.parent[0].id;
        this.getQuerie();
      });
  }
  newQuery() {
    console.log( this.QueryTittle  + '    '   +  this.newQueryMsg + '    '  + this.studentId );

    this.parentService.newInQuery({
      title : this.QueryTittle,
      description : this.newQueryMsg ,
      studentId: this.studentId,
    })
      .subscribe((res: any) => {
     this.QueryTittle = '';
     this.newQueryMsg = '';
     this.studentId = '';
this.queries.push(res);
this.closeNewQueryModel();
this.alertService.successAlert('Query Send Successfuly');

      });
  }

  closeReplyModel() {
    $('#queryReply').modal('hide');
  }

  closeNewQueryModel() {
    $('#newQuery').modal('hide');
  }

  getQuerie() {
    this.parentService.getQueries()
      .subscribe((res: any) => {
        console.log(res);
        this.queries = res;
        // this.studentId = this.parent[0].id;
      });
  }


  getQueryDetails(id) {
    this.parentService.getQuery(id)
      .subscribe((res: any) => {
        this.soQuiry = true;
        this.query = res;
      });
  }
  replyTo_Query() {
// console.log(this.query.id + ' ' +  this.replyText);

this.parentService.replyToQuery({
  description: this.replyText,
id: this.query.id
})
.subscribe((res: any) => {
this.query = res;
this.closeReplyModel();
this.alertService.successAlert('Reply Successfuly');
});
  }




  hideQuery() {
    this.soQuiry = false;
  }

}
