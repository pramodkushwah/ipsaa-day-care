app.controller('UserManagementController', function ($scope, $http, $filter) {
    $scope.dropdownCenters = [];
    $scope.disableSave = false;

    function debounce(func, wait, immediate) {
        var timeout;
        return function () {
            var context = this, args = arguments;
            var later = function () {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }

    $scope.searchCenter = '';
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.barsize = 5;//odd value
    $scope.searchUser = '';
    $scope.pagebar = [];
    $scope.getData = function () {
        return $filter('filter')($scope.users, $scope.searchUser);
    };
    $scope.numberOfPages = function () {
        return Math.ceil($scope.getData().length / $scope.pageSize);
    };
    $scope.$watch('searchUser', function (newValue, oldValue) {
        if (oldValue != newValue) {
            $scope.currentPage = 0;
            $scope.updatePageBar();
        }
    }, true);
    $scope.updatePageBar = function () {
        var pages = [];
        var size = ($scope.barsize - 1) / 2;
        var current = $scope.currentPage;
        var total = $scope.numberOfPages();

        var i = current - size >= 0 ? current - size : 0;
        for (; i < current; i++) {
            pages.push({number: i, current: false});
        }

        pages.push({number: current, current: true});

        var end = total;
        for (i = current + 1; i < end && (i - current) <= size; i++) {
            pages.push({number: i, current: false});
        }

        $scope.pagebar = pages;
    };

    $scope.changePage = function (page) {
        $scope.currentPage = page.number;
        $scope.updatePageBar();
    };

    $scope.selectedRole = "NA";

    $scope.showUser = function (user) {
        $http.get('/api/user/' + user.id).then(function (response) {
            $scope.workingUser = response.data;

            $scope.dropdownCenters = [];
            for (var i = 0; i < $scope.centers.length; i++) {
                var missing = true;
                for (var j = 0; j < $scope.workingUser.centers.length; j++) {
                    if ($scope.centers[i].name == $scope.workingUser.centers[j]) {
                        missing = false;
                    }
                }
                if (missing) {
                    $scope.dropdownCenters.push(angular.copy($scope.centers[i]));
                }
            }

            $scope.workingUser.mode = "Show";
            $scope.showUserPanel = true;
            $scope.editUserPanel = false;
        });
    };

    $scope.removeRole = function (index) {
        if ($scope.workingUser) {
            $scope.workingUser.roles.splice(index, 1)
        }
    };

    $scope.addRole = function (selectedRole) {
        if ($scope.workingUser) {

            var alreadyThere = false;
            for (var i = 0; i < $scope.workingUser.roles.length; i++) {
                if ($scope.workingUser.roles[i] == selectedRole.name) {
                    alreadyThere = true;
                }
            }

            if (!alreadyThere) {
                $scope.workingUser.roles.push(selectedRole.name);
            }
        }
    };

    $scope.addCenter = function (selectedCenter) {
        if (selectedCenter) {
            if ($scope.workingUser) {
                for (var i = 0; i < $scope.dropdownCenters.length; i++) {
                    if ($scope.dropdownCenters[i].id == selectedCenter.id) {
                        $scope.dropdownCenters.splice(i, 1);
                        break;
                    }
                }
                $scope.workingUser.centers.push(selectedCenter.name);
            }
        }
    };

    $scope.removeCenter = function (index) {
        if ($scope.workingUser) {
            for (var i = 0; i < $scope.centers.length; i++) {
                if ($scope.workingUser.centers[index] == $scope.centers[i].name) {
                    $scope.dropdownCenters.push(angular.copy($scope.centers[i]));
                    $scope.workingUser.centers.splice(index, 1);
                    break;
                }
            }
        }
    };

    $scope.cancelUser = function () {
        $scope.workingUser = {};
        $scope.showUserPanel = false;
        $scope.editUserPanel = false;
    };

    $scope.saveUser = debounce(function (user) {
        $scope.disableSave = true;
        if (user.employee) {
            user.empId = user.employee.id;
        }
        if (user.id) {
            $http.put('/api/user/', user).then(function () {
                $scope.disableSave = false;
                ok("User saved!");
                $scope.workingUser = {};
                $scope.editUserPanel = false;
                $scope.showUserPanel = false;
                refresh();
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        } else {
            $http.post('/api/user/', user).then(function () {
                $scope.disableSave = false;
                ok("User saved!");
                $scope.workingUser = {};
                $scope.editUserPanel = false;
                $scope.showUserPanel = false;
                refresh();
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        }
    }, 200, true);

    $scope.removeUser = function (user) {

        swal({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Deactivate!'
        }).then(function () {

            $http.delete('/api/user/' + user.id).then(function () {
                ok("User has been deactivated.");
                refresh();
            }, function (response) {
                error(response.data.error);
            });

        })

    };

    $scope.resetPassword = function (user) {
        swal({
            title: "New Password",
            input: "text",
            showCancelButton: true,
            closeOnConfirm: false,
            buttonsStyling: false,
            inputPlaceholder: "New Password"
        }).then(function (inputValue) {
            if (inputValue === false) return false;
            if (inputValue === "") {
                swal.showInputError("Missing password");
                return false
            }

            $http.post('/api/user/resetpwd', {
                id: user.id,
                password: inputValue
            }).then(function () {
                ok("Password changed successfully");
            }, function (response) {
                error(response.data.error);
            });
        });


    };

    $scope.selectEmployee = function(employee){
    console.log(employee);
        $scope.workingUser.firstname = employee.name;
        $scope.workingUser.lastname = " ";
        $scope.workingUser.email = employee.email;
        $scope.workingUser.phone = employee.mobile;
    }

    $scope.newUser = function () {
        $scope.workingUser = {
            mode: 'New',
            active: true,
            roles: []
        };
        $scope.editUserPanel = true;
        $scope.dropdownCenters = angular.copy($scope.centers);
        $scope.workingUser.centers = [];
    };

    function refresh() {

        $http.get('/api/user/').then(function (response) {
            $scope.users = response.data;
            $scope.updatePageBar();
        });

        $http.get('/api/user/roles/').then(function (response) {
            $scope.roles = response.data;
        });

        loadEmployees();

        loadCenters();
    }

    function loadEmployees() {
        var filter = {active: true};
        $http.post('/api/staff/filter', filter)
            .then(function (response) {
                $scope.employees = response.data.stafflist;
            }, function (response) {
                error(response.data.error);
            });
    }

    function loadCenters() {
        $http.get('/api/center/all').then(function (response) {
            $scope.centers = response.data;
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
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    refresh();
});
app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        return typeof input == 'undefined' ? [] : input.slice(start);
    }
});
app.filter('centerFilter', function () {
    return function (input, name) {
        var out = [];
        if (typeof input === 'object') {
            if (typeof name === 'object' || typeof name === 'string') {
                if (name != '') {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].centers.length != 0) {
                            var centers = input[i].centers;
                            for (var j = 0; j < centers.length; j++) {
                                if (centers[j] == name) {
                                    out.push(input[i]);
                                }
                            }
                        } else {
                            out.push(input[i]);
                        }
                    }
                } else {
                    return input;
                }
            }
        }
        return out;
    }
});