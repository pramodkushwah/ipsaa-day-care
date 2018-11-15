import { Component, OnInit } from '@angular/core';
import { PayrollService } from '../../../../providers/payroll/payroll.service';
import { AdminService } from '../../../../providers/admin/admin.service';
import { PagerService } from '../../../../providers/pagination/pager.service';

@Component({
  selector: 'app-salary',
  templateUrl: './salary.component.html',
  styleUrls: ['./salary.component.css']
})
export class SalaryComponent implements OnInit {
  salaryList: any[] = [];
  editable: boolean;
  viewPanel: boolean;
  selectedSalary: any;
  salaryListCopy: any[];
  allItems: any;
  pagedItems: any;
  pager: any = {};
  searchKey: any;
  centers: any[];
  salaries: any[] = [];

  constructor(
    private payrollService: PayrollService,
    private adminService: AdminService,
    private pagerService: PagerService
  ) {}

  ngOnInit() {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
    this.getSalary();
    this.getCenters();
  }

  getSalary() {
    this.payrollService.getSalary().subscribe((response: any[]) => {
      this.salaryList = response;
      this.salaryListCopy = JSON.parse(JSON.stringify(response));
      this.allItems = response;
      this.setPage(1);
      // checked if searchKey entered before
      if (this.searchKey) {
        this.searchEmployee(this.searchKey);
      }
    });
  }

  getCenters() {
    this.adminService.getCenters().subscribe((response: any[]) => {
      this.centers = response;
    });
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

  showSidePanel(update: boolean, salary: any) {
    this.editable = update;
    this.selectedSalary = salary;
    // this.viewPanel = true;
    this.adminService.viewPanel.next(true);
  }

  getEmployeeByCenter(centerCode: any) {
      this.salaryList = JSON.parse(JSON.stringify(this.salaryListCopy));
      if (centerCode === 'ALL') {
      this.allItems = JSON.parse(JSON.stringify(this.salaryListCopy));
      this.salaries = JSON.parse(JSON.stringify(this.allItems));
      this.setPage(1);
    } else {
      this.allItems = this.salaryList.filter((sal: any) => {
        return sal.centerCode === centerCode;
      });
      this.salaries = JSON.parse(JSON.stringify(this.allItems));
      this.setPage(1);
    }
    // // checked if searchKey entered before
    if (this.searchKey) {
      this.searchEmployee(this.searchKey);
    }
  }

  searchEmployee(event: any) {
    this.searchKey = event;
    const val = event.target.value;
    if (val && val.trim() !== '') {
      this.salaryList = (this.salaries.length) ? this.salaries : this.salaryList;
      this.allItems = this.salaryList.filter((sal: any) => {
        return (
          sal.eid.startsWith(val) ||
          (sal.employerCode && sal.employerCode.startsWith(val)) ||
          (sal.firstName && sal.firstName.startsWith(val)) ||
          (sal.lastName && sal.lastName.startsWith(val)) ||
          (sal.netSalary && sal.netSalary.toString().startsWith(val))
        );
      });
      this.setPage(1);
    } else {
        this.allItems = (this.salaries.length) ? this.salaries : this.salaryList;
        this.setPage(1);
    }
  }
}
