app.controller('StudentAttendanceController', function ($scope, $http) {

    function debounce(func, wait, immediate) {
        var timeout;
        return function() {
            var context = this, args = arguments;
            var later = function() {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }

    $scope.sortType = 'expectedIn';
    $scope.selectedProgram = '';
    $scope.selectedCenter = '';

    $http.get('/api/center/').then(function (response) {
        $scope.centers = response.data;
    });

    refresh();

    $scope.attendanceSorter = function (attendance) {
        return attendance.status + attendance.expectedIn;
    };

    $scope.clockin = debounce(function (student) {
        student.clockinDisabled=true;
        $http.post('/api/attendance/student/clockin/', {studentId: student.id})
            .then(function (response) {
                student.clockinDisabled=false;
                ok("Clock in OK");
                refresh();
            }, function (response) {
                student.clockinDisabled=false;
                error(response.data.error);
            })
    },200,true);

    $scope.clockout = debounce(function (student) {
        student.clockoutDisabled=true;
        $http.post('/api/attendance/student/clockout/', {studentId: student.id})
            .then(function (response) {
                student.clockoutDisabled=false;
                ok("Clock out OK");
                refresh();
            }, function (response) {
                student.clockoutDisabled=false;
                error(response.data.error);
            })
    },200,true);

    function refresh() {
        $http.get('/api/attendance/student/').then(function (response) {
            $scope.students = response.data;
            $scope.studentsCopy = angular.copy($scope.students);
            // angular.forEach($scope.students, function (student) {
            //     if (!student.img) {
            //         student.imgPath = '/assets/img/default-avatar.png'
            //     } else {
            //         student.imgPath = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/' + student.img;
            //     }
            //     student.clockinDisabled=false;
            //     student.clockoutDisabled=false;
            // })
        });
    }

    $scope.centerChange = function(center){
        scope = $scope;
        if(center){
            $scope.students = $scope.studentsCopy.filter(student => {
                return student.center === center.name;
            });    
            scope.getPrograms(center.id);
        } else{
            $scope.students = $scope.studentsCopy;
        }
    }
    $scope.programs = []
    $scope.getPrograms = function(centerId) {
        $http.get('/api/center/programs/'+ centerId).then(function (response) {
            $scope.programs = response.data;
        });
    }

    $scope.presentFilteredStudent = function(){
        
    }

    $scope.programChange = function(program){
        console.log(program);
        
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
});