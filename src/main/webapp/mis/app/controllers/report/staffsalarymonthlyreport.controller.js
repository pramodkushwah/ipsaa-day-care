app.controller('staffSalaryMonthlyReportController', function ($http, $scope, Upload) {
    $scope.disableGenerateButton = false;
    var allmonths = moment.months();
    $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
    $scope.months = [];
    for (var i = 0; i < allmonths.length; i++) {
        var month = allmonths[i];
        $scope.months.push({ moy: i + 1, name: allmonths[i] });
    }

    $http.get('/api/le/').then(function (response) {
        $scope.employers = response.data;
    });

    $scope.generateReport = function() {
        if (!$scope.selectedEmployer) {
            error("Select Employer");
            return false;
        }
        if (!$scope.selectedMonth) {
            error("Select Month");
            return false;
        }
        if (!$scope.selectedYear) {
            error("Select Year");
            return false;
        }

        $scope.disableGenerateButton = true;
        $scope.salaries = [];
        
        var req_body = {
            
           employerCode: $scope.selectedEmployer.code,
           month: $scope.selectedMonth.moy,
           year: $scope.selectedYear
        }

        $http.post("/api/report/staffCollection/excel", req_body, {
            responseType: 'arraybuffer'
        }).then(function (response) {
            $scope.disableGenerateButton = false;
            var blob = new Blob([response.data], {
                type: 'application/octet-stream'
            });
            saveAs(blob, response.headers("fileName"));
            ok('Salary Sheet Generated');
        }, function (response) {
            $scope.disableGenerateButton = false;
            error('Unbale to generate salary sheet');
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

    function ok(message) {
        swal({
            title: message,
            type: 'success',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }
    

});