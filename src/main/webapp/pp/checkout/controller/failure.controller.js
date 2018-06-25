app.controller('CheckoutFailureController', function ($scope, $http, $stateParams) {

    $scope.responseDetails = {};

    function loadResponse() {
        if ($stateParams.PGResponseId) {
            $http.get('/hdfc/payment/' + $stateParams.PGResponseId).then(
                function (response) {
                    $scope.responseDetails = response.data;
                }, function (response) {
                    error(response.data.error);
                }
            );
        } else {
            error("Invalid Response Id.");
        }
    }

    loadResponse();
});