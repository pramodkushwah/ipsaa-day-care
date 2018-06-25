app.controller('InquiriesController', function ($scope, $http, InquiryService, $state, $stateParams) {

    $scope.followupFilters = ['ALL', 'TODAY', 'DUE', 'OPEN'];
    $scope.followupFilter = 'TODAY';

    //also change it to home.controller.js
    $scope.followUpDispositions = ["Callback", "Followup", "NewInquiry", "Revisit"];
    $scope.changeTab = function (index) {
        switch (index) {
            case 1:
                $('#inquiries').addClass("active");
                $('#followups').removeClass("active");
                $scope.show = false;
                break;
            case 2:
                $('#followups').addClass("active");
                $('#inquiries').removeClass("active");
                $scope.show = false;
                break;
        }
    };

    function refresh() {
        loadCenters(function () {
            loadInquiries(null, function () {
                loadFollowUps(null, function () {
                    $('.main-panel').perfectScrollbar('update');
                });
            });
        });
    }

    $scope.loadFollowup = function (inquiryId) {
        if (inquiryId) {
            $state.go('app.followup', {inquiryId: inquiryId});
        }
    };

    function createFollowUpRequest() {
        return {dispositions: $scope.followUpDispositions};
    }

    $scope.addInquiry = function () {
        $state.go('app.inquiry', {id: 'new'});
    };

    $scope.loadInquiry = function (id) {
        if (id) {
            $state.go('app.inquiry', {id: id});
        }
    };

    function loadCenters(success) {
        InquiryService.getCenters(
            function (response) {
                $scope.centers = response.data;
                if (success) {
                    success(response);
                }
            }, failure
        );
    }

    function loadInquiries(center, success) {
        InquiryService.getInquiries(center,
            function (response) {
                $scope.inquiries = response.data;
                if (success) {
                    success(response);
                }
            }, failure
        );
    }

    function loadFollowUps(request, success) {
        if (!request) {
            request = createFollowUpRequest();
        }
        // request.date = moment().startOf('day');
        var day = moment().date();
        var month = moment().month() + 1;
        var year = moment().year();

        switch ($scope.followupFilter) {
            case 'DUE':
                request.to = year + "-" + month + "-" + (day-1);
                break;
            case 'TODAY':
                request.date = year + "-" + month + "-" + day;
                break;
            case 'OPEN':
                request.from = year + "-" + month + "-" + (day+1);
                break;
        }

        $http.post('/api/inquiry/followUps/', request).then(
            function (response) {
                $scope.followUps = response.data;
                if (success) {
                    success(response);
                }
            }, failure
        );
    }

    $scope.centerChanged = function (center) {
        $scope.selectedCenter = center;
        loadInquiries(center);
        var request = createFollowUpRequest();
        request.centerCodes = center ? [center.code] : [];
        loadFollowUps(request);
    };

    $scope.followupFilterChanged = function (filter) {
        $scope.followupFilter = filter;
        $scope.centerChanged($scope.selectedCenter);
    };

    function failure(response) {
        error(response.data.error);
    }

    function error(message) {
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    refresh();

    if ($stateParams.tab && $stateParams.tab == 'followup') {
        // $scope.changeTab(2);
        // $('#followups').tab('show');
        // $('#followups').addClass('active').attr('aria-expanded','true');
    }
});