import { Component, OnInit } from '@angular/core';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { AlertService } from '../../../providers/alert/alert.service';

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
      });
  }
  newQuery() {

  }

  replyToQuery() {

  }

}
