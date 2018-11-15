import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../providers/admin/admin.service';
import swal from 'sweetalert';
import { PagerService } from '../../../../providers/pagination/pager.service';

declare let $: any;

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html'
})
export class AppStaffComponent implements OnInit {
  constructor(
    private adminService: AdminService,
    private pagerService: PagerService
  ) {}

  loadingStaffList = false;
  centers: any;
  selectedCenter: any = 'all';
  staffType: object[] = [
    { name: 'CxO', value: 'CxO' },
    { name: 'Management', value: 'Management' },
    { name: 'Teacher', value: 'Teacher' },
    { name: 'Staff', value: 'Staff' }
  ];

  update: boolean;
  search: string;
  selectedStaffStatus: any = 'true';
  selectedStaffType: any = 'ALL';
  viewPanel = false;
  allItems = []; // all staff list will be stored here
  staffList = []; // filtered stafflist with pagination stored here
  filteredByCenter = [];
  filteredStaff = [];
  searchedStaff = [];
  pagedItems = [];
  searchKey: any; // search string for name
  pager: any = {}; // for pagination
  activeStatus = true;
  selectedStaff = {};
  paginator = {
    active: 1,
    items: 10,
    lenght: 1
  };

  ngOnInit() {
    // setting centers response from backend
    this.getCenter();
    // setting staffList response recived from backend
    this.getStaff();
    this.subscribeViewPanelChange();
  }

  getCenter() {
    this.adminService.getCenters().subscribe(response => {
      this.centers = response;
    });
  }

  getStaff() {
    this.adminService.getStaff().subscribe(response => {
      this.allItems = response;
      this.staffList = this.allItems;
      this.setPage(1);
      this.staffLoaded();
    });
  }

  showStaff(staff) {
    this.update = false;
    this.selectedStaff = staff;
    this.adminService.viewPanel.next(true);
  }
  addNewStaff(staff) {
    this.update = true;
    this.selectedStaff = {};
    this.adminService.viewPanel.next(true);
  }

  editStaff(staff) {
    this.update = true;
    this.selectedStaff = staff;
    this.adminService.viewPanel.next(true);
  }

  // set the page of pagination after data intialized and changes
  setPage(page: number) {
    this.pager = this.pagerService.getPager(this.staffList.length, page);

    // get current page of items
    this.pagedItems = this.staffList.slice(
      this.pager.startIndex,
      this.pager.endIndex + 1
    );
    // console.log(this.pagedItems);
  }

  searchStaff(event) {
    let list = this.allItems;
    if (this.filteredStaff.length) {
      list = this.filteredStaff;
    }
    this.staffList = list;
    this.searchKey = event;
    const val = event.target.value;
    if (val && val.trim() !== '') {
      this.staffList = list.filter(staff => {
        return staff.name.toLowerCase().startsWith(val.toLowerCase());
      });
      this.searchedStaff = this.staffList;
    } else {
      this.searchedStaff = [];
    }
    this.setPage(1);
  }

  filterStaffByCenter() {
    let list = this.allItems;
    if (this.filteredStaff.length) {
      list = this.filteredStaff;
    }

    if (this.searchedStaff.length) {
      list = this.searchedStaff;
    }

    this.staffList = list;
    // this.filteredStaff = this.allItems;
    if (this.selectedCenter !== 'all') {
      this.staffList = list.filter(
        staff => staff.centerName === this.selectedCenter.name
      );
      this.filteredStaff = this.staffList;
    } else {
      this.filteredByCenter = [];
    }
    this.setPage(1);
    this.staffLoaded();
  }

  filterStaff() {
    this.loadingStaffList = true;

    const filter = {
      employeeType: this.selectedStaffType,
      active: this.selectedStaffStatus,
      pageNumber: 0,
      pageSize: 0
    };
    this.adminService.filterStaff(filter).subscribe(response => {
      this.staffList = response.stafflist;
      this.filteredStaff = this.staffList;
      if (this.searchedStaff.length) {
        this.searchStaff(this.searchKey);
      }
      if (this.selectedCenter !== 'all') {
        this.filterStaffByCenter();
      } else {
        this.staffLoaded();
      }
    });
  }

  staffLoaded() {
    setTimeout(() => {
      this.loadingStaffList = false;
    }, 500);
  }

  deleteStaffSwal(staffId) {
    swal({
      title: 'Are you sure want to deactivate Staff?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      buttons: ['Cancel', 'OK'],
      closeOnClickOutside: true,
      closeOnEsc: true,
      dangerMode: true
    }).then(value => {
      if (value) {
        this.adminService.deleteStaffById(staffId);
      }
    });
  }

  hasPrivilege(privilege) {}

  subscribeViewPanelChange = () => {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
  }
}
