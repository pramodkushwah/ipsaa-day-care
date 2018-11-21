app.controller('IpsaaClubReportController', function ($scope, $http) {
  var allmonths = moment.months();
  $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
  $scope.months = [];
  $scope.selectedCenter = null;
  $scope.selectedMonth = null;
  $scope.selectedYear = null;

  $http.get('/api/center/').then(function (response) {
    $scope.centers = response.data;
    $scope.centers.unshift({code:'All', name:'All', id:'All'});
  }, function (response) {
      error('Fail to centers.');
  });

  for (var i = 0; i < allmonths.length; i++) {
      var month = allmonths[i];
      $scope.months.push({ moy: i + 1, name: allmonths[i] });
  };


  $scope.downloadFee = function(){
    if(validate()){
      const request = {
        centerCode:$scope.selectedCenter.name,
        month:$scope.selectedMonth.moy,
        year:$scope.selectedYear
      }
      console.log(request);
      $scope.disableDownloadFee = true;
      $http.post('/api/report/ipsaaclub/studentfee/excel', request, {responseType: 'arraybuffer'}).then(
          function (response) {
              $scope.disableDownloadFee = false;
              var blob = new Blob([response.data], {
                  type: 'application/octet-stream'
              });
              saveAs(blob, response.headers("fileName"));
          }, function (response) {
              $scope.disableDownloadFee = false;
              error(response.data.error);
          }
      );
    }
  };

  $scope.downloadCollection = function(){
    if(validate()){
      const request = {
        centerCode:$scope.selectedCenter.name,
        month:$scope.selectedMonth.moy,
        year:$scope.selectedYear
      }
      console.log(request);
      $scope.disableDownloadCollection = true;
      $http.post('/api/report/ipsaaclub/collectionfee/excel', request, {responseType: 'arraybuffer'}).then(
          function (response) {
              $scope.disableDownloadCollection = false;
              var blob = new Blob([response.data], {
                  type: 'application/octet-stream'
              });
              saveAs(blob, response.headers("fileName"));
          }, function (response) {
              $scope.disableDownloadCollection = false;
              error(response.data.error);
          }
      );
    }
  };

  function validate() {
    if(!$scope.selectedCenter){
      error('Select Center !!');
      return false;
    }
    if(!$scope.selectedMonth){
      error('Select Month !!');
      return false;
    }
    if(!$scope.selectedYear){
      error('Select Year !!');
      return false;
    }
    if($scope.selectedCenter&&$scope.selectedMonth&&$scope.selectedYear){
      return true;
    }else {
      return false;
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