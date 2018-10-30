app.controller('StudentFeeSlipController', function ($scope, $http) {
    
    $scope.quarters = [
        {value: 1, name: "FYQ4"},
        {value: 2, name: "FYQ1"},
        {value: 3, name: "FYQ2"},
        {value: 4, name: "FYQ3"}
    ];
    
    $scope.years = [moment().year() - 1,moment().year()];
    $scope.generateSlipDisable = false;
    $scope.generateSlipPdfDisable = false;
    $scope.slipEmail = {};
    $scope.checkedSlipCount = 0;
    $scope.sendPaymentLinkDisable = false;
    $scope.reGenerateSlipLoader = false;
    $scope.slectedElementList = []
    $scope.generateSlip = false;
    $scope.selectedPeriod = 'Quarterly';
    $scope.selectedQuarter = Math.floor(new Date().getMonth() / 3) + 1;
    $scope.selectedYear = new Date().getFullYear();
    $scope.monthNames = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December'
    ];

    $scope.quarterNames = [
        'FYQ4',
        'FYQ1',
        'FYQ2',
        'FYQ3'
    ];
    

    $scope.day = moment().date();
    $scope.month = moment().month() + 1;
    $scope.year = moment().year();

    $scope.dropdownData = [];

    //populate months
    function populateMonths() {
        $scope.dropdownData.push({
            duration: "Monthly",
            month: $scope.month,
            year: $scope.year
        });
        if ($scope.day > 15) {
            if ($scope.month == 12) {
                $scope.dropdownData.push({
                    duration: "Monthly",
                    month: 1,
                    year: $scope.year + 1
                });
            } else {
                $scope.dropdownData.push({
                    duration: "Monthly",
                    month: $scope.month + 1,
                    year: $scope.year
                });
            }
        }
    }

    populateMonths();

    // populate quarter
    function populateQuarters() {
        switch ($scope.month) {
            case 1:
            case 2:
            case 3:
                $scope.dropdownData.push({
                    duration: "Quarterly",
                    quarter: 1,
                    year: $scope.year - 1
                });
                break;
            case 4:
            case 5:
            case 6:
                $scope.dropdownData.push({
                    duration: "Quarterly",
                    quarter: 2,
                    year: $scope.year
                });
                break;
            case 7:
            case 8:
            case 9:
                $scope.dropdownData.push({
                    duration: "Quarterly",
                    quarter: 3,
                    year: $scope.year
                });
                break;
            case 10:
            case 11:
            case 12:
                $scope.dropdownData.push({
                    duration: "Quarterly",
                    quarter: 4,
                    year: $scope.year
                });
                break;
        }

        if ($scope.month == 3 && $scope.day > 15) {
            $scope.dropdownData.push({
                duration: "Quarterly",
                quarter: 2,
                year: $scope.year
            });
        } else if ($scope.month == 6 && $scope.day > 15) {
            $scope.dropdownData.push({
                duration: "Quarterly",
                quarter: 3,
                year: $scope.year
            });
        } else if ($scope.month == 9 && $scope.day > 15) {
            $scope.dropdownData.push({
                duration: "Quarterly",
                quarter: 4,
                year: $scope.year
            });
        } else if ($scope.month == 12 && $scope.day > 15) {
            $scope.dropdownData.push({
                duration: "Quarterly",
                quarter: 1,
                year: $scope.year
            });
        }

    }

    populateQuarters();

    function populateYears() {
        if ($scope.month == 12 || $scope.month == 1) {
            $scope.dropdownData.push({
                duration: 'Yearly',
                year: $scope.year + 1
            });
        }
    }

    populateYears();

    $scope.getYears = function (duration, selectedMonth, selectedQuarter) {
        var out = [];
        for (var i = 0; i < $scope.dropdownData.length; i++) {
            var obj = $scope.dropdownData[i];
            if (typeof  duration != 'undefined') {
                switch (duration) {
                    case 'Monthly':
                        if (obj.duration == 'Monthly'
                            && typeof selectedMonth != 'undefined'
                            && selectedMonth == obj.month) {
                            out.push(obj.year);
                        }
                        break;
                    case 'Quarterly':
                        if (obj.duration == 'Quarterly'
                            && typeof selectedQuarter != 'undefined'
                            && selectedQuarter == obj.quarter) {
                            out.push(obj.year);
                        }
                        break;
                    case 'Yearly':
                        if (obj.duration == 'Yearly') {
                            out.push(obj.year);
                        }
                        break;
                }
            }
        }
        //remove duplicate years from out array
        var set = {};
        for (var i = 0; i < out.length; i++) {
            var obj1 = out[i];
            if (typeof set[obj1] == 'undefined') {
                set[obj1] = true;
            }
        }
        out = [];
        for (var i in set) {
            out.push(i);
        }
        return out;
    };

    $scope.getFeeSlips = function () {
        if (validateRequest()) {
            var postobject = {
                centerCode: $scope.selectedCenter.code,
                period: $scope.selectedPeriod,
                month: $scope.selectedMonth ? $scope.selectedMonth : 0,
                quarter: $scope.selectedQuarter ? $scope.selectedQuarter : 0,
                year: $scope.selectedYear
            };
            $scope.generateSlipDisable = true;
            $http.post('/api/student/feeslip/generate', postobject).then(function (response) {
                $scope.generateSlipDisable = false;
                $scope.studentfeelist = response.data;
                angular.forEach($scope.studentfeelist, function (studentFee) {

                    studentFee.isAnnualFee = studentFee.annualFee != null;
                    studentFee.isDeposit = studentFee.deposit != null;

                });
                $scope.showDetails(null);
            }, function (response) {
                $scope.generateSlipDisable = false;
                error(response.data.error);
            });
        }
    };

    function validateRequest() {
        if (!$scope.selectedCenter) {
            error("Select Center");
            return false;
        }

        if (!$scope.selectedQuarter) {
            error("Select Quarter");
            return false;
        }

        if (!$scope.selectedYear) {
            error("Select Year");
            return false;
        }
        return true;
    }

    $scope.toggleAll = function (allchecked) {
        $scope.checkedSlipCount = 0;
        if ($scope.studentfeelist) {
            for (i = 0; i < $scope.studentfeelist.length; i++) {
                $scope.studentfeelist[i].selected = allchecked;
                if (allchecked) {
                    $scope.checkedSlipCount++;
                }
            }
        }
    };

    $scope.toggleOneSlip = function (slip) {
        if (slip.selected) {
            $scope.checkedSlipCount++;
        } else {
            $scope.checkedSlipCount--;
        }
    };

    $scope.sendSlipEmail = function (studentfeelist, slipEmail) {
        var list = [];
        for (var i = 0; i < studentfeelist.length; i++) {
            var slip = studentfeelist[i];
            if (slip.selected) {
                list.push(slip.id);
            }
        }
        if (list.length == 0) {
            error("Please select al least on slip.");
            return;
        }

        slipEmail.slipIds = list;
        slipEmail.body = $("#slipEmailMessage").clone().html();
        $scope.sendPaymentLinkDisable = true;
        $http.post('/api/student/paymentLink/', slipEmail).then(
            function (response) {
                $scope.sendPaymentLinkDisable = false;
                ok("Successfully sent emails");
            }, function (response) {
                $scope.sendPaymentLinkDisable = false;
                error(response.data.error);
            }
        );

    };

    $scope.cancelSlipEmail = function () {
        $scope.slipEmail = {};
        $scope.showPanel = "";
    };

    $scope.downloadSlips = function () {
        if (validateRequest()) {
            $scope.slectedElementList = [];
            for (var i = 0; i < $scope.studentfeelist.length; i++) {
                var slip = $scope.studentfeelist[i];
                if (slip.selected) {
                    $scope.slectedElementList.push(slip.id);
                }
            }
            $scope.generateSlipPdfDisable = true;
            $http.post('/api/student/feeslips/pdf', $scope.slectedElementList, {responseType: 'arraybuffer'}).then(
                function (response) {
                    $scope.generateSlipPdfDisable = false;
                    var blob = new Blob([response.data], {
                        type: 'application/octet-stream'
                    });
                    saveAs(blob, response.headers("fileName"));
                }, function (response) {
                    $scope.generateSlipPdfDisable = false;
                    error(response.data.error);
                }
            );

        }
    };

    $scope.generateFeeSlip = function () {
        console.log($scope.studentfeelist.length);
        if (validateRequest()) {
            $scope.slectedElementList = [];
            for (var i = 0; i < $scope.studentfeelist.length; i++) {
                var slip = $scope.studentfeelist[i];
                if (slip.selected) {
                    $scope.slectedElementList.push(slip.id);
                }
            }
            $scope.generateSlip = true;
            $http.post('/api/student/feeslip/generate-all', $scope.slectedElementList, {responseType: 'arraybuffer'}).then(()=>{
                $scope.getFeeSlips();
                $scope.generateSlip = false;
            });
        }
    }

    $scope.cancelStudentSlip = function () {
        $scope.showPanel = "slip";
        $scope.selected = {};
    };

    $scope.reGenerateSlip = function (id) {
        swal({
            title: 'Are you sure?',
            text: "You want to regenerate Slip",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'From Start',
            cancelButtonText: 'From Specific Date'
        }).then(function () {
            $http.post('/api/student/feeslip/regenerate', {id: id}).then(
                function (response) {
                    $scope.selected = response.data;
                    $scope.getFeeSlips();
                    ok("Slip Successfully Regenerated.");
                },
                function (response) {
                    error(response.data.error)
                });
        }, function(){
            swal({
                title: 'Specific Date',
                // html: `<input type="date">`,
                input:'text',
                inputPlaceholder: new Date().toISOString(),
                showCancelButton: true,
                buttonsStyling: false,
            }).then(function(date){
                $http.post('/api/student/feeslip/regenerate', {id: id, spaceifyRegenrationDate: date}).then(
                    function (response) {
                        $scope.selected = response.data;
                        $scope.getFeeSlips();
                        ok("Slip Successfully Regenerated.");
                    },
                    function (response) {
                        error(response.data.error)
                    });                
            })
        });
    };

    $scope.reGenerateFeeSlips = function(){
        console.log("asdfsd");
        
        $scope.slectedElementList = [];
        for (var i = 0; i < $scope.studentfeelist.length; i++) {
            var slip = $scope.studentfeelist[i];
            if (slip.selected) {
                $scope.slectedElementList.push(slip.id);
            }
        }
        $scope.reGenerateSlipLoader = true;
    
        $http.post('/api/student/feeslip/regenerateAll',$scope.slectedElementList, {responseType: 'arraybuffer'}).then(function(response){
            $scope.getFeeSlips();
            $scope.reGenerateSlipLoader = false;
            ok("Slips regenerated Successfully");
        },function(error){

        });
    }

    $scope.saveStudentSlip = function () {
        if (!$scope.selected.isDeposit) {
            $scope.selected.deposit = null;
        }
        if (!$scope.selected.isAnnualFee) {
            $scope.selected.annualFee = null;
        }
        $http.post('/api/student/feeslip', $scope.selected).then(function (response) {
            $scope.getFeeSlips();
            ok("Saved!")
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.setSlips = function (slips) {
        $scope.studentfeelist = slips;
    };

    $scope.showDetails = function (studentfee) {
        console.log(studentfee);
        
        if (!studentfee) {
            $scope.showPanel = "";
            $scope.selected = {};
        } else {
            $scope.showPanel = "slip";
            $scope.selected = angular.copy(studentfee);
            $scope.selected.actualBaseFee = $scope.selected.totalFee - $scope.selected.adjust - $scope.selected.balance - $scope.selected.latePaymentCharge - $scope.selected.extraCharge;
            // $scope.calculateFinalFee($scope.selected);
            $scope.selected.finalBaseFee = ($scope.selected.baseFee * ((100 - $scope.selected.baseFeeDiscount)/100)).toFixed(2);
            $scope.selected.finalTransportFees = $scope.selected.transportFee * $scope.selected.feeRatio;
            if($scope.selected.gstAmount){
                $scope.selected.gstFee = Number(((Number($scope.selected.finalAnnualCharges)) * 0.18).toFixed(2));//annual-fee-gst
                if($scope.selected.feeRatio){
                    $scope.selected.baseFeeGst = Number((((Number($scope.selected.finalBaseFee) * $scope.selected.feeRatio)) * 0.18).toFixed(2));
                } else {
                    $scope.selected.baseFeeGst = Number((((Number($scope.selected.finalBaseFee) * 3)) * 0.18).toFixed(2));
                }
            } else {
                $scope.selected.gstFee = 0;
                $scope.selected.baseFeeGst = 0;
            }
        }
    };

    $scope.calculateFinalFee = function (slip) {

        var totalFee;
        $scope.studentfeelist.filter( (fee) => {
          if(fee.id === slip.id) {
            totalFee = fee.totalFee - fee.extraCharge - fee.latePaymentCharge - fee.adjust - fee.uniformCharges - fee.stationary - fee.balance;
          }
        });

        if (slip.latePaymentCharge && !isNaN(slip.latePaymentCharge)) {
            totalFee = totalFee + slip.latePaymentCharge;
        }

        if (slip.extraCharge && !isNaN(slip.extraCharge)) {
            totalFee = totalFee + slip.extraCharge;
        }

        if (slip.uniformCharges && !isNaN(slip.uniformCharges)) {
            totalFee = totalFee + slip.uniformCharges;
        }

        if (slip.stationary && !isNaN(slip.stationary)) {
            totalFee = totalFee + slip.stationary;
        }

        if (slip.adjust && !isNaN(slip.adjust)) {
            totalFee = totalFee - slip.adjust;
        }

        if (slip.balance && !isNaN(slip.balance)) {
                    totalFee = totalFee + slip.balance;
                }

        slip.totalFee = parseInt(totalFee + "");
    };

    $http.get('/api/center/').then(function (response) {
        $scope.centers = response.data;
        if(Math.floor(new Date().getMonth() / 3)===3){
            $scope.years.push(moment().year()+1);
        }
    });

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


