app.controller('StudentApprovalController', function ($scope, $http, $stateParams, $state) {
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
    var tabs = ['#studentprofile', '#photo', '#fatherprofile', '#motherprofile', '#fee'];
    $scope.selectTab = function (index) {
      for (var i = 0; i < tabs.length; i++) {
          var tab = tabs[i];
          if (tab === tabs[index]) {
              $(tab).addClass("active");
          } else {
              $(tab).removeClass("active");
          }
      }
      $('[data-toggle="tooltip"]').tooltip();  
      $scope.studentFee.finalBaseFee = $scope.studentFee.finalBaseFee / 3;
      $scope.studentFee.finalTransportFee = $scope.studentFee.transportFee * 3;
    };
    $scope.id = $stateParams.id;
    $scope.workingStudent = {};
    $scope.studentFee = {};
    $scope.disableApprove = false;
    $scope.disableReject = false;
    $scope.hasFee=false;

    $scope.approveStudent = function (student) {
        if (student) {
            $scope.disableApprove=true;
            $scope.disableReject=true;
            $http.get('/api/student/approve/'+student.id).then(
                function (response) {
                    ok("Student Approved");
                    loadStudent();
                    $scope.disableApprove=false;
                    $scope.disableReject=false;
                    $state.go('app.student_approvals',{centerId:$scope.workingStudent.center.id});
                },
                function (response) {
                    error(response.data.message);
                    $scope.disableApprove=false;
                    $scope.disableReject=false;
                }
            );
        }
    };
    $scope.rejectStudent = function (student) {
        if (student) {
            $scope.disableApprove=true;
            $scope.disableReject=true;
            $http.get('/api/student/reject/'+student.id).then(
                function (response) {
                    ok("Student Rejected");
                    loadStudent();
                    $scope.disableApprove=false;
                    $scope.disableReject=false;
                    $state.go('app.student_approvals',{centerId:$scope.workingStudent.center.id});
                },
                function (response) {
                    error(response.data.message);
                    $scope.disableApprove=false;
                    $scope.disableReject=false;
                }
            );
        }
    };


    function loadStudent() {
        $http.get('/api/student/' + $stateParams.id).then(
            function (response) {
                $scope.workingStudent = response.data;
                loadFee($scope.workingStudent);
                if ($scope.workingStudent.parents) {
                    for (var i = 0; i < $scope.workingStudent.parents.length; i++) {
                        var parent = $scope.workingStudent.parents[i];
                        $scope.workingStudent[parent.relationship.toLowerCase()] = parent;
                    }
                }
                if ($scope.workingStudent.imagePath==null) {
                    $scope.workingStudent.imgPath = '/assets/img/default-avatar.png'
                } else {
                    $scope.workingStudent.imgPath = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/' + $scope.workingStudent.imagePath;
                }
            }, function (response) {
                // TODO : handel error in fetching student
            }
        );
    }

    function loadFee(student) {
        if (student) {
            $http.get('/api/student/fee/' + student.id).then(
                function (response) {
                    $scope.studentFee = response.data;
                    if($scope.studentFee.admissionNumber==null){
                        $scope.hasFee=false;
                    }else{
                        $scope.hasFee=true;
                    }
                },
                function (response) {
                    // TODO : Error fetching fee
                }
            );
        }
    }

    function refresh() {
        loadStudent();
    }
    refresh();
});