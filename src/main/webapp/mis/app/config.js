app.config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
    $urlRouterProvider.otherwise("/app/home");

    $stateProvider
        .state('app', {
            abstract: true,
            url: "/app",
            templateUrl: "views/layout/layout.html",
            controller: "MenuController"
        })
        .state('login', {
            url: "/login",
            controller: 'LoginController',
            templateUrl: "views/login.html"
        })
        .state('app.home', {
            url: "/home",
            controller: 'HomeController',
            templateUrl: "views/mis/home.html"
        })
        .state('app.students', {
            url: "/students",
            controller: 'StudentController',
            templateUrl: "views/mis/students.html"
        })
        .state('app.centers', {
            url: "/centers",
            controller: 'CenterController',
            templateUrl: "views/mis/centers.html"
        })
        .state('app.staff', {
            url: "/staff",
            controller: 'StaffController',
            templateUrl: "views/mis/staff.html"
        })
        .state('app.programs', {
            url: "/programs",
            controller: 'ProgramController',
            templateUrl: "views/mis/programs.html"
        })

        .state('app.activity', {
            url: "/activity",
            controller: 'GroupActivityController',
            templateUrl: "views/mis/groupactivity.html"
        })
        .state('app.centerfeemanagement', {
            url: "/centerfeemanagement",
            controller: 'CenterFeeManagementController',
            templateUrl: "views/mis/centerfeemanagement.html"
        })
        .state('app.studentfeemanagement', {
            url: "/studentfeemanagement",
            controller: 'StudentFeeManagementController',
            templateUrl: "views/mis/studentfeemanagement.html"
        })
        .state('app.feeslips', {
            url: "/feeslips",
            controller: 'StudentFeeSlipController',
            templateUrl: "views/mis/feeslips.html"
        })
        .state('app.feepayment', {
            url: "/feepayment",
            controller: 'StudentFeePaymentController',
            templateUrl: "views/mis/feepayment.html"
        })
        .state('app.studentattendance', {
            url: "/studentattendance",
            controller: 'StudentAttendanceController',
            templateUrl: "views/mis/attendance/studentattendance.html"
        })
        .state('app.usermanagement', {
            url: "/usermanagement",
            controller: 'UserManagementController',
            templateUrl: "views/mis/usermanagement.html"
        })
        .state('app.studentmessage', {
            url: "/studentmessage",
            controller: 'StudentMessageController',
            templateUrl: "views/mis/studentmessage.html"
        })
        .state('app.support', {
            url: "/support",
            controller: 'SupportController',
            templateUrl: "views/mis/support/support.html"
        })
        .state('app.support.viewcase', {
            url: "/view/{id}",
            controller: 'SupportViewController',
            templateUrl: "views/mis/support/supportview.html"
        })
        .state('app.staffmessage', {
            url: "/staffmessage",
            controller: 'StaffMessageController',
            templateUrl: "views/mis/staffmessage.html"
        })
        .state('app.rolemanagement', {
            url: "/rolemanagement",
            controller: 'RoleManagementController',
            templateUrl: "views/mis/rolemanagement.html"
        })
        .state('app.importdata', {
            url: "/importdata",
            controller: 'ImportDataController',
            templateUrl: "views/mis/importdata.html"
        })
        .state('app.salarymanagement', {
            url: "/salarymanagement",
            controller: 'SalaryManagementController',
            templateUrl: "views/mis/salary.html"
        })
        .state('app.monthlysalary', {
            url: "/monthlysalary",
            controller: 'MonthlySalaryController',
            templateUrl: "views/mis/monthlySalary.html"
        })
        .state('app.holiday', {
            url: "/holiday",
            controller: 'HolidayController',
            templateUrl: "views/mis/holiday.html"
        })
        .state('app.foodmenu', {
            url: "/foodmenu",
            controller: 'FoodMenuController',
            templateUrl: "views/mis/foodmenu.html"
        })
        .state('app.sharingsheet', {
            url: "/sharingsheet",
            controller: 'SharingSheetController',
            templateUrl: "views/mis/sharingsheet.html"
        })
        .state('app.hygiene', {
            url: "/hygiene",
            controller: 'HygieneController',
            templateUrl: "views/mis/hygiene.html"
        })
        .state('app.student_approvals', {
            url: "/student_approvals",
            controller: 'StudentApprovalsController',
            params: {centerId: null},
            templateUrl: "views/mis/studentapprovals.html"
        })
        .state('app.student_approval', {
            url: "/student_approval/{id}",
            controller: 'StudentApprovalController',
            templateUrl: "views/mis/studentapproval.html"
        })
        .state('app.staff_approvals', {
            url: "/staff_approvals",
            controller: 'StaffApprovalsController',
            params: {centerId: null},
            templateUrl: "views/mis/staffapprovals.html"
        })
        .state('app.staff_approval', {
            url: "/staff_approval/{id}",
            controller: 'StaffApprovalController',
            templateUrl: "views/mis/staffapproval.html"
        })
        .state('app.inquiry', {
            url: "/inquiry/{id}",
            controller: 'InquiryController',
            templateUrl: "views/mis/inquiry/inquiry.html"
        })
        .state('app.inquiries', {
            url: "/inquiries/{id}",
            controller: 'InquiriesController',
            params: {tab: null},
            templateUrl: "views/mis/inquiry/inquiries.html"
        })
        .state('app.followup', {
            url: "/followup/{inquiryId}",
            controller: 'FollowUpController',
            templateUrl: "views/mis/inquiry/followup.html"
        })
        .state('app.stdattendancereport', {
            url: "/stdattendancereport",
            controller: 'StudentAttendanceReportController',
            templateUrl: "views/mis/report/stdattendancereport.html"
        })
        .state('app.staffattendancereport', {
            url: "/staffattendancereport",
            controller: 'StaffAttendanceReportController',
            templateUrl: "views/mis/report/staffattendancereport.html"
        })
        .state('app.stdfeereport', {
            url: "/feereport",
            controller: 'FeeReportController',
            templateUrl: "views/mis/report/feereport.html"
        })
        .state('app.inquiryreport', {
            url: "/inquiryreport",
            controller: 'InquiryReportController',
            templateUrl: "views/mis/report/inquiryreport.html"
        })
        .state('app.collectionfeereport', {
            url: "/collectionfeereport",
            controller: 'CollectionFeeReportController',
            templateUrl: "views/mis/report/collectionfeereport.html"
        })
        .state('app.hdfcgatewayfeereport', {
            url: "/hdfcgatewayfeereport",
            controller: 'hdfcgatewayfeereportController',
            templateUrl: "views/mis/report/hdfcgatewayfeereport.html"
        })
        .state('app.staffattendance', {
            url: "/staffattendance",
            controller: 'StaffAttendanceController',
            templateUrl: "views/mis/attendance/staffattendance.html"
        })

        .state('app.staffleaves', {
            url: "/staffleaves",
            controller: 'StaffLeaveController',
            templateUrl: "views/mis/attendance/staffleaves.html"
        })

        .state('app.staffattendancelogs', {
            url: "/staffattendancelogs",
            controller: 'StaffAttendanceLogController',
            templateUrl: "views/mis/attendance/staffattendancelog.html"
        })
        .state('app.gallery', {
            url: "/gallery",
            controller: 'GalleryController',
            templateUrl: "views/mis/gallery/gallery.html"
        })
        .state('app.staffsalarymonthlyreport', {
            url: "/staffsalarymonthlyreport",
            controller: 'staffSalaryMonthlyReportController',
            templateUrl: "views/mis/report/staffsalarymonthlyreport.html"
        })
        .state('app.staffreport', {
          url: "/staffreport",
          controller: 'staffReportController',
          templateUrl: "views/mis/report/staffreport.html"
        })


    ;

    $httpProvider.interceptors.push(['$q', '$location', '$localStorage', 'jwtHelper',
        function ($q, $location, $localStorage, jwtHelper) {
            return {
                'request': function (config) {
                    config.headers = config.headers || {};
                    if ($localStorage.token) {
                        config.headers.Authorization = 'Bearer ' + $localStorage.token;
                    }
                    return config;
                },
                'responseError': function (response) {
                    //do not redirect on app api rejects @ 403 and they may be due to
                    //autherisation error, not authentication error
                    if (response.status === 401) {
                        $location.path('/login');
                    }
                    return $q.reject(response);
                }
            };
        }]);
});