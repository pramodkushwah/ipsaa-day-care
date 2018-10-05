app.controller('FeeReportController', function ($scope, $http) {
    $scope.months = moment.months();
    $scope.years = [moment().year() - 2, moment().year() - 1, moment().year()];
    $scope.disableDownload = false;

    $scope.quarters = [
        { value: 1, name: "FYQ4" },
        { value: 2, name: "FYQ1" },
        { value: 3, name: "FYQ2" },
        { value: 4, name: "FYQ3" }
    ];
    $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
    
    $scope.download = function () {
        if (!($scope.selectedCenter && $scope.selectedCenter.id)) {
            error("Please select Center.");
            return;
        }

        if (!($scope.selectedQuarter && $scope.selectedQuarter.value)) {
            error("Please select Quarter.");
            return;
        }

        if (!($scope.selectedYear)) {
            error("Please select Year.");
            return;
        }


        var request = {};
        request.centerCode = $scope.selectedCenter.code;
        request.quarter = $scope.selectedQuarter.value;
        request.year = $scope.selectedYear;

        $scope.disableDownload = true;
        $http.post('/api/report/studentfee/excel', request, {responseType: 'arraybuffer'}).then(
            function (response) {
                $scope.disableDownload = false;
                var blob = new Blob([response.data], {
                    type: 'application/octet-stream'
                });
                saveAs(blob, response.headers("fileName"));
            }, function (response) {
                $scope.disableDownload = false;
                error(response.data.error);
            }
        );

    };


    function loadCenters() {
        $http.get('/api/center/').then(
            function (response) {
                $scope.centers = response.data;
                $scope.centers.unshift({code:'All', name:'All', id:'All'});
            }, function (response) {
                error('Fail to centers.');
            }
        );
    }

    function refresh() {
        loadCenters();
    }

    refresh();

    function ok(message) {
        swal({title: message, type: 'success', buttonsStyling: false, confirmButtonClass: "btn btn-warning"});
    }

    function error(message) {
        swal({title: message, type: 'error', buttonsStyling: false, confirmButtonClass: "btn btn-warning"});
    }
});