app.controller('hdfcgatewayfeereportController', function ($scope, $http) {
  $scope.quarters = [
    { value: 1, name: "FYQ4" },
    { value: 2, name: "FYQ1" },
    { value: 3, name: "FYQ2" },
    { value: 4, name: "FYQ3" }
  ];
  $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];

  $scope.quarterYear = {
    period: 'Quarterly',
    reportType: 'Paid'
  };

  $scope.getFeeByHDFC = function(){
    console.log($scope.quarterYear);
    $http.post('/api/report/collectionfee/hdfc', $scope.quarterYear, {
      responseType: 'arraybuffer'
    }).then(function(response){
      var blob = new Blob([response.data], {
        type: 'application/octet-stream'
      });
      saveAs(blob, response.headers("fileName"));
      ok('Hdfc Report Generated');
    },function(error){
      error('Something went wrong');
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