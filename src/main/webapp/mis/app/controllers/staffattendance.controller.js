app.controller('StaffAttendanceController', function ($scope, $http) {
    $scope.uploadDelimiter = "\t";
    $scope.uploadBioIdColumn = 1;
    $scope.uploadDateColumn = 2;
    $scope.disableUploadBtn = false;
    $scope.disablePullBtn = false;
    $scope.pullDryRun = true;


    $scope.pullAttendance = function (from, to) {
        $scope.disablePullBtn = true;
        var req = {from: from, to: to, dryRun: $scope.pullDryRun};
        $http.post('/api/attendance/staff/pull', req, {responseType: 'arraybuffer'}).then(function (response) {
            $scope.disablePullBtn = false;
            var blob = new Blob([response.data], {
                type: 'application/octet-stream'
            });
            saveAs(blob, response.headers("fileName"));
        }, function (response) {
            $scope.disablePullBtn = false;
            error(response.data.error);
        });
    };

    function initDateTimePicker() {
        var datepicker = $('.datepicker');
        datepicker.datetimepicker({
            format: 'YYYY-MM-DD',
            useCurrent: true,
            showTodayButton: true
        });
        datepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });

        $("#pullTo").on("dp.change", function () {
            $scope.pullTo = $("#pullTo").val();
        });
        $("#pullFrom").on("dp.change", function () {
            $scope.pullFrom = $("#pullFrom").val();
        });
    }

    initDateTimePicker();

    $scope.uploadExcel = function () {
        $scope.disableUploadBtn = true;
        var fd = new window.FormData();
        fd.append('file', $scope.file);
        $http.post('/api/attendance/staff/import/excel/', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(
            function (response) {
                $scope.disableUploadBtn = false;
                ok("Attendances imported successfully");
            }, function (response) {
                $scope.disableUploadBtn = false;
                error(response.data.error);
            }
        );
    };

    refresh();

    $scope.clockin = debounce(function (staff) {
        staff.clockinDisabled = true;
        $http.post('/api/attendance/staff/clockin/', {employeeId: staff.id})
            .then(function (response) {
                staff.clockinDisabled = false;
                ok("Clock in OK");
                refresh();
            }, function (response) {
                staff.clockinDisabled = false;
                error(response.data.error);
            })
    }, 200, true);

    $scope.clockout = debounce(function (staff) {
        staff.clockoutDisabled = true;
        $http.post('/api/attendance/staff/clockout/', {employeeId: staff.id})
            .then(function (response) {
                staff.clockoutDisabled = false;
                ok("Clock out OK");
                refresh();
            }, function (response) {
                staff.clockoutDisabled = false;
                error(response.data.error);
            })
    }, 200, true);

    $scope.singleLeave = debounce(function (eid, halfLeave) {
        var url = '/api/staff/leave/single-day-leave?eid=' + eid;
        if (halfLeave) {
            url = url + "&halfLeave=" + halfLeave;
        }
        $http.post(url)
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

    function refresh() {
        $http.get('/api/attendance/staff/').then(function (response) {
            $scope.stafflist = response.data;

            angular.forEach($scope.stafflist, function (staff) {
                if (!staff.img) {
                    staff.imgPath = '/assets/img/default-avatar.png'
                } else {
                    staff.imgPath = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/' + staff.img;
                }
                staff.clockinDisabled = false;
                staff.clockoutDisabled = false;
            });
        });

    }

    $scope.attendanceSorter = function (attendance) {
        if (attendance.actualOut) {
            return "zzzzzzzz" + attendance.expectedIn;
        } else {
            return attendance.status + attendance.expectedIn;
        }
    };

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

});
