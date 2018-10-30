app.controller('IpsaaclubslipController', function ($scope, $http, Auth) {

  $scope.selectedCenter = {};
  $scope.downloadPdfDisable = false;
  $scope.STUDENTFEE_WRITE = Auth.hasPrivilege('STUDENTFEE_WRITE');
  $http.get('/api/center/').then(function (response) {
    $scope.centers = response.data;
  });

  $scope.getGeneratedFeeSlips = function(){    
    if($scope.selectedCenter) {
      $http.post('/api/student/ipsaaclub/feeslip/list', {'centerCode' : $scope.selectedCenter.code}).then(function(response){
        $scope.generatedFeeSlips = response.data;
      })
    }
  }


 $scope.loadStudentFee = function(studentFee){
    $scope.selectedStudentFee = studentFee;
  }

  $scope.showPayNow = function(studentFee){
    $scope.paynow = studentFee;
  }

  $scope.downloadSlip = function (slip) {
    console.log(slip);
        $scope.downloadPdfDisable = true;
        $http.post('/api/student/ipsaaclub/feeslips/pdf', [slip.id], {responseType: 'arraybuffer'}).then(
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
 $scope.saveSlip = function(slip){
    if($scope.selectedCenter) {
      $http.post('/api/student/ipsaaclub/slip/update', {

      extraCharge: $scope.selected.extraCharge,
      id:slip.id
      }).then(function(response){
        $scope.generatedFeeSlips = response.data;
      })
    }
  }
  $scope.downloadRecipt = function (payment) {
    console.log(payment);
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
    $scope.payFee = function(insertStudentFee) {
        $http.post('/api/student/ipsaaclub/generate/' + insertStudentFee.studentId, {}).then(function (response){
            ok('Student Fee generated');
        },function(error){
            error('Somthing went wrong');
        })
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