import { Component, OnInit } from '@angular/core';
import { ParentService } from '../../providers/parentPotel/parent.service';

@Component({
  selector: 'app-pp',
  templateUrl: './pp.component.html',
  styleUrls: ['./pp.component.css']
})
export class PpComponent implements OnInit {


  constructor(private parentService: ParentService) { }

  ngOnInit() {
    this.getStudents();
  }
  getStudents() {
    this.parentService.getStudentDetails()
      .subscribe((res: any) => {
console.log(res);

      });
  }

}
