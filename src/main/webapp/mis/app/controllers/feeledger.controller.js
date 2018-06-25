app.controller('FeeLedgerController', function ($scope, $http) {

    refresh();

    $scope.loadCenter = function () {

        $http.get('/api/center/' +  $scope.selectedCenter + '/fee/').then(function(response){
            $scope.feelist = response.data;
        });

        $http.get('/api/center/' +  $scope.selectedCenter + '/charge/').then(function(response){
            $scope.chargelist = response.data;
        });

    };

    function refresh() {

        $http.get('/api/center/').then(function(response){
            $scope.centers = response.data;
        });

    }

});

