app.controller('HomeController', function ($scope, $http, Auth, $state) {

    $scope.hasStatPrivilege = Auth.hasPrivilege("DASHBOARD_STATS");
    if ($scope.hasStatPrivilege) {
        init();
    }
    //also change it to inquiries.controller.js
    $scope.followUpDispositions = ["Callback", "Followup", "NewInquiry", "Revisit"];
    $scope.selectedZone = null;
    $scope.selectedCity = null;
    $scope.selectedCenter = null;
    $scope.searchParent = '';
    $scope.showfollowups = '';

    $scope.PAGESIZE = 10;
    $scope.BARSIZE = 5; //put odd values
    $scope.staffPage = [];
    $scope.staffPageBar = [];

    $scope.SALARY_READ = Auth.hasPrivilege('SALARY_READ');

    $scope.$on('$includeContentLoaded', function (event, url) {
        if (url == 'views/mis/dashboard/feecard.html') {
            //  populating month, year and quarter
            $scope.quarters = [
                {value: 1, name: "FYQ4"},
                {value: 2, name: "FYQ1"},
                {value: 3, name: "FYQ2"},
                {value: 4, name: "FYQ3"}
            ];
            $scope.years = [moment().year() - 1, moment().year()];
            var allmonths = moment.months();
            $scope.feeStats = {
                monthly: {
                    collectedFee: 0,
                    expectedFee: 0
                },
                quarterly: {
                    collectedFee: 0,
                    expectedFee: 0
                }
            };
            $scope.months = [];
            for (var mnth = 0; mnth <= 11; mnth++) {
                $scope.months.push({moy: mnth + 1, name: allmonths[mnth]});
            }
            $scope.selectedMonth = moment().month() + 1;
            $scope.selectedQuarter = ((Math.ceil((moment().month()) / 3)) + 1);
            $scope.quarterlyYear = moment().year();
            $scope.monthlyYear = moment().year();

            $scope.monthChanged = function (month) {
                $scope.selectedMonth = month;
                loadMonthlyFee();
            };
            $scope.monthlyYearChanged = function (year) {
                $scope.monthlyYear = year;
                loadMonthlyFee();
            };
            $scope.quarterChanged = function (quarter) {
                $scope.selectedQuarter = quarter;
                loadQuarterlyFee();
            };
            $scope.quarterlyYearChanged = function (year) {
                $scope.quarterlyYear = year;
                loadQuarterlyFee();
            };
            $scope.$on('centerFilterChanged', function (event, args) {
                loadMonthlyFee();
                loadQuarterlyFee();
            });

            function loadMonthlyFee() {
                var req = {};
                if ($scope.selectedZone !== null) {
                    req.zone = $scope.selectedZone.name;
                }
                if ($scope.selectedCity !== null) {
                    req.city = $scope.selectedCity.name;
                }
                if ($scope.selectedCenter !== null) {
                    req.center = $scope.selectedCenter.code;
                }
                req.year = $scope.monthlyYear;
                req.month = $scope.selectedMonth;
                req.feeDuration = 'Monthly';
                $http.post('/api/stats/fee', req).then(function (response) {
                    $scope.feeStats.monthly.collectedFee = response.data.collectedFee;
                    $scope.feeStats.monthly.expectedFee = response.data.expectedFee;
                });
            }

            function loadQuarterlyFee() {
                var req = {};
                if ($scope.selectedZone !== null) {
                    req.zone = $scope.selectedZone.name;
                }
                if ($scope.selectedCity !== null) {
                    req.city = $scope.selectedCity.name;
                }
                if ($scope.selectedCenter !== null) {
                    req.center = $scope.selectedCenter.code;
                }
                req.year = $scope.quarterlyYear;
                req.quarter = $scope.selectedQuarter;
                req.feeDuration = 'Quarterly';
                $http.post('/api/stats/fee', req).then(function (response) {
                    $scope.feeStats.quarterly.collectedFee = response.data.collectedFee;
                    $scope.feeStats.quarterly.expectedFee = response.data.expectedFee;
                });
            }

            $scope.showFeePanel = function (duration) {
                if (duration) {
                    req = {};
                    if ($scope.selectedZone !== null) {
                        req.zone = $scope.selectedZone.name;
                    }
                    if ($scope.selectedCity !== null) {
                        req.city = $scope.selectedCity.name;
                    }
                    if ($scope.selectedCenter !== null) {
                        req.center = $scope.selectedCenter.code;
                    }
                    req.feeDuration = duration;
                    $http.post('/api/dash/studentfee', req).then(function (response) {
                        $scope.studentfeelist = response.data;
                        $scope.showtab = 'feelist';
                    });

                }
            };
            loadMonthlyFee();
            loadQuarterlyFee();
        }
    });

    $scope.updateStaffPage = function (pageNumber) {
        var pageSize = $scope.PAGESIZE;
        var staffs = $scope.stafflist;
        var staffPage = [];
        var staffPageBar = [];
        var staffCount = staffs.length;
        var pageCount = Math.ceil(staffCount / pageSize);
        var elementCount = 0;
        var count;
        for (var i = ((pageNumber - 1) * pageSize); i < staffCount && elementCount < pageSize; i++, elementCount++) {
            staffPage.push(angular.copy(staffs[i]));
        }
        count = Math.ceil($scope.BARSIZE / 2);
        i = pageNumber <= count ? 1 : pageNumber - (count - 1);
        for (; i <= pageNumber; i++) {
            staffPageBar.push(i);
        }
        count--;
        while (count !== 0 && i <= pageCount) {
            staffPageBar.push(i);
            i++;
            count--;
        }
        $scope.staffPage = staffPage;
        $scope.staffPageNumber = pageNumber;
        $scope.staffPageCount = pageCount;
        $scope.staffPageBar = staffPageBar;
    };

    $scope.studentsPage = [];
    $scope.studentsPageBar = [];
    $scope.updateStudentPage = function (pageNumber) {
        var pageSize = $scope.PAGESIZE;
        var students = $scope.students;
        var studentsPage = [];
        var studentPageBar = [];
        var studentCount = students.length;
        var pageCount = Math.ceil(studentCount / pageSize);
        var elementCount = 0;
        var i;
        var count;
        for (i = ((pageNumber - 1) * pageSize); i < studentCount && elementCount < pageSize; i++, elementCount++) {
            studentsPage.push(angular.copy(students[i]));
        }
        count = Math.ceil($scope.BARSIZE / 2);
        i = pageNumber <= count ? 1 : pageNumber - (count - 1);
        for (; i <= pageNumber; i++) {
            studentPageBar.push(i);
        }
        count--;
        while (count !== 0 && i <= pageCount) {
            studentPageBar.push(i);
            i++;
            count--;
        }

        $scope.studentsPage = studentsPage;
        $scope.studentsPageNumber = pageNumber;
        $scope.studentsPageCount = pageCount;
        $scope.studentPageBar = studentPageBar;
    };

    $scope.refresh = function () {

        var req = {};
        if ($scope.selectedZone !== null) {
            req.zone = $scope.selectedZone.name;
        }
        if ($scope.selectedCity !== null) {
            req.city = $scope.selectedCity.name;
        }
        if ($scope.selectedCenter !== null) {
            req.center = $scope.selectedCenter.code;
        }

        $http.post('/api/stats/', req).then(function (response) {
            $scope.stats = response.data;
        });

    };

    $scope.zoneChange = function () {

        if ($scope.selectedZone !== null) {

            $http.get('/api/city/?zone=' + $scope.selectedZone.name).then(function (response) {
                $scope.cities = response.data;
            });
            $http.get('/api/center/?zone=' + $scope.selectedZone.name).then(function (response) {
                $scope.centers = response.data;
            });
            $scope.refresh();
            $scope.showPanel('NONE');
        } else {
            $http.get('/api/city/').then(function (response) {
                $scope.cities = response.data;
            });

            $http.get('/api/center/').then(function (response) {
                $scope.centers = response.data;
            });

            $scope.selectedCity = null;
            $scope.selectedCenter = null;
            $scope.refresh();
            $scope.showPanel('NONE');
        }
        $scope.$emit('centerFilterChanged', {});
    };

    $scope.cityChange = function () {

        if ($scope.selectedCity !== null) {
            $http.get('/api/center/?city=' + $scope.selectedCity.name).then(function (response) {
                $scope.centers = response.data;
            });
        } else {
            var url = '/api/center/';
            if ($scope.selectedZone !== null) url = url + '?zone=' + $scope.selectedZone.name;
            $http.get(url).then(function (response) {
                $scope.centers = response.data;
            });
        }
        $scope.selectedCenter = null;
        $scope.refresh();
        $scope.showPanel('NONE');
        $scope.$emit('centerFilterChanged', {});
    };

    $scope.centerChange = function () {
        $scope.refresh();
        $scope.showPanel('NONE');
        $scope.$emit('centerFilterChanged', {});
    };

    function init() {

        $http.get('/api/zone/').then(function (response) {
            $scope.zones = response.data;
        });

        $http.get('/api/city/').then(function (response) {
            $scope.cities = response.data;
        });

        $http.get('/api/center/').then(function (response) {
            $scope.centers = response.data;
        });

        $http.get('/api/dash/', {}).then(function (response) {
            var array = response.data;

            if (array.indexOf('student') != -1) {
                array.splice(array.indexOf('student'), 1);
                array.push('student');
            }
            if (array.indexOf('fee') != -1) {
                array.splice(array.indexOf('fee'), 1);
                array.push('fee');
            }

            //removing parent and users box from front-end [ Gopal Kumar ]
            array.splice(array.indexOf('parent'), 1);
            array.splice(array.indexOf('user'), 1);
            $scope.dashlist = array;
        });

        $http.post('/api/stats/', {}).then(function (response) {
            $scope.stats = response.data;
        });
    }

    $scope.showPanel = function (panel) {
        $scope.showtab = panel;
        var req = {};
        if ($scope.selectedZone !== null) {
            req.zone = $scope.selectedZone.name;
        }
        if ($scope.selectedCity !== null) {
            req.city = $scope.selectedCity.name;
        }
        if ($scope.selectedCenter !== null) {
            req.center = $scope.selectedCenter.code;
        }

        switch (panel) {
            case 'studentlist':
                $http.post("/api/dash/student", req).then(function (response) {
                    $scope.students = response.data;
                    $scope.updateStudentPage(1);
                });
                $scope.refresh();
                break;
            case 'stafflist':
                $http.post("/api/dash/staff", req).then(function (response) {
                    $scope.stafflist = response.data;
                    $scope.updateStaffPage(1);
                });
                $scope.refresh();
                break;
            case 'userlist':
                $http.post("/api/dash/user", req).then(function (response) {
                    $scope.users = response.data;
                    //$scope.updateStaffPage(1);
                });
                $scope.refresh();
                break;
            case 'parentlist':
                $http.post("/api/dash/parents", req).then(function (response) {
                    $scope.parents = response.data;
                });
                $scope.refresh();
                break;
            case 'followuplist':
                //TODO :
                $http.post('/api/dash/followupreport', req).then(function (response) {
                    $scope.followupreportlist = response.data;
                });
                $scope.refresh();
                break;
        }
    };

    $scope.showFollowups = function (centerCode) {
        var req = createFollowUpRequest();
        req.centerCodes = [centerCode];
        $http.post('/api/inquiry/followUps/', req).then(
            function (response) {
                $scope.showfollowups = centerCode;
                $scope.followUps = response.data;
            }, function (response) {
                error(response.data.error);
            });

    };

    function createFollowUpRequest() {
        return {dispositions: $scope.followUpDispositions};
    }

    $scope.openInquiry = function (inquiryId) {
        if (inquiryId) {
            var url = $state.href('app.inquiry', {id: inquiryId});
            window.open(url, '_blank');
        }
    }
});