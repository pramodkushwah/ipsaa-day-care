app.controller('StudentMessageController', function ($scope, $http, Upload) {

    refresh();

    $scope.selectedcount = 0;
    var MAX_FILE_SIZE_MB = 10; //File size in mb
    var MAX_FILE_BYTES = 1024 * 1024 * MAX_FILE_SIZE_MB;
    $scope.MAX_FILES = 5;
    $scope.images = [];
    $scope.progress = {hide: true, percent: '0%'};
    $scope.attachments = [];
    $scope.sendDisable = false;
    $scope.count = 0;
    $scope.cids = [];

    $scope.centers = [];
    $scope.programs = [];
    $scope.groups = [];

    $scope.selectedCenter='';
    $scope.selectedProgram='';
    $scope.selectedGroup='';
    $scope.searchStudentName='';
    $scope.allcheckeddisabled = false;

       ////////////
       $scope.ccInput = '';
       $scope.ccInputList = [];


    $scope.filterFunction = function(student) {

        var matched = true;
        if ($scope.searchStudentName) {
            matched = matched && student.fullName.toLowerCase().indexOf($scope.searchStudentName.toLowerCase()) != -1;
        }

        if ($scope.selectedCenter) {
            matched = matched && student.center.code == $scope.selectedCenter;
        }

        if ($scope.selectedProgram) {
            matched = matched && student.program.code == $scope.selectedProgram;
        }

        if ($scope.selectedGroup) {
            matched = matched && student.group.id == $scope.selectedGroup;
        }
        return matched;
    };

    $scope.toggleAll = function (centerCode) {
        $scope.selectedcount = 0;
        var center = null;
        if (centerCode) {
            center = findCenter($scope.centers, centerCode);
            center.selectedStudents = 0;
        } else {
            for (var i = 0; i < $scope.centers.length; i++) {
                var c = $scope.centers[i];
                c.selectedStudents = 0;
            }
        }

        angular.forEach($scope.students, function (student, index, students) {
            if ($scope.filterFunction(student)) {
                student.checked = $scope.allchecked;
            }
            if (student.checked) {
                $scope.selectedcount++;
            }
            if (center) {
                if (center.code == student.center.code && student.checked) {
                    center.selectedStudents++;
                }
            } else {
                if (student.checked) {
                    var studentCenter = findCenter($scope.centers, student.center.code);
                    studentCenter.selectedStudents++;
                }
            }
            if (students.length - 1 == index) {
            }
        });
    };

    $scope.toggleOne = function (student) {
        var center = findCenter($scope.centers, student.center.code);
        if (student.checked) {
            $scope.selectedcount++;
            center.selectedStudents++;
        } else {
            $scope.selectedcount--;
            center.selectedStudents--;
        }
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

    $scope.showSMS = function () {
        $scope.smsbox = !$scope.smsbox;
    };

    $scope.showEmail = function () {
        $scope.emailbox = !$scope.emailbox;
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
            subject: $scope.emailsubject,
            emailcontent: '',
            images: [],
            cc: $scope.ccInputList,
        };

        // 1. removing url image from img
        // 2. putting image and their cid in postobject.images and postobject.cids
        var message = $('#message').clone();
        var imgs=message.find('img');
        for(i=0;i<imgs.length;i++){
            var img = $(imgs[i]);
            var cid = img.attr('cid');
            img.removeAttr('src');
            img.attr('src', 'cid:' + cid);
            if(typeof $scope.images[cid] != 'undefined') {
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
        angular.forEach($scope.students, function (student) {
            if (student.checked) {
                postobject.ids.push(student.id + '');
            }
        });

        Upload.upload(
            {
                url: '/api/send/student/email',
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

    $scope.sendSMS = function () {

        var postobject = {
            ids: [],
            smscontent: $scope.smscontent
        };

        angular.forEach($scope.students, function (student) {
            if (student.checked) {
                postobject.ids.push(student.id);
            }
        });

        $http.post('/api/send/student/sms/', postobject).then(function (response) {
            ok("Successfully sent messages")
        })
    };

    $scope.$watch('file', function (newValue) {
            if (newValue) {
                if (newValue.size <= MAX_FILE_BYTES) {
                    $scope.attachments.push(newValue);
                } else {
                    error("File size exceeds to " + MAX_FILE_SIZE_MB + " MB")
                }
            }
    });

    $scope.removeAttachment = function (index) {
        $scope.attachments.splice(index, 1);
    };

    function refresh() {

        $http.get('/api/program/').then(function (response) {
            $scope.programs = response.data;
        });

        $http.get('/api/group/').then(function (response) {
            $scope.groups = response.data;
        });

        $http.get('/api/center/').then(function (response) {
            $scope.centers = response.data;
            for (var i = 0; i < $scope.centers.length; i++) {
                var center = $scope.centers[i];
                center.selectedStudents = 0;
            }
        });

        $http.post('/api/student/filter/', {}).then(function (response) {
            $scope.students = response.data.students;
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

    document.getElementById('message').addEventListener('paste', function (event) {
        var pastedData = event.clipboardData.items[0];
        if (pastedData.type.indexOf("image") === 0) {
            composeThumbnail(pastedData.getAsFile()); // this still works!
        }
    });

    function composeThumbnail(file) {
        if (!/^image\//.test(file.type)) { // if not an image; 0 since we take only 1 image, if multiple dragg  ed at once, consider only the first one in the array
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

        $scope.images[getCurrentCid()]=file;

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
});