app.service('StudentFeeService', function ($http) {
    var service = {};

    //needs baseFee, feeDuration, discount and adjust
    service.calculateFinalFee = function (fee, duration) {
        const baseFee = fee.baseFee;
        const discount = fee.discount;
        const adjust = fee.adjust;
        var transportFee = fee.transportFee;

        if (typeof duration == 'undefined') {
            duration = fee.feeDuration;
        }
        var finalFee = baseFee - (baseFee * discount) / 100;
        finalFee = finalFee + adjust;
        finalFee = finalFee.toFixed(0) / 1;
        switch (duration) {
            case 'Quarterly':
                finalFee = finalFee * 3;
                transportFee = transportFee * 3;
                break;
            case 'Yearly':
                finalFee = finalFee * 12;
                transportFee = transportFee * 12;
                break;
        }
        fee.finalFee = finalFee + transportFee;
    }

    //needs baseFee and finalFee
    service.calculateDiscount = function (fee) {
        const baseFee = fee.baseFee;
        const finalFee = fee.finalFee;
        var discount = ((baseFee - finalFee) / baseFee) * 100;
        discount = discount.toFixed(1) / 1;
        var x = baseFee - ((baseFee * discount) / 100);
        var adjust = finalFee - x;
        adjust = adjust.toFixed(5) / 1;
        fee.discount = discount;
        fee.adjust = adjust;
    }

    //needs finalFee, cgst and sgst
    service.calculateGstFee = function (fee) {
        var transportFee = fee.transportFee;
        switch (fee.feeDuration) {
            case 'Quarterly':
                transportFee = transportFee * 3;
                break;
            case 'Yearly':
                transportFee = transportFee * 12;
                break;
        }
        const finalFee = fee.finalFee - transportFee;
        var totalgst = 0;
        if (fee.cgst) {
            totalgst += fee.cgst;
            fee.cGstAmount = (finalFee * fee.cgst) / 100;
        }
        if (fee.sgst) {
            totalgst += fee.sgst;
            fee.sGstAmount = (finalFee * fee.sgst) / 100;
        }
        if (fee.igst) {
            totalgst += fee.igst;
            fee.iGstAmount = (finalFee * fee.igst) / 100;
        }
        fee.gstFee = finalFee + ((finalFee * totalgst) / 100);
        // fee.gstFeeAdjust = (fee.gstFee - Math.trunc(fee.gstFee)).toFixed(2);
        // fee.gstFee = Math.trunc(fee.gstFee)
    }

    return service;
});