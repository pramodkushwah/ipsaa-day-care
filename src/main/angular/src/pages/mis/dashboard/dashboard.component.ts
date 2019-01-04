import { Component, OnInit, AfterViewInit } from '@angular/core';
import { DashboardService } from '../../../providers/dashboard/dashboard.service';
import { Student } from '../../../modal/student';
import { AdminService } from '../../../providers/admin/admin.service';
import { AlertService } from '../../../providers/alert/alert.service';

declare const $: any;
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {
  zones: any;
  cities: any;
  centers: any;
  citiesCopy: any = [];
  centersCopy: any = [];
  statsResult: any = {};
  selectedZone: any = 'all';
  selectedCity: any = 'all';
  selectedCenter: any = 'all';
  months: any[] = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];
  quarters: any[] = ['FYQ4', 'FYQ1', 'FYQ2', 'FYQ3'];
  monthly: any = {
    feeDuration: 'Monthly',
    month: new Date().getMonth() + 1,
    year: new Date().getFullYear()
  };
  quarterly: any = {
    feeDuration: 'Quarterly',
    quarter: Math.floor(new Date().getMonth() / 3) + 1,
    year: new Date().getFullYear()
  };
  years: any[] = [new Date().getFullYear() - 1, new Date().getFullYear(), new Date().getFullYear() + 1];
  monthlyFee: any = {};
  quarterlyFee: any = {};
  students: Student[] = [];
  tableData: any[] = [];
  staff: any;
  tableColumn: any[] = [];
  tableTitle: string;
  tableFor: string;
  selectedStudent: any = {};
  update: boolean;
  viewPanel: boolean;
  selectedCenterFromList: any;
  selectedStaff: any = {};
  dashboardTabs: any[] = [];
  history: any;
  viewPanelForFee: boolean;
  DASHBOARD_STATS: boolean;

  terget: any;
  SALARY_READ: boolean;
  tableIcon: string;
  constructor(private dashboardService: DashboardService, private alertService: AlertService, private adminService: AdminService) { }

  ngOnInit() {
    this.DASHBOARD_STATS = this.adminService.hasPrivilage('DASHBOARD_STATS');
    this.SALARY_READ = this.adminService.hasPrivilage('SALARY_READ');
    if (this.DASHBOARD_STATS) {
      this.loadDashboard();
    }
    this.subscribeViewPanelChange();
  }

  ngAfterViewInit() {

    if ($('#back-to-top').length) {
      const scrollTrigger = 100,
        backToTop = function () {
          const scrollTop = $(window).scrollTop();
          if (scrollTop > scrollTrigger) {
            $('#back-to-top').addClass('show');
          } else {
            $('#back-to-top').removeClass('show');
          }
        };
      backToTop();
      $(window).on('scroll', function () {
        backToTop();
      });
      $('#back-to-top').on('click', function (e) {
        e.preventDefault();
        $('html,body').animate({
          scrollTop: 0
        }, 700);
      });
    }

  }

  loadDashboard() {
    this.getDashboardTabs();
    this.getMonthlyFee({});
    this.getQuarterlyFee({});
    this.getStatsResult();
    this.getZones();
    this.getCities();
    this.getCenters();
  }

  getDashboardTabs() {
    this.dashboardService.getDashboardTabs().subscribe((response: any[]) => {
      this.dashboardTabs = response;
    });
  }

  getZones() {
    this.dashboardService.getZones().subscribe((response: any) => {
      this.zones = response;
    });
  }

  getCities() {
    this.dashboardService.getCities().subscribe((response: any) => {
      this.cities = response;
      this.citiesCopy = JSON.parse(JSON.stringify(response));
    });
  }

  getCenters() {
    this.dashboardService.getCenters().subscribe((response: any) => {
      this.centers = response;
      this.centersCopy = JSON.parse(JSON.stringify(response));
    });
  }

  bySelectingZone() {
    this.selectedCity = 'all';
    this.selectedCenter = 'all';
    if (this.selectedZone !== 'all') {
      // this.cities = this.selectedZone.cities;
      this.centers = this.centersCopy.filter(center => {
        return center.zone === this.selectedZone.name;
      });
      this.cities.filter(city => {
        return city.zone === this.selectedZone.name;
      });
    } else {
      this.centers = this.centersCopy;
      this.cities = this.citiesCopy;
    }
    this.getStatsResult();
  }
  bySelectingCity() {
    this.selectedCenter = 'all';
    if (this.selectedCity !== 'all') {
      this.centers = this.centersCopy.filter(center => {
        return center.city === this.selectedCity.name;
      });
    } else {
      this.centers = this.centersCopy.filter(center => {
        return center.zone === this.selectedZone.name;
      });
    }
    this.getStatsResult();
  }

  getStatsResult() {
    console.log(this.selectedCenter);

    const object: any = {};
    if (this.selectedZone !== 'all') {
      object.zone = this.selectedZone.name;
    }
    if (this.selectedCity !== 'all') {
      object.city = this.selectedCity.name;
    }
    if (this.selectedCenter !== 'all') {
      object.center = this.selectedCenter.code;
    }
    this.dashboardService.getStats(object).subscribe((response: any) => {
      this.getMonthlyFee(this.monthly);
      this.getQuarterlyFee(this.quarterly);
      this.statsResult = response;
    });
  }

  getCenterByZone(zone: string) {
    this.dashboardService.getCenterByZone(zone).subscribe((response: any) => {
      this.centers = response;
    });
  }

  getMonthlyFee(object: any) {
    if (this.selectedZone !== 'all') {
      object.zone = this.selectedZone.name;
    }
    if (this.selectedCity !== 'all') {
      object.city = this.selectedCity.name;
    }
    if (this.selectedCenter !== 'all') {
      object.center = this.selectedCenter.code;
    }
    this.adminService.viewPanel.next(false);
    if (object) {
      object = Object.assign(object, this.monthly);
    } else {
      object = Object.assign({}, this.monthly);
    }
    this.dashboardService.getFee(object).subscribe((response: any) => {
      this.monthlyFee = response;
    });
  }

  getQuarterlyFee(object: any) {
    if (this.selectedZone !== 'all') {
      object.zone = this.selectedZone.name;
    }
    if (this.selectedCity !== 'all') {
      object.city = this.selectedCity.name;
    }
    if (this.selectedCenter !== 'all') {
      object.center = this.selectedCenter.code;
    }
    this.adminService.viewPanel.next(false);
    if (typeof(object)) {
      object = Object.assign(object, this.quarterly);
    } else {
      object = Object.assign(Object, this.quarterly);
    }
    this.dashboardService.getFee(object).subscribe((response: any) => {
      this.quarterlyFee = response;
    });
  }

  getStudents(object) {
    this.adminService.viewPanel.next(false);
    this.tableTitle = 'Students';
    this.tableIcon = '/assets/img/students.png';
    this.tableData = [];
    this.dashboardService.getStudents(object).subscribe((response: any) => {
      this.tableData = response;
      this.students = response;
      this.tableColumn = [
        'name',
        'program',
        'group',
        'present',
        'expectedIn',
        'expectedOut',
        'checkin',
        'checkout',
        'extraHours'
      ];
    });
  }

  getStaff() {
    const object = {
      center: this.selectedCenter.code,
      city: this.selectedCity.name,
      zone: this.selectedZone.name
    };
    this.adminService.viewPanel.next(false);
    this.alertService.loading.next(true);

    this.tableFor = 'staff';
    this.tableTitle = 'Staff';
    this.tableIcon = '/assets/img/staff.png';
    this.tableData = [];
    this.dashboardService.getStaff(object).subscribe((response: any) => {
      this.tableData = response;
      this.alertService.loading.next(false);

      this.tableColumn = [
        'eid',
        'name',
        'designation',
        'mobile',
        'center',
        'employer',
        'ctc'
      ];

      this.scroll(this.terget);
    }, (err) => {
      this.alertService.loading.next(false);
      //   this.scroll(this.terget);
      // }, (err) => {
      //   this.alertService.loading.next(false);

    });
  }

  getCenterList() {
    this.adminService.viewPanel.next(false);
    this.alertService.loading.next(true);
    this.tableFor = 'center';
    this.tableTitle = 'Centers';
    this.tableIcon = '/assets/img/centers.png';
    this.tableData = [];
    this.dashboardService.getCenterList().subscribe((response: any) => {
      this.tableData = response;
      this.alertService.loading.next(false);
      this.tableColumn = [
        'code',
        'capacity',
        'name',
        'type',
        'address',
        'city',
        'zone'
      ];
      this.scroll(this.terget);
    }, (err) => {
      this.alertService.loading.next(false);

    });
  }

  getFilteredStudents(filterType: any) {
    const object = {
      center: this.selectedCenter.code,
      city: this.selectedCity.name,
      zone: this.selectedZone.name,
      status: 'new request'
    };
    this.alertService.loading.next(true);

    this.adminService.viewPanel.next(false);
    this.tableFor = 'student';
    this.tableTitle = filterType + ' Students';
    this.tableIcon = '/assets/img/students.png';
    this.tableData = [];
    this.tableColumn = [];
    this.dashboardService.getStudents(object).subscribe((response: any) => {
      this.students = response;
      this.alertService.loading.next(false);

      switch (filterType) {
        case 'Present':
          this.tableColumn = [
            'name',
            'program',
            'group',
            'corporate',
            'checkin',
            'expectedOut'
          ];
          this.tableData = this.students.filter(student => {
            return student.present;
          });
          break;
        case 'Corporate':
          this.tableColumn = [
            'name',
            'program',
            'group',
            'present',
            'checkin',
            'checkout',
            'expectedOut',
            'extraHours'
          ];
          this.tableData = this.students.filter((student: any) => {
            if (student.checkin < student.expectedIn) {
              student.extraHours += Math.trunc(
                student.expectedIn - student.checkin
              );
            }
            if (student.checkout > student.expectedOut) {
              student.extraHours += Math.trunc(
                student.checkout - student.expectedOut
              );
            }
            return student.corporate;
          });
          break;
        case 'Non-corporate':
          this.tableColumn = [
            'name',
            'program',
            'group',
            'present',
            'checkin',
            'checkout',
            'expectedOut',
            'extraHours'
          ];
          this.tableData = this.students.filter((student: any) => {
            if (student.checkin < student.expectedIn) {
              student.extraHours += Math.trunc(
                student.expectedIn - student.checkin
              );
            }
            if (student.checkout > student.expectedOut) {
              student.extraHours += Math.trunc(
                student.checkout - student.expectedOut
              );
            }
            return !student.corporate;
          });
          break;
      }

      this.scroll(this.terget);
    }, (err) => {
      this.alertService.loading.next(false);

    });
  }

  getStudentFee(feeDuration: any) {
    this.adminService.viewPanel.next(false);
    this.alertService.loading.next(true);

    this.tableFor = '';
    const object: any = {};
    if (feeDuration === 'Monthly') {
      this.tableTitle = 'Ipsaa Club Students Fee';
      this.tableIcon = '/assets/img/ipsaa_club.png';
    } else {
      this.tableTitle = 'Student Fee';
      this.tableIcon = '/assets/img/fee.png';
    }
    this.tableData = [];
    this.tableColumn = [];
    object.feeDuration = feeDuration;
    if (this.selectedZone !== 'all') {
      object.zone = this.selectedZone.name;
    }
    if (this.selectedCity !== 'all') {
      object.city = this.selectedCity.name;
    }
    if (this.selectedCenter !== 'all') {
      object.center = this.selectedCenter.code;
    }
    this.dashboardService.getStudentFee(object).subscribe((response: any) => {
      this.tableData = response;
      this.alertService.loading.next(false);

      this.tableColumn = [
        'name',
        'program',
        'group',
        'center',
        'baseFee',
        'discount',
        'finalFee',
        'feeDuration'
      ];
      this.scroll(this.terget);
    }, (err) => {
      this.alertService.loading.next(false);

    });
  }

  getFolloups() {
    this.adminService.viewPanel.next(false);
    this.alertService.loading.next(true);
    this.tableFor = '';
    this.tableTitle = 'Followup Report';
    this.tableIcon = '/assets/img/inquiries.png';
    this.tableData = [];
    this.tableColumn = [];
    this.dashboardService.getFollowups().subscribe((response: any) => {
      this.tableData = response;
      this.alertService.loading.next(false);

      this.tableColumn = [
        'centerName',
        'openFollowUps',
        'previousFollowUps',
        'todayFollowUps',
      ];

      this.scroll(this.terget);
    }, (err) => {
      this.alertService.loading.next(false);

    });
  }

  showDetail(data: any) {
    switch (this.tableFor) {
      case 'student':
        this.selectedStudent = data;
        this.update = true;
        this.adminService.viewPanel.next(true);
        break;
      case 'center':
        this.selectedCenterFromList = data;
        this.update = true;
        this.adminService.viewPanel.next(true);
        break;
      case 'staff':
        this.selectedStaff = data;
        this.update = true;
        this.adminService.viewPanel.next(true);
        break;
    }
  }
  subscribeViewPanelChange = () => {
    this.adminService.viewPanel.subscribe((val: boolean) => {
      this.viewPanel = val;
      if (!val) {
        this.history = null;
      }
    });
    this.adminService.viewPanelForFee.subscribe(value => {
      this.viewPanelForFee = value;
    });
  }

  isAccessible(tab: string) {
    return (this.dashboardTabs.indexOf(tab) === -1);
  }

  getHistory(history: any) {
    this.history = history;
  }


  scroll(el: any) {
    this.terget = el;
    el.scrollIntoView();

  }


  getFilterStaff(fitlterBy) {

    this.adminService.viewPanel.next(false);
    this.alertService.loading.next(true);

    this.tableFor = 'staff';
    this.tableTitle = 'Staff';
    this.tableIcon = '/assets/img/staff.png';
    this.tableData = [];

    const object: any = {};
    if (this.selectedZone !== 'all') {
      object.zone = this.selectedZone.name;
    }
    if (this.selectedCity !== 'all') {
      object.city = this.selectedCity.name;
    }
    if (this.selectedCenter !== 'all') {
      object.center = this.selectedCenter.code;
    }


    this.dashboardService.getFilterStaff(fitlterBy, object)
      .subscribe((res: any) => {

        this.tableData = res;
        this.alertService.loading.next(false);
        switch (fitlterBy) {
          case 'newjoinings':
            this.tableColumn = [
              'name',
              'designation',
              'mobile',
              'center',
              'employer',
              'doj',
              'active'
            ];
            this.tableTitle = 'Staff New Joinee';
            this.tableIcon = '/assets/img/staff.png';
            break;
          case 'newleavings':
            this.tableColumn = [
              'name',
              'designation',
              'mobile',
              'center',
              'employer',
              'dol',
              'active'
            ];
            this.tableTitle = 'Staff New Joinee';
            this.tableIcon = '/assets/img/staff.png';
            break;
          case 'recruitmentHeadCountList':
            this.tableColumn = [
              'name',
              'designation',
              'mobile',
              'center',
              'employer',
              'dol',
              'active'
            ];
            this.tableTitle = 'Staff Active Headcount';
            this.tableIcon = '/assets/img/staff.png';
            break;
          case 'presentStaff':
            this.tableColumn = [
              'name',
              'designation',
              'mobile',
              'center',
              'employer',
              'checkIn',
              'checkOut'
            ];
            this.tableTitle = 'Present Staff';
            this.tableIcon = '/assets/img/staff.png';
            break;
          case 'absentStaff':
            this.tableColumn = [
              'name',
              'designation',
              'mobile',
              'center',
              'employer'
            ];
            this.tableTitle = 'Absent Staff';
            this.tableIcon = '/assets/img/staff.png';
            break;
          case 'onLeaveStaff':
            this.tableColumn = [
              'name',
              'designation',
              'mobile',
              'center',
              'employer'
            ];
            this.tableTitle = 'Staff On Leave';
            this.tableIcon = '/assets/img/staff.png';
            break;

        }
        this.scroll(this.terget);

      }, (err) => {
        this.alertService.errorAlert(err);
      });
  }
}


