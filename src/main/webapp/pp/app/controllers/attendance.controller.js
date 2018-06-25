app.controller('AttendanceController', function ($scope, $http, Auth) {
    $scope.selectedStudent={};
    $scope.students=[];
    refresh();

    $('.monthpicker').datetimepicker({
        viewMode: 'years',
        format: 'MMM-YYYY'
    }).show();

    $scope.yearChange = function () {
        var cy = $scope.selectedYear;
        $scope.months = fetchMonths(cy);
        if (cy != moment().year()) {
            $scope.selectedMonth = $scope.months[0];
        }  else {
            $scope.selectedMonth = $scope.months[$scope.months.length-1]
        }
        loadAttendance($scope.selectedStudent);
    };

    $scope.monthChange = function () {
        loadAttendance($scope.selectedStudent);
    };

    function fetchMonths(year) {
        months = [];
        var allmonths = moment.months();
        var counttill = year == moment().year() ? moment().month() : 11;
        for (var i = 0; i <= counttill; i++) {
            months.push({
                name: allmonths[i],
                moy: i+1
            });
        }
        return months;
    }

    function refresh() {

        var now = moment();
        var month = now.month();
        var day = now.day();
        var year = now.year();

        $scope.years = [year, year - 1];
        $scope.months = fetchMonths(year);

        $scope.selectedYear = now.year();
        $scope.selectedMonth = $scope.months[$scope.months.length-1];

        loadAttendance($scope.selectedStudent);
    }
    
    function loadAttendance(student) {

        var daynames = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
        var formonth = moment().year($scope.selectedYear).month($scope.selectedMonth.moy-1);
        var days = formonth.daysInMonth();
        var som = formonth.startOf('month');
        $scope.days = [];

        for (var i = 1; i <= days; i++) {
            $scope.days.push({
                day: i,
                wd: daynames[som.weekday()],
                date: som.format("YYYY-MM-DD")
            });
            som = som.add(1, 'd');
        }
        $scope.attendanceMap = {};
        if(student.id){
            $http.get('/api/pp/student/attendance/'+student.id+'?year='+$scope.selectedYear + '&month='+$scope.selectedMonth.moy).then(function (response) {
                $scope.attendanceRecords = response.data;

                for (var j = 0; j < $scope.attendanceRecords.length; j++) {
                    $scope.attendanceMap[$scope.attendanceRecords[j].attendanceDate] = $scope.attendanceRecords[j];
                }
                for (var i = 0; i < days; i++) {
                    $scope.days[i].attendanceRecord = $scope.attendanceMap[$scope.days[i].date]
                }
            });
        }
    }

    $scope.studentChanged=function (student) {
        loadAttendance($scope.selectedStudent);
    };

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students=response.data;
            if($scope.students.length>0){
                $scope.selectedStudent=$scope.students[0];
                loadAttendance($scope.selectedStudent);
            }
        });
    }

    loadStudents();
});