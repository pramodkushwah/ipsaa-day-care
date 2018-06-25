var app
    = angular.module('app',
    [
        "angular-jwt",
        "ui.router",
        "ngAnimate",
        "xeditable",
        "ngStorage"
    ]);

app.run(function ($rootScope, $location, $templateCache, editableOptions, editableThemes, $localStorage, jwtHelper, Auth) {
    if ($location.protocol() == 'http'
        && $location.absUrl().indexOf('portal.ipsaa.in') != -1) {
        var httpsUrl = $location.absUrl().replace('http', 'https');
        window.location = httpsUrl;
    }
    editableOptions.theme = 'bs3';
    $templateCache.removeAll();
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        // redirect to login page if not logged in and trying to access a restricted page
        var restrictedPage = $.inArray($location.path(), ['/login']) === -1;
        var loggedIn = $localStorage.token && !jwtHelper.isTokenExpired($localStorage.token);
        if (restrictedPage && !loggedIn) {
            $location.path('/login');
        }
        if (restrictedPage && Auth.getUser().domain != '/pp/') {
            $location.path('/login');
        }
    });
    $rootScope.$on('$includeContentLoaded', function (event, url) {
        // updateScrollbar();
    });
    $rootScope.$on('$viewContentLoaded', function () {
        // updateScrollbar();
    });

    function updateScrollbar() {
        $('.main-panel').perfectScrollbar({
            suppressScrollX: true
        });
        $('.main-panel').perfectScrollbar('update');
        $('.table-responsive').perfectScrollbar({
            useBothWheelAxes: false,
            suppressScrollY: true
        });
        $('.table-responsive').perfectScrollbar('update');
    }

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if (
            toState.name == 'app.activity' ||
            toState.name == 'app.profile' ||
            toState.name == 'app.attendance' ||
            toState.name == 'app.support' ||
            toState.name == 'app.sharingsheet' ||
            toState.name == 'app.fee' ||
            toState.name == 'app.foodmenu'
        ) {
            $('.navbar-toggle').click();
            $('.sidebar .collapse').collapse('hide').on('hidden.bs.collapse', function () {
                $(this).css('height', 'auto');
            });
        }
    });

});