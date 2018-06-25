app.controller('GalleryController', function ($http, $scope, Upload) {
    $scope.centers = [];
    $scope.students = [];
    $scope.photos = [];
    $scope.files = [];
    $scope.filesURI = [];
    $scope.saveDisabled = false;

    // $scope.S3URL = 'http://localhost:8080/s3/gallery/photo/';
    $scope.S3URL = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/';

    $scope.currentPage = 0;
    $scope.pageSize = 18;
    $scope.getData = function () {
        return $scope.photos;
    };
    $scope.numberOfPages = function () {
        return Math.ceil($scope.getData().length / $scope.pageSize);
    };
    $scope.nextPage = function () {
        $scope.currentPage++;
    };
    $scope.previousPage = function () {
        $scope.currentPage--;
    };

    function initDateTimePicker() {
        //datepicker config
        var datepicker = $('.datepicker');
        datepicker.datetimepicker({
            format: 'YYYY-MM-DD',
            useCurrent: true,
            showTodayButton: true
        });
        datepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });
        $("#from").on("dp.change", function () {
            $scope.from = $("#from").val();
        });
        $("#to").on("dp.change", function () {
            $scope.to = $("#to").val();
        });
        $scope.to = moment().format('YYYY-MM-DD');
        $scope.from = moment().subtract(30, 'days').format('YYYY-MM-DD')

    }

    initDateTimePicker();

    $scope.loadPhotos = function () {
        var filter = {};
        if ($scope.selectedCenter && $scope.selectedCenter.id) {
            filter.centerId = $scope.selectedCenter.id;
        }
        if ($scope.selectedStudent && $scope.selectedStudent.id) {
            filter.studentId = $scope.selectedStudent.id;
        }
        if ($scope.from) {
            filter.from = $scope.from;
        }
        if ($scope.to) {
            filter.to = $scope.to;
        }
        $scope.currentPage = 0;
        loadPhotos(filter);
    };

    $scope.loadFilterStudents = function (center) {
        if (center && center.code) {
            $http.post('/api/student/filter/', {centerCode: center.code}).then(
                function (response) {
                    $scope.filterStudents = response.data.students;
                }, function (response) {
                    error(response.data.error);
                }
            );
        }
    };

    function displayImage(file) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
            $scope.$apply(function () {
                // $scope.imageURI = reader.result;
                $scope.filesURI.push(reader.result)
            });
        };
        reader.onerror = function (error) {
            error("Failed to load image:" + error);
        };
    }

    $scope.editPhoto = function (photo, mode) {
        $scope.photo = angular.copy(photo);
        $scope.photo.mode = mode ? mode : "Edit";
        $scope.photo.center = {id: photo.centerId, code: photo.centerCode};
        $scope.photo.student = {id: photo.studentId};
        // $scope.imageURI = $scope.S3URL + photo.id;
        $scope.files = [];
        $scope.filesURI = [];
        if (photo.imagePath) {
            $scope.filesURI = [$scope.S3URL + photo.imagePath];
        }
        $scope.loadStudents($scope.photo)
    };

    $scope.save = function (photo) {

        var files = $scope.files;
        photo = angular.copy(photo);
        photo.files = files;
        if (!validate(photo)) {
            return;
        }
        if (photo.center && photo.center.id) {
            photo.centerId = photo.center.id;
        }
        delete photo.center;
        if (photo.student && photo.student.id) {
            photo.studentId = photo.student.id;
        }
        if (!photo.studentId) {
            delete photo.studentId;
        }
        delete photo.student;

        var req = {
            url: '/api/gallery/photo/',
            data: photo,
            arrayKey: '',
            method: photo.id ? 'POST' : 'POST'
        };


        $scope.saveDisabled =true;
        Upload.upload(req).then(
            function (response) {
                $scope.saveDisabled =false;
                $scope.photo = {};
                ok("Successfully uploaded photo");
                $scope.filesURI = [];
                $scope.files = [];
                $('#files').val(null);
                // $('#img').attr('src', "");
                refresh()
            }, function (response) {
                $scope.saveDisabled =false;
                $scope.filesURI = [];
                $scope.files = [];
                $('#files').val(null);
                error(response.data.error);
            }
        );
    };

    $scope.loadStudents = function (photo) {
        if (photo && photo.type && photo.type == 'Student' && photo.center && photo.center.code) {
            $http.post('/api/student/filter/', {centerCode: photo.center.code}).then(
                function (response) {
                    $scope.students = response.data.students;
                }, function (response) {
                    error(response.data.error);
                }
            );
        }
    };

    function loadPhotos(filter) {
        filter = filter ? filter : {};
        $http.post('/api/gallery/photo/filter/', filter).then(
            function (response) {
                $scope.photos = response.data;
            }, function (response) {
                error(response.data.error);
            }
        );
    }

    function validate(photo) {
        if (!photo.type) {
            error("Please select Type");
            return false;
        }

        if (!photo.center) {
            error("Please select Center.");
            return false;
        }

        if (photo.type == 'Student' && !photo.student) {
            error("Please select Student.");
            return false;
        }

        if (photo.id == null && (!photo.files)) {
            error("Please choose at least one Image File.");
            return false;
        }

        return true;
    }

    function loadCenters() {
        $http.get('/api/center/').then(
            function (response) {
                $scope.centers = response.data;
            }, function (response) {
                error(response.data.error);
            }
        );
    }

    function refresh() {
        loadCenters();
        var filter = {};
        if ($scope.from) {
            filter.from = $scope.from;
        }
        if ($scope.to) {
            filter.to = $scope.to;
        }
        loadPhotos(filter);
    }

    refresh();

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

    $('#files').change(function () {
        $scope.files = [];
        $scope.filesURI = [];
        var files = (this).files;
        if (files && files.length > 0) {
            $scope.files = files;
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                displayImage(file);
            }
        }
    });
});
app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        return typeof input == 'undefined' ? [] : input.slice(start);
    }
});