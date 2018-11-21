app.config(function ($stateProvider, $urlRouterProvider) {
    // $urlRouterProvider.otherwise("/app/profile");

    $stateProvider
        .state('app', {
            abstract: true,
            url: "/app",
            templateUrl: "views/checkout/layout.html"
        })
        .state('app.checkoutdetails', {
            url: "/checkoutdetails/{slipId}/{parentId}",
            templateUrl: "views/checkout/checkout-details.html",
            controller: 'CheckoutController'
        })
        .state('app.ipsaaclubcheckoutdetails', {
            url: "/ipsaaclubcheckoutdetails/{slipId}/{parentId}",
            templateUrl: "views/checkout/checkout-details.html",
            controller: 'CheckoutController'
        })
        .state('app.success', {
            url: "/success/{PGResponseId}",
            templateUrl: "views/checkout/success.html",
            controller: 'CheckoutSuccessController'
        })
        .state('app.failure', {
            url: "/failure/{PGResponseId}",
            templateUrl: "views/checkout/failure.html",
            controller: 'CheckoutFailureController'
        });

});