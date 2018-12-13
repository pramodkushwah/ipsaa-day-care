import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  selectedYear: any;
  selectedMonth: any;
  selectedStudent: any;
  years: any = [];
  students: any = [];
  days: any = [];
  months = [{ 'month': 'January', 'id': 1 },
  { 'month': 'February', 'id': 2 },
  { 'month': 'March', 'id': 3 },
  { 'month': 'April', 'id': 4 },
  { 'month': 'May', 'id': 5 },
  { 'month': 'June', 'id': 6 },
  { 'month': 'July', 'id': 7 },
  { 'month': 'August', 'id': 8 },
  { 'month': 'September', 'id': 9 },
  { 'month': 'October', 'id': 10 },
  { 'month': 'November', 'id': 11 },
  { 'month': 'December', 'id': 12 }];


  constructor() { }

  ngOnInit() {
    this.years.push( (new Date()).getFullYear() - 1 );

  this.years.push( (new Date()).getFullYear() );
  }

  getEmployeeAttendance() {

  }
}
