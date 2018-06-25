app.controller('ProfileController', function ($scope, $http, Auth) {
    $scope.bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-', 'NA'];
    $scope.showFooter = true;
    $scope.students=[];
    loadStudents();
    function debounce(func, wait, immediate) {
        var timeout;
        return function () {
            var context = this, args = arguments;
            var later = function () {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }

    var tabs = ['#studentprofile', '#fatherprofile', '#motherprofile', '#notification'];

    $scope.showstudent = true;
    $scope.editlocklabel = $scope.showstudent ? "edit" : "cancel";

    $scope.toggleEdit = function () {
        $scope.showstudent = !$scope.showstudent;
        $scope.editlocklabel = $scope.showstudent ? "edit" : "cancel";
    };

    $scope.selectTab = function (index) {
        for (var i = 0; i < tabs.length; i++) {
            var tab = tabs[i];
            if (tab === tabs[index]) {
                $(tab).addClass("active");
            } else {
                $(tab).removeClass("active");
            }
        }
        $scope.showFooter = index != tabs.length - 1;
    };


    $scope.editStudent = function () {
        $scope.showstudent = false;
    };

    $scope.saveProfile = function () {
        var student = $scope.student;
        var copy = $scope.studentCopy;
        if ((copy.mother.account == true && copy.mother.email != student.mother.email) || (copy.father.account == true && copy.father.email != student.father.email)) {
            var message = "Since you are changing your email id, your login account id will change!";
            swal({
                title: 'Are you sure?',
                text: message,
                type: 'warning',
                width: 600,
                buttonsStyling: false,
                confirmButtonClass: "btn btn-warning",
                cancelButtonClass: "btn btn-default",
                showCancelButton: true
            }).then(function () {
                save(student);
            });
        } else {
            save(student);
        }
    };

    function save(copy) {
        for (var i = 0; i < copy.parents.length; i++) {
            var parent = copy.parents[i];
            delete copy[parent.relationship.toLowerCase()]
        }
        $http.put('/api/pp/student/profile', copy).then(function (response) {
            ok("Profile Updated");
            loadStudent($scope.selectedStudent);
        }, function (response) {
            loadStudent($scope.selectedStudent);
            error(response.data.error);
        });
    };

    $scope.studentChanged = function (student) {
      loadStudent(student);
    };

    function loadStudent(student) {
        if(student.id){
            $http.get('/api/pp/student/'+student.id).then(function (response) {
                $scope.student = response.data;
                setupProfilePic($scope.student);
                for (var i = 0; i < $scope.student.parents.length; i++) {
                    var parent = $scope.student.parents[i];
                    $scope.student[parent.relationship.toLowerCase()] = parent;
                }
                $scope.studentCopy = angular.copy($scope.student);
            });
        }
    }

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
           $scope.students=response.data;
           if($scope.students.length>0){
               $scope.selectedStudent=$scope.students[0];
               loadStudent($scope.selectedStudent);
           }
        });
    }

    $scope.notify = debounce(function (parent, com) {
        if (com == 'sms') {
            $http.get('/api/pp/student/sms/' + parent.id + '/' + parent.smsEnabled).then(
                function (response) {
                    var message = "Sms notification successfully ";
                    message = parent.smsEnabled ? message + 'Enabled' : message + 'Disabled';
                    ok(message);
                },
                function (response) {
                    parent.smsEnabled = !parent.smsEnabled;
                    error(response.data.error);
                }
            );
        } else if (com == 'email') {
            $http.get('/api/pp/student/email/' + parent.id + '/' + parent.emailEnabled).then(
                function (response) {
                    var message = "Email notification successfully ";
                    message = parent.emailEnabled ? message + 'Enabled' : message + 'Disabled';
                    ok(message);
                },
                function (response) {
                    parent.emailEnabled = !parent.emailEnabled;
                    error(response.data.error);
                }
            );
        }
    }, 200, true);

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

    function setupProfilePic(student) {

        if (!student.imagePath) {
            student.imgPath = '/assets/img/default-avatar.png'
        } else {
            student.imgPath = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/' + student.imagePath;
        }

    }

});

app.filter('idFilter', function () {
    return function (input, id) {
        var out = [];
        if (typeof input === 'object') {
            if (typeof id === 'object' || typeof id === 'string') {
                if (id != null || id != '') {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].id == id) {
                            out.push(input[i]);
                        }
                    }
                }
            }
        }
        return out;
    }
});