app.controller('StaffApprovalsController', function ($scope, $http,$stateParams) {
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
    $scope.approvals=[];
    $scope.centers = [];

    $scope.centerChanged = function (center) {
        if(center){
            loadApprovals(center);
        }else{
            $scope.approvals=[];
        }
    };

    function loadApprovals(center) {
        $http.get('/api/staff/approvals/'+center.id).then(
            function (response) {
                $scope.approvals=response.data;
            },
            function (response) {
                // TODO : error in fetching approvals
            }
        );
    }

    function refresh() {
        $http.get('/api/staff/approvals/count').then(
            function (response) {
                $scope.centers = response.data;
            }
        );
    }

    refresh();

    if ($stateParams.centerId) {
        $scope.selectedCenter = {id: $stateParams.centerId};
        loadApprovals($scope.selectedCenter);
    }


});