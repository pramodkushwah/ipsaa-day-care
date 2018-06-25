app.controller('LoginController', function ($scope, $http, Auth, $location) {


    $scope.login = function () {
        var formData = {
            email: $scope.email,
            password: $scope.password
        };

        $scope.dataLoading = true;
        Auth.login(formData,
            authSuccess,
            function (res) {
                $scope.error = "Invalid Login";
                $scope.dataLoading = false;
                error(res.data.error);

            }
        );

        function authSuccess(res) {
            var user = Auth.getUser();
            if (user.domain === "/pp/") {
                window.location = "/pp/index.html";
            } else {
                window.location = "/mis/index.html";
            }
            $scope.dataLoading = false;
        }
    };

    $scope.showResetPassword = function () {
        $http.post('/pp/resetpassword/email', {email: $scope.email})
            .then(function (response) {
                swal({
                    title: 'Create New Password',
                    html:
                    '<p>Token is sent to ' + $scope.email + '.</p>' +
                    '<div class="form-group">\n' +
                    '  <label for="token" class="control-label">Token</label>\n' +
                    '  <input type="number" name="token" id="token" class="form-control" required>\n' +
                    '</div>\n' +
                    '<div class="form-group">\n' +
                    '  <label for="password1" class="control-label">Create new password</label>\n' +
                    '  <input type="password" name="password1" id="password1" class="form-control" required>\n' +
                    '</div>\n' +
                    '<div class="form-group">\n' +
                    '  <label for="password2" class="control-label">Confirm new password</label>\n' +
                    '  <input type="password" name="password2" id="password2" class="form-control" required>\n' +
                    '</div>',
                    showCancelButton: true,
                    buttonsStyling: false,
                    confirmButtonText: 'Submit',
                    confirmButtonClass: "btn btn-warning",
                    cancelButtonClass: "btn btn-warning",
                    showLoaderOnConfirm: true,
                    preConfirm: function () {
                        return new Promise(function (resolve, reject) {
                            var token = $('#token').val();
                            var password1 = $('#password1').val();
                            var password2 = $('#password2').val();
                            if (token == '')
                                reject('Token is empty.')
                            if (password1 == '')
                                reject('New Password is empty.')
                            if (password2 == '')
                                reject('Confirm Password is empty.')
                            if (password1 != password2) {
                                reject('Passwords do not match.');
                            } else {
                                $http.post('/pp/resetpassword/', {password: password1, token: token})
                                    .then(
                                        function (response) {
                                            resolve("Password is changed login with new password.")
                                        }, function (response) {
                                            reject(response.data.error);
                                        }
                                    );
                            }
                        })
                    },
                    onOpen: function () {
                        $('#token').focus()
                    }
                }).then(function (message) {
                    swal({
                        type: 'success',
                        title: message
                    }).then(function () {
                        $location.path('/login');
                    })
                })
            }, function (response) {
                if (response.status == 400) {
                    $scope.error = response.data.error;
                }
            });
    };

    function error(message) {
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

});