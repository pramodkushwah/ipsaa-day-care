app.controller('StudentApprovalsController', function ($scope, $http, $state, $stateParams) {
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

    $scope.centerChanged = function (center) {
        if(center){
            loadApprovals(center);
        }else{
            $scope.approvals=[];
        }
    };

    function loadApprovals(center) {
        $http.get('/api/student/approvals/'+center.id).then(
            function (response) {
                $scope.approvals=response.data;
            },
            function (response) {
                // TODO : error in fetching approvals
            }
        );
    }
    function refresh() {
        $http.get('/api/student/approvals/count').then(
            function (response) {
                $scope.centers = response.data;
            }
        );

    }

    refresh();
    if($stateParams.centerId){
        $scope.selectedCenter={id: $stateParams.centerId};
        loadApprovals($scope.selectedCenter);
    }
});