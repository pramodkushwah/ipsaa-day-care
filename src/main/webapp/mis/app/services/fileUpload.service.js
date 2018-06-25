/**
 * Created by abhishek on 8/4/17.
 */

app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function (file, uploadUrl, dryRun, sync) {
        var fd = new window.FormData();
        fd.append('file', file);
        fd.append('dryRun', dryRun);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .then(function successCallback(response) {
                sync(response.status, response.data);
            }, function errorCallback(response) {
                sync(response.status, response.data);
            });
    };
}]);