app.controller('IpsaaclubslipController', function ($scope, $http, Auth) {

  $scope.selectedCenter = {};
  $scope.downloadPdfDisable = false;
  $scope.STUDENTFEE_WRITE = Auth.hasPrivilege('STUDENTFEE_WRITE');
  $scope.STUDENTFEE_RECEIPT_CONFIRM = Auth.hasPrivilege('STUDENTFEE_RECEIPT_CONFIRM');
  $http.get('/api/center/').then(function (response) {
    $scope.centers = response.data;
  });

  $scope.getGeneratedFeeSlips = function () {
    if ($scope.selectedCenter) {
      $http.post('/api/student/ipsaaclub/feeslip/list', { 'centerCode': $scope.selectedCenter.code }).then(function (response) {
        $scope.generatedFeeSlips = response.data;
      })
    }
  }

  $scope.selected = {};

  $scope.loadStudentFee = function (studentFee) {
    $scope.selectedStudentFee = studentFee;
  }

  $scope.showPayNow = function (studentFee) {
    console.log(studentFee);
    $scope.selected = angular.copy(studentFee);
    $scope.selected.paymentDate = moment(new Date()).format("YYYY-MM-DD");
  }

  $scope.downloadSlip = function (slip) {
    console.log(slip);
    $scope.downloadPdfDisable = true;
    $http.post('/api/student/ipsaaclub/feeslips/pdf', [slip.id], { responseType: 'arraybuffer' }).then(
      function (response) {
        $scope.downloadPdfDisable = false;
        var blob = new Blob([response.data], {
          type: 'application/octet-stream'
        });
        saveAs(blob, response.headers("fileName"));
      }, function (response) {
        $scope.downloadPdfDisable = false;
        error(response.data.error);
      }
    );
  };
  $scope.saveSlip = function (slip) {
    if ($scope.selectedCenter) {
      $http.post('/api/student/ipsaaclub/slip/update', {
        extraCharge: $scope.selectedStudentFee.extraCharge,
        id: slip.id
      }).then(function (response) {
        ok('Extra Charges saved, Now you have to pay');
      })
    }
  }
  $scope.downloadRecipt = function (payment) {
    $scope.downloadPdfDisable = true;
    $http.get('/api/student/download/ipssaclub/receipt/' + payment.recordId, {}).then(
      function (response) {
        $scope.downloadPdfDisable = false;
        var blob = new Blob([response.data], {
          type: 'application/octet-stream'
        });
        saveAs(blob, response.headers("fileName"));
      }, function (response) {
        $scope.downloadPdfDisable = false;
        error(response.data.error);
      }
    );
  };


  $scope.payFee = function (insertStudentFee) {
    $http.post('/api/student/ipsaaclub/generate/' + insertStudentFee.studentId, {}).then(function (response) {
      ok('Student Fee generated');
      $scope.getGeneratedFeeSlips();
    }, function (error) {
      error('Somthing went wrong');
    })
  }

  $scope.cancelStudentSlip = function () {
    $scope.showPanel = false;
    $scope.selected = {};
  };

  $scope.payStudentFee = function () {
    var fee_diff = $scope.selected.paidAmount - $scope.selected.payableAmount;
    if(!($scope.selected && $scope.selected.paidAmount)){
      error("Please enter paid amount.");
      return ;
    }
    if(fee_diff > 0){
      swal({
        type: 'warning',
        title: 'Confirmation',
        text: 'Your are about to pay extra amount of Ruppes '+ fee_diff + '.',
        confirmButtonText: 'Proceed',
        showCancelButton: 'true'
      }).then( function() {
          //proceed callback
          $scope.feePaymentRequest();
        }, function (cancel) {}
      );
    } else {
      $scope.feePaymentRequest();
    }
  };

  $scope.feePaymentRequest = function() {
    $scope.disabledRecordPayment = true;
    $http.post('/api/student/ipsaaclub/payfee', $scope.selected).then(function (response) {
      $('#myModal').modal('toggle');
      $scope.selectedStudentFee.payments.push(response.data);
      $scope.disabledRecordPayment = false;
      ok("Successfully applied payment");
      $scope.getGeneratedFeeSlips();
    }, function (response) {
      $scope.disabledRecordPayment = false;
      error(response.data.error);
    });
  }

  $scope.addExtraCharges = function (){
    $scope.selectedStudentFee.totalFee = $scope.selectedStudentFee.finalFee;
    $scope.selectedStudentFee.totalFee += $scope.selectedStudentFee.extraCharge;
    
  }

  $scope.downloadReceipt = function (receipt) {
    if (receipt && receipt.id) {
        $scope.disabledReceiptDownload = true;
        $http.get('/api/student/download/ipsaaclub/receipt/'+ receipt.id,{responseType: 'arraybuffer'}).then(
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

  $scope.confirm=function(payment){
    $http.put('/api/student/ipsaaclub/record/update', {
          confirmed: true,
          id: payment.id          
        }).then(function (response) {
          $.extend(payment,response.data);
          ok('Payment Confirmed');
        })
  }

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

  function error(message) {
    swal({
      title: 'Error',
      text: message,
      type: 'error',
      buttonsStyling: false,
      confirmButtonClass: "btn btn-warning"
    });
  }

  function ok(message) {
    swal({
      title: 'Success',
      text: message,
      type: 'success',
      buttonsStyling: false,
      confirmButtonClass: "btn btn-warning"
    });
  }
});