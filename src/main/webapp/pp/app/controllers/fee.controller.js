app.controller('FeeController', function ($scope, $http, $sce) {

    $scope.details = true;

    $scope.feeledger = true;
    $scope.showtime = false;
    $scope.payuok = false;
    var keepitdown = true;
    $scope.disabledDownloadFeeSlip = false;
    $scope.disabledDownloadFeeReceipt = false;
    $scope.selectedStudent = {};

    $scope.students = [];

    loadStudents();
    refresh($scope.selectedStudent);

    $scope.showDetails = function () {
        $scope.details = true;
        $scope.feeledger = true;
    };

    function refresh(student) {
        if (student.id) {
            $http.get('/api/pp/student/fee/' + student.id).then(function (response) {
                $scope.fee = response.data;
                $scope.details=true;
            },function (response) {
                $scope.fee={};
                $scope.details=false;
            });
            $http.get('/api/pp/student/feeledger/' + student.id).then(function (response) {
                $scope.studentfeeledger = response.data;
                $scope.showtime = keepitdown;
                $http.get('/api/pp/student/mydetails').then(
                    function (response) {
                        $scope.myDetails = response.data;
                        $http.get('/hdfc/checkout/' + $scope.studentfeeledger.id + '/' + $scope.myDetails.id).then(
                            function (response) {
                                $scope.checkoutDetails = response.data;
                            }, function (response) {
                                error(response.data.error);
                            }
                        );
                    }, function (response) {
                        error(response.data.error);
                    }
                );
            },function (response) {
                $scope.studentfeeledger={};
                $scope.feeledger=false;
                error(response.data.error);
            });
        }
    }

    $scope.studentChanged = function (student) {
        refresh(student);
    };

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students = response.data;
            if ($scope.students.length > 0) {
                $scope.selectedStudent = $scope.students[0];
                refresh($scope.selectedStudent);
            }
        });
    }

    $scope.checkout = function () {
        // $("#checkout").attr('action',$scope.checkoutDetails.transactionUrl).submit();

        $("#checkout")
            .attr('action',$scope.checkoutDetails.checkoutDetailsUrl+'/'+$scope.studentfeeledger.id + '/' + $scope.myDetails.id)
            .attr('target','_blank')
            .attr('method','get')
            .submit();

    };

    $scope.downloadFeeSlip = function (id) {
        $scope.disabledDownloadFeeSlip = true;
        $http.get('/api/pp/student/feeslip/' + id).then(
            function (response) {
                $scope.disabledDownloadFeeSlip = false;
                var numbers = response.data.bytes;
                var bytes = new Uint8Array(numbers.length);
                for (var i = 0; i < numbers.length; i++) {
                    bytes[i] = numbers[i];
                }
                var file = new Blob([bytes], {type: 'application/pdf'});
                saveAs(file, response.data.fileName);
            }, function (response) {
                $scope.disabledDownloadFeeSlip = false;
                error(response.data.error);
            }
        );
    };

    $scope.downloadFeeReceipt = function (id) {
        $scope.disabledDownloadFeeReceipt = true;
        $http.get('/api/pp/student/feereceipt/' + id).then(
            function (response) {
                $scope.disabledDownloadFeeReceipt = false;
                var numbers = response.data.bytes;
                var bytes = new Uint8Array(numbers.length);
                for (var i = 0; i < numbers.length; i++) {
                    bytes[i] = numbers[i];
                }
                var file = new Blob([bytes], {type: 'application/pdf'});
                saveAs(file, response.data.fileName);
            }, function (response) {
                $scope.disabledDownloadFeeReceipt = false;
                error(response.data.error);
            }
        );
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
