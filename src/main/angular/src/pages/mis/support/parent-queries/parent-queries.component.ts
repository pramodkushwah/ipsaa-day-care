import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../../providers/alert/alert.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import * as _ from 'underscore';
declare let $: any;

@Component({
  selector: 'app-parent-queries',
  templateUrl: './parent-queries.component.html',
  styleUrls: ['./parent-queries.component.css']
})
export class ParentQueriesComponent implements OnInit {

  showSupport = false;
  queries: Array<any>;
  showClosed = false;
  selectedQuery: any;
  replyText: string;
  constructor(
    private alertService: AlertService,
    private adminService: AdminService,
  ) { }

  ngOnInit() {
    this.parentQueries();
  }

  parentQueries() {
    if (this.showClosed) {
      this.getQueries('all');
    } else {
      this.getQueries();
    }
  }

  getQueries(forAll?: string) {
    this.adminService.getParentsQueries(forAll)
      .subscribe((res: any) => {
        this.queries = res;
        console.log(res);
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  viewcase(query) {
    this.adminService.getSelectedParentQueries(query.id)
      .subscribe((res: any) => {
        this.showSupport = true;
        this.selectedQuery = res;
        console.log(res);
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }

  replyToQuery(query) {

    this.adminService.replyToQuery(query.id, { 'id': query.id, 'description': this.replyText, })
      .subscribe((res) => {
        this.selectedQuery = res;
        this.filterQueries(res);
        this.closeReplyModel();
        this.alertService.successAlert('');
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }
  closeQuery(query) {

    this.adminService.closeQuery(query.id)
      .subscribe((res) => {
        this.filterQueries(res);
        this.selectedQuery = res;
        // this.showSupport = false;
        this.alertService.successAlert('');
      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }


  hideSupport() {
    this.showSupport = false;
  }
  closeReplyModel() {
    $('#queryReply').modal('hide');
  }

  filterQueries(res) {
this.queries.forEach( element => {
if (element.id === res.id) {
  _.extend(element, res);

}
});
  }

}
