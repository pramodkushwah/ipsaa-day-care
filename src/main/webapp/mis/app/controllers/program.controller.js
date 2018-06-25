app.controller('ProgramController', function ($scope, $http, $filter) {

    refresh();

    $scope.selectProgramTab = function () {
        $('#programs').addClass("active");
        $('#groups').removeClass("active");
        $('#activities').removeClass("active");
        $scope.editgroup = false;
    };

    $scope.selectGroupTab = function () {
        $('#groups').addClass("active");
        $('#programs').removeClass("active");
        $('#activities').removeClass("active");
        $scope.editprogram = false;
    };
    // $scope.selectActivityTab = function () {
    //     $('#activities').addClass("active");
    //     $('#programs').removeClass("active");
    //     $('#groups').removeClass("active");
    //
    // };




    $scope.newGroup = function () {
        $scope.insertedGroup = {
            mode: 'New',
            name: '',
            description: ''
        };
        $scope.editgroup = true;
    };

    $scope.editGroup = function (group) {
        $scope.insertedGroup = angular.copy(group);
        $scope.insertedGroup.mode = 'Edit';
        $scope.editgroup = true;
    };

    $scope.saveGroup = function (group) {
        if (group.id) {
            $http.put('/api/group/', group).then(function () {
                ok("Group saved");
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        } else {
            $http.post('/api/group/', group).then(function () {
                ok("Group saved");
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        }
    };

    $scope.cancelGroup = function () {
        $scope.insertedGroup = {
            name: '',
            description: ''
        };
        $scope.editgroup = false;
    };

    $scope.removeGroup = function (group) {
        $http.delete('/api/group/' + group.id).then(function () {
            ok("Group deleted");
            refresh();
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.newProgram = function () {
        $scope.insertedProgram = {
            mode: 'New',
            code: '',
            name: '',
            description: '',
            groups:[]
        };
        $scope.editprogram = true;
    };

    $scope.editProgram = function (program) {
        $scope.insertedProgram = angular.copy(program);
        $scope.insertedProgram.mode = "Edit";
        $scope.editprogram = true;
    };

    $scope.cancelProgram = function () {
        $scope.insertedProgram = {
            code: '',
            name: '',
            description: ''
        };
        $scope.editprogram = false;
    };

    $scope.saveProgram = function (program) {

        if (program.id) {
            $http.put('/api/program/', program).then(function () {
                ok("Program saved");
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        } else {
            $http.post('/api/program/', program).then(function () {
                ok("Program saved");
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        }
    };

    $scope.removeProgram = function (program) {
        $http.delete('/api/program/' + program.id).then(function () {
            ok("Program deleted");
            refresh();
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.removeProgramGroup = function (index) {
        if ($scope.insertedProgram) {
            $scope.insertedProgram.groups.splice(index, 1)
        }
    };

    $scope.addProgramGroup = function () {
        if ($scope.insertedProgram && $scope.selectedGroup) {

            var already = false;
            for (var i = 0; i < $scope.insertedProgram.groups.length; i++) {
                if ($scope.selectedGroup.id === $scope.insertedProgram.groups[i].id) {
                    already = true;
                }
            }

            if (!already) {
                $scope.insertedProgram.groups.push($scope.selectedGroup)
            }
        }
    };

    function refresh() {

        $scope.editgroup = false;
        $scope.editprogram = false;
        $http.get('/api/program/').then(function (response) {
            $scope.programs = response.data;
        });

        $http.get('/api/group/').then(function (response) {
            $scope.groups = response.data;
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

});
