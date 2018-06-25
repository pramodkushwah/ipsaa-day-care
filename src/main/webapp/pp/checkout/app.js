var app
    = angular.module('app',
    [
        "angular-jwt",
        "ui.router",
        "ngAnimate",
        "xeditable",
        "ngStorage"
    ]);

app.run(function ($rootScope) {

    console.log("app");

});

function ok(message) {
    swal({
        title: message,
        type: 'success',
        buttonsStyling: false,
        confirmButtonClass: "btn btn-warning"
    });
}

function error(message) {
    swal({
        title: message,
        type: 'error',
        buttonsStyling: false,
        confirmButtonClass: "btn btn-warning"
    });
}