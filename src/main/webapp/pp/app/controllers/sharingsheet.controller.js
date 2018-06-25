app.controller('SharingSheetController', function ($scope, $http) {

    $scope.students=[];
    $scope.selectedStudent={};
    $scope.selectedDate = moment().format("YYYY-MM-DD");
    $scope.fe = {
        entryType: 'Food',
        food : 'Food',
        entryAt : moment().format("HH:mm"),
        feedQty : 'Some'
    };

    $scope.showSharingSheet = function () {
        $http.get('/api/pp/student/ss/' + $scope.selectedDate+'/'+$scope.selectedStudent.id).then(function (response) {
            $scope.sharingSheet = response.data;
        });

        $http.get('/api/pp/student/pss/' + $scope.selectedDate+'/'+$scope.selectedStudent.id).then(function (response) {
            $scope.pss = response.data;
        });
    };

    $scope.saveParentSharingSheet = function() {
        $http.post('/api/pp/student/pss/', $scope.pss).then(function (response) {
            $scope.pss = response.data;
        })
    };

    $scope.addFood = function () {
        var entry = angular.copy($scope.fe);
        entry.id = $scope.pss.id;
        entry.studentId=$scope.selectedStudent.id;
        $http.post('/api/pp/student/pssentry/', entry).then(function (response) {
            $scope.pss = response.data;
        })
    };

    $scope.getLabel = function (entry) {
        if (entry.entryType == 'Diaper') return 'Diaper';
        if (entry.entryType == 'Food') return entry.food;
        if (entry.entryType == 'Nap') return "till " + entry.entryTo;
    };

    $('.datepicker').datetimepicker({
        format: 'YYYY-MM-DD',
        maxDate: moment.now(),
        useCurrent: true,
        showTodayButton: true
    });

    $('.timepicker').datetimepicker({
        format: 'HH:mm a'
    });

    $('.datepicker').on('dp.show', function (e) {
        $('.bootstrap-datetimepicker-widget').addClass('open');
    });

    $('.timepicker').on('dp.show', function (e) {
        $('.bootstrap-datetimepicker-widget').addClass('open');
    });

    $("#selectedDate").on("dp.change", function() {
        $scope.selectedDate = $("#selectedDate").val();
        $scope.showSharingSheet();
    });

    $("#feedTime").on("dp.change", function() {
        $scope.fe.entryAt = $("#feedTime").val();
    });

    $("#bedTime").on("dp.change", function() {
        $scope.pss.bedTime = $("#bedTime").val();
    });

    $("#wakeupTime").on("dp.change", function() {
        $scope.pss.wakeupTime = $("#wakeupTime").val();
    });


    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students=response.data;
            if($scope.students.length>0){
                $scope.selectedStudent=$scope.students[0];
                $scope.showSharingSheet();
            }
        });
    }

    $scope.studentChanged=function (student) {
        $scope.showSharingSheet();
    };

    loadStudents();

});