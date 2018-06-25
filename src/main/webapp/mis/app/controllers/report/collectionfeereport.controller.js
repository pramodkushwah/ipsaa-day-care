app.controller('CollectionFeeReportController', function ($http, $scope) {

    $scope.disableDownload = false;

    //populate months, years, quarter dropdown
    $scope.quarters = [
        {value: 1, name: "FYQ4"},
        {value: 2, name: "FYQ1"},
        {value: 3, name: "FYQ2"},
        {value: 4, name: "FYQ3"}
    ];
    var allmonths = moment.months();
    $scope.months = [];
    $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
    for (var i = 0; i < allmonths.length; i++) {
        var month = allmonths[i];
        $scope.months.push({moy: i + 1, name: allmonths[i]});
    }

    //set current month, quarter and year





    $scope.downloadReport = function () {
        if (!$scope.selectedCenter) {
            error("Select Center");
            return;
        }

        if (!$scope.selectedPeriod) {
            error("Select Period");
            return;
        }

        if ($scope.selectedPeriod === 'Monthly' && !$scope.selectedMonth) {
            error("Select Month");
            return;
        }

        if ($scope.selectedPeriod === 'Quarterly' && !$scope.selectedQuarter) {
            error("Select Quarter");
            return;
        }

        if ($scope.selectedPeriod === 'Yearly' && !$scope.selectedYear) {
            error("Select Year");
            return;
        }

        if (!$scope.reportType) {
            error("Select Report Type");
            return;
        }


        var postobject = {
            centerCode: $scope.selectedCenter.code,
            period: $scope.selectedPeriod,
            reportType: $scope.reportType
        };

        if($scope.selectedPeriod == 'Monthly') postobject.month = $scope.selectedMonth ? $scope.selectedMonth : 0;
        else if($scope.selectedPeriod == 'Quarterly') postobject.quarter = $scope.selectedQuarter.value ? $scope.selectedQuarter.value: 0;

        postobject.year  = $scope.selectedYear;
        $scope.disableDownload = true;
        $http.post('/api/report/collectionfee',postobject, {responseType: 'arraybuffer'}).then(
            function (response) {
                $scope.disableDownload = false;
                var blob = new Blob([response.data], {
                    type: 'application/octet-stream'
                });
                saveAs(blob, response.headers("fileName"));
            },function (response) {
                $scope.disableDownload = false;
                error(response.data.error);
            }
        );


    };

    function loadCenters() {
        $http.get('/api/center/').then(
            function (response) {
                $scope.centers = response.data;
                if (Array.isArray($scope.centers) && $scope.centers.length == 1) {
                    $scope.selectedCenter = $scope.centers[0];
                }
            }, function (response) {
                error(response.data.error);
            }
        );
    }

    function refresh() {
        loadCenters();
    }

    refresh();
});