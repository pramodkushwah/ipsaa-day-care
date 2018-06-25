app.controller('FeeReportController', function ($scope, $http) {
    $scope.months = moment.months();
    $scope.years = [moment().year() - 2, moment().year() - 1, moment().year()];
    $scope.disableDownload = false;


    $scope.download = function (center) {
        if (!(center && center.id)) {
            error("Please select Center.");
            return;
        }
        var request = {};
        request.centerId = center.id;
        request.month = $scope.selectedMonth;
        request.year = $scope.selectedYear;

        $scope.disableDownload = true;
        $http.post('/api/report/studentfee', request, {responseType: 'arraybuffer'}).then(
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