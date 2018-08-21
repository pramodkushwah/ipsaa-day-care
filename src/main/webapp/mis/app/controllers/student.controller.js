app.controller('StudentController', function ($scope, $http, fileUpload, $localStorage, $filter, StudentFeeService) {
    var tabs = ['#studentprofile', '#photo', '#fatherprofile', '#motherprofile', '#fee'];
    $scope.disableSave = false;
    $scope.activeFilter = true;
    $scope.privileges = [];
    $scope.workingStudent = newStudent();

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
                ok("Proile image uploaded successfully!");
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

    $scope.showStudent = function (student) {
        $http.get('/api/student/' + student.id).then(function (response) {
            $scope.workingStudent = "Show";
            $scope.workingStudent = response.data;
            $scope.workingStudent.mode = "Show";
            $scope.workingStudent.centerId = $scope.workingStudent.center.id + "";
            $scope.workingStudent.programId = $scope.workingStudent.program.id + "";
            $scope.workingStudent.groupId = $scope.workingStudent.group.id + "";
            for (var i = 0; i < $scope.workingStudent.parents.length; i++) {
                var parent = $scope.workingStudent.parents[i];
                $scope.workingStudent[parent.relationship.toLowerCase()] = parent;
            }
            $scope.showstudent = true;
            $scope.editstudent = false;
            $scope.addstudent = false;

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

    $scope.editStudent = function (student) {
        $http.get('/api/student/' + student.id).then(function (response) {
            $scope.workingStudent = response.data;
            $scope.workingStudent.mode = "Edit";
            $scope.workingStudent.centerId = $scope.workingStudent.center.id + "";
            $scope.workingStudent.programId = $scope.workingStudent.program.id + "";
            $scope.workingStudent.groupId = $scope.workingStudent.group.id + "";
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

            setupProfilePic($scope.workingStudent);
        });
    };

    $scope.addStudent = function () {
        $scope.addstudent = true;
        $scope.showstudent = false;
        $scope.editstudent = false;
        setupProfilePic($scope.workingStudent);
        $scope.workingStudent = newStudent();
    };

    function newStudent() {
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
            }
        };
        return student;
    }

    $scope.saveStudent = debounce(function (student) {
        $scope.disableSave = true;
        var postStudent = jQuery.extend(true, {}, student);

        postStudent.father.relationship = "Father";
        postStudent.mother.relationship = "Mother";

        if (!postStudent.hasSibling && !postStudent.siblingId) {
            postStudent.parents = [postStudent.father, postStudent.mother];
        }

        delete postStudent.father;
        delete postStudent.mother;

        if (validateStudent(postStudent)) {
            if (postStudent.admissionNumber) {
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

            } else {
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
            error("Excepter In cannot be empty.");
            return false;
        }
        if (!student.expectedOut) {
            error("Excepter Out cannot be empty.");
            return false;
        }
        if (!student.profile) {
            error("Comment cannot be empty");
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

    $scope.programChanged = function (centerId, programId) {
        loadProgramFee(centerId, programId);
    };

    $scope.corporateClicked = function (student) {
        if (student) {
            if (student.corporate) {
                delete student.fee;
            } else {
                if (typeof student.centerId != 'undefined' && typeof student.programId != 'undefined') {
                    loadProgramFee(student.centerId, student.programId);
                }
            }
        }
    };

    $scope.finalFeeChanged = function (fee) {
        fee.transportFee = 0;
        StudentFeeService.calculateDiscount(fee);
        StudentFeeService.calculateGstFee(fee);
    };

    $scope.durationChange = function (fee) {
        StudentFeeService.calculateFinalFee(fee);
        StudentFeeService.calculateGstFee(fee);
    };

    $scope.transportFeeChanged = function (fee) {
        StudentFeeService.calculateFinalFee(fee);
    };

    function loadProgramFee(centerId, programId) {
        if (centerId && programId) {
            $http.post('/api/center/fee/', {centerId: centerId, programId: programId}).then(
                function (response) {
                    $scope.workingStudent.feeMessage = null;
                    var programFee = response.data;
                    $scope.workingStudent.fee = $scope.workingStudent.fee ? $scope.workingStudent.fee : {};
                    var studentFee = $scope.workingStudent.fee;

                    studentFee.annualFee = programFee.annualFee;
                    studentFee.admissionFee = programFee.admissionFee;
                    studentFee.baseFee = programFee.fee;
                    studentFee.deposit = programFee.deposit;
                    studentFee.igst = programFee.igst;
                    studentFee.discount = 0;
                    studentFee.transportFee =0;
                    studentFee.adjust =0;
                    studentFee.comment = '';
                    studentFee.feeDuration = 'Quarterly';
                    
                    studentFee.discountAnnualCharges = 0
                    studentFee.finalAnnualFee = programFee.annualFee
                    studentFee.discountAdmissionCharges = 0
                    studentFee.finalAdmissionCharges = programFee.admissionFee
                    studentFee.discountBaseFee = 0
                    studentFee.finalBaseFee = programFee.fee
                    studentFee.discountSecurityDeposit = 0
                    studentFee.finalSecurityDeposit = programFee.deposit
                    StudentFeeService.calculateGstFee(studentFee);  
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

  $scope.calculateDiscount = function (base, final, targetDiscount) {
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

    StudentFeeService.calculateGstFee(fee);
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