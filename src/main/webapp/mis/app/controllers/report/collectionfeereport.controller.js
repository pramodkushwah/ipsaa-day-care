app.controller('CollectionFeeReportController', function ($http, $scope) {

    $scope.loader = '';
    //populate months, years, quarter dropdown
    $scope.quarters = [
        {value: 1, name: "FYQ4"},
        {value: 2, name: "FYQ1"},
        {value: 3, name: "FYQ2"},
        {value: 4, name: "FYQ3"}
    ];
    var allmonths = moment.months();
    $scope.months = [];
    $scope.years = [moment().year() - 1, moment().year(), moment().year() + 1];
    for (var i = 0; i < allmonths.length; i++) {
        var month = allmonths[i];
        $scope.months.push({moy: i + 1, name: allmonths[i]});
    }

    //set current month, quarter and year

    /*  
    type : string =  'excel' | 'table' 
        'excel'-- to download report as excel file
        'table'-- to generate table in the view

    txn_status : string = true | false
    */
    $scope.generateReport = function (type, txn_status) {
         
        if ( !$scope.selectedCenter ) {
            error("Select Center");
            return;
        }

        if ( !$scope.selectedQuarter ) {
            error("Select Quarter");
            return;
        }

        if ( !$scope.selectedYear ) {
            error("Select Year");
            return;
        }

        var postobject = {
            centerCode: $scope.selectedCenter.code,
            period: 'Quarterly',
            reportType: 'Paid'
        };

        postobject.quarter = $scope.selectedQuarter.value ? $scope.selectedQuarter.value: 0;

        postobject.year  = $scope.selectedYear;
        $scope.loader = type == "excel" ? "disableDownload" : "disableGet";

        var reqUrl = '/api/report/collectionfee';
        reqUrl = type == "excel" ? reqUrl + '/excel' : reqUrl;
        var resType = type == "table" ? 'json' : 'arrayBuffer';
        
        // need to send in post request body for confirmed and unconfirmed transactions
        if (txn_status) {
            if(txn_status == "confirmed")
                postobject.confirm = true;
            else
                postobject.confirm = false;
        } 

        $http.post(reqUrl, postobject, { responseType: resType }).then(

            function (response) {
                $scope.loader = '';
                if(type == 'table') {
                    $scope.feeReports = response.data;
                }
                else {
                    var blob = new Blob([response.data], {
                        type: 'application/octet-stream'
                    });
                    saveAs(blob, response.headers("fileName"));
                }
                
            },function (response) {
                $scope.loader = '';
                error(response.data.error);
            }
        );


    };

    $scope.confirmTransaction = function(txnId) {

        var res = {
            id: txnId,
            confirmed : true
        };

        $http.put('/api/student/payfee', res).then(
            function(response) {
                console.log(response);
            },
            function(error) {
                console.log(error);
            }

        );
    }


    function loadCenters() {
        $http.get('/api/center/').then(
            function (response) {
                $scope.centers = response.data;
                $scope.centers.unshift({code: 'All', name: 'All'});
                if (Array.isArray($scope.centers) && $scope.centers.length == 1) {
                    $scope.selectedCenter = $scope.centers[0];
                }
            }, function (response) {
                error(response.data.error);
            }
        );
    }

    function refresh() {
        loadCenters();
    }

    refresh();
});