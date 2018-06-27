app.controller('SalaryManagementController', function ($scope, $http, Auth, $filter, $rootScope) {
    $scope.centerFilter = "ALL";
    $scope.edit = false;
    $scope.show = false;
    $scope.add = false;
    $scope.SALARY_WRITE = Auth.hasPrivilege('SALARY_WRITE');
    $scope.salaries = [];

    // pagination code start
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.barsize = 5;//odd value
    $scope.pagebar = [];
    $scope.searchStaff = '';
    $scope.getData = function () {
        var f1 = $filter('filter')($scope.salaries, $scope.searchStaff);
        if ($scope.selectedCenter) {
            f1 = $filter('filter')(f1, {centerCode: $scope.selectedCenter.code});
        }
        return f1;
    };
    $scope.numberOfPages = function () {
        return Math.ceil($scope.getData().length / $scope.pageSize);
    };

    $scope.$watchGroup(['searchStaff', 'selectedCenter'], function () {
        $scope.currentPage = 0;
        $scope.updatePageBar();
    });
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

    // $scope.esiChecked = function (salary) {
    //     if (salary.esid) {
    //         salary.esi
    //     } else {
    //
    //     }
    // };

    $scope.initRunningSalary = {
        pfd: false,
        esid: false,
        profd: false,
        ctc: 0.0,
        basic: 0.0,
        bonus: 0.0,
        conveyance: 0.0,
        entertainment: 0.0,
        otherAllowance: 0.0,
        hra: 0.0,
        medical: 0.0,
        arrears: 0.0,
        shoes: 0.0,
        special: 0.0,
        tiffin: 0.0,
        uniform: 0.0,
        washing: 0.0,
        totalEarning: 0.0,
        esi: 0.0,
        pf: 0.0,
        retention: 0.0,
        tds: 0.0,
        advance: 0.0,
        totalDeduction: 0.0,
        netSalary: 0.0,
        professionalTax: 0.0
    };

    function calculateBasic(salary) {
        var ctc = salary.ctc ? salary.ctc : 0;
        return (ctc * 40) / 100;
    }

    function calculateHRA(salary) {
        var basic = salary.basic ? salary.basic : 0;
        return (basic * 40) / 100;
    }

    function calculateSpecial(salary) {
        console.log($scope.runningSalary);
        return salary.ctc - salary.basic - salary.hra - salary.conveyance - salary.bonus - salary.medical;
    }

    function calculatePFR(salary) {
        console.log("salary check : "+salary.pfd +"   :  "+salary.basic);
        if (salary.pfd && salary.basic <= 15000) {
            return ((salary.basic * 12) / 100).toFixed(0)/1;
        } else {
            return 1800;
        }
    }

    function calculatePFE(salary) {
        console.log("salary check : "+salary.pfd +"   :  "+salary.basic);
        if (salary.pfd && salary.basic <= 15000) {
            return ((salary.basic * 12) / 100).toFixed(0)/1;
        } else {
            return 1800;
        }
    }

    function calculateGross(salary) {
        return salary.ctc - salary.pfr;
        // return salary.basic +
        //     salary.hra +
        //     salary.conveyance +
        //     salary.special -
        //     salary.pfr;
    }

    function calculateESI(salary) {
        if(!salary.esid || salary.grossSalary > 21000){
            return 0;
        }
        return (salary.ctc - salary.pfr - salary.bonus)*1.75/100;
    }

    // $scope.$watch('runningSalary', function () {
    //     if (!$scope.runningSalary) {
    //         return;
    //     }

    //     $scope.runningSalary.basic = calculateBasic($scope.runningSalary);
    //     $scope.runningSalary.hra = calculateHRA($scope.runningSalary);
    //     $scope.runningSalary.special = calculateSpecial($scope.runningSalary);
    //     $scope.runningSalary.pfr = calculatePFR($scope.runningSalary);
    //     $scope.runningSalary.pfe = calculatePFE($scope.runningSalary);
    //     $scope.runningSalary.esi = calculateESI($scope.runningSalary);
    //     $scope.runningSalary.grossSalary = calculateGross($scope.runningSalary);

    //     // $scope.runningSalary.totalEarning = $scope.runningSalary.basic + $scope.runningSalary.bonus + $scope.runningSalary.conveyance + $scope.runningSalary.entertainment
    //     //     + $scope.runningSalary.hra + $scope.runningSalary.medical + $scope.runningSalary.arrears + $scope.runningSalary.shoes + $scope.runningSalary.special +
    //     //     $scope.runningSalary.tiffin + $scope.runningSalary.uniform + $scope.runningSalary.washing;
    //     // $scope.runningSalary.totalEarning = $scope.runningSalary.grossSalary + $scope.runningSalary.bonus;

    //     // $scope.runningSalary.totalDeduction = $scope.runningSalary.retention + $scope.runningSalary.tds + $scope.runningSalary.advance;

    //     if ($scope.runningSalary.pfd) {
    //         $scope.runningSalary.totalDeduction = $scope.runningSalary.totalDeduction + $scope.runningSalary.pfe;
    //         $scope.runningSalary.totalDeduction = $scope.runningSalary.totalDeduction + $scope.runningSalary.pfr;
    //     }

    //     if ($scope.runningSalary.esid) {
    //         $scope.runningSalary.totalDeduction = $scope.runningSalary.totalDeduction + $scope.runningSalary.esi;
    //     }

    //     if ($scope.runningSalary.profd) {
    //         $scope.runningSalary.totalDeduction = $scope.runningSalary.totalDeduction + $scope.runningSalary.professionalTax;
    //     }

    //     $scope.runningSalary.netSalary =
    //         $scope.runningSalary.grossSalary + $scope.runningSalary.bonus
    //         - $scope.runningSalary.pfe - $scope.runningSalary.esi -$scope.runningSalary.professionalTax;

    //     //Roundoff
    //     $scope.runningSalary.totalEarning = Math.round(($scope.runningSalary.totalEarning * 100) / 100);
    //     $scope.runningSalary.totalDeduction = Math.round(($scope.runningSalary.totalDeduction * 100) / 100);
    //     $scope.runningSalary.netSalary = Math.round(($scope.runningSalary.netSalary * 100) / 100);
    // }, true);


    $scope.onChange = function(){
        $scope.runningSalary.basic = calculateBasic($scope.runningSalary);
        $scope.runningSalary.hra = calculateHRA($scope.runningSalary);
        $scope.runningSalary.special = calculateSpecial($scope.runningSalary);
        $scope.runningSalary.pfr = calculatePFR($scope.runningSalary);
        $scope.runningSalary.pfe = calculatePFE($scope.runningSalary);
        $scope.runningSalary.grossSalary = calculateGross($scope.runningSalary);
        $scope.runningSalary.esi = calculateESI($scope.runningSalary);
        $scope.runningSalary.totalDeduction = 0;
        if ($scope.runningSalary.pfd) {
            $scope.runningSalary.totalDeduction += $scope.runningSalary.pfe;
            $scope.runningSalary.totalDeduction += $scope.runningSalary.pfr;
        }

        if ($scope.runningSalary.esid) {
            $scope.runningSalary.totalDeduction += $scope.runningSalary.esi;
        }else{            
            $scope.runningSalary.totalDeduction += 0;
        }

        if ($scope.runningSalary.profd) {
            if($scope.runningSalary.ctc>=12000){
                $scope.runningSalary.professionalTax = 200;
            }
            $scope.runningSalary.totalDeduction += $scope.runningSalary.professionalTax;
        } else {
            $scope.runningSalary.professionalTax = 0;
        }

        $scope.runningSalary.netSalary = Math.round($scope.runningSalary.ctc  - $scope.runningSalary.totalDeduction);        
    }

    $scope.addSalary = function () {
        $scope.add = true;
        $scope.edit = false;
        $scope.show = false;
        $scope.runningSalary = angular.copy($scope.initRunningSalary);
    };

    $scope.showSalary = function (salary) {
        $scope.show = true;
        $scope.edit = false;
        $scope.add = false;
        $scope.runningSalary = angular.copy($scope.initRunningSalary)
        $scope.runningSalary = salary;
    };

    $scope.editSalary = function (salary) {
        $scope.edit = true;
        $scope.add = false;
        $scope.show = false;
        $scope.runningSalary = angular.copy(salary);
    };

    $scope.cancelSalary = function () {
        $scope.edit = false;
        $scope.show = false;
        $scope.add = false;

    };

    $scope.saveSalary = function (salary) {
        var request = {
            ctc: salary.ctc,
            eid: salary.eid,
            basic: salary.basic,
            bonus: salary.bonus,
            conveyance: salary.conveyance,
            entertainment: salary.entertainment,
            hra: salary.hra,
            medical: salary.medical,
            arrears: salary.arrears,
            shoes: salary.shoes,
            special: salary.special,
            tiffin: salary.tiffin,
            uniform: salary.uniform,
            washing: salary.washing,
            retention: salary.retention,
            tds: salary.tds,
            advance: salary.advance
        };

        request.esid = !!salary.esid;
        request.pfd = !!salary.pfd;
        request.profd = !!salary.profd;
        if (request.profd) {
            request.professionalTax = salary.professionalTax;
        }
        if (request.esid) {
            request.esi = salary.esi;
        }
        if (request.pfd) {
            request.pfe = salary.pfe;
            request.pfr = salary.pfr;
        }

        $http.post('/api/employee/salary/update', request).then(
            function successCallback(response) {
                if (add) {
                    $scope.cancelSalary();
                    init();
                    ok("Salary Saved Successfully.");
                }
            }, function errorCallback(response) {
                if (add) {
                    $scope.cancelSalary();
                    error(response.data.error);
                }
            });
    };

    $scope.employeeChanged = function (employee) {
        if (employee) {
            $scope.runningSalary.eid = employee.eid;
            $scope.employer = employee.employer;
            $http.get('/api/employee/salary/' + employee.eid).then(
                function (respose) {
                    $scope.runningSalary = respose.data;
                    $scope.edit = true;
                }, function (response) {
                    if (response.status == 404) {
                        $scope.runningSalary = angular.copy($scope.initRunningSalary);
                        $scope.runningSalary.eid = employee.eid;
                        $scope.runningSalary.employer = employee.employer;
                        $scope.add = true;
                    } else {
                        error(response.data.message);
                    }
                });
        } else {
            $scope.runningSalary.eid = '';
        }
    };

    function init() {
        $http.get('/api/center/').then(function (response) {
            $scope.centerList = response.data;
        }, function (response) {
            error(response.data.error);
        });

        $http.post('/api/staff/filter', {active: true}).then(function (response) {
            $scope.employees = response.data.stafflist;
            $scope.updatePageBar();
        }, function (response) {
            error(response.data.error);
        });

        $http.get('/api/employee/salary').then(
            function (response) {
                $scope.salaries = response.data;
            }, function (response) {
                error(response.data.error);
            }
        );
    }

    init();

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
app.filter('centerNameFilter', function () {
    return function (input, centerName) {
        var out = [];
        if (typeof input === 'object') {
            if (typeof centerName === 'object' || typeof centerName === 'string') {
                if (centerName != null || centerName != '') {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].centerName == centerName) {
                            out.push(input[i]);
                        }
                    }
                }
            }
        }
        return out;
    }
});