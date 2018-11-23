app.controller('StudentController', function ($scope, $http, fileUpload, $localStorage, $filter, StudentFeeService) {
    var tabs = ['#studentprofile', '#photo', '#fatherprofile', '#motherprofile', '#fee'];
    $scope.disableSave = false;
    $scope.activeFilter = true;
    $scope.privileges = [];
    $scope.workingStudent = newStudent();
    $scope.webCamOpen = false;
    $scope.pdfGenerate = false;

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

    $scope.hasPrivilege = function (privilege) {
        var size = $scope.privileges.length;
        var i = 0;
        for (i = 0; i < size; i++) {
            if ($scope.privileges[i] == privilege) {
                return true;
            }
        }
        return false;
    };

    $scope.programFilter = "ALL";

    refresh();
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.barsize = 5;//odd value
    $scope.pagebar = [];
    $scope.searchStudent = '';
    $scope.getData = function () {
        return $filter('filter')($scope.students, $scope.searchStudent);
    };
    $scope.numberOfPages = function () {
        return Math.ceil($scope.getData().length / $scope.pageSize);
    };
    $scope.$watch('searchStudent', function (newValue, oldValue) {
        if (oldValue != newValue) {
            $scope.currentPage = 0;
            $scope.updatePageBar();
        }
    }, true);
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

    $http.get('/api/program/').then(function (response) {
        $scope.programList = response.data;
    });

    $scope.changeFilter = function (filter) {
        $scope.programFilter = filter;
        refresh();
    };

    $scope.setFiles = function (element) {
        $scope.imgfile = element.files[0];
        displayImage($scope.imgfile);
    };

    function displayImage(file) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
            $scope.$apply(function () {
                $scope.studentImageURI = reader.result;
            });
        };
        reader.onerror = function (error) {
            error("Failed to load image:" + error);
        };
    }

    $scope.uploadImage = function () {
        fileUpload.uploadFileToUrl($scope.imgfile, "/api/student/" + $scope.workingStudent.id + "/profile-pic", false, function (status, data) {
            if (status == 200) {
                ok("Profile image uploaded successfully!");
            } else {
                $scope.message = "Error processing file !!!";
                error("Failed to Upload image!");
            }
        });
    };

    $scope.browseImage = function () {
        $('#studentimg').trigger('click');
    };

    $scope.selectTab = function (index) {
        for (var i = 0; i < tabs.length; i++) {
            var tab = tabs[i];
            if (tab === tabs[index]) {
                $(tab).addClass("active");
            } else {
                $(tab).removeClass("active");
            }
          $('[data-toggle="tooltip"]').tooltip();
        }
    };

    $scope.bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-', 'NA'];
    $scope.isIpsaaclub = false;
    $scope.showStudent = function (student, callback, corporateChanged) {
        $scope.workingStudent = null;
        $http.get('/api/student/' + student.id).then(function (response) {
            $scope.workingStudent = response.data;
            $scope.selectedStudent = JSON.parse(JSON.stringify(response.data));     
            $scope.workingStudent.mode = "Show";
            $scope.workingStudent.centerId = $scope.workingStudent.center.id + "";
            $scope.getProgramsByCenter($scope.workingStudent.centerId);
            $scope.getGroupsByProgram($scope.workingStudent.program);
            $scope.workingStudent.programId = $scope.workingStudent.program.id + "";
            $scope.workingStudent.groupId = $scope.workingStudent.group.id + "";
            for (var i = 0; i < $scope.workingStudent.parents.length; i++) {
                var parent = $scope.workingStudent.parents[i];
                $scope.workingStudent[parent.relationship.toLowerCase()] = parent;
            }
            $scope.showstudent = true;
            $scope.editstudent = false;
            $scope.addstudent = false;

            // if corporate is checked or unchecked then update
            if(typeof corporateChanged !== "undefined")
              $scope.workingStudent.corporate = corporateChanged;
              
            if($scope.workingStudent.program.id === 72932732558618){
                $scope.isIpsaaclub = true;
            } else {
                $scope.isIpsaaclub = false;
            }
            
            callback(); // function updateFields() to update fields in front-end coming from backend with different names 
            setupProfilePic($scope.workingStudent);
        });
    };

    function setupProfilePic(student) {
        if (student.studentImageData) {
            $scope.studentImageURI = "data:image/PNG;base64," + student.studentImageData;
        } else {
            $scope.studentImageURI = "/assets/img/student.png";
        }
    }

    $scope.createParentAccount = function (parent) {
        $http.get('/api/student/createParentAccount/' + parent.id).then(
            function () {
                ok("Account created successfully!");
                $scope.showstudent = false;
                $scope.editstudent = false;
                $scope.addstudent = false;
                $scope.workingStudent = newStudent();
            },
            function (response) {
                error(response.data.error);
            }
        );

    };

    $scope.editStudent = function (student, callback, corporateChanged) {
        $scope.workingStudent = null;
        $http.get('/api/student/' + student.id).then(function (response) {
            $scope.workingStudent = response.data;
            console.log(JSON.parse(JSON.stringify($scope.workingStudent)));
            $scope.selectedStudent = JSON.parse(JSON.stringify($scope.workingStudent));
            $scope.workingStudent.mode = "Edit";
            $scope.workingStudent.centerId = $scope.workingStudent.center.id + "";
            $scope.workingStudent.programId = $scope.workingStudent.program.id + "";
            $scope.workingStudent.groupId = $scope.workingStudent.group.id + "";
            if($scope.workingStudent.program.id === 72932732558618){
                $scope.isIpsaaclub = true;
            } else {
                $scope.isIpsaaclub = false;
            }
            delete $scope.workingStudent.center;
            delete $scope.workingStudent.program;
            delete $scope.workingStudent.group;

            if ($scope.workingStudent.parents) {
                for (var i = 0; i < $scope.workingStudent.parents.length; i++) {
                    var parent = $scope.workingStudent.parents[i];
                    $scope.workingStudent[parent.relationship.toLowerCase()] = parent;
                }
            }
            $scope.showstudent = false;
            $scope.editstudent = true;
            $scope.addstudent = false;

            // if corporate is checked or unchecked then update
            if(typeof corporateChanged !== "undefined")
              $scope.workingStudent.corporate = corporateChanged;
            
            callback();
            setupProfilePic($scope.workingStudent);
        });
    };

    $scope.updateFields = function() {
      // if student is of corporate hide fee
      if ($scope.workingStudent.corporate) {
        delete $scope.workingStudent.fee;
        return;
      }
      var fee = $scope.workingStudent.fee;
      fee.admissionFee = fee.admissionCharges;
      fee.deposit = fee.securityDeposit;
      fee.finalBaseFee = (fee.finalBaseFee / 3);
      StudentFeeService.calculateGstFee(fee, $scope.workingStudent);
      delete fee;
    };

    $scope.addStudent = function () {
        $scope.isIpsaaclub = false;
        $scope.addstudent = true;
        $scope.showstudent = false;
        $scope.editstudent = false;
        setupProfilePic($scope.workingStudent);
        $scope.workingStudent = newStudent();
    };

    function newStudent() {
        var x = new Date();
        var month = x.getMonth() + 1;
        var student = {
            mode: 'New',
            active: true, id: null, corporate: false,
            parentAccount: null,
            father: {
                id: null,
                residentialAddress: {},
                officeAddress: {}
            },
            mother: {
                id: null,
                residentialAddress: {},
                officeAddress: {}
            },
            admissionDate: x.getFullYear() + '-' + month + '-' + x.getDate()
        };
        return student;
    }


    $scope.saveStudent = debounce(function (student) {
        // var postStudent = jQuery.extend(true, {}, student);
        var postStudent = jQuery.extend(true, {}, student);

        postStudent.father.relationship = "Father";
        postStudent.mother.relationship = "Mother";

        if (!postStudent.hasSibling && !postStudent.siblingId) {
            postStudent.parents = [postStudent.father, postStudent.mother];
        }

        delete postStudent.father;
        delete postStudent.mother;

        if (validateStudent(postStudent)) {
          // to update existing student record on click of save button because admission no. already found
          if (postStudent.admissionNumber) {              
              const admissionDateChanged = postStudent.admissionDate != $scope.selectedStudent.admissionDate;
              const programIdChanged = postStudent.programId != $scope.selectedStudent.program.id;
              console.log(programIdChanged);
              const formalSchoolChanged = postStudent.formalSchool != $scope.selectedStudent.formalSchool;
              if(admissionDateChanged||programIdChanged||formalSchoolChanged){
                  var message = "";
                  (admissionDateChanged) ? message+=" Admission Date, ": "";
                  (programIdChanged) ? message+=" Program, ": "";
                  (formalSchoolChanged) ? message+=" Formal School, ": "";
                  showConfirmationSwal('you want to change'+message+' It may affect Student Fee',function(){
                    $scope.disableSave = true;
                    $http.put("/api/student/", postStudent).then(function (response) {
                        $scope.addstudent = false;
                        $scope.showstudent = false;
                        $scope.editstudent = false;
                        ok("Student updated!");
                        $scope.disableSave = false;
                        refresh();
                    }, function (response) {
                        $scope.disableSave = false;
                        error(response.data.error);
                    });
                  }, function(){
                      $scope.disableSave = false;
                      console.log($scope.disableSave);
                  })
              } else {
                $scope.disableSave = true;
                  $http.put("/api/student/", postStudent).then(function (response) {
                      $scope.addstudent = false;
                      $scope.showstudent = false;
                      $scope.editstudent = false;
                      ok("Student updated!");
                      $scope.disableSave = false;
                      refresh();
                  }, function (response) {
                      $scope.disableSave = false;
                      error(response.data.error);
                  });
              }

          // to add new student because admission no. doesn't exist
          } else {
            $scope.disableSave = true;
              $http.post("/api/student/", postStudent).then(function () {
                  $scope.addstudent = false;
                  $scope.showstudent = false;
                  $scope.editstudent = false;
                  ok("Student saved!");
                  $scope.disableSave = false;
                  refresh();
              }, function (response) {
                  $scope.disableSave = false;
                  error(response.data.error);
              });
            }
        } else {
            $scope.disableSave = false;
        }

    }, 200, true);

    function validateStudent(student) {   
        if (student.mode == 'New' && !student.fee && !student.corporate) {
            $scope.selectTab(4);
            error("Student fee is not created.");
            return false;
        }
        if (!student.centerId) {
            error("Please Select center.");
            return false;
        }
        if (!student.programId) {
            error("Please Select program.");
            return false;
        }
        if (!student.groupId) {
            error("Please Select group.");
            return false;
        }
        if (!student.firstName) {
            error("First name cannot be empty.");
            return false;
        }
        if (!student.lastName) {
            error("Last name cannot be empty.");
            return false;
        }
        if (!student.admissionDate) {
            error("DOA name cannot be empty.");
            return false;
        }
        if (!student.dob) {
            error("DOB name cannot be empty.");
            return false;
        }
        if (!student.nickName) {
            error("Nick name cannot be empty.");
            return false;
        }
        if (!student.nationality) {
            error("Nationality cannot be empty.");
            return false;
        }
        if (!student.gender) {
            error("Please select Gender.");
            return false;
        }
        if (!student.bloodGroup) {
            error("Please select Blood Group.");
            return false;
        }
        if (!student.familyType) {
            error("Please select Family Type.");
            return false;
        }
        if (!student.expectedIn) {
            error("Expected In cannot be empty.");
            return false;
        }
        if (!student.expectedOut) {
            error("Expected Out cannot be empty.");
            return false;
        }
        if (!student.profile) {
            error("Comment of profile cannot be empty");
            return false;
        }
        if (student.fee && !student.fee.comment && (student.fee.discountAnnualCharges || student.fee.discountAdmissionCharges || student.fee.discountBaseFee || student.fee.discountSecurityDeposit)) {
            error("Comment is compulsory if you give any discount");
            return false;
        }
        if (!student.profile.length > 1000) {
            error("Comment must be less then 1000 characters.");
            return false;
        }
        if (!student.hasSibling) {
            for (var i = 0; i < student.parents.length; i++) {
                if (!validateParent(student.parents[i])) {
                    return false;
                }
            }
        } else {
            if (!$scope.siblingCenter) {
                error("Please select sibling Center.");
                return false;
            }
            if (!$scope.siblingProgram) {
                error("Please select sibling Program.");
                return false;
            }
            if (!$scope.siblingGroup) {
                error("Please select sibling Group.");
                return false;
            }
            if (!student.siblingId) {
                error("Please select Sibling.");
                return false;
            }
        }

        return true;
    }

    function validateParent(parent) {
        if (!parent.firstName) {
            error(parent.relationship + "'s First name cannot be empty.");
            return false;
        }
        if (!parent.lastName) {
            error(parent.relationship + "'s Last name cannot be empty.");
            return false;
        }
        if (!parent.mobile) {
            error(parent.relationship + "'s Mobile cannot be empty.");
            return false;
        }
        if (!parent.email) {
            error(parent.relationship + "'s Email is invalid or empty.");
            return false;
        }
        return true;
    }

    $scope.resetParentPassword = function (parent) {
        var student = {};
        student.id = $scope.workingStudent.id;
        $http.put('/api/student/parent/resetPassword', parent)
            .then(function () {
                $scope.addstudent = false;
                $scope.showstudent = false;
                $scope.editstudent = false;
                refresh();
                ok("Password reset successfull")
            }, function (response) {
                error(response.data.error);
            });

    };

    $scope.cancelStudent = function () {
        $scope.workingStudent = {};
        $scope.showstudent = false;
        $scope.editstudent = false;
        $scope.addstudent = false;
    };

    $scope.activeFilterChange = function () {
        refresh();
        $scope.cancelStudent();
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
        });

        $http.post('/api/student/filter/', {
            active: $scope.activeFilter,
            programCode: $scope.programFilter,
            pageSize: 0,
            pageNumber: 0
        }).then(function (response) {
            $scope.students = response.data.students;
            $scope.updatePageBar();
        });
        $http.get('/api/user/my_privileges').then(
            function (response) {
                $scope.privileges = response.data;
            }, function (response) {
                $scope.privileges = [];
            }
        );
    }

    $scope.getProgramsByCenter = function(centerId){
      $scope.workingStudent.programId = '';
      $scope.workingStudent.groupId = '';
      $http.get('/api/center/programs/'+centerId).then(function (response) {
        $scope.programs = response.data;
        });
    }

    $scope.getGroupsByProgram = function(program){
        $scope.groups = program.groups;
    }

    $scope.deleteStudentSwal = function (student) {
        swal({
            title: 'Are you sure want to deactivate student?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes'
        }).then(function () {
            $scope.deleteStudent(student);
        })
    };

    $scope.deleteStudent = function (student) {
        $http.delete('/api/student/' + student.id).then(function () {
            ok("Student is deactivated.");
            refresh();
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.siblingSelected = function () {
        if (!$scope.workingStudent.siblingId) {
            return;
        }
        $http.get('/api/student/' + $scope.workingStudent.siblingId).then(
            function (response) {
                var sibling = response.data;
                angular.forEach(sibling.parents, function (parent) {
                    $scope.workingStudent[parent.relationship.toLowerCase()] = parent;
                });
            }, function (response) {
                error(response.data.error)
            }
        );
    };

    $scope.siblingChecked = function (checked) {
        if (!checked) {
            $scope.workingStudent.hasSibling = false;
            $scope.workingStudent.father = {};
            $scope.workingStudent.mother = {};
            delete $scope.siblingCenter;
            delete $scope.siblingProgram;
            delete $scope.siblingGroup;
            delete $scope.workingStudent.siblingId;
        } else {
            $scope.workingStudent.hasSibling = true;
            $scope.siblingSelected();
        }
    };

    $scope.siblingCenterChanged = function (center) {
        $scope.siblingCenter = center;
    };
    $scope.siblingProgramChanged = function (program) {
        $scope.siblingProgram = program;
    };
    $scope.siblingGroupChanged = function (group) {
        $scope.siblingGroup = group;
    };

    $scope.download = function () {
        window.location.href = encodeURI('/api/student/download?token=Bearer ' + $localStorage.token + '&program=' + $scope.programFilter);
    };

    $scope.centerChanged = function() {
      $scope.workingStudent.programId = null;
    }

    $scope.programChanged = function (student) {
        loadProgramFee(student.centerId, student.programId);
    };

    $scope.corporateClicked = function (student) {
      if (student) {
        if (student.corporate) {
          showConfirmationSwal('your calculated fee will be removed!', function() {
            delete student.fee;
          },
          // reset on cancel
          function() {
            $scope.workingStudent.corporate = !student.corporate;
            $('#corporate')[0].checked = $scope.workingStudent.corporate;
          });

        } else {
          // if student already exist then get it's fee
          showConfirmationSwal('Your fee will be recalculated!', function() {
            if (typeof student.centerId != 'undefined' && typeof student.programId != 'undefined') {
              loadProgramFee(student.centerId, student.programId);
            }
          },
          // reset on cancel
          function () {
            $scope.workingStudent.corporate = !student.corporate;
            $('#corporate')[0].checked = $scope.workingStudent.corporate;
          });
            
        }
      }
      $scope.workingStudent.corporate;
    };

    $scope.finalFeeChanged = function (fee) {
        fee.transportFee = 0;
        StudentFeeService.calculateDiscount(fee);
        StudentFeeService.calculateGstFee(fee,$scope.workingStudent);
    };

    $scope.durationChange = function (fee) {
        StudentFeeService.calculateFinalFee(fee);
        StudentFeeService.calculateGstFee(fee,$scope.workingStudent);
    };

    $scope.transportFeeChanged = function (fee) {
        StudentFeeService.calculateFinalFee(fee,$scope.workingStudent);
    };

    function loadProgramFee(centerId, programId) {
        if (centerId && programId) {
            if(programId == 72932732558618){
                $scope.isIpsaaclub = true;
            } else {
                $scope.isIpsaaclub = false;
            }
            $http.post('/api/center/fee/', {centerId: centerId, programId: programId}).then(
                function (response) {
                    $scope.workingStudent.feeMessage = null;
                    var programFee = response.data;
                    $scope.workingStudent.fee = $scope.workingStudent.fee ? $scope.workingStudent.fee : {};
                    var student = $scope.workingStudent;
                    var studentFee = $scope.workingStudent.fee;
                    studentFee.programId = $scope.workingStudent.programId;
                    studentFee.annualFee = programFee.annualFee;
                    studentFee.admissionFee = programFee.admissionFee;
                    studentFee.baseFee = programFee.fee;
                    studentFee.deposit = programFee.deposit;
                    studentFee.igst = programFee.igst;
                    studentFee.discount = 0;
                    studentFee.adjust =0;
                    studentFee.comment = '';
                    studentFee.feeDuration = 'Quarterly';
                    
                    studentFee.discountAnnualCharges = 0;
                    studentFee.finalAnnualFee = programFee.annualFee;
                    studentFee.discountAdmissionCharges = 0;
                    studentFee.finalAdmissionCharges = programFee.admissionFee;
                    studentFee.discountBaseFee = 0;
                    studentFee.finalBaseFee = programFee.fee;
                    studentFee.discountSecurityDeposit = 0;
                    studentFee.finalSecurityDeposit = programFee.deposit;
                    console.log(studentFee);
                    
                    StudentFeeService.calculateGstFee(studentFee, student);
                    StudentFeeService.calculateFinalFee(studentFee);
                },
                function (response) {
                    if (response.status == 404) {
                        $scope.workingStudent.feeMessage = "Program Fee not found.";
                    } else {
                        error(response.error);
                    }
                    delete $scope.workingStudent.fee;
                }
            );
        }
    }

  $scope.generatePdf = function(studentId) {
    $scope.pdfGenerate = true;
    $http.get('/api/student/pdf/' + studentId, {
      responseType: 'arraybuffer'
    }).then(
      function (response) {
        
        var blob = new Blob([response.data], {
          type: 'application/octet-stream'
        });
        saveAs(blob, response.headers("fileName"));
        $scope.pdfGenerate = false;
      },
      function (response) {
        $scope.pdfGenerate = false;
        console.log(response);
        // error(response.error);
      }
    );
  }

  $scope.calculateDiscount = function (base, final, targetDiscount) {
    var fee = $scope.workingStudent.fee;
    var fee = $scope.workingStudent.fee;
    if (fee[base] > 0 && fee[final]) {
      if (fee[base] - fee[final] > 0) {
        fee[targetDiscount] =
          Number((((fee[base] - fee[final]) / fee[base]) * 100).toFixed(2));
      }
      else {
        fee[targetDiscount] = 0;
        fee[final] = fee[base];
      }
    } else {
      fee[final] = 0;
      fee[targetDiscount] = 100;
    }

    StudentFeeService.calculateGstFee(fee,$scope.workingStudent);
    StudentFeeService.calculateFinalFee(fee);
  }

  $scope.monthlyTransportFeesChanged = function (fee) {
    if (fee.transportFee > 0) {
      fee.finalTransportFees = fee.transportFee * 3;
    } else {
      fee.transportFee = 0;
      fee.finalTransportFees = 0;
    }
    StudentFeeService.calculateFinalFee(fee);
  }

  $scope.monthlyUniformChargesChanged = function (fee) {
    if (fee.uniformCharges < 0)
      fee.uniformCharges = 0;
    StudentFeeService.calculateFinalFee(fee);
  }

  $scope.monthlyStationeryChargesChanged = function (fee) {
    if (fee.satationary < 0)
      fee.satationary = 0;
    StudentFeeService.calculateFinalFee(fee);
  }

  $scope.formalClicked = function(student) {
    student.fee.formalSchool = student.formalSchool;
    StudentFeeService.calculateGstFee(student.fee, student);
    StudentFeeService.calculateFinalFee(student.fee);
  }

  $scope.openWebCam = function() {
    $scope.webCamOpen = true;
    // References to all the element we will need.
    var video = document.querySelector('#camera-stream'),
      image = document.querySelector('#snap'),
      start_camera = document.querySelector('#start-camera'),
      controls = document.querySelector('.controls'),
      take_photo_btn = document.querySelector('#take-photo'),
      delete_photo_btn = document.querySelector('#delete-photo'),
      download_photo_btn = document.querySelector('#download-photo'),
      error_message = document.querySelector('#error-message');


    // The getUserMedia interface is used for handling camera input.
    // Some browsers need a prefix so here we're covering all the options
    navigator.getMedia = (navigator.getUserMedia ||
      navigator.webkitGetUserMedia ||
      navigator.mozGetUserMedia ||
      navigator.msGetUserMedia);

    if (!navigator.getMedia) {
      displayErrorMessage("Your browser doesn't have support for the navigator.getUserMedia interface.");
      hideUI(video, controls, start_camera, snap, error_message);
    }
    else {

      // Request the camera.
      navigator.getMedia(
        {
          video: true
        },
        // Success Callback
        function (stream) {
          // Create an object URL for the video stream and
          // set it as src of our HTLM video element.
          $scope.camraStream = stream;
          video.src = window.URL.createObjectURL(stream);

          // Play the video element to start the stream.
          video.play();
          video.onplay = function () {
            showVideo(video, controls, start_camera, snap, error_message);
          };

        },
        // Error Callback
        function (err) {
          displayErrorMessage("There was an error with accessing the camera stream: " + err.name, err);
          hideUI(video, controls, start_camera, snap, error_message);
        }
      );

    }

    // Mobile browsers cannot play video without user input,
    // so here we're using a button to start it manually.
    start_camera.addEventListener("click", function (e) {
      e.preventDefault();

      // Start video playback manually.
      $scope.openWebCam();
    //   video.play();
    //   showVideo(video, controls, start_camera, snap, error_message);

    });


    take_photo_btn.addEventListener("click", function (e) {

      e.preventDefault();

      var snap = takeSnapshot(video);

      // Show image. 
      image.setAttribute('src', snap);
      image.classList.add("visible");

      // Enable delete and save buttons
      delete_photo_btn.classList.remove("disabled");
      download_photo_btn.classList.remove("disabled");

      // Set the href attribute of the download button to the snap url.
      download_photo_btn.href = snap;

      // Pause video playback of stream.
      video.pause();
      
      //Usage example:
      var file = dataURLtoFile(snap, 'snapshot.png');
      var element = {files: []};
      element.files[0] = file;
      $scope.setFiles(element);
      stopCamra();
    });

    function stopCamra(){
        var track = $scope.camraStream.getTracks()[0];
        track.stop();
    }

    function dataURLtoFile(dataurl, filename) {
        var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while(n--){
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, {type:mime});
    }


    delete_photo_btn.addEventListener("click", function (e) {

      e.preventDefault();

      // Hide image.
      image.setAttribute('src', "");
      image.classList.remove("visible");

      // Disable delete and save buttons
      delete_photo_btn.classList.add("disabled");
      download_photo_btn.classList.add("disabled");

      // Resume playback of stream.
      video.play();

    });
  }

  function showVideo(video, controls, start_camera, snap, error_message) {
    // Display the video stream and the controls.

    hideUI(video, controls, start_camera, snap, error_message);
    video.classList.add("visible");
    controls.classList.add("visible");
  }


  function takeSnapshot(video) {
    // Here we're using a trick that involves a hidden canvas element.  

    var hidden_canvas = document.querySelector('canvas'),
      context = hidden_canvas.getContext('2d');

    var width = video.videoWidth,
      height = video.videoHeight;

    if (width && height) {

      // Setup a canvas with the same dimensions as the video.
      hidden_canvas.width = width;
      hidden_canvas.height = height;

      // Make a copy of the current frame in the video on the canvas.
      context.drawImage(video, 0, 0, width, height);

      // Turn the canvas image into a dataURL that can be used as a src for our photo.
      return hidden_canvas.toDataURL('image/png');
    }
  }


  function displayErrorMessage(error_msg, error) {
    error = error || "";
    if (error) {
      console.error(error);
    }

    error_message.innerText = error_msg;
    error_message.classList.add("visible");
  }


  function hideUI(video, controls, start_camera, snap, error_message) {
    // Helper function for clearing the app UI.hideUI

    controls.classList.remove("visible");
    start_camera.classList.remove("visible");
    video.classList.remove("visible");
    snap.classList.remove("visible");
    error_message.classList.remove("visible");
  }

  $scope.generateStudentFee = function() {
    $scope.disableGenerate = true;
    $http.post('/api/student/ipsaaclub/generate/' + $scope.workingStudent.id, {}).then(function (response){
        $scope.disableGenerate = false;
        ok('Student Fee generated');
    },function(err){
        $scope.disableGenerate = false;
        error(err.data.error);
    })
    };

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

    function showConfirmationSwal(message, confirmCallback, cancelCallback) {
      swal({
        type: 'warning', 
        title: 'Are you sure?',
        text: message,
        confirmButtonText: 'Confirm',
        showCancelButton: true
      }).then(
        function() {
          if(confirmCallback) 
            confirmCallback();
        },
        function() {
          if(cancelCallback)
            cancelCallback();
        }
      );
    }

    refresh();
});

app.filter('idFilter', function () {
    return function (input, id) {
        var out = [];
        if (typeof input === 'object') {
            if (typeof id === 'object' || typeof id === 'string') {
                if (id != null || id != '') {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].id == id) {
                            out.push(input[i]);
                        }
                    }
                }
            }
        }
        return out;
    }
});

app.filter('startFrom', function () {
    return function (input, start) {
        start = +start; //parse to int
        return typeof input == 'undefined' ? [] : input.slice(start);
    }
});