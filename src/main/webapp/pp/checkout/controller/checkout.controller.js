app.controller('CheckoutController', function ($scope, $http, $stateParams, $state) {
    $scope.checkoutDetails = {};

    $scope.checkout = function () {
        $('#checkout-form').attr('action',$scope.checkoutDetails.transactionUrl).submit();
    };

    function loadCheckoutDetails() {
        $http.get('/hdfc/checkout/' + $stateParams.slipId + "/" + $stateParams.parentId).then(
            function (response) {
                $scope.checkoutDetails = response.data;
            }, function (response) {
                error(response.data ? response.data.error : "Unknown error. Please contact support.")
            }
        );
    }

    loadCheckoutDetails();
});