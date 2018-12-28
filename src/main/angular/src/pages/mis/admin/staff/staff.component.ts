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
  staffListCopy: any = [];
  searchStaffList: any = [];

  constructor(
    private adminService: AdminService,
    private pagerService: PagerService
  ) { }
  loadingStaffList = false;
  centers: any;
  selectedCenter: any = 'All';
  staffType = [
    { name: 'CxO', value: 'CxO' },
    { name: 'Management', value: 'Management' },
    { name: 'Teacher', value: 'Teacher' },
    { name: 'Staff', value: 'Staff' }
  ];

  update: boolean;
  search: string;
  selectedStaffStatus = 'true';
  selectedStaffType: any = 'All';
  viewPanel = false;
  allItems = []; // all staff list will be stored here
  staffList = []; // filtered stafflist with pagination stored here
  filteredByCenter = [];
  filteredStaff = [];
  searchedStaff = [];
  pagedItems = [];
  searchKey: any; // search string for name
  pager: any = {}; // for pagination
  activeStatus = 'true';
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
    console.log(this.selectedStaffType);

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
      this.staffListCopy = response.slice(0);
      this.staffList = response.slice(0);
      // this.filterStaff('true');
      this.filterStaffByCenter();
      // this.setPage(1);
      // this.staffLoaded();
    });
  }




  showStaff(staff) {
    this.update = false;
    this.selectedStaff = staff;
    this.adminService.viewPanel.next(true);
  }
  addNewStaff() {
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
    // debugger;
    // this.allItems = this.staffListCopy.slice(0);
    this.pager = this.pagerService.getPager(this.staffList.length, page);
    // get current page of items
    this.pagedItems = this.staffList.slice(
      this.pager.startIndex,
      this.pager.endIndex + 1
    );
    // console.log(this.pagedItems);
  }

  searchStaff(event) {
    // debugger;
    const val = event.target.value.toLowerCase();
    if (val && val.trim() !== '') {
      this.staffList = this.searchStaffList.filter( staff => {
        return staff.name.toLowerCase().startsWith(val);
      });
    } else {
      console.log( this.searchStaffList);
      this.staffList = this.searchStaffList;
    }
    this.setPage(1);
  }

  filterStaffByCenter() {
    let status: boolean;
    if (this.selectedStaffStatus === 'true') {
      status = true;
    } else {
      status = false;

    }
    console.log('clcik hhhhh');

    if (this.selectedCenter === 'All') {
      console.log(' if1');

      if (this.selectedStaffType === 'All') {
        console.log(' if2');

        this.staffList = this.allItems.filter(staff => {
          console.log(status);

          return staff.active === status;
        });
      } else {
        console.log(' else1');

        this.staffList = this.allItems.filter(staff => {
          return staff.type === this.selectedStaffType && staff.active === status;
        });
      }
    } else {
      console.log(' else2');

      if (this.selectedStaffType === 'All') {
        console.log(' if3');

        this.staffList = this.allItems.filter(staff => {
          return staff.centerCode === this.selectedCenter.code && staff.active === status;
        });
      } else {
        console.log(' else3');

        this.staffList = this.allItems.filter(staff => {
          return staff.type === this.selectedStaffType && staff.active === status &&
            staff.centerCode === this.selectedCenter.code;
        });
      }
    }

    //  this.fitlerStaffListActive();


    // let list = this.allItems;
    // if (this.filteredStaff.length) {
    //   list = this.filteredStaff;
    // }

    // if (this.searchedStaff.length) {
    //   list = this.searchedStaff;
    // }

    // this.staffList = list;
    // // this.filteredStaff = this.allItems;
    // if (this.selectedCenter !== 'all') {
    //   this.staffList = list.filter(
    //     staff => staff.centerName === this.selectedCenter.name
    //   );
    //   this.filteredStaff = this.staffList;
    // } else {
    //   this.filteredByCenter = [];
    // }
    this.searchStaffList = this.staffList;

    this.setPage(1);
    // this.staffLoaded();
  }

  // filterStaff(status) {
  //   const a = 'true' === status;
  //   this.loadingStaffList = true;
  //   this.staffList = this.staffListCopy.filter((staff: any) => {
  //     return staff.active === a;
  //   });
  //   this.setPage(1);
  //   this.staffLoaded();
  //   // const filter = {
  //   //   employeeType: this.selectedStaffType,
  //   //   active: this.selectedStaffStatus,
  //   //   pageNumber: 0,
  //   //   pageSize: 0
  //   // };
  //   // this.adminService.filterStaff(filter).subscribe(response => {
  //   //   this.staffList = response.stafflist;
  //   //   this.filteredStaff = this.staffList;
  //   //   if (this.searchedStaff.length) {
  //   //     this.searchStaff(this.searchKey);
  //   //   }
  //   //   if (this.selectedCenter !== 'all') {
  //   //     this.filterStaffByCenter();
  //   //   } else {
  //   //     this.staffLoaded();
  //   //   }
  //   // });
  // }

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
        this.adminService.deleteStaffById(staffId)
          .subscribe((res: any) => {
            this.getStaff();
          });
      }
    });
  }

  hasPrivilege(privilege) { }

  subscribeViewPanelChange = () => {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
    });
  }
}
