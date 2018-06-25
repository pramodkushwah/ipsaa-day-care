app.controller('FoodMenuController', function ($scope, $http) {

    $scope.students=[];

    $scope.studentChanged = function (student) {
      loadFoodMenu(student);
    };

    function loadFoodMenu(student) {
        if(student.id){
            $http.get('/api/pp/student/foodmenu/'+student.id).then(function (response) {
                $scope.menus = response.data;
            });
        }
    }

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students=response.data;
            if($scope.students.length>0){
                $scope.selectedStudent=$scope.students[0];
                loadFoodMenu($scope.selectedStudent);
            }
        });
    }
    loadStudents();
});