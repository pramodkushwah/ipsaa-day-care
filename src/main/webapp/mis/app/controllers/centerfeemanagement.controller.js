app.controller('CenterFeeManagementController', function ($scope, $http) {

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

    var tabs = ['#programfee', '#additionalcharges', '#chargelist'];
    refresh();

    $http.get('/api/center/').then(function (response) {
        $scope.centers = response.data;
    });

    $scope.cgstChecked = function (fee) {
        if (fee.isCgst) {
            fee.isIgst = false;
            fee.igst = null;
        } else {
            fee.cgst = null;
        }
    };
    $scope.sgstChecked = function (fee) {
        if (fee.isSgst) {
            fee.isIgst = false;
            fee.igst = null;
        } else {
            fee.sgst = null;
        }
    };
    $scope.igstChecked = function (fee) {
        if (fee.isIgst) {
            fee.isCgst = false;
            fee.cgst = null;
            fee.isSgst = false;
            fee.sgst = null;
        } else {
            fee.igst = null;
        }
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
        $scope.addcharge = false;
        $scope.addcentercharge = false;
        $scope.addprogramfee = false;
    };

    $scope.editCharge = function (charge) {
        charge.mode = 'Edit';
        $scope.insertedCharge = angular.copy(charge);
        $scope.addcharge = true;
        $scope.addcentercharge = false;
        $scope.addprogramfee = false;

    };

    $scope.newCharge = function () {
        $scope.insertedCharge = {
            mode: 'New'
        };
        $scope.addcharge = true;
        $scope.addcentercharge = false;
        $scope.addprogramfee = false;

    };

    $scope.saveCharge = debounce(function (charge) {
        if ($scope.insertedCharge.mode == 'New') {
            $http.post('/api/charge/', $scope.insertedCharge).then(function (response) {
                $scope.addcharge = false;
                $scope.addcentercharge = false;
                $scope.addprogramfee = false;

                refresh();
                ok("Charge saved");
            }, function (response) {
                error(response.data.error);
            });
        } else if ($scope.insertedCharge.mode == 'Edit') {
            $http.put('/api/charge/', $scope.insertedCharge).then(function (response) {
                $scope.addcharge = false;
                $scope.addcentercharge = false;
                $scope.addprogramfee = false;
                refresh();
                ok("Charge Saved");
            }, function (response) {
                error(response.data.error);
            });
        }
    }, 200, true);

    $scope.cancelCharge = function () {
        $scope.addcharge = false;
    };

    $scope.newCenterCharge = function () {
        $scope.insertCenterCharge = {
            mode: 'New',
            center: $scope.selectedCenter.id
        };
        $scope.addcentercharge = true;
        $scope.addcharge = false;
        $scope.addprogramfee = false;

    };

    $scope.cancelCenterCharge = function () {
        $scope.insertCenterCharge = {
            mode: 'New'
        };
        $scope.addcentercharge = false;
    };

    $scope.saveCenterCharge = debounce(function () {
        var centerCharge = angular.copy($scope.insertCenterCharge);
        centerCharge.centerId = $scope.insertCenterCharge.center;
        centerCharge.chargeId = $scope.insertCenterCharge.charge;

        if ($scope.insertCenterCharge.mode == 'Edit') {
            $http.put('/api/center/charge', centerCharge).then(function (response) {
                $scope.loadCenterFee();
                $scope.addcentercharge = false;
                $scope.addcharge = false;
                $scope.addprogramfee = false;
                refresh();
                ok("Charge saved for center")
            }, function (response) {
                error(response.data.error);
            });
        } else if ($scope.insertCenterCharge.mode == 'New') {
            $http.post('/api/center/charge', centerCharge).then(function (response) {
                $scope.loadCenterFee();
                $scope.addcentercharge = false;
                $scope.addcharge = false;
                $scope.addprogramfee = false;
                refresh();
                ok("Charge saved for center")
            }, function (response) {
                error(response.data.error);
            });
        }
    }, 200, true);

    $scope.editCenterCharge = function (charge) {
        $scope.insertCenterCharge = {
            mode: 'Edit',
            id: ''
        };
        $scope.insertCenterCharge.id = charge.id;
        $scope.insertCenterCharge.center = charge.center.id;
        $scope.insertCenterCharge.charge = charge.charge.id;
        $scope.insertCenterCharge.amount = charge.amount;
        $scope.addcentercharge = true;
        $scope.addcharge = false;
        $scope.addprogramfee = false;

    };

    $scope.deleteCenterCharge = function (charge) {
        $http.delete('/api/center/charge/' + charge.id).then(function () {
            $scope.loadCenterFee();
            refresh();
            ok("Deleted Charge for center");
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.newProgramFee = function () {
        $scope.insertProgramFee = {
            mode: 'New'
        };
        if ($scope.insertProgramFee.address && $scope.insertProgramFee.address.state) {
            $scope.insertProgramFee.center_state = $scope.insertProgramFee.address.state;
        }
        $scope.addprogramfee = true;
        $scope.addcharge = false;
        $scope.addcentercharge = false;
    };

    $scope.editProgramFee = function (fee) {
        $scope.insertProgramFee = angular.copy(fee);
        $scope.insertProgramFee.program = fee.program.id;
        $scope.insertProgramFee.center = fee.center.id;
        $scope.insertProgramFee.mode = 'Edit';
        $scope.addprogramfee = true;
        $scope.addcharge = false;
        $scope.addcentercharge = false;
    };

    $scope.cancelProgramFee = function () {
        $scope.insertProgramFee = {
            mode: 'New'
        };
        $scope.addprogramfee = false;
    };

    $scope.deleteProgramFee = function (fee) {
        $http.delete('/api/center/program/fee/' + fee.id).then(function (response) {
            $scope.addprogramfee = false;
            $scope.addcharge = false;
            $scope.addcentercharge = false;
            $scope.loadCenterFee();
            ok("Removed");
            refresh();
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.saveProgramFee = debounce(function () {
        $scope.insertProgramFee.programId = $scope.insertProgramFee.program;
        $scope.insertProgramFee.centerId = $scope.selectedCenter.id;
        $scope.disableSave = true;
        if ($scope.insertProgramFee.mode == 'New') {
            $http.post('/api/center/program/fee/', $scope.insertProgramFee).then(function (responce) {
                $scope.addprogramfee = false;
                $scope.addcharge = false;
                $scope.addcentercharge = false;
                $scope.disableSave = false;
                $scope.loadCenterFee();
                $scope.cancelProgramFee();
                refresh();
                ok("Saved");
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        } else if ($scope.insertProgramFee.mode = 'Edit') {
            $http.put('/api/center/program/fee/', $scope.insertProgramFee).then(function (response) {
                $scope.addprogramfee = false;
                $scope.addcharge = false;
                $scope.addcentercharge = false;
                $scope.disableSave = false;
                $scope.loadCenterFee();
                refresh();
                ok("Saved");
            }, function (response) {
                $scope.disableSave = false;
                error(response.data.error);
            });
        }
    }, 200, true);


    $scope.loadCenterFee = function () {
        $scope.insertProgramFee = {};
        $scope.addprogramfee = false;
        $scope.feelist = [];
        $http.get('/api/center/' + $scope.selectedCenter.id + '/fee/').then(function (response) {
            var feelist = response.data;
            $scope.feelist = feelist;
            for (var i = 0; i < feelist.length; i++) {
                var fee = feelist[i];
                if ($scope.selectedCenter.address && $scope.selectedCenter.address.state) {
                    fee.center_state = $scope.selectedCenter.address.state;
                }
                fee.isSgst = !!fee.sgst;
                fee.isCgst = !!fee.cgst;
                fee.isIgst = !!fee.igst;
            }
            console.log(feelist);
        });
        $http.get('/api/center/' + $scope.selectedCenter.id + '/charge/').then(function (response) {
            $scope.chargelist = response.data;
        });
    };


    function refresh() {
        $http.get('/api/charge/').then(function (response) {
            $scope.charges = response.data;
        });
        $http.get('/api/program').then(function (response) {
            $scope.programs = response.data;
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