app.controller('CheckoutController', function ($scope, $http, $stateParams, $state, $location) {
    $scope.checkoutDetails = {};

    $scope.checkout = function () {
        $('#checkout-form').attr('action',$scope.checkoutDetails.transactionUrl).submit();
    };

    function loadCheckoutDetails() {
        console.log($location.url());
        console.log($location.absUrl());
        console.log($location.path());
        if($location.path().indexOf("ipsaaclubcheckoutdetails") > -1) {
           $http.get('/hdfc/checkout/ipsaaclub/' + $stateParams.slipId + "/" + $stateParams.parentId).then(
              function (response) {
                  $scope.checkoutDetails = response.data;
              }, function (response) {
                  error(response.data ? response.data.error : "Unknown error. Please contact support.")
              }
          );
        } else{
         $http.get('/hdfc/checkout/' + $stateParams.slipId + "/" + $stateParams.parentId).then(
             function (response) {
                 $scope.checkoutDetails = response.data;
             }, function (response) {
                 error(response.data ? response.data.error : "Unknown error. Please contact support.")
             }
         );
      }

    }

    loadCheckoutDetails();
});