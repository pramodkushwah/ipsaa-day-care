import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';
import * as _ from 'underscore';
@Component({
  selector: 'app-student-attendance',
  templateUrl: './student-attendance.component.html',
  styleUrls: ['./student-attendance.component.css']
})
export class StudentAttendanceComponent implements OnInit {
  public attendance: any[] = [];
  private attendanceCopy: any;
  bharatRamSchool = false;
  programs = [];
  selectedCenter: any;
  selectedCenterId: any;
  selectedProgram: any;
  centers: any = [];
  imgLaod: boolean[] = [];

  constructor(private adminService: AdminService, private alertService: AlertService) { }

  ngOnInit() {
    this.getStudentAttendance();
    this.adminService.getCenters().subscribe(response => {
      this.centers = response;
    });
  }

  getStudentAttendance() {
    this.alertService.loading.next(true);
    this.adminService.getStudentAttendance().subscribe((response: any[]) => {
      this.attendance = response;
      this.attendanceCopy = response;
      this.attendance = this.attendanceCopy.filter(student => {
        return student.centerType !== 'School';
      });
      this.alertService.loading.next(false);
    }, error => {
      this.alertService.loading.next(false);
    });
  }

  clockin(student: any) {
    student.clockinDisabled = true;
    this.adminService.clockInStudent(student).subscribe((response: any) => {
      _.extend(student, response);
      student.clockinDisabled = false;
      this.alertService.successAlert('Clock in OK');
    }, (error: any) => {
      student.clockinDisabled = false;
    });
  }

  clockOut(student: any) {
    student.clockoutDisabled = true;
    if (student.centerType !== 'School') {
      this.adminService.clockOutStudent(student).subscribe((response: any) => {
        student.clockoutDisabled = false;
        _.extend(student, response);
        this.alertService.successAlert('Clock out OK');
      }, (error: any) => {

      });
    } else {
      this.adminService.markPresent(student.id).subscribe((response: any) => {
        student.clockoutDisabled = false;
        _.extend(student, response);
        this.alertService.successAlert('Clock out OK');
      }, (error: any) => {
        student.clockoutDisabled = false;
      });
    }
  }

  centerChange(center) {
    if (center && center.type === 'School') {
      this.getPrograms(center);
      this.attendance = this.attendanceCopy.filter(student => {
        return student.center === center.name;
      });
    } else if (center) {
      this.attendance = this.attendanceCopy.filter(student => {
        return (student.center === center.name);
      });
      this.bharatRamSchool = false;
    } else {
      this.bharatRamSchool = false;
      this.attendance = this.attendanceCopy.filter(student => {
        return student.centerType !== 'School';
      });
    }
  }

  getPrograms(center) {
    this.selectedCenterId = center.id;
    this.bharatRamSchool = true;
    this.adminService.getProgramsByCenterId(center.id).subscribe(response => {
      this.programs = response;
    });
  }

  presentFilteredStudent() {
    this.adminService.markPresents(this.selectedCenterId, this.selectedProgram.id).subscribe(response => {
      this.attendance = response;
      this.alertService.successAlert('Present Marked');
    });
  }

  programChange(program) {
    this.attendance = this.attendanceCopy.filter(student => {
      return (student.program === program.name && student.centerType === 'School');
    });
  }



  searchStudent(event: any) {
  const val = event.target.value.toLowerCase();
  if (val && val.trim() !== '') {
    this.attendance = this.attendanceCopy.filter(student => {
      return (
        student.fullName.toLowerCase().startsWith(val) ||
        (student.program && student.program.toLowerCase().startsWith(val)) ||
        (student.group && student.group.toLowerCase().startsWith(val)) ||
        (student.center && student.center.toLowerCase().startsWith(val)) ||
        (student.status && student.status.toString().startsWith(val))
      );
    });

  } else {
    this.attendance = this.attendanceCopy;
  }

  }
}
