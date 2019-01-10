import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import { PagerService } from '../../../../providers/pagination/pager.service';
import { AlertService } from '../../../../providers/alert/alert.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {
  students: any[] = [];
  activeStatus = 'true';
  pageNumber = 0;
  pageSize = 0;
  programCode = 'ALL';

  // array of all items to be paged
  allItems: any[] = [];

  // pager object
  pager: any = {};

  // paged items
  pagedItems: any[] = [];
  viewPanel: boolean;
  selectedStudent: any = {};
  update: boolean;
  programs: any;
  studentsCopy: any;
  searchKey: any;
  loader: boolean;

  constructor(
    private adminService: AdminService,
    private pagerService: PagerService,
    private alertService: AlertService
  ) { }

  ngOnInit() {
    this.getStudents();
    this.getPrograms();
    this.subscribeViewPanelChange();
  }

  getStudents() {

    const object = {
      active: status,
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      programCode: this.programCode
    };
    this.allItems = [];

    this.loader = true;
    this.adminService.getStudents(object).subscribe((response: any) => {
      this.loader = false;
      console.log(response);

      let status: boolean;
      if (this.activeStatus === 'true') {
        status = true;
      } else {
        status = false;

      }

      this.allItems = response.students.filter(stud => {
        return stud.active === status;
      });
      this.students = this.allItems;
      this.studentsCopy = JSON.parse(JSON.stringify(this.students));
      this.pageSize = response.pageSize;
      this.pageNumber = response.pageNumber;
      // initialize to page 1
      this.setPage(1);
      // checked if searchKey entered before
      if (this.searchKey) {
        this.searchStudent(this.searchKey);
      }
    });
  }

  getPrograms() {
    this.adminService.getPrograms().subscribe((response: any) => {
      this.programs = response;
    });
  }

  showStudent(student) {
    this.update = false;
    console.log(student);
    this.selectedStudent = student;
    this.adminService.viewPanel.next(true);
  }

  editStudent(student) {
    console.log(student);
    this.update = true;
    this.selectedStudent = student;
    this.adminService.viewPanel.next(true);
  }

  addNewStudent() {
    this.update = true;
    this.selectedStudent = {};
    this.adminService.viewPanel.next(true);
  }

  deleteStudentSwal(student: any) {
    this.adminService.isFeePanding(student.id).subscribe((res: any) => {
      if (res.isPending) {
        this.alertService
          .confirm("As " + student.fullName + " Fee is still outstanding")
          .then(isConfirm => {
            if (isConfirm) {
              this.adminService
                .deleteStudentForcefully(student.id)
                .subscribe(response => {
                  this.allItems.splice(this.allItems.indexOf(student), 1);
                  this.setPage(1);
                  this.alertService.successAlert(
                    "Student successfully deleted"
                  );
                });
            }
          });
      } else {
        this.alertService.confirm("").then(isConfirm => {
          if (isConfirm) {
            this.adminService
              .deleteStudentById(student.id)
              .subscribe((response: any) => {
                this.alertService.successAlert(
                  "You have deleted student record successfully"
                );
              });
          }
        });
      }
    });
  }

  hasPrivilege(previlage) {
    return this.adminService.hasPrivilage(previlage);
  }

  setPage(page: number) {
    // if (page < 1 || page > this.pager.totalPages) {
    //  return;
    // }

    // get pager object from service
    this.pager = this.pagerService.getPager(this.allItems.length, page);

    // get current page of items
    this.pagedItems = this.allItems.slice(
      this.pager.startIndex,
      this.pager.endIndex + 1
    );
  }

  subscribeViewPanelChange = () => {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
  }

  searchStudent(event: any) {
    this.searchKey = event;
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.allItems = this.students.filter(student => {
        return student.fullName.toLowerCase().startsWith(val);
      });
      this.setPage(1);
    } else {
      this.allItems = this.students;
      this.setPage(1);
    }
  }
  pushNewStudent(student) {
    this.students.push(student);
    this.studentsCopy.push(student);
    this.setPage(1);
  }
}
