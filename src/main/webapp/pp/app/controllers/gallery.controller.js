app.controller('GalleryController', function ($scope, $http) {
    $scope.photos = [];
    // $scope.S3URL = 'http://localhost:8080/s3/gallery/photo/';
    $scope.S3URL = 'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/';
    $scope.galleryColumns = [];
    $scope.totalGalleryColumns = 6;
    $scope.currentPage = 1;
    $scope.disableNext = false;


    $scope.nextPage = function () {
        $scope.currentPage++;
        loadPhotos($scope.selectedStudent, $scope.currentPage);
    };

    $scope.previousPage = function () {
        $scope.currentPage--;
        loadPhotos($scope.selectedStudent, $scope.currentPage);
    };

    function loadStudents() {
        $http.get('/api/pp/student/me').then(function (response) {
            $scope.students = response.data;
            if ($scope.students.length > 0) {
                $scope.selectedStudent = $scope.students[0];
                loadPhotos($scope.selectedStudent, 1);
            }
        });
    }

    function loadPhotos(student, pageNumber) {
        if (!(student && student.id && pageNumber)) {
            return;
        }
        $http.get('/api/pp/student/gallery/' + student.id + "/" + pageNumber).then(
            function (response) {
                $scope.photos = response.data;
                $scope.disableNext = $scope.photos.length == 0;

                $scope.galleryColumns = [];
                for (var j = 0; j < $scope.totalGalleryColumns; j++) {
                    $scope.galleryColumns.push([]);
                }

                var photos = response.data;
                // photos.reverse();
                var i = 0;
                while (i < photos.length) {
                    for (var j = 0; j < $scope.galleryColumns.length; j++) {
                        var galleryColumn = $scope.galleryColumns[j];
                        if (photos[i]) {
                            galleryColumn.push(photos[i]);
                            i++;
                        }
                    }
                }
                console.log();
            }, function (response) {
                error('Unable to load Gallery Photos.')
            }
        );
    }

    loadStudents();

});
