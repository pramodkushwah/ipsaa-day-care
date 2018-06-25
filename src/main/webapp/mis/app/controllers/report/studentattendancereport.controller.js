app.controller('StudentAttendanceReportController', function ($scope, $http) {
    $scope.centers = [];
    $scope.students = [];
    $scope.disableDownload = false;

    $scope.download = function (center) {
        if (!(center && center.id)) {
            error("Please select Center.");
            return;
        }
        if (!$scope.from) {
            error("Please select from date.");
            return;
        }
        if (!$scope.to) {
            error("Please select to date.");
            return;
        }
        var request = {
            centerId: center.id,
            from: $scope.from,
            to: $scope.to
        };
        $scope.disableDownload = true;
        $http.post('/api/report/studentattendance', request, {responseType: 'arraybuffer'}).then(
            function (response) {
                $scope.disableDownload = false;
                var blob = new Blob([response.data], {
                    type: 'application/octet-stream'
                });
                saveAs(blob, response.headers("fileName"));

            }, function (response) {
                $scope.disableDownload = false;
                error(response.data.error);
            }
        );
    };

    function loadCenters() {
        $http.get('/api/center/').then(
            function (response) {
                $scope.centers = response.data;
            }, function (response) {
                error('Fail to centers.');
            }
        );
    }

    function refresh() {
        loadCenters();
    }

    refresh();

    function ok(message) {
        swal({title: message, type: 'success', buttonsStyling: false, confirmButtonClass: "btn btn-warning"});
    }

    function error(message) {
        swal({title: message, type: 'error', buttonsStyling: false, confirmButtonClass: "btn btn-warning"});
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

        $("#to").on("dp.change", function () {
            $scope.to = $("#to").val();
        });
        $("#from").on("dp.change", function () {
            $scope.from = $("#from").val();
        });
    }

    initDateTimePicker();
});