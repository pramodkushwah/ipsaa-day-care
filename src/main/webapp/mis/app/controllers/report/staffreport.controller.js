app.controller('staffReportController', function ($http, $scope, Upload) {
  $scope.disableGenerateButton = false;
  $scope.employers = [];
  var allmonths = moment.months();
  $scope.months = [];
  for (var mnth = 0; mnth <= 11; mnth++) {
      $scope.months.push({moy: mnth + 1, name: allmonths[mnth]});
  }

  $http.get('/api/le/').then(function (response) {

    $scope.employers.push({ id: "ALL", code: 'ALL', name: 'ALL' });
    response.data.forEach(emp => {
      $scope.employers.push(emp);
    });
  });

  $scope.generateReport = function () {
    if (!$scope.selectedEmployer) {
      error("Select Employer");
      return false;
    }

    $scope.disableGenerateButton = true;
    $scope.salaries = [];

    var req_body = {
      employerCode: $scope.selectedEmployer.code,
      month: $scope.selectedMonth
    }

    $http.post("/api/report/staff/excel", req_body, {
      responseType: 'arraybuffer'
    }).then(function (response) {
      $scope.disableGenerateButton = false;
      var blob = new Blob([response.data], {
        type: 'application/octet-stream'
      });
      saveAs(blob, response.headers("fileName"));
      ok('Staff Report Generated');
    }, function (response) {
      $scope.disableGenerateButton = false;
      error('Unbale to generate Staff Report');
    });
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