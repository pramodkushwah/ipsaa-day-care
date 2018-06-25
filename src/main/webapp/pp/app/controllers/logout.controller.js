app.controller('LogoutController', function ($scope, $http, Auth ) {
    Auth.logout(function () {
        window.location = "/"
    })
});