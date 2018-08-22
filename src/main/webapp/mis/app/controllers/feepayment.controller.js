app.controller('StudentFeePaymentController', function ($scope, $http) {

    $scope.disabledRecordPayment=false;
    var allmonths = moment.months();

    $scope.selectedYear = moment().year();
    $scope.showYear = true;
    $scope.showFeeDetails = false;
    $scope.selectedPeriod = 'Quarterly';
    $scope.commentField = false;
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

    $scope.feeTypes = [
      { type: 'Uniform Charges', final: 'uniformCharges', paid: 'uniformPaidAmountTotal' },
      { type: 'Stationery Charges', final: 'stationary', paid: 'stationaryPaidAmountTotal' },
      { type: 'Transport Fees', final: 'transportFee', paid: 'transportPaidAmountTotal' },
      { type: 'Annual Charges', final: 'finalAnnualCharges', paid: 'annualPaidAmountTotal' },
      { type: 'Admission Charges', final: 'finalAdmissionFee', paid: 'addmissionPaidAmountTotal' },
      { type: 'Base Fee', final: 'finalBaseFee', paid: 'programPaidAmountTotal' },
      { type: 'Security Deposite Fee', final: 'finalDepositFee', paid: 'depositPaidAmountTotal'}
      
    ];

  $scope.toggleHide = function() {
    $scope.showFeeDetails = !$scope.showFeeDetails;
  }


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
            setTimeout(() => {
              $('[data-toggle="tooltip"]').tooltip();  
            }, 1000);
        }
    };

    $scope.getFeeSlips = function () {

        if (!$scope.selectedCenter) {
            error("Select Center");
            return;
        }

        if (!$scope.selectedQuarter) {
            error("Select Quarter");
            return;
        }

        if (!$scope.selectedYear) {
            error("Select Year");
            return;
        }

        var postobject = {
            centerCode: $scope.selectedCenter.code,
            period: $scope.selectedPeriod
        };

        postobject.quarter = $scope.selectedQuarter.value 
        ? $scope.selectedQuarter.value
        : 0;

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

    $scope.showCommentField = function(reciept) {
      var this_receipt = reciept;
      if (reciept && reciept.id) {
        swal({
          title: 'Confirm',
          type: 'warning',
          input: 'textarea',
          text: 'Please submit a comment to decline transaction',
          showCancelButton: true,
          inputValidator: value => {
            return new Promise((resolve, reject) => {
              if (value !== '') {
                resolve();
              } else {
                reject('Comment Required');
              }
            });
          }
        }).then(function(text) {
            if (text) {
              $http
                .put('/api/student/payfee', {
                  id: this_receipt.id,
                  confirmed: false,
                  comments: text
                })
                .then(function(response) {
                    $scope.getFeeSlips();
                    ok('success');
                  }, function(response) {
                    error(response.data.error);
                  });
            }
          }, function(cancel) {});
      }
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

});


