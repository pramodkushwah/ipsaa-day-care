app.controller('GroupActivityController', function ($scope, $http) {
    $scope.centers = [];
    $scope.groups = [];
    $scope.activities = [];
    $scope.workingActivity = newActivity();

    $scope.showForm = false;
    $scope.saveDisabled = false;

    $scope.saveActivity = debounce(function (activity) {
        $scope.saveDisabled = true;
        if (activity.mode == 'New') {
            $http.post('/api/group/activity', activity).then(
                function (response) {
                    $scope.saveDisabled = false;
                    $scope.getActivities($scope.selectedCenter, $scope.selectedGroup);
                    $scope.cancel();
                    ok("Successfully Added");
                }, function (response) {
                    $scope.saveDisabled = false;
                    $scope.getActivities($scope.selectedCenter, $scope.selectedGroup);
                    error(response.data.error);
                }
            );
        } else if (activity.mode == 'Edit') {
            $http.put('/api/group/activity', activity).then(
                function (response) {
                    $scope.saveDisabled = false;
                    $scope.getActivities($scope.selectedCenter, $scope.selectedGroup);
                    $scope.cancel();
                    ok("Successfully Added");

                }, function (response) {
                    $scope.saveDisabled = false;
                    $scope.getActivities($scope.selectedCenter, $scope.selectedGroup);
                    error(response.data.error);
                }
            );
        }
    }, 200, true);

    $scope.showActivity = function (activity) {
        activity.mode = "Show";
        $scope.workingActivity = angular.copy(activity);
        $scope.showForm = true;
    };

    $scope.editActivity = function (activity) {
        activity.mode = "Edit";
        $scope.workingActivity = angular.copy(activity);
        $scope.showForm = true;
    };


    $scope.addActivity = function () {
        $scope.workingActivity = newActivity();
        $scope.showForm = true;
    };

    $scope.cancel = function () {
        $scope.workingActivity = newActivity();
        $scope.showForm = false;

    };

    $scope.getActivities = function (center, group) {
        var url = '?';
        url = url + (center ? 'centerId=' + center.id : '');
        url = url + (center ? '&' : '');
        url = url + (group ? 'groupId=' + group.id : '');
        $http.get('/api/group/activity' + url).then(
            function (response) {
                $scope.activities = response.data;
                angular.forEach($scope.activities, function (activity) {
                    activity.centerId = activity.center.id;
                    activity.groupId = activity.group.id;
                })
            }
        );
    };

    $scope.centerChanged = function (center) {

    };
    $scope.groupChanged = function (group) {

    };

    function newActivity() {
        var now = moment();
        var date=now.year()+'-'+(now.month()+1)+'-'+now.date();
        return {mode: 'New',date: date};
    }

    function loadCenters() {
        $http.get('/api/center/').then(
            function (response) {
                $scope.centers = response.data;
            }
        );
    }

    function loadGroups() {
        $http.get('/api/group').then(
            function (response) {
                $scope.groups = response.data;
            }
        );
    }

    function refresh() {
        loadCenters();
        loadGroups();
    }

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