app.controller('StaffController', function ($scope, $http, $localStorage, fileUpload, $filter, $rootScope) {

    var tabs = ['#staffprofile', '#staffphoto', '#staffaddress', '#staffpermanentaddress'];

    $scope.disableSave = false;
    $scope.disableAppt = false;
    $scope.activeFilter = true;
    $scope.employeeTypeFilter = "ALL";
    $scope.selectedCenter = "";
    $scope.selectedType = "";

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

    // pagination code start
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.barsize = 5;//odd value
    $scope.pagebar = [];
    $scope.searchStaff = '';
    $scope.getData = function () {
        var filterObj = {};
        if ($scope.selectedType) {
            filterObj.type = $scope.selectedType;
        }
        if ($scope.selectedCenter && $scope.selectedCenter.code) {
            filterObj.centerCode = $scope.selectedCenter.code;
        }

        var f1 = $filter('filter')($scope.stafflist, {name: $scope.searchStaff});
        f1 = $filter('filter')(f1, filterObj);
        return f1;
    };
    $scope.numberOfPages = function () {
        return Math.ceil($scope.getData().length / $scope.pageSize);
    };
    $scope.$watch('searchStaff', function (newValue, oldValue) {
        if (oldValue != newValue) {
            $scope.currentPage = 0;
            $scope.updatePageBar();
        }
    }, true);
    $scope.updatePageBar = function () {
        var pages = [];
        var size = ($scope.barsize - 1) / 2;
        var current = $scope.currentPage;
        var total = $scope.numberOfPages();

        var i = current - size >= 0 ? current - size : 0;
        for (; i < current; i++) {
            pages.push({number: i, current: false});
        }

        pages.push({number: current, current: true});

        var end = total;
        for (i = current + 1; i < end && (i - current) <= size; i++) {
            pages.push({number: i, current: false});
        }

        $scope.pagebar = pages;
    };
    $scope.changePage = function (page) {
        $scope.currentPage = page.number;
        $scope.updatePageBar();

    };

    // pagination code end

    $scope.activeFilterChange = function () {
        refresh();
    };

    $scope.showImport = function () {
        $scope.editstaff = false;
        $scope.showstaff = false;
        $scope.addstaff = false;
        $scope.import = true;
    };

    $scope.downloadApptLetter = function (employeeId) {
        $scope.disableAppt = true;
        $http.get('/api/staff/appt_letter/' + employeeId, {responseType: 'arraybuffer'}).then(
            function (response) {
                $scope.disableAppt = false;
                var blob = new Blob([response.data], {
                    type: 'application/octet-stream'
                });
                saveAs(blob, response.headers("fileName"));
            }, function (response) {
                $scope.disableAppt = false;
                error(response.data.error);
            }
        );

    };

    $scope.centers = {};
    $http.get('/api/center/').then(function (response) {
        $scope.centers = response.data;
    });

    $scope.showStaff = function (staff) {
        $http.get('/api/staff/' + staff.id).then(function (response) {
            $scope.workingStaff = response.data;
            $scope.workingStaff.costCenterId = $scope.workingStaff.costCenter ? $scope.workingStaff.costCenter.id + "" : "";
            $scope.workingStaff.mode = "Show";
            $scope.showstaff = true;
            $scope.editstaff = false;
            $scope.addstaff = false;
            $scope.import = false;
            if ($(".selectpicker").length != 0) {
                $(".selectpicker").selectpicker();
            }
            for (var i = 0; i < $scope.allStaff.length; i++) {
                var staff = $scope.allStaff[i];
                if (staff.reportingManagerId != null) {
                    if (staff.reportingManagerId == $scope.workingStaff.reportingManagerId) {
                        $scope.workingStaff.searchReportingManager = staff.reportingManagerName;
                        break;
                    }
                }
            }
            setupProfilePic($scope.workingStaff);
        })
    };
    $scope.editStaff = function (staff) {
        $http.get('/api/staff/' + staff.id).then(function (response) {
            $scope.workingStaff = response.data;
            $scope.workingStaff.costCenterId = $scope.workingStaff.costCenter ? $scope.workingStaff.costCenter.id + "" : "";
            $scope.workingStaff.mode = "Edit";
            $scope.showstaff = false;
            $scope.editstaff = true;
            $scope.addstaff = false;
            $scope.import = false;
            if ($(".selectpicker").length != 0) {
                $(".selectpicker").selectpicker();
            }
            for (var i = 0; i < $scope.allStaff.length; i++) {
                var staff = $scope.allStaff[i];
                if (staff.reportingManagerId != null) {
                    if (staff.reportingManagerId == $scope.workingStaff.reportingManagerId) {
                        $scope.workingStaff.searchReportingManager = staff.reportingManagerName;
                        break;
                    }
                }
            }
            setupProfilePic($scope.workingStaff);
        });
    };
    $scope.addStaff = function () {
        $scope.workingStaff = {};
        $scope.editstaff = true;
        $scope.showstaff = false;
        $scope.addstaff = true;
        $scope.import = false;
    };

    $scope.download = function () {
        window.location.href = encodeURI('/api/staff/download?token=Bearer ' + $localStorage.token + '&employeeType=' + $scope.employeeTypeFilter);
    };

    $scope.saveStaff = debounce(function () {

        if (!$scope.workingStaff.profile || !moment($scope.workingStaff.profile.doj, "YYYY-MM-DD").isValid()) {
            error("Invalid - DOJ");
            return;
        }

        if (!$scope.workingStaff.profile || !moment($scope.workingStaff.profile.dob, "YYYY-MM-DD").isValid()) {
            error("Invalid - DOB");
            return;
        }

        if (!$scope.workingStaff || !$scope.workingStaff.profile.gender) {
            error("Please select gender.");
            return;
        }

        if (!$scope.workingStaff.employerId) {
            error("Please select Employer.");
            return;
        }

        if ($scope.workingStaff.expectedIn && !moment($scope.workingStaff.expectedIn, "HH:mm").isValid()) {
            error("Invalid - Expected In");
            return;
        }

        if ($scope.workingStaff.expectedOut && !moment($scope.workingStaff.expectedOut, "HH:mm").isValid()) {
            error("Invalid - Expected Out");
            return;
        }

        $scope.disableSave = true;
        if ($scope.workingStaff.eid) {
            $http.put('/api/staff/', $scope.workingStaff).then(function (response) {
                $scope.editstaff = false;
                $scope.showstaff = false;
                $scope.addstaff = false;
                $scope.import = false;
                $scope.disableSave = false;
                refresh();
                ok("Updated")
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        } else {
            $http.post('/api/staff/', $scope.workingStaff).then(function (response) {
                $scope.showstaff = false;
                $scope.editstaff = false;
                $scope.addstaff = false;
                $scope.import = false;
                $scope.disableSave = false;
                refresh();
                ok("Created");
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        }
    }, 200, true);

    $scope.selectReportingManager = function (staff) {
        $scope.workingStaff.reportingManagerId = staff.id;
        $scope.workingStaff.searchReportingManager = staff.name;
    };

    $scope.cancelStaff = function () {
        $scope.editstaff = false;
        $scope.showstaff = false;
        $scope.addstaff = false;
        $scope.import = false;
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
    };

    $scope.setFiles = function (element) {
        $scope.imgfile = element.files[0];
        displayImage($scope.imgfile);
    };

    function displayImage(file) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
            $scope.$apply(function () {
                $scope.staffImageURI = reader.result;
            });
        };
        reader.onerror = function (error) {
            error("Failed to load image:" + error);
        };
    }

    $scope.uploadImage = function () {

        $scope.disableSave = true;
        fileUpload.uploadFileToUrl($scope.imgfile, "/api/staff/" + $scope.workingStaff.id + "/profile-pic", false, function (status, data) {
            if (status == 200) {
                $scope.disableSave = false;
                $scope.workingStaff = {};
                $scope.showstaff = false;
                $scope.editstaff = false;
                $scope.addstaff = false;
                ok("Uploaded successfully")

            } else {
                $scope.disableSave = false;
                error("Error!");
                $scope.workingStaff = {};
                $scope.showstaff = false;
                $scope.editstaff = false;
                $scope.addstaff = false;
                $scope.message = "Error processing file !!!";
            }
        });
    };

    $scope.browseImage = function () {
        $('#staffimg').trigger('click');
    };

    $scope.deleteStaff = function (staff) {
        $http.delete('/api/staff/' + staff.id).then(function (response) {
            refresh();
            ok("Staff  is deactivated.")
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.deleteStaffSwal = function (staff) {
        swal({
            title: 'Are you sure want to deactivate staff?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes'
        }).then(function () {
            $scope.deleteStaff(staff);
        });
    };

    function loadAllStaff() {
        $http.get('/api/staff/all/')
            .then(
                function (response) {
                    $scope.allStaff = response.data;
                }, function (response) {
                    error(response.data.error);
                }
            );
    }

    function loadLegalEntities() {
        $http.get('/api/le/')
            .then(function (response) {
                $scope.legalEntityList = response.data;
            }, function (response) {
                error(response.data.error);
            })
    }

    function refresh() {
        loadLegalEntities();
        loadAllStaff();

        var filter = {
            employeeType: $scope.employeeTypeFilter,
            active: $scope.activeFilter,
            pageNumber: 0,
            pageSize: 0
        };
        $http.post('/api/staff/filter', filter).then(function (response) {
            $scope.stafflist = response.data.stafflist;
            $scope.updatePageBar();
        });
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
        if (!message) message = "Unknown error, please contact support";

        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    function setupProfilePic(staff) {
        if (staff.staffImageData) {
            $scope.staffImageURI = "data:image/PNG;base64," + staff.staffImageData;
        } else {
            $scope.staffImageURI = "/assets/img/student.png";
        }
    }


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

        $("#doj").on("dp.change", function () {
            if (typeof $scope.workingStaff == 'undefined') {
                $scope.workingStaff = {};
            }
            if (typeof $scope.workingStaff.profile == 'undefined') {
                $scope.workingStaff.profile = {};
            }
            $scope.workingStaff.profile.doj = $("#doj").val();
        });

        $("#dol").on("dp.change", function() {
          if (typeof $scope.workingStaff == "undefined") {
            $scope.workingStaff = {};
          }
          if (typeof $scope.workingStaff.profile == "undefined") {
            $scope.workingStaff.profile = {};
          }
          $scope.workingStaff.profile.dol = $("#dol").val();
        });

        $("#dob").on("dp.change", function () {
            if (typeof $scope.workingStaff == 'undefined') {
                $scope.workingStaff = {};
            }

            if (typeof $scope.workingStaff.profile == 'undefined') {
                $scope.workingStaff.profile = {};
            }
            $scope.workingStaff.profile.dob = $("#dob").val();
        });

        //timepicker cnfig
        var timepicker = $('.timepicker');
        timepicker.datetimepicker({
            format: 'HH:mm'
        });
        timepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });

        $("#expectedOut").on("dp.change", function () {
            if (typeof $scope.workingStaff == 'undefined') {
                $scope.workingStaff = {};
            }
            $scope.workingStaff.expectedOut = $("#expectedOut").val();
        });
        $("#expectedIn").on("dp.change", function () {
            if (typeof $scope.workingStaff == 'undefined') {
                $scope.workingStaff = {};
            }
            $scope.workingStaff.expectedIn = $("#expectedIn").val();
        });


    }

    $rootScope.$on('$includeContentLoaded', function (event, url) {
        initDateTimePicker();
    });

});

app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        return typeof input == 'undefined' ? [] : input.slice(start);
    }
});