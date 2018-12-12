app.controller('HolidayController', function ($scope, $http, $rootScope, $filter) {

    $scope.centerFilter = "ALL";
    $scope.edit = false;
    $scope.show = false;
    $scope.add = false;
    $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
    $scope.months = [];
    var m = moment.months();
    for (var i = 0; i < m.length; i++) {
        var mon = m[i];
        $scope.months.push({
            name: mon,
            value: i + 1
        });
    }
    $scope.selectedMonth = moment().month() + 1;
    $scope.selectedYear = moment().year();


    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.barsize = 5;//odd value
    $scope.pagebar = [];
    $scope.searchHoliday = '';
    $scope.getData = function () {
        var f1 = $filter('filter')($scope.holidays, $scope.searchHoliday);
        if ($scope.selectedCenter && $scope.selectedCenter.id) {
            f1 = $filter('filter')(f1, $scope.selectedCenter.code);
        }
        return f1;
    };
    $scope.numberOfPages = function () {
        return Math.ceil($scope.getData().length / $scope.pageSize);
    };

    $scope.resetPagination = function () {
            $scope.currentPage = 0;
            $scope.updatePageBar();
    };

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


    $http.get('/api/center/all').then(function (response) {
        $scope.centerList = response.data;
        $scope.centerList.unshift({id:'All',name:'All',code:'All',zone:{},address:{}});
        $scope.centerListCopy = angular.copy($scope.centerList);
    });

    $http.get('/api/zone/').then(function(response){
        $scope.zones = response.data;
        // $scope.zones.unshift({id:'All',name:'All'});
    });

    $scope.getStates = function(zone){
        filterCenterByZone(zone.id);        
        $http.get('/api/state/zone/'+zone.id).then(function(response){
            $scope.states = response.data;
            // $scope.states.unshift({id:'All',name:'All'});
        });
        
    };

    $scope.getCities = function(state){
        $http.get('/api/city/state/'+state.id).then(function(response){
            $scope.cities = response.data;
            // $scope.cities.unshift({id:'All',name:'All'});
        });
        filterCenterByState(state.name);
    };

    function filterCenterByZone(id){
        $scope.centerList = $scope.centerListCopy;
        $scope.centerList = $scope.centerListCopy.filter(element => {
            return (element.zone.id == id || element.name == 'All');
        });
        $scope.selectedState = '';
    }

    function filterCenterByState(name){
        $scope.centerList = $scope.centerListCopy.filter(element => {
            return (element.state == name || element.name == 'All');
        });
        $scope.selectedCity = '';
    }

    $scope.filterCenterByCity = function(city){
        $scope.centerList = $scope.centerListCopy.filter(element => {
            return (element.address.city == city.name || element.name == 'All');
        });
        $scope.selectedCenter = '';
    }



    $scope.loadHolidays = function () {
        loadHolidays();
    };

    function loadHolidays() {
        var filter = {};
        // if ($scope.selectedCenter && $scope.selectedCenter.id) {
        //     filter.centerId = $scope.selectedCenter.id;
        // }
        if ($scope.selectedMonth) {
            filter.month = $scope.selectedMonth;
        }
        if (!$scope.selectedYear) {
            error("Please select year.");
            return;
        }
        filter.year = $scope.selectedYear;

        $http.post('/api/holiday/', filter).then(function (response) {
            $scope.holidays = response.data;
            $scope.currentPage = 0;
            $scope.updatePageBar();
        });
    }

    loadHolidays();

    $scope.showHoliday = function (holiday, mode) {
        $scope.show = true;
        $scope.add = false;
        $scope.edit = false;
        $scope.mode = mode;
        $scope.holiday = holiday;
    };

    $scope.delHoliday = function (holiday) {
        $http.delete('/api/holiday/' + holiday.id).then(
            function successCallback(response) {
                $scope.show = false;
                $scope.add = false;
                $scope.edit = false;
                loadHolidays();
            },
            function errorCallback(response) {
                error("Error deleting the holiday");
            });
    };

    $scope.editHoliday = function (holiday) {
        $scope.holiday = angular.copy(holiday);

        $scope.show = false;
        $scope.add = false;
        $scope.edit = true;
        $scope.mode = "Edit";
    };

    $scope.addHoliday = function () {
        $scope.holiday = {};
        $scope.holiday.optional = false;
        $scope.holiday.floating = false;
        $scope.holiday.centers = [];
        $scope.selectedCenter = "";
        $scope.show = false;
        $scope.add = true;
        $scope.edit = false;
        $scope.mode = "Add";
    };

    $scope.addCenter = function (center) {
        if ($scope.holiday) {

            if(center.code === 'All'){
                $scope.holiday.centers = [];
                $scope.centerList.forEach(element => {
                    $scope.holiday.centers.push(element);
                });
                $scope.holiday.centers.splice(0,1);
                $scope.holiday.selectedCenter = '';
                return;
            }

            var alreadyThere = false;
            for (var i = 0; i < $scope.holiday.centers.length; i++) {
                if ($scope.holiday.centers[i].code == center.code) {
                    alreadyThere = true;
                }
            }

            if (!alreadyThere) {
                $scope.holiday.centers.push(center);
            }
        }
    };

    $scope.removeCenter = function (index) {
        if ($scope.holiday) {
            $scope.holiday.centers.splice(index, 1);
        }
    };

    $scope.cancelHoliday = function () {
        $scope.show = false;
        $scope.add = false;
        $scope.edit = false;
        $scope.holiday = {};
        $scope.selectedCenter = "";
    };

    $scope.saveHoliday = function () {
        var holiday = angular.copy($scope.holiday);
        if (holiday.centers != null
            && holiday.centers != ""
            && holiday.centers != "undefined") {
            var centerCodeList = [];
            for (var count = 0; count < holiday.centers.length; count++) {
                centerCodeList.push(holiday.centers[count].id);
            }
            holiday.centers = centerCodeList;
        } else {
            error("Please select atleast one center");
            return;
        }
        $http.post('/api/holiday/save', holiday).then(
            function successCallback(response) {
                loadHolidays();
                $scope.holiday = {};
                $scope.selectedCenter = "";
                $scope.add = false;
                $scope.edit = false;
                $scope.show = false;
                ok("Holiday Successfully saved");
            }, function errorCallback(response) {
                error("Error saving holiday");
            });
    };

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

    function initDateTimePicker() {
        var datepicker = $('.datepicker');
        datepicker.datetimepicker({
            format: 'YYYY-MM-DD',
            useCurrent: true,
            showTodayButton: true
        });

        datepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });

        $("#holidayDate").on("dp.change", function () {
            if ($scope.holiday) {
                $scope.holiday.holidayDate = $("#holidayDate").val();
            }
        });
    }

    $rootScope.$on('$includeContentLoaded', function (event, url) {
        initDateTimePicker();
    });
});
app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        return typeof input == 'undefined' ? [] : input.slice(start);
    }
});