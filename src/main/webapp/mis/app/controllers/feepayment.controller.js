app.controller('StudentFeePaymentController', function ($scope, $http) {
    $scope.selectedYear = new Date().getFullYear().toString();
    $scope.disabledRecordPayment=false;
    var allmonths = moment.months();

    $scope.selectedYear = moment().year();
    $scope.showYear = true;
    $scope.selected = {};

    $scope.years = [moment().year() - 1,moment().year()];

    $scope.months = [];
    for (var mnth = 0 ; mnth <= 11; mnth++) {
        $scope.months.push({moy: mnth + 1, name: allmonths[mnth]});
    }

    $scope.quarters = [
        {value: 1, name: "FYQ4"},
        {value: 2, name: "FYQ1"},
        {value: 3, name: "FYQ2"},
        {value: 4, name: "FYQ3"}
    ];

    $http.get('/api/center/').then(function (response) {
        $scope.centers = response.data;
    });

    $scope.showDetails = function (studentfee) {
        if (!studentfee) {
            $scope.showPanel = false;
            $scope.selected = {};
        } else {
            $scope.showPanel = true;
            $scope.selected = angular.copy(studentfee);
        }
    };

    $scope.getFeeSlips = function () {

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

        var postobject = {
            centerCode: $scope.selectedCenter.code,
            period: $scope.selectedPeriod
        };

        if($scope.selectedPeriod == 'Monthly') postobject.month = $scope.selectedMonth ? $scope.selectedMonth : 0;
        else if($scope.selectedPeriod == 'Quarterly') postobject.quarter = $scope.selectedQuarter.value ? $scope.selectedQuarter.value: 0;

        postobject.year  = $scope.selectedYear;

        $http.post('/api/student/feeslip/list', postobject).then(function (response) {
            $scope.studentfeelist = response.data;
            $scope.showDetails(null)
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.cancelStudentSlip = function () {
        $scope.showPanel = false;
        $scope.selected = {};
    };

    $scope.payStudentFee = function () {
        if(!($scope.selected && $scope.selected.paidAmount)){
            error("Please enter paid amount.");
            return ;
        }
        $scope.disabledRecordPayment=true;
        $http.post('/api/student/payfee', $scope.selected).then(function (response) {
            $scope.getFeeSlips();
            $scope.disabledRecordPayment=false;
            ok("Successfully applied payment")
        }, function (response) {
            $scope.disabledRecordPayment=false;
            error(response.data.error);
        });
    };

    $scope.downloadReceipt = function (receipt) {
        if (receipt && receipt.id) {
            $scope.disabledReceiptDownload = true;
            $http.get('/api/student/download/receipt/'+ receipt.id,{responseType: 'arraybuffer'}).then(
                function (response) {
                    $scope.disabledReceiptDownload = false;
                    var blob = new Blob([response.data], {
                        type: 'application/octet-stream'
                    });
                    saveAs(blob, response.headers("fileName"));
                }, function (response) {
                    $scope.disabledReceiptDownload = false;
                    error(response.data.error);
                }
            );
        }
    };

    $scope.confirm = function (receipt) {
        if (receipt && receipt.id) {
            receipt.disabled = true;
            $http.put('/api/student/payfee', {id: receipt.id, confirmed: true}).then(
                function (response) {
                    receipt.disabled = false;
                    $scope.getFeeSlips();
                    ok("success");
                }, function (response) {
                    receipt.disabled = false;
                    error(response.data.error);
                }
            );
        }
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

});


