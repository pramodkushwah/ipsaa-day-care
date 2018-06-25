angular.module('app').service('InquiryService', function ($http) {
    function error(message) {
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    function validateLog(log) {
        if (!log)
            return true;

        if (!log.callDisposition) {
            error("Please Enter Call Disposition.");
            return false;
        }
        if (
            log.callDisposition == "NewInquiry" ||
            log.callDisposition == "Followup" ||
            log.callDisposition == "Callback" ||
            log.callDisposition == "ParentMessage" ||
            log.callDisposition == "Revisit"
        ) {
            if (!log.callBackDate) {
                error("Please Enter Callback Date.");
                return false;
            }
            if (!log.callBackTime) {
                error("Please Enter Callback Time.");
                return false;
            }
            if (!log.callBackNumber) {
                error("Please Enter Callback Number.");
                return false;
            }
        }
        return true;
    }

    function isEmptyLog(log) {
        if (!log) return true;
        var v = !log.callDisposition
            && !log.inquiryDate
            && !log.callBackDate
            && !log.callBackTime
            && !log.callBackNumber
            && !log.comment;
        if (v) {
            delete log.callBack;
        }
        return v;
    }

    var service = {};

    service.validateInquiry = function (inquiry) {
        if (!inquiry.inquiryNumber) {
            error("Please Enter Inquiry Number.");
            return false;
        }
        if (!inquiry.leadSource) {
            error("Please Select Lead Source.");
            return false;
        }
        if (!inquiry.inquiryType) {
            error("Please Select Inquiry Type.");
            return false;
        }
        if (!inquiry.centerId) {
            error("Please Select Center.");
            return false;
        }
        if (!inquiry.programId) {
            error("Please Select Program.");
            return false;
        }
        if (!inquiry.groupId) {
            error("Please Select Group.");
            return false;
        }
        if (!inquiry.whoVisited) {
            error("Please Enter Who Visited.");
            return false;
        }
        if (!inquiry.fromTime) {
            error("Please Enter From Time.");
            return false;
        }
        if (!inquiry.toTime) {
            error("Please Enter To Time.");
            return false;
        }
        if (!inquiry.feeOffer) {
            error("Please Enter Fee Offer.");
            return false;
        }
        if (!inquiry.inquiryDate) {
            error("Please Enter Inquiry Date.");
            return false;
        }

        if (!inquiry.childFirstName) {
            error("Please Enter Child First Name.");
            return false;
        }
        if (!inquiry.childLastName) {
            error("Please Enter Child Last Name.");
            return false;
        }
        if (!inquiry.childDob) {
            error("Please Enter Child Dob.");
            return false;
        }

        if (!inquiry.fatherFirstName) {
            error("Please Father's First Name.");
            return false;
        }
        if (!inquiry.fatherMobile) {
            error("Please Father's Mobile Number.");
            return false;
        }

        if (!inquiry.motherFirstName) {
            error("Please Mother's First Name.");
            return false;
        }
        if (!inquiry.motherMobile) {
            error("Please Mother's Mobile Number.");
            return false;
        }
        if (!inquiry.address || !inquiry.address.address) {
            error("Please Enter Address.");
            return false;
        }
        if (!inquiry.id && isEmptyLog(inquiry.log)) {
            error('Please select CallDisposition.');
            return false;
        }
        if (!isEmptyLog(inquiry.log)) {
            return validateLog(inquiry.log);
        }
        return true;
    };

    service.prepareInquiry = function (inquiry) {
        if (inquiry.center) {
            inquiry.centerId = inquiry.center.id;
        }
        if (inquiry.program) {
            inquiry.programId = inquiry.program.id;
        }
        if (inquiry.group) {
            inquiry.groupId = inquiry.group.id;
        }
        if (inquiry.leadSource) {
            inquiry.leadSource = inquiry.leadSource.toUpperCase();
        }
        if (inquiry.log) {
            if (inquiry.log.callBackDate) {
                inquiry.log.callBack = inquiry.log.callBackDate;
            }
            if (inquiry.log.callBack) {
                if (inquiry.log.callBackTime) {
                    inquiry.log.callBack = inquiry.log.callBack + ' ' + inquiry.log.callBackTime + ' IST';
                }
            } else {
                if (inquiry.log.callBackTime) {
                    inquiry.log.callBack = inquiry.log.callBackTime + ' IST';
                }
            }
        }
    };

    service.saveInquiry = function (inquiry, success, failure) {
        this.prepareInquiry(inquiry);
        if (!this.validateInquiry(inquiry)) return;
        if (inquiry.id) {
            $http.put('/api/inquiry/', inquiry).then(success, failure);
        } else {
            $http.post('/api/inquiry/', inquiry).then(success, failure);
        }
    };

    service.getInquiry = function (id, success, failure) {
        if (id) {
            $http.get('/api/inquiry/' + id).then(success, failure);
        }
    };

    service.getInquiries = function (center, success, error) {
        var filter = '';
        if (center && center.id) {
            filter = '?centerId=' + center.id;
        }
        $http.get('/api/inquiry/' + filter).then(success, error);
    };

    service.getCenters = function (success, failure) {
        $http.get('/api/center/').then(success, failure);
    };

    service.getPrograms = function (success, failure) {
        $http.get("/api/program/").then(success, failure);
    };

    return service;
});