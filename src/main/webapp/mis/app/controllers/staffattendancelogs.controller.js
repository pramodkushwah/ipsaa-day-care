app.controller('StaffAttendanceLogController', function ($scope, $http) {

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

    $scope.yearChange = function () {
        var cy = $scope.selectedYear;
        $scope.months = fetchMonths(cy);
        if (cy != moment().year()) {
            $scope.selectedMonth = $scope.months[0];
        } else {
            $scope.selectedMonth = $scope.months[$scope.months.length - 1]
        }
        loadAttendance();
    };

    $scope.monthChange = function () {
        loadAttendance();
    };

    $scope.applyLeave = function (atten, halfLeave,cancel) {
        var url = '/api/staff/leave/single-day-leave?date=' + atten.date;
        if (atten.selectedLeaveType) {
            url = url + "&leaveType=" + atten.selectedLeaveType;
        }
        if (halfLeave) {
            url = url + "&halfLeave=" + halfLeave;
        }
        atten.disableApply = true;
        $http.post(url).then(
            function (response) {
                atten.disableApply = false;
                ok(cancel? "Leave Canceled.": "Leave Applied");
                loadAttendance();
            }, function (response) {
                atten.disableApply = false;
                error(response.data.error);
            }
        )
    };

    function loadAttendance() {

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
            year: $scope.selectedYear
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

    function refresh() {
        var now = moment();
        var month = now.month();
        var day = now.day();
        var year = now.year();
        $scope.years = [year, year - 1];
        $scope.months = fetchMonths(year);
        $scope.selectedYear = now.year();
        $scope.selectedMonth = $scope.months[$scope.months.length - 1];

        loadAttendance();
    }

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

    refresh();
});
