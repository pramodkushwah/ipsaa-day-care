app.controller('NavController', function ($scope, $http, Auth) {
    $scope.logout = function () {
        Auth.logout(function () {
            window.location = "/mis/index.html"
        })
    };
    $scope.notifications = [];
});