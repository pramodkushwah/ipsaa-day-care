app.controller('StaffApprovalController', function ($scope, $http, $stateParams,$state) {
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

    var tabs = ['#staffprofile', '#staffphoto', '#staffaddress', '#staffpermanentaddress', '#salary'];
    $scope.selectTab = function (index) {
        for (var i = 0; i < tabs.length; i++) {
            var tab = tabs[i];
            if (tab === tabs[index]) {
                $(tab).addClass("active");
            } else {
                $(tab).removeClass("active");
            }
        }
    }
    $scope.id = $stateParams.id;
    $scope.workingStaff = {};
    $scope.salary = {};
    $scope.disableApprove = false;
    $scope.disableReject = false;
    $scope.hasSalary = false;


    function loadStaff() {
        $http.get('/api/staff/' + $stateParams.id).then(
            function (response) {
                $scope.workingStaff = response.data;
                loadSalary($scope.workingStaff);
                if ($scope.workingStaff.imagePath==null) {
                    $scope.workingStaff.imgPath = '/assets/img/default-avatar.png'
                } else {
                    $scope.workingStaff.imgPath = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/' + $scope.workingStaff.imagePath;
                }
            }, function (response) {
                // TODO : handel error in fetching staff
            }
        );
    }

    function loadSalary(staff) {
        $http.get('/api/employee/salary/' + staff.eid).then(
            function (response) {
                $scope.salary = response.data;
                $scope.hasSalary = true;
            },
            function (response) {
                if (response.status == 404) {
                    $scope.hasSalary = false;
                } else {
                    error(response.data.error);
                }
                // TODO : handel error in fetching salary
            }
        );
    }

    $scope.approveStaff = function (staff) {
        if (staff) {
            $scope.disableApprove = true;
            $scope.disableReject = true;
            $http.get('/api/staff/approve/' + staff.id).then(
                function (response) {
                    ok("Approved Successfully.");
                    refresh();
                    $scope.disableApprove = false;
                    $scope.disableReject = false;
                    $state.go('app.staff_approvals', {centerId: $scope.workingStaff.costCenter.id});

                },
                function (response) {
                    // TODO : Error while approving
                    error(response.data.error);
                    $scope.disableApprove = false;
                    $scope.disableReject = false;
                }
            );
        }
    }
    $scope.rejectStaff = function (staff) {
        if (staff) {
            $scope.disableApprove = true;
            $scope.disableReject = true;
            $http.get('/api/staff/reject/' + staff.id).then(
                function (response) {
                    ok("Rejected Successfully.");
                    refresh();
                    $scope.disableApprove = false;
                    $scope.disableReject = false;
                    $state.go('app.staff_approvals', {centerId: $scope.workingStaff.costCenter.id});
                },
                function (response) {
                    // TODO : Error while rejecting
                    error(response.data.error);
                    $scope.disableApprove = false;
                    $scope.disableReject = false;
                }
            );
        }
    }
    function refresh() {
        loadStaff();
    }

    refresh();
});