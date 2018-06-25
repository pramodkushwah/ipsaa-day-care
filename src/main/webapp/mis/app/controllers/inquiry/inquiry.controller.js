app.controller('InquiryController', function ($scope, $http, $stateParams, $state, InquiryService) {
    //change in these array also change in followup.controller.js
    $scope.leadSources = [
        "Building",
        "Corporate",
        "Advertisement",
        "Reference",
        "Website",
        "Newspaper",
        "Signboards",
        "Others"];
    $scope.inquiryTypes = [
        "Web",
        "Walkin",
        "Call",
        "Email"];
    $scope.dispositions = [
        "NewInquiry",
        "Followup",
        "Callback",
        "ParentMessage",
        "Enrolled",
        "Drop",
        "NotInterested",
        "Revisit"
    ];
    $scope.inquiryNumbers = [];
    $scope.centers = [];
    $scope.program = [];
    $scope.groups = [];
    $scope.selectedCenter = {};
    $scope.workingInquiry = newInquiry();
    $scope.disableSave = false;

    var today = moment();

    $scope.saveInquiry = debounce(function (inquiry) {
        $scope.disableSave = true;
        InquiryService.saveInquiry(inquiry, function (response) {
            $scope.workingInquiry = response.data;
            ok("Inquiry Saved Successfully.");
            refresh();
        }, failure);
    }, 200, true);

    $scope.cancel = function () {
        $state.go('app.inquiries');
    };

    $scope.$watchGroup(['workingInquiry.motherMobile', 'workingInquiry.fatherMobile', 'workingInquiry.secondaryNumbers', 'workingInquiry.logs'],
        function () {
            var numberSet = new Set();
            if ($scope.workingInquiry.motherMobile)
                numberSet.add($scope.workingInquiry.motherMobile);
            if ($scope.workingInquiry.fatherMobile)
                numberSet.add($scope.workingInquiry.fatherMobile);
            if ($scope.workingInquiry.secondaryNumbers) {
                var secondary = $scope.workingInquiry.secondaryNumbers.split(/[ ,]+/);
                for (i = 0; i < secondary.length; i++) {
                    numberSet.add(secondary[i]);
                }
            }
            if ($scope.workingInquiry.logs) {
                for (i = 0; i < $scope.workingInquiry.logs.length; i++) {
                    numberSet.add($scope.workingInquiry.logs[i].callBackNumber);
                }
            }
            $scope.inquiryNumbers = Array.from(numberSet);
        });

    function refresh() {
        loadCenters();
        loadPrograms(function (response) {
            $scope.programs = response.data;
            loadInquiry($stateParams.id, 'Show');
        });
    }

    function loadCenters() {
        InquiryService.getCenters(
            function (response) {
                $scope.centers = response.data;
            }, failure
        );
    }

    function loadPrograms(success) {
        InquiryService.getPrograms(success, failure);
    }

    function newInquiry() {
        var today = moment().year() + '-' +
            (moment().month() + 1) + '-' +
            moment().date();
        $scope.inquiryNumbers = [];
        return {
            mode: 'New',
            inquiryDate: today
        };
    }

    function loadInquiry(id, mode) {
        if (id) {
            if (id != 'new')
                InquiryService.getInquiry(id,
                    function (response) {
                        $scope.workingInquiry = response.data;
                        $scope.workingInquiry.mode = mode ? mode : 'Edit';
                        $scope.workingInquiry.center = {id: $scope.workingInquiry.centerId, code: $scope.workingInquiry.centerCode};
                        for (var i = 0; i < $scope.programs.length; i++) {
                            if ($scope.programs[i].code == $scope.workingInquiry.programCode) {
                                $scope.workingInquiry.program = $scope.programs[i];
                                break;
                            }
                        }
                        $scope.workingInquiry.group = {id: $scope.workingInquiry.groupId, name: $scope.workingInquiry.groupName};
                        $scope.inquiryNumbers = [];
                    }, failure
                );
            else
                $scope.workingInquiry = newInquiry();
        }
    }

    function failure(response) {
        error(response.data.error);
        $scope.disableSave = false;
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

    function initDateTimePicker() {
        var datepicker = $('.datepicker');
        datepicker.datetimepicker({
            format: 'YYYY-MM-DD',
            useCurrent: true,
            showTodayButton: true
        });
        datepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });
        var timepicker = $('.timepicker');
        timepicker.datetimepicker({
            format: 'HH:mm'
        });
        timepicker.on('dp.show', function (e) {
            $('.bootstrap-datetimepicker-widget').addClass('open');
        });
        $("#callBackTime").on("dp.change", function () {
            if (typeof $scope.workingInquiry.log == 'undefined') {
                $scope.workingInquiry.log = {};
            }
            $scope.workingInquiry.log.callBackTime = $("#callBackTime").val();
        });
        $("#fromTime").on("dp.change", function () {
            $scope.workingInquiry.fromTime = $("#fromTime").val();
        });
        $("#toTime").on("dp.change", function () {
            $scope.workingInquiry.toTime = $("#toTime").val();
        });
        $("#childDob").on("dp.change", function () {
            $scope.workingInquiry.childDob = $("#childDob").val();
        });
        $("#inquiryDate").on("dp.change", function () {
            // if (typeof $scope.workingInquiry.log == 'undefined') {
            //     $scope.workingInquiry.log = {};
            // }
            $scope.workingInquiry.inquiryDate = $("#inquiryDate").val();
        });
        $("#callBackDate").on("dp.change", function () {
            if (typeof $scope.workingInquiry.log == 'undefined') {
                $scope.workingInquiry.log = {};
            }
            $scope.workingInquiry.log.callBackDate = $("#callBackDate").val();
        });
    }

    refresh();

    initDateTimePicker();
});