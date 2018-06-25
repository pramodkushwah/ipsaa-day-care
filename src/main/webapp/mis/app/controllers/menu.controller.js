app.controller('MenuController', function ($scope, $http, $timeout, fileUpload, Auth, NotificationService) {


    $http.get('/api/user/menu/').then(function (response) {
        $scope.menu = response.data;
    });

    $http.get('/api/user/me/').then(function (response) {
        $scope.self = response.data;

        if ($scope.self.profileImage) {
            $scope.profileImageURI = "http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/" + $scope.self.profileImage;
        } else {
            $scope.profileImageURI = "/assets/img/faces/default_profile_pic.png";
        }
    });

    $scope.menuToggle = function () {
        $timeout(function () {
            $('.sidebar .sidebar-wrapper').perfectScrollbar('update');
        }, 500);
    };
    $scope.menuClicked = function () {
        $('.navbar-toggle').click();
        $('.sidebar .collapse').collapse('hide').on('hidden.bs.collapse', function () {
            $(this).css('height', 'auto');
        });
    }

    $scope.profilePicChangeMouseOver = function () {
        $scope.originalImage = $("#profile-pic").attr("src");
        $("#profile-pic").attr("src", "/assets/img/faces/default_profile_pic.png");
    };

    $scope.profilePicChangeMouseLeave = function () {
        $("#profile-pic").attr("src", $scope.originalImage);
    };

    $scope.ppFileBrowse = function () {
        $('#profilePicUpload').trigger('click');
    };

    $scope.$watch('file', function () {
        //this triggers at initial load also, so need to put a check on $scope.file
        if ($scope.file) {
            fileUpload.uploadFileToUrl($scope.file, "/api/user/profile-pic", false, function (status, data) {
                if (status == 200) {
                    location.reload();
                } else {
                    $scope.message = "Error processing file !!!";
                }
            });
        }
    });
    $scope.logout = function () {
        Auth.logout(function () {
            window.location = "/mis/index.html"
        })
    };

    (function initNotification() {
        NotificationService.init();
    })();

});