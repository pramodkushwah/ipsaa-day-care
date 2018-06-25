app.controller('ActivityController', function ($scope, $http) {
    $scope.days = [
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    ];
    $scope.activities = [];
    $scope.students=[];
    $scope.dates = [];



    $scope.studentChanged=function (student) {
        loadActivity(student);
    };

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students=response.data;
            if($scope.students.length>0){
                $scope.selectedStudent=$scope.students[0];
                loadActivity($scope.selectedStudent);
            }
        });
    }

    function loadActivity(student){
        if(student.id){
            $http.get('/api/pp/student/activities/'+student.id).then(function (response) {
                $scope.activities = response.data;
            });
        }
    };

    loadStudents();
});