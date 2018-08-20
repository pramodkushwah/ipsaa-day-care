app.controller('StudentFeeManagementController', function ($scope, $http, Auth, StudentFeeService) {
    $scope.studentIds = []; //Student id having fee already generated
    $scope.disableSave = false;
    $scope.STUDENTFEE_WRITE = Auth.hasPrivilege('STUDENTFEE_WRITE');
    $scope.showBreakdown = false;
    $scope.discountTypes = [];

    function debounce(func, wait, immediate) {
        var timeout;
        return function () {
            var context = this, args = arguments;
            var later = function () {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }

    $scope.newStudentFee = function () {
        $scope.addstudentfee = true;
        $scope.insertStudentFee = {
            mode: "Add",
            baseFee: 0,
            finalFee: 0,
            discount: 0,
            transportFee: 0,
            adjust: 0,
            comment: "",
            gstFee: 0,
            update: false,

        };
    };

    $scope.cancelStudentFee = function () {
        $scope.addstudentfee = false;
        $scope.insertStudentFee = {mode: "Add"};
    };

    $scope.calculateDiscount = function(base, final, targetDiscount) {
      if ($scope.insertStudentFee[base] > 0 && $scope.insertStudentFee[final]) {
        if ($scope.insertStudentFee[base] - $scope.insertStudentFee[final] > 0) {
          $scope.insertStudentFee[targetDiscount] =
          ((($scope.insertStudentFee[base] - $scope.insertStudentFee[final]) / $scope.insertStudentFee[base]) * 100).toFixed(2);
        }
        else {
          $scope.insertStudentFee[targetDiscount] = 0;
          $scope.insertStudentFee[final] = $scope.insertStudentFee[base];
        }
      } else {
        $scope.insertStudentFee[final] = 0;
        $scope.insertStudentFee[targetDiscount] = 100;
      }

      StudentFeeService.calculateGstFee($scope.insertStudentFee);
      StudentFeeService.calculateFinalFee($scope.insertStudentFee);
    }

    $scope.monthlyTransportFeesChanged = function(fee) {
      if(fee.transportFee > 0) {
        fee.finalTransportFees = fee.transportFee * 3;
      } else {
        fee.finalTransportFees = 0;
      }
      StudentFeeService.calculateFinalFee(fee);
    }

  $scope.monthlyUniformChargesChanged = function(fee) {
      StudentFeeService.calculateFinalFee(fee);
  }

  $scope.monthlyStationeryChargesChanged = function(fee) {
      StudentFeeService.calculateFinalFee(fee);
  }

    $scope.toggleBreakdown = function() {
      if(!$scope.showBreakdown)
        $scope.showBreakdown = true;
      else 
        $scope.showBreakdown = false;
    }

    function loadProgramFee(req, success, failure) {
        $http.post('/api/center/fee/', req).then(function (response) {
            success(response.data);
        }, function (response) {
            failure(response.data);
        });
    }

    $scope.getProgramFee = function () {
        if ($scope.insertStudentFee.program) {
            var req = {
                programId: $scope.insertStudentFee.program.id,
                centerId: $scope.selectedCenter.id
            };
            loadProgramFee(req, function (data) {
                $scope.insertStudentFee.baseFee = data.fee;
                $scope.insertStudentFee.sgst = data.sgst;
                $scope.insertStudentFee.cgst = data.cgst;
                $scope.insertStudentFee.igst = data.igst;
                $scope.insertStudentFee.discount = 0;
                $scope.insertStudentFee.transportFee = 0;
                $scope.insertStudentFee.adjust = 0;
                $scope.insertStudentFee.comment = "";
                $scope.insertStudentFee.feeDuration = "Quarterly";
                StudentFeeService.calculateFinalFee($scope.insertStudentFee);
                StudentFeeService.calculateGstFee($scope.insertStudentFee)
            }, function (data) {
                $scope.insertStudentFee.program = "";
                error("Program Fee not found!!");
            })
        } else {
            $scope.insertStudentFee.student = null;
            $scope.students = [];
            $scope.insertStudentFee.baseFee = 0;
            $scope.insertStudentFee.discount = 0;
            $scope.insertStudentFee.adjust = 0;
            $scope.insertStudentFee.finalFee = 0;
            $scope.insertStudentFee.comment = "";
            $scope.insertStudentFee.feeDuration = "Monthly";
        }
    };

    $scope.saveStudentFee = debounce(function () {
        $scope.disableSave = true;
        if ($scope.insertStudentFee.mode == "Add") {
            if (!$scope.insertStudentFee.program) {
                error("Please select Program.");
                $scope.disableSave = false;
                return;
            }
            if (!$scope.insertStudentFee.student) {
                error("Please select Student.");
                $scope.disableSave = false;
                return;
            }
            $scope.insertStudentFee.studentId = $scope.insertStudentFee.student.id;
            delete $scope.insertStudentFee.student;
            delete $scope.insertStudentFee.program;
            $http.post('/api/student/fee/', $scope.insertStudentFee).then(function (response) {
                $scope.addstudentfee = false;
                $scope.loadCenterStudentsFee();
                $scope.disableSave = false;
                ok("Student Fee saved!");
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        } else if ($scope.insertStudentFee.mode == "Edit") {
            delete $scope.insertStudentFee.student;
            delete $scope.insertStudentFee.program;
            delete $scope.insertStudentFee.center;
            delete $scope.insertStudentFee.group;
            $http.put('/api/student/fee/', $scope.insertStudentFee).then(function (response) {
                $scope.addstudentfee = false;
                $scope.disableSave = false;
                ok("Student Fee updated!");
                $scope.loadCenterStudentsFee();
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        }
    }, 200, true);

    $scope.loadStudentFee = function (fee, mode) {
        if (fee && fee.id) {
            $http.post('/api/student/fee/get', {id: fee.id}).then(
                function (response) {
                    $scope.insertStudentFee = response.data;
                    $scope.insertStudentFee.mode = !mode ? 'Show' : mode;
                    $scope.insertStudentFee.adjust = $scope.insertStudentFee.adjust ? $scope.insertStudentFee.adjust : 0;
                    StudentFeeService.initializeFee($scope.insertStudentFee);
                    StudentFeeService.calculateGstFee($scope.insertStudentFee);
                    StudentFeeService.calculateFinalFee($scope.insertStudentFee);
                    $scope.addstudentfee = true;
                },
                function (response) {
                    error(response.data.error)
                }
            );
        }
    };

    $scope.getStudents = function () {
        if ($scope.insertStudentFee.program) {
            var req = {
                programCode: $scope.insertStudentFee.program.code,
                centerCode: $scope.selectedCenter.code
            };
            $http.post('/api/student/filter', req).then(function (response) {
                $scope.students = response.data.students;
            });
        }
    };

    $http.get('/api/program').then(function (response) {
        $scope.programs = response.data;
    });

    $http.get('/api/center/').then(function (response) {
        $scope.centers = response.data;
    });

    $scope.loadCenterStudentsFee = function () {
        $scope.insertStudentFee = {};
        $scope.studentfeelist = [];
        $http.get('/api/student/fee?centerId=' + $scope.selectedCenter.id).then(function (response) {
            $scope.studentfeelist = response.data;
            angular.forEach($scope.studentfeelist, function (fee) {
                $scope.studentIds.push(fee.studentId);
            });
            $scope.cancelStudentFee();
        });
    };

    function ok(message) {
        swal({
            title: 'Success',
            text: message,
            type: 'success',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
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
});

app.filter('studentFilterFromFee', function () {
    return function (students, feeStudents) {
        var out = [];
        angular.forEach(students, function (student) {
            if (!feeStudents.includes(student.id))
                out.push(student);
        });
        return out;
    }
});