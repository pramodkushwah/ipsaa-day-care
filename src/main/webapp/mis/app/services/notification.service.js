app.service('NotificationService', function ($http, $rootScope, Auth, $state) {
    var types = ['followup'];
    
    var TIMEOUT = 1000 * 60 * 60;//In milliseconds

    function initFollowupNotification() {
        if (Auth.hasPrivilege('FOLLOWUP_NOTIFICATION')) {
            // followupPopup();
            $http.post('/api/stats/', {}).then(function (response) {
                var data = response.data;
                if (data.todayFollowups || data.previousFollowups) {
                    // console.log(data);
                    var message = 'Today Followups: ' + data.todayFollowups +
                        '\nDue followups: ' + data.previousFollowups;
                    followupPopup(message);
                }
            });

        }
    }

    function followupPopup(message) {
        swal({
            title: message,
            type: 'warning',
            buttonsStyling: false,
            allowOutsideClick: false,
            showCancelButton: true,
            confirmButtonText: 'Go to followups',
            cancelButtonText: 'Remind me later',
            confirmButtonClass: "btn btn-warning",
            cancelButtonClass: 'btn btn-danger'
        }).then(
            function (result) {
                $state.go('app.inquiries', {tab: 'followup'});
            }, function (result) {
                // TODO : Remind later.

            }
        );

    }

    var service = {};
    service.init = function () {
        for (i = 0; i < types.length; i++) {
            var type = types[i];
            switch (type) {
                case 'followup':
                    setInterval(initFollowupNotification, TIMEOUT);
                    break;
            }
        }
    };

    return service;
});