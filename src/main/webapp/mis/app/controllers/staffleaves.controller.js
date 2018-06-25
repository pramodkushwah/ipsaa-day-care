app.controller('StaffLeaveController', function ($scope, $http, $rootScope, Auth) {

    $scope.leaveTypes = [
        "CASUAL",
        "SICK",
        "PAID",
        "UNPAID",
        // "HOLIDAY",
        "ADOPTION",
        "MATERNITY",
        "BEREAVEMENT"
    ];

    $scope.hrAdmin = Auth.hasPrivilege("HR_ADMIN");

    $scope.disableUploadBtn = false;

    function debounce(func, wait, immediate) {
        var timeout;
        return function () {
            var context = this, args = arguments;
            var later = function () {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }

    $scope.yearChange = function () {
        var cy = $scope.selectedYear;
        $scope.months = fetchMonths(cy);
        if (cy != moment().year()) {
            $scope.selectedMonth = $scope.months[0];
        } else {
            $scope.selectedMonth = $scope.months[$scope.months.length - 1]
        }
        loadAttendance();
        loadAttendanceSummary();
    };

    $scope.monthChange = function () {
        loadAttendance();
        loadAttendanceSummary();
    };

    $scope.upload = function () {
        $scope.disableUploadBtn = true;
        var fd = new window.FormData();
        fd.append('file', $scope.file);
        $http.post('/api/staff/leave/import/', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(
            function (response) {
                $scope.disableUploadBtn = false;
                ok("Leaves imported successfully");
            }, function (response) {
                $scope.disableUploadBtn = false;
                error(response.data.error);
            }
        );
    };

    $scope.selectEmployee = function (emp) {
        $scope.selectedEmployee = emp;
        $scope.searchEmployee = emp.name;
        $scope.leave = !$scope.leave ? {} : $scope.leave;
        $scope.leave.eid = emp.eid;
        loadAttendance();
        loadAttendanceSummary();
    };

    $scope.approveLeave = function (atten) {
        atten.disableApprove = true;
        $http.get('/api/staff/leave/approve/' + atten.leaveId).then(
            function (response) {
                atten.disableApprove = false;
                loadAttendance();
                loadAttendanceSummary();
                ok("Leave Approved Successfully.");
            }, function (response) {
                atten.disableApprove = false;
                error(response.data.error);
            }
        );
    };

    $scope.rejectLeave = function (atten) {
        atten.disableApprove = true;
        $http.get('/api/staff/leave/reject/' + atten.leaveId).then(
            function (response) {
                atten.disableApprove = false;
                loadAttendance();
                loadAttendanceSummary();
                ok("Leave Rejected Successfully.");
            }, function (response) {
                atten.disableApprove = false;
                error(response.data.error);
            }
        );
    };

    $scope.singleLeave = debounce(function (eid) {
        $http.post('/api/staff/leave/single-day-leave?eid=' + eid)
            .then(function (response) {
                ok("Leave operation successful");
                refresh();
            }, function (response) {
                error(response.data.error);
            })
    }, 200, true);

    $scope.multiLeave = debounce(function (eid) {

        if (!($scope.leave && $scope.leave.leaveType)) {
            error("Please select leave type.");
            return;
        }
        $http.post('/api/staff/leave/multi-day-leave', $scope.leave)
            .then(function (response) {
                ok("Leave operation successful");
                $scope.leave = {};
                refresh();
            }, function (response) {
                error(response.data.error);
            })
    }, 200, true);

    $scope.deleteAttendance = function (atten) {
        if(atten && atten.id){
            atten.disableDelete = true;
            $http.delete('/api/attendance/staff/hradmin/'+atten.attendanceId).then(
                function (response) {
                    atten.disableDelete = false;
                    loadAttendance();
                    loadAttendanceSummary();
                },function (response) {
                    atten.disableDelete = false;
                    error(response.data.error);
                }
            );
        }

    };

    $scope.saveAttendance = function (atten) {
        if ($scope.selectedEmployee && $scope.selectedEmployee.id) {
            var req = {
                employeeId: $scope.selectedEmployee.id
            };
            if (!atten.clockin) {
                error("Please enter clockin.");
                return;
            }
            if (!atten.clockout) {
                error("Please enter clockout.");
                return;
            }
            req.clockin = atten.clockin;
            req.clockout = atten.clockout;
            req.date = atten.date;
            atten.disableSave = true;
            $http.post('/api/attendance/staff/hradmin/',req).then(
                function (response) {
                    atten.disableSave = false;
                    loadAttendance();
                    loadAttendanceSummary();
                },function (response) {
                    atten.disableSave = false;
                    error(response.data.error);
                }
            );

        }
    };


    function fetchMonths(year) {
        months = [];
        var allmonths = moment.months();
        var counttill = year == moment().year() ? moment().month() : 11;
        for (var i = 0; i <= counttill; i++) {
            months.push({
                name: allmonths[i],
                moy: i + 1
            });
        }
        return months;
    }

    function loadAttendance() {

        if (!$scope.searchEmployee) {
            error("Please select Employee");
            return;
        }

        if (!$scope.selectedYear) {
            error("Please select year");
            return;
        }

        if (!$scope.selectedMonth) {
            error("Please select Month");
            return;
        }

        var req = {
            month: $scope.selectedMonth.moy,
            year: $scope.selectedYear,
            empId: $scope.selectedEmployee.id
        };

        $http.post('/api/attendance/staff/', req).then(
            function (response) {
                $scope.attendanceList = response.data;
                for (var i = 0; i < $scope.attendanceList.length; i++) {
                    var obj = $scope.attendanceList[i];
                    switch (obj.status) {
                        case 'Absent':
                            obj.class = 'bg-danger';
                            break;

                        case 'Present':
                            obj.class = 'bg-success';
                            break;

                        case 'Leave':
                            obj.class = 'bg-warning';
                            break;

                        case 'Holiday':
                            obj.class = 'bg-info';
                            break;

                    }
                }

            },
            function (response) {
                error(response.data.error);
            }
        );
    }

    function loadAttendanceSummary() {
        if (!$scope.searchEmployee) {
            error("Please select Employee");
            return;
        }

        if (!$scope.selectedYear) {
            error("Please select year");
            return;
        }

        if (!$scope.selectedMonth) {
            error("Please select Month");
            return;
        }

        var req = {
            month: $scope.selectedMonth.moy,
            year: $scope.selectedYear,
            empId: $scope.selectedEmployee.id
        };

        $http.post('/api/staff/leave/summary/', req).then(
            function (response) {
                // $scope.attendanceList = transformMonthAttendances(response.data, req.month, req.year);

                $scope.leaveSummary = {
                    year: req.year
                };
                $scope.leaveSummary.month = moment(req.year + "-" + req.month + "-01", "YYYY-MM-DD").format("MMMM");

                for (var i = 0; i < $scope.leaveTypes.length; i++) {
                    var leaveType = $scope.leaveTypes[i];
                    $scope.leaveSummary[leaveType] = 0;
                }

                var total = 0;
                for (var i = 0; i < response.data.length; i++) {
                    var obj = response.data[i];
                    $scope.leaveSummary[obj.type] = obj.count;
                    total += obj.count;
                }

                $scope.leaveSummary.total = total;

                $http.post('/api/staff/leave/summary/', {empId: req.empId, year: req.year}).then(
                    function (response) {
                        var total = 0;
                        for (var i = 0; i < response.data.length; i++) {
                            var obj = response.data[i];
                            total += obj.count;
                        }
                        $scope.leaveSummary.yearTotal = total;
                    }, function (response) {
                        error(response.data.error);
                    }
                );
            },
            function (response) {
                error(response.data.error);
            }
        );
    }

    function loadStaff() {
        $http.get('/api/staff/reporting/')
            .then(
                function (response) {
                    $scope.allStaff = response.data;
                }, function (response) {
                    error(response.data.error);
                }
            );
    }

    $rootScope.$on('$includeContentLoaded', function (event, url) {
        initDateTimePicker();
    });

    function initDateTimePicker() {
        var date = new Date();
        var datepicker = $('.datepicker');
        var conf = {
            format: 'YYYY-MM-DD',
            useCurrent: false,
            showTodayButton: true
        };

        if (!$scope.hrAdmin) {
            conf.minDate = date.setDate(date.getDate() + 1)
        }
        datepicker.datetimepicker(conf);

        datepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });

        $("#leave_fromDate").on("dp.change", function () {
            if (!$scope.leave) $scope.leave = {};
            $scope.leave.fromDate = $("#leave_fromDate").val();
        });
        $("#leave_toDate").on("dp.change", function () {
            if (!$scope.leave) $scope.leave = {};
            $scope.leave.toDate = $("#leave_toDate").val();
        });
    }

    function ok(message) {
        swal({
            title: message,
            type: 'success',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    function error(message) {
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    function refresh() {
        loadStaff();

        var now = moment();
        var month = now.month();
        var day = now.day();
        var year = now.year();
        $scope.years = [year, year - 1];
        $scope.months = fetchMonths(year);
        $scope.selectedYear = now.year();
        $scope.selectedMonth = $scope.months[$scope.months.length - 1];

        initDateTimePicker();

    }

    refresh();
});
