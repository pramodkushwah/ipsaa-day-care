app.controller('RoleManagementController', function ($scope, $http) {

    refresh();

    $scope.showRole = function (role) {
        $scope.workingRole = angular.copy(role);
        $scope.workingRole.mode = "Show";
    };

    $scope.removePrivilege = function (index) {
        if ($scope.workingRole) {
            $scope.workingRole.privileges.splice(index, 1)
        }
    };

    $scope.addPrivilege = function (selectedPrivilege) {
        if ($scope.workingRole) {

            var alreadyThere = false;
            for (var i = 0; i < $scope.workingRole.privileges.length; i++) {
                if ($scope.workingRole.privileges[i].name == selectedPrivilege.name) {
                    alreadyThere = true;
                }
            }

            if (!alreadyThere) {
                $scope.workingRole.privileges.push(selectedPrivilege);
            }
        }
    };

    $scope.cancelRole = function () {
        $scope.workingRole = {
            mode: 'New',
            privileges: []
        };
    };

    $scope.saveRole = function (role) {
        if (role.id) {
            $http.put('/api/user/role/', role).then(refresh);
        } else {
            $http.post('/api/user/role/', role).then(refresh);
        }
    };

    $scope.deleteRole = function (role) {
        $http.delete('/api/user/role/' + role.id).then(refresh);
    };

    $scope.newRole = function () {
        $scope.workingRole = {
            mode: 'New',
            privileges: []
        };
    };

    function refresh() {

        $scope.workingRole = {
            mode: 'New',
            privileges: []
        };

        $http.get('/api/user/roles/').then(function (response) {
            $scope.roles = response.data;
        });

        $http.get('/api/user/privileges/').then(function (response) {
            $scope.privileges = response.data;
        });

    }
});
