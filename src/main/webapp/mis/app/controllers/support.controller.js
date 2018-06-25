app.controller('SupportController', function ($scope, $http) {

    $scope.showClosed = false;

    $scope.refresh = function () {
        if ($scope.showClosed) {
            $http.get('/api/support/all').then(function(response){
                $scope.queries = response.data;
            });
        }
        else {
            $http.get('/api/support/').then(function(response){
                $scope.queries = response.data;
            });
        }
    };

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

    $scope.refresh();


});

app.controller('SupportViewController', function ($scope, $stateParams, $http) {

    refresh();

    function refresh() {
        $http.get('/api/support/' +$stateParams.id).then(function(response){
            $scope.query = response.data;
        });
    }

    $scope.closeQuery = function (query) {
        $http.post('/api/support/' + query.id + '/close')
            .then(function(response){
            refresh();
            ok("Closed")
        }, function (response) {
            error(response.data.message);
        });
    };

    $scope.replyToQuery = function (query) {
        swal({
            title: 'Reply',
            html:
            '<div class="form-group label-floating">' +
            '<label class="control-label">Description*</label>' +
            '<textarea id="swal-rdescription" class="swal2-textarea" rows="3"></textarea>' +
            '</div>',

            preConfirm: function () {
                return new Promise(function (resolve) {
                    resolve($('#swal-rdescription').val())
                })
            },
            onOpen: function () {
                $('#swal-rdescription').focus()
            }
        }).then(function (result) {
            $http.post('/api/support/' +query.id + '/reply', {
                id : query.id,
                description : result
            }).then(function(response){
                refresh();
                ok("Replied")
            }, function (response) {
                error(response.data.message);
            });

        }).catch(swal.noop)
    }
});
