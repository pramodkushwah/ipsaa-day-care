app.controller('StaffMessageController', function ($scope, $http, Upload) {

    var MAX_FILE_SIZE_MB = 10; //File size in mb
    var MAX_FILE_BYTES = 1024 * 1024 * MAX_FILE_SIZE_MB;
    $scope.MAX_FILES = 5;
    $scope.progress = { hide: true, percent: '0%' };
    $scope.attachments = [];
    $scope.sendDisable = false;
    $scope.count = 0;
    $scope.cids = [];
    $scope.images = [];

    $scope.stafflist = [];
    $scope.searchStaffName = '';
    $scope.selectedcount = 0;

    ////////////
    $scope.ccInput = '';
    $scope.ccInputList = [];


    $scope.filterFunction = function (staff) {

        var matched = true;
        if ($scope.searchStaffName) {
            matched = matched && staff.name.toLowerCase().indexOf($scope.searchStaffName.toLowerCase()) != -1;
        }

        if ($scope.selectedCenter) {
            matched = matched && staff.centerCode == $scope.selectedCenter;
        }

        return matched;
    };

    $scope.sendSMS = function () {

        var postobject = {
            ids: [],
            smscontent: $scope.smscontent
        };

        angular.forEach($scope.stafflist, function (staff) {
            if (staff.checked) {
                postobject.ids.push(staff.id);
            }
        });

        $http.post('/api/send/staff/sms/', postobject).then(function (response) {
            ok("Successfully sent messages");
        })
    };

    $scope.onCcAdd = function (value) {
        if ($scope.validateEmail($scope.ccInput)) {

            $scope.ccInputList.push($scope.ccInput);
            $scope.ccInput = '';
        }else{
            error('Please Enter a valid Email');

        }
    }
    $scope.onCcListDelete = function (value) {
        $scope.ccInputList = [];

    }
    $scope.showEnteredEmails = function (value) {
        return $scope.ccInputList.join(', ');

    }



    $scope.validateEmail = function (email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    $scope.sendEmail = function () {
        var postobject = {
            ids: [],
            cids: [],
            files: [],
            cc: $scope.ccInputList,
            subject: $scope.emailsubject,
            emailcontent: '',
            images: []
        };
        var message = $('#message').clone();
        var imgs = message.find('img');
        for (i = 0; i < imgs.length; i++) {
            var img = $(imgs[i]);
            var cid = img.attr('cid');
            img.removeAttr('src');
            img.attr('src', 'cid:' + cid);
            if (typeof $scope.images[cid] != 'undefined') {
                postobject.cids.push(cid);
                postobject.images.push($scope.images[cid]);
            }
        }
        postobject.emailcontent = message.html();
        for (i = 0; i < $scope.attachments.length; i++) {
            if ($scope.attachments[i].size < (1024 * 1024 * MAX_FILE_BYTES))
                postobject.files.push($scope.attachments[i]);
            else {
                error("Attachment size must less then " + MAX_FILE_SIZE + " MB");
                return;
            }
        }
        $scope.sendDisable = true;
        $scope.progress.hide = false;
        $scope.progress.percent = 0;
        angular.forEach($scope.stafflist, function (staff) {
            if (staff.checked) {
                postobject.ids.push(staff.id);
            }
        });
        Upload.upload(
            {
                url: '/api/send/staff/email',
                data: postobject,
                arrayKey: ''
            }
        ).then(
            function (response) {
                ok("Successfully sent emails");
                $scope.progress.hide = true;
                $scope.progress.percent = 0;
                $scope.sendDisable = false;
            }, function (response) {
                error(response.data.error);
                $scope.progress.hide = true;
                $scope.progress.percent = 0;
                $scope.sendDisable = false;
            }, function (event) {
                $scope.progress.percent = ((event.loaded * 100) / event.total).toFixed(2) + "%";
            }
        );
    };

    $scope.$watch('file', function (newValue) {
        if (newValue) {
            if (newValue.size <= MAX_FILE_BYTES) {
                $scope.attachments.push(newValue);
            } else {
                error("File size exceeds to " + MAX_FILE_SIZE_MB + " MB");
            }
        }
    });

    $scope.toggleAll = function () {
        $scope.selectedcount = 0;
        for (var i = 0; i < $scope.centers.length; i++) {
            var center = $scope.centers[i];
            center.selectedStaff = 0;
        }
        angular.forEach($scope.stafflist, function (staff) {

            if ($scope.filterFunction(staff)) {
                staff.checked = $scope.allchecked;
            }

            if (staff.checked) {
                $scope.selectedcount++;
            }
            var center = findCenter($scope.centers, staff.centerCode);
            if (center) {
                if (staff.checked) {
                    center.selectedStaff++;
                }
            }

        })
    };

    $scope.toggleOne = function (staff) {
        var center = findCenter($scope.centers, staff.centerCode);
        if (staff.checked) {
            $scope.selectedcount++;
            if (center) {
                center.selectedStaff = center.selectedStaff ? center.selectedStaff + 1 : 1;
            }
        } else {
            $scope.selectedcount--;
            if (center) {
                center.selectedStaff = center.selectedStaff ? center.selectedStaff - 1 : 0;
            }
        }
    };

    $scope.showSMS = function () {
        $scope.smsbox = !$scope.smsbox;
    };

    $scope.showEmail = function () {
        $scope.emailbox = !$scope.emailbox;
    };

    function findCenter(centers, code) {
        for (var i = 0; i < centers.length; i++) {
            var center = centers[i];
            if (center.code == code) {
                return center;
            }
        }
        return null;
    }

    function refresh() {
        $http.post('/api/staff/filter/', {}).then(function (response) {
            $scope.stafflist = response.data.stafflist;
        });

        $http.get('/api/center/').then(function (response) {
            $scope.centers = response.data;
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

    refresh();

    document.getElementById('message').addEventListener('paste', function (event) {
        var pastedData = event.clipboardData.items[0];
        if (pastedData.type.indexOf("image") === 0) {
            console.log('calling thumbnail function'); // does not show up in the console! o.O
            composeThumbnail(pastedData.getAsFile()); // this still works!
        }
    });

    function composeThumbnail(file) {
        if (!/^image\//.test(file.type)) { // if not an image; 0 since we take only 1 image, if multiple dragged at once, consider only the first one in the array
            console.log('ERROR: Not an image file.');
            return false;
        }

        // compose an <img> for the thumbnail
        var thumbnailImage = document.createElement("img");
        thumbnailImage.setAttribute("cid", generateCid());
        thumbnailImage.setAttribute("style", "width:300px");

        thumbnailImage.file = file;
        // document.getElementById('message').appendChild(thumbnailImage);
        var range = window.getSelection().getRangeAt(0);
        range.deleteContents();
        range.insertNode(thumbnailImage);

        $scope.images[getCurrentCid()] = file;

        var reader = new FileReader();
        reader.onload = function (event) {
            thumbnailImage.src = this.result;
        };
        reader.readAsDataURL(file);
    }

    function generateCid() {
        var val = 'inline_image-';
        $scope.count++;
        val = val + $scope.count;
        return val;
    }

    function getCurrentCid() {
        var val = 'inline_image-';
        val = val + $scope.count;
        return val;
    }







    (function () {
        'use strict';
        angular
            .module('chipsCustomSeparatorDemo', ['ngMaterial'])
            .controller('CustomSeparatorCtrl', DemoCtrl);

        function DemoCtrl($mdConstant) {
            // Use common key codes found in $mdConstant.KEY_CODE...
            this.keys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA];
            this.tags = [];

            // Any key code can be used to create a custom separator
            var semicolon = 186;
            this.customKeys = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA, semicolon];
            this.contacts = ['test@example.com'];
        }
    })();







});