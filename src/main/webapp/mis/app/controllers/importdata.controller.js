/**
 * Created by abhishek on 15/4/17.
 */

app.controller('ImportDataController', function ($scope, $http, fileUpload) {
    $scope.importUrl = "";

    $scope.importUrlValidator = function () {
        switch ($scope.importType) {
            case 'Employee' :
                $scope.importUrl = '/api/staff/import';
                break;
            case 'Employee Salary' :
                $scope.importUrl = '/api/staff/import/salary';
                break;
            case 'Student' :
                $scope.importUrl = '/api/student/import';
                break;
            default :
                $scope.importUrl = '';
        }
        if ($scope.importUrl == '') {
            return false;
        } else {
            return true;
        }
    };

    $scope.upload = function () {
        if ($scope.importUrlValidator()) {
            var file = $scope.file;
            $scope.results = [];
            $scope.message = "Processing file ...";
            fileUpload.uploadFileToUrl(file, $scope.importUrl, $scope.dryRun, function (status, data) {
                if (status == 200) {
                    $scope.message = "File successfully processed.";
                } else {
                    $scope.message = "Error processing file !!!";
                }
                for (var i in data) {
                    $scope.results.push({"key": i, "message": data[i]});
                }
            });
        }
    }

    $scope.importType = "";

    $scope.dryRun = "true";

    $scope.changeDryRun = function () {
        if ($scope.dryRun == "true") {
            $scope.dryRun = "false";
        } else {
            $scope.dryRun = "true";
        }
        $scope.message = "";
    }

    $scope.changeImportType = function (importType) {
        $scope.results = [];
        $scope.importType = importType;
        $scope.message = "";
    }
});