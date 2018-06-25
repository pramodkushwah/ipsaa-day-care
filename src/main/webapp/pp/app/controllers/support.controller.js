app.controller('SupportController', function ($scope, $http) {


    $scope.selectedStudent={};
    $scope.students=[];
    refresh();

    $scope.newQuery = function () {
        var dropdown='    <div class="form-group label-floating">\n' +
            '      <label class="control-label">Student*</label>\n' +
            '      <select id="swal-student" class="form-control" >\n' +
            '        <option value="">select</option>\n';
        angular.forEach($scope.students,function(student){
            dropdown=dropdown+"<option value='"+student.id+"'>"+student.fullName+"</option>\n";
        });
        dropdown=dropdown+'      </select>\n' +
            '</div>';
        swal({
            title: 'New Query',
            html:
            dropdown+
            '<div class="form-group label-floating">' +
            '<label class="control-label">Title*</label>' +
            '<input id="swal-title" class="swal2-input">' +
            '</div>'+
            '<div class="form-group label-floating">' +
            '<label class="control-label">Description*</label>' +
            '<textarea id="swal-description" class="swal2-textarea" rows="3"></textarea>' +
            '</div>',
            confirmButtonColor:'#ffca05',

            preConfirm: function () {
                return new Promise(function (resolve,reject) {
                    if(!$('#swal-student').val()){
                        reject('Please select Student.');
                    }
                    if(!$('#swal-title').val()){
                        reject('Title should not be empty.');
                    }
                    if(!$('#swal-description').val()){
                        reject('Description should not be empty.');
                    }
                    resolve([
                        $('#swal-title').val(),
                        $('#swal-description').val(),
                        $('#swal-student').val()
                    ])
                })
            },
            onOpen: function () {
                $('#swal-title').focus()
            }
        }).then(function (result) {
            $http.post('/api/pp/student/support/', {
                title : result[0],
                description : result[1],
                studentId: result[2]
            }).then(function(response){
                refresh();
                ok("Created")
            }, function (response) {
                error(response.data.message);
            });

        }).catch(swal.noop)
    };

    function refresh() {
        $http.get('/api/pp/student/support').then(function(response){
            $scope.queries = response.data;
        });
    }

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

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students=response.data;
            if($scope.students.length>0){
                $scope.selectedStudent=$scope.students[0];
            }
        });
    }
    loadStudents();
});

app.controller('SupportViewController', function ($scope, $stateParams, $http) {

    refresh();

    function refresh() {
        $http.get('/api/pp/student/support/' +$stateParams.id).then(function(response){
            $scope.query = response.data;
        });
    }

    $scope.replyToQuery = function (query) {
        swal({
            title: 'Reply',
            html:
            '<div class="form-group label-floating">' +
            '<label class="control-label">Description*</label>' +
            '<textarea id="swal-rdescription" class="swal2-textarea" rows="3"></textarea>' +
            '</div>',
            confirmButtonColor:'#ffca05',
            preConfirm: function () {
                return new Promise(function (resolve) {
                    resolve($('#swal-rdescription').val())
                })
            },
            onOpen: function () {
                $('#swal-rdescription').focus()
            }
        }).then(function (result) {
            $http.post('/api/pp/student/support/' +query.id + '/reply', {
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
