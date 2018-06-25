app.config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
    $urlRouterProvider.otherwise("/app/profile");

    $stateProvider
        .state('app', {
            abstract: true,
            url: "/app",
            templateUrl: "views/layout/layout.html"
        })
        .state('login', {
            url: "/login",
            controller: 'LoginController',
            templateUrl: "views/login.html"
        })
        .state('logout', {
            url: "/logout",
            controller: 'LogoutController'
        })
        .state('app.activity', {
                url: "/activity",
                controller: "ActivityController",
                templateUrl: "/pp/views/activity.html"
            }
        )
        .state('app.profile', {
            url: "/profile",
            controller: 'ProfileController',
            templateUrl: "/pp/views/profile.html"
        })
        .state('app.attendance', {
            url: "/attendance",
            controller: 'AttendanceController',
            templateUrl: "/pp/views/attendance.html"
        })
        .state('app.support', {
            url: "/support",
            controller: 'SupportController',
            templateUrl: "/pp/views/support.html"
        })
        .state('app.support.viewcase', {
            url: "/view/{id}",
            controller: 'SupportViewController',
            templateUrl: "/pp/views/supportview.html"
        })
        .state('app.sharingsheet', {
            url: "/sharingsheet",
            controller: 'SharingSheetController',
            templateUrl: "/pp/views/sharingsheet.html"
        })
        .state('app.fee', {
            url: "/fee",
            controller: 'FeeController',
            templateUrl: "/pp/views/fee.html"
        })
        .state('app.feesuccess', {
            url: "/fee",
            controller: 'FeeSuccessController',
            templateUrl: "/pp/views/feesuccess.html"
        })
        .state('app.feefailure', {
            url: "/fee",
            controller: 'FeeFailureController',
            templateUrl: "/pp/views/feefailure.html"
        })
        .state('app.foodmenu', {
            url: "/foodmenu",
            controller: 'FoodMenuController',
            templateUrl: "/pp/views/foodmenu.html"
        })
        .state('app.gallery', {
            url: "/gallery",
            controller: 'GalleryController',
            templateUrl: "/pp/views/gallery.html"
        })
    ;

    $httpProvider.interceptors.push(['$q', '$location', '$localStorage', 'jwtHelper',
        function ($q, $location, $localStorage, jwtHelper) {
            return {
                'request': function (config) {
                    config.headers = config.headers || {};
                    if ($localStorage.token) {
                        config.headers.Authorization = 'Bearer ' + $localStorage.token;
                    }
                    return config;
                },
                'responseError': function (response) {
                    //do not redirect on app api rejects @ 403 and they may be due to
                    //autherisation error, not authentication error
                    if (response.status === 401) {
                        $location.path('/login');
                    }
                    return $q.reject(response);
                }
            };
        }]);
});