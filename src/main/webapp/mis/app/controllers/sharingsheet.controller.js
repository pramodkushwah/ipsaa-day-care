app.controller('SharingSheetController', function ($scope, $http) {

    var tabs = ['#foodtab', '#diapertab', '#napstab', '#notestab'];

    $scope.selectedDate = moment().format("YYYY-MM-DD");
    $scope.se = {};
    $scope.selectTab = function (index) {
        for (var i = 0; i < tabs.length; i++) {
            var tab = tabs[i];
            if (tab === tabs[index]) {
                $(tab).addClass("active");
            } else {
                $(tab).removeClass("active");
            }
        }
    };

    $scope.centerChange = function () {
        $scope.searchStudent = null;
        $scope.selectedStudent = null;
        $scope.loadStudents();
    };

    $scope.loadStudents = function () {
        var req = {
            centerCode: $scope.selectedCenter.code
        };
        $http.post('/api/student/filter', req).then(function (response) {
            $scope.students = response.data.students;
            $scope.filteredStudents = $scope.students;
        });
    };

    $scope.showSharingSheet = function (student) {
        $scope.selectedStudent = student;
        $scope.searchStudent = student.fullName;

        $http.post('/api/student/pss/', {
            studentId: student.id,
            date: $scope.selectedDate
        }).then(function (response) {
            $scope.pss = response.data;
        });

        $http.post('/api/student/ss/', {
                studentId: student.id,
                date: $scope.selectedDate
            }

        ).then(function (response) {

            $scope.sharingSheet = response.data;
            $scope.se = {
                studentName: $scope.sharingSheet.studentName,
                studentId: $scope.sharingSheet.studentId,
                date: $scope.sharingSheet.sharingDate
            }

        });
    };

    $scope.$watch('searchStudent', function (newValue, oldValue) {

        if (!newValue) return;
        var searchBox = newValue.toLowerCase();
        $scope.filteredStudents = [];
        angular.forEach($scope.students, function (student) {
            if (student.fullName.toLowerCase().indexOf(searchBox) != -1) {
                $scope.filteredStudents.push(student);
            }
        });
    });

    $scope.saveEntry = function (type) {

        if (!$scope.sharingSheet || !$scope.sharingSheet.studentId) {
            error("Select a student");
            return;
        }

        $scope.se.entryType = type;

        $http.post('/api/student/ss/se', $scope.se).then(
            function (response) {
                $scope.sharingSheet = response.data;
                $scope.se = {
                    studentName: $scope.sharingSheet.studentName,
                    studentId: $scope.sharingSheet.studentId,
                    date: $scope.sharingSheet.sharingDate
                };
            }, function (response) {
                error(response.data.error)
            }
        );
    };

    $scope.getLabel = function (entry) {
        if (entry.entryType == 'Diaper') return 'Diaper';
        if (entry.entryType == 'Food') return entry.food;
        if (entry.entryType == 'Nap') return "till " + entry.entryTo;
    };

    init();

    function init() {
        $scope.sharingSheet = {};
        $http.get('/api/center/').then(function (response) {
            $scope.centers = response.data;
        });

        $('.datepicker').datetimepicker({
            format: 'YYYY-MM-DD',
            maxDate: moment.now(),
            useCurrent: true,
            showTodayButton: true
        });

        $('.timepicker').datetimepicker({
            format: 'HH:mm'
        });

        $('.datepicker').on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });

        $('.timepicker').on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });

        $("#entryAt").on("dp.change", function() {
            $scope.se.entryAt = $("#entryAt").val();
        });

        $("#entryAtD").on("dp.change", function() {
            $scope.se.entryAt = $("#entryAtD").val();
        });

        $("#entryFrom").on("dp.change", function() {
            $scope.se.entryFrom = $("#entryFrom").val();
        });

        $("#entryTo").on("dp.change", function() {
            $scope.se.entryTo = $("#entryTo").val();
        });

        $("#selectedDate").on("dp.change", function() {
            $scope.selectedDate = $("#selectedDate").val();
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
        if (!message) message = "Unknown error, please contact support";

        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }
});