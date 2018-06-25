angular.module('app')
    .factory('Auth', ['jwtHelper', '$http', '$localStorage', '$timeout',
        function (jwtHelper, $http, $localStorage, $timeout) {

        function urlBase64Decode(str) {
            var output = str.replace('-', '+').replace('_', '/');
            switch (output.length % 4) {
                case 0:
                    break;
                case 2:
                    output += '==';
                    break;
                case 3:
                    output += '=';
                    break;
                default:
                    throw 'Illegal base64url string!';
            }
            return window.atob(output);
        }

        return {
            login: function (data, success, error) {

                $http.post('/login', data).then(
                    function (response) {
                        if (response.data.token && !jwtHelper.isTokenExpired(response.data.token)) {
                            $localStorage.token = response.data.token;
                            $localStorage.privileges = response.data.privileges;
                            success(response);
                        }
                    },
                    error
                );

            },
            logout: function (success) {
                delete $localStorage.token;
                delete $localStorage.refresh_token;
                success();
            },
            getUser: function () {
                var token = $localStorage.token;
                var user = {};
                if (typeof token !== 'undefined') {
                    user = JSON.parse(urlBase64Decode(token.split('.')[1]));
                }
                return user;
            },
            hasPrivilege: function (privilege) {
                if(typeof $localStorage.privileges !== 'undefined'){
                    var privileges = $localStorage.privileges;
                    for(var i=0;i<privileges.length;i++){
                        if(privileges[i]==privilege){
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        };
    }
]);