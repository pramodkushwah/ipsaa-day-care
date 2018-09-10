app.controller('MonthlySalaryController', function ($scope, $http) {
    $scope.disableGenerateButton = false;
    $scope.disableUpdateBtn = false;
    $scope.regenerateRecords = {};
    $scope.years = [];

    $http.get('/api/le/').then(function (response) {
        $scope.employers = response.data;
        $scope.employers.push({code: 'ALL', name: 'ALL', id: 'ALL'});
    });

    $scope.generate = function (month, year) {
        if (!$scope.selectedEmployer) {
            error("Select Employer");
            return false;
        }
        if (!year) {
            error("Select Year");
            return false;
        }
        if (!month) {
            error("Select Month");
            return false;
        }
        $scope.year = year;
        $scope.month = month.moy;
        $scope.disableGenerateButton = true;
        $scope.salaries = [];
        $http.post("/api/employee/payslip?month=" + month.value + "&year=" + year.value + "&employerId=" + $scope.selectedEmployer.id).then(function (response) {
            $scope.disableGenerateButton = false;
            $scope.salaries = response.data;
        }, function (response) {
            $scope.disableGenerateButton = false;
        });
    };

    $scope.regenerateAllPaySlip = function(){
        const requestObject = {}
        const regeneratedRecordIds = []
        Object.keys($scope.regenerateRecords).forEach((key)=>{
            if($scope.regenerateRecords[key]) {
                regeneratedRecordIds.push(key);
            }
        })
        requestObject.month = $scope.selectedMonth.value;
        requestObject.year = $scope.selectedYear.value;
        requestObject.ids = regeneratedRecordIds;
        console.log(requestObject);

        $http.post('/api/employee/payslip/regenerateall/', requestObject).then(function(response){
            ok("All selected Payslip regenerated successfully.");
            $scope.regenerateRecords = {};
        }, function(error){
            error(response.data.error);
        })
        
    }

    $scope.showSlip = function (salary) {
        $scope.paySlip = angular.copy(salary);
        $scope.paySlip.mode = "edit";
    };

    $scope.regeneratePaySlip = function (paySlip) {
        paySlip.disableRegenerateBtn = true;
        $http.put('/api/employee/payslip/regenerate/', paySlip).then(
            function (response) {
                paySlip.disableRegenerateBtn = false;
                $scope.paySlip = {};
                ok("Payslip regenerated successfully.");
                $scope.generate($scope.selectedMonth, $scope.selectedYear);
            }, function (response) {
                paySlip.disableRegenerateBtn = false;
                error(response.data.error);
            }
        );
    };

    $scope.update = function (paySlip) {
        paySlip.disableUpdateBtn = true;
        $http.put('/api/employee/payslip/', paySlip).then(
            function (response) {
                paySlip.disableUpdateBtn = false;
                $scope.paySlip = {};
                $scope.generate($scope.selectedMonth, $scope.selectedYear);
                ok("Payslip regenerated successfully.");
            }, function (response) {
                paySlip.disableUpdateBtn = false;
                error(response.data.error);
            }
        );
    };

    function init() {
        var now = moment();
        var month = now.month();
        var year = now.year();
        var allMonths = moment.months();

        var obj = {value: year - 1, months: []};
        $scope.years.push(obj);
        for (var i = 0; i < allMonths.length; i++) {
            var m = allMonths[i];
            obj.months.push({
                name: m,
                value: i + 1
            });
        }

        obj = {value: year, months: []};
        $scope.years.push(obj);
        for (var i = 0; i <= month; i++) {
            var m = allMonths[i];
            obj.months.push({
                name: m,
                value: i + 1
            });
        }
    }

    init();

    $scope.downloadPaySlip = function (salary) {
        salary.disableDownloadBtn = true;
        $http.get('/api/employee/payslip/pdf/' + salary.id, {
            responseType: 'arraybuffer'
        }).then(
            function (response) {
                salary.disableDownloadBtn = false;
                var blob = new Blob([response.data], {
                    type: 'application/octet-stream'
                });
                saveAs(blob, response.headers("fileName"));
            },
            function (response) {
                salary.disableDownloadBtn = false;
                error(response.data.error);
            }
        );
    };

    $scope.lockControls = function(paySlip) {

        // if salary lock set from backend then disable lock button
        if(paySlip.islock) {          
            error("Salary already locked");    
            return false;
        }

        $http.put('/api/employee/payslip/lock', {
            id: paySlip.id,
            lock : true
        }).then(
            function (response) {
                ok('Salary Slip locked');
                paySlip.islock = true;
                
                // lock for the salary table entry
                $scope.salaries.find(function(salary){
                    if(salary.id == paySlip.id)
                        salary.islock = true;
                });
            },
            function (response) {
                error(response.data.error);
            }
        );
    }

    $scope.openFileExplorer = function(){
        $('#file-tag').click();
    }

    $scope.uploadExcel = function (element) {
            $scope.excelFile = element.files[0];
        console.log("asdfdsf", $scope.excelFile);
        var formData = new FormData();
        formData.append('employerId',$scope.selectedEmployer.id);
        formData.append('year',$scope.selectedYear.value);
        formData.append('month',$scope.selectedMonth.value);
        formData.append('file',$scope.excelFile);
        $http.put('/api/employee/payslip/upload',formData,{
            headers: {'Content-Type': undefined}
        }).then(function(response){
            console.log(response);
        },function(error){
            console.log(error);
        });
    }

    function error(message) {
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-danger"
        });
    }

    function ok(message) {
        swal({
            title: message,
            type: 'success',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-success"
        });
    }
});