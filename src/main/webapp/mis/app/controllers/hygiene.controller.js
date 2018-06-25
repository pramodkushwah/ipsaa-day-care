app.controller('HygieneController', function ($scope, $http) {
    $scope.centers = [];
    $scope.selectedCenter;
    $scope.hygienes = [];
    $scope.workingHygiene = {};
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

    $scope.centerChanged = function (center) {
        loadHygienes(center);
    }

    function loadHygienes(center) {
        if (center) {
            $scope.selectedCenter = center;
            $http.get('/api/hygiene/' + center.id).then(
                function (response) {
                    $scope.hygienes = response.data;
                    for (var i = 0; i < $scope.hygienes.length; i++) {
                        $scope.hygienes[i].edit = false;
                        $scope.saving=false;
                        $scope.hygienes.delete=false;
                    }
                }
            );
        } else {
            $scope.hygienes = [];
        }
    }


    $scope.save = debounce(function (workingHygiene) {
        if ($scope.selectedCenter) {
            if (workingHygiene) {
                if (workingHygiene.name) {
                    workingHygiene.centerId = $scope.selectedCenter.id;
                    if (workingHygiene.id) {
                        $scope.disableSave = true;
                        workingHygiene.saving=true;
                        $http.put('/api/hygiene/', workingHygiene).then(
                            function (response) {
                                $scope.workingHygiene = {};
                                $scope.disableSave = false;
                                loadHygienes($scope.selectedCenter);
                            }, function (response) {
                                $scope.disableSave = false;
                            }
                        );
                    } else {
                        $scope.disableSave = true;
                        $http.post('/api/hygiene/', workingHygiene).then(
                            function (response) {
                                $scope.workingHygiene = {};
                                $scope.disableSave = false;
                                loadHygienes($scope.selectedCenter);
                            }, function (response) {
                                $scope.disableSave = false;
                            }
                        );
                    }
                } else {
                    error("Enter name of hygiene")
                }
            }
        } else {
            error("Select Center")
        }
    }, 200, true);

    $scope.delete = debounce(function (hygiene) {
        if (hygiene) {
            if (hygiene.id) {
                hygiene.delete = true;
                $http.delete('/api/hygiene/' + hygiene.id).then(
                    function (response) {
                        loadHygienes($scope.selectedCenter);
                        $scope.workingHygiene = {};
                    }, function (response) {
                    }
                );
            }
        }
    }, 200, true);

    $scope.edit = function (hygiene) {
        hygiene.edit = true;
    }

    $scope.cancel = function (hygiene) {
        hygiene.edit = false;
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

    function refresh() {
        $http.get('/api/center/').then(function (response) {
            $scope.centers = response.data;
        });
    }

    refresh();
});