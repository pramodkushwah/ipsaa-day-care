app.controller('FoodMenuController', function ($scope, $http) {

    $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
    $scope.months = moment.months();
    $scope.foodMenuList = [];
    $scope.centerList = {};
    $scope.zoneList = [];
    $scope.selectedYear = moment().year();

    $scope.generateDisable = false;
    $scope.saveDisable = false;
    $scope.saveShow = false;

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


    $scope.save = debounce(function () {
        $scope.saveDisable = true;
        if ($scope.foodMenuList.mode == 'New') {
            var list = [];
            angular.forEach($scope.foodMenuList.list, function (menu) {
                angular.forEach(menu.pairs, function (pair) {
                    var obj = {
                        breakfast: menu.breakfast,
                        lunch: menu.lunch,
                        dinner: menu.dinner,
                        date: menu.date,
                        centerCode: pair.centerCode
                    };
                    list.push(obj);
                })
            });
            $http.post('/api/food/savelist', list)
                .then(function (response) {
                    ok("Successfully saved.");
                    $scope.saveDisable = false;
                    $scope.saveShow = false;
                    resetAll();
                }, function (response) {
                    $scope.saveDisable = false;
                    $scope.saveShow = false;
                    error(response.data.error);
                    resetAll();
                });
        } else if ($scope.foodMenuList.mode == 'Old') {
            var list = [];
            angular.forEach($scope.foodMenuList.list, function (menu) {
                angular.forEach(menu.pairs, function (pair) {
                    var obj = {
                        id: pair.id,
                        breakfast: menu.breakfast,
                        lunch: menu.lunch,
                        dinner: menu.dinner,
                        date: menu.date,
                        centerCode: pair.centerCode
                    };
                    list.push(obj);
                })
            });
            $http.put('/api/food/savelist', list).then(
                function (response) {
                    ok("Successfully saved.");
                    $scope.saveDisable = false;
                    $scope.saveShow = false;
                    resetAll();
                }, function (response) {
                    $scope.saveDisable = false;
                    $scope.saveShow = false;
                    error(response.data.error)
                    resetAll();
                }
            );
        }
    }, 200, true);

    function resetAll() {
        $scope.foodMenuList={};
        $scope.selectedMonth='';
        $scope.selectedZone='';
    }

    $scope.getMenuList = debounce(function () {
        if (!$scope.selectedMonth) {
            error("Please select month.");
            return;
        }
        if (!$scope.selectedZone) {
            error("Please select zone.");
            return;
        }
        var month = $scope.selectedMonth;
        var year = $scope.selectedYear;
        var zoneId = $scope.selectedZone.id;
        $scope.generateDisable = true;

        $http.post('/api/food/monthlylist', {zoneId: zoneId, year: year, month: month}).then(
            function (response) {
                var list = response.data;
                if (list.length == 0) {
                    $scope.foodMenuList = newMenu($scope.selectedYear, month);
                } else {
                    $scope.foodMenuList = {mode: 'Old', list: transformResponse(list)};
                }
                $scope.generateDisable = false;
                $scope.saveShow = true;
            }, function (response) {
                $scope.generateDisable = false;
                error(response.data.error);
            }
        );
    }, 200, true);


    function transformResponse(response) {
        var list = [];
        var obj = {};

        angular.forEach(response, function (one) {
            if (typeof obj[one.date] == 'undefined') {
                var v = {};
                v.breakfast = one.breakfast;
                v.lunch = one.lunch;
                v.dinner = one.dinner;
                v.pairs = [{centerCode: one.center.code, id: one.id}];
                v.date = one.date;
                obj[one.date] = v;
            } else {
                obj[one.date].pairs.push({centerCode: one.center.code, id: one.id});
            }
        });

        for (property in obj) {
            list.push(obj[property]);
        }

        return list;
    }

    function newMenu(year, month) {
        if (typeof year != 'undefined' && typeof month != 'undefined') {
            menus = [];
            month = (parseInt(month) + 1);
            v = year + '-' + month;
            days = moment(v, 'YYYY-MM').daysInMonth();

            var pairs = [];
            angular.forEach($scope.selectedCenters, function (center) {
                pairs.push({centerCode: center.code, id: null});
            });

            for (i = 0; i < days; i++) {
                foodmenu = {};
                foodmenu.date = year + '-' + month + '-' + (i + 1);
                foodmenu.breakfast = '';
                foodmenu.lunch = '';
                foodmenu.dinner = '';

                foodmenu.pairs = pairs;

                menus.push(foodmenu);
            }
            return {mode: 'New', list: menus};
        }
        return [];
    }

    $scope.zoneChanged = function (zone) {
        if (zone) {
            $http.get('/api/center/?zone=' + zone.name).then(
                function (response) {
                    $scope.centerList = angular.copy(response.data);
                    $scope.selectedCenters = angular.copy(response.data);
                }
            );
        } else {
            $scope.centerList = [];
        }
        $scope.foodMenuList={};
        $scope.selectedMonth='';
    };

    $scope.monthChanged=function (month) {
        $scope.foodMenuList={};
    };

    function loadZones() {
        $http.get('/api/zone/').then(function (response) {
            $scope.zoneList = response.data;
        });
    }

    loadZones();

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
});