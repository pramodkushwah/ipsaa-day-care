var app
    = angular.module('app',
    [
        "angular-jwt",
        "ui.router",
        "ngAnimate",
        "xeditable",
        "ngStorage",
        "ngFileUpload"
    ]);

app.run(function ($rootScope, $location, editableOptions,
                  editableThemes, $localStorage, jwtHelper,
                  Auth, $templateCache, NotificationService) {
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

        if (Auth.getUser().domain != '/mis/') {
            $location.path('/login');
        }
    });

    $rootScope.$on('$includeContentLoaded', function (event, url) {
        if ((url == 'views/layout/header.html')) {
            reloadScript('/assets/js/material-dashboard.js', 'text/javascript', 'utf-8');
        }
        $.material.init();
        // updateScrollBar();
    });
    $rootScope.$on('$viewContentLoaded', function () {
        $.material.init();
        // updateScrollBar();
    });
    $(window).resize(function () {
        $('.table-responsive').perfectScrollbar('update');
    });

    function updateScrollBar() {
        $('.main-panel').perfectScrollbar({
            suppressScrollX: true
        });
        $('.main-panel').perfectScrollbar('update');
        $('.table-responsive').perfectScrollbar({
            useBothWheelAxes: false,
            suppressScrollY: true
        });
        $('.table-responsive').perfectScrollbar('update');
        $('.nav').perfectScrollbar('update');
    }

    function reloadScript(url, type, charset) {
        if (type === undefined) type = 'text/javascript';
        if (url) {
            var script = document.querySelector("script[src*='" + url + "']");
            if (!script) {
                $('<script>').attr('src', url).appendTo('head');
            } else {
                $('script[src="' + url + '"]').remove();
                $('<script>').attr('src', url).appendTo('head');
            }
        }
    }

    NotificationService.init();

});