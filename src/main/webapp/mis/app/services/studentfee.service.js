app.service('StudentFeeService', function ($http) {
  var service = {};

  // initailize fee, discounts and final fee 
  service.initializeFee = function (fee) {
    fee.annualFee = fee.annualFee || 0;
    fee.discountAnnualCharges = fee.discountAnnualCharges || 0;
    fee.finalAnnualFee = fee.discountAnnualCharges
      ? fee.finalAnnualFee
      : fee.annualFee || 0;

    fee.admissionCharges = fee.admissionCharges || 0;
    fee.discountAdmissionCharges = fee.discountAdmissionCharges || 0;
    fee.finalAdmissionCharges = fee.discountAdmissionCharges
      ? fee.finalAdmissionCharges
      : fee.admissionCharges || 0;

    fee.baseFee = fee.baseFee || 0;
    fee.discountBaseFee = fee.discountBaseFee || 0;
    if (fee.programId === 72932732558618) {
      fee.finalBaseFee = fee.discountBaseFee
        ? fee.finalBaseFee
        : fee.baseFee | 0;
    } else {
      fee.finalBaseFee = fee.discountBaseFee
        ? (fee.finalBaseFee / 3).toFixed(2)
        : fee.baseFee | 0;
    }

    fee.securityDeposit = fee.securityDeposit || 0;
    fee.discountSecurityDeposit = fee.discountSecurityDeposit || 0;
    fee.finalSecurityDeposit = fee.discountSecurityDeposit
      ? fee.finalSecurityDeposit
      : fee.securityDeposit || 0;

    fee.transportFee = fee.transportFee || 0;
    fee.finalTransportFees = fee.transportFee ? fee.transportFee * 3 : 0;

    fee.uniformCharges = fee.uniformCharges || 0;
    fee.stationary = fee.stationary || 0;
  }

  service.calculateFinalFee = function (fee) {
    fee.finalTransportFees = fee.transportFee ? fee.transportFee * 3 : 0;
    var final = 0;
    if (fee.finalAnnualFee > 0) {
      final += Number(fee.finalAnnualFee);
    }
    if (fee.finalAdmissionCharges > 0) {
      final += Number(fee.finalAdmissionCharges);
    }
    if (fee.finalBaseFee > 0) {      
      if (fee.programId === 72932732558618) {
        final += fee.finalBaseFee;
      } else {
        final += Number(fee.finalBaseFee * 3);
      }
    }
    if (fee.finalSecurityDeposit > 0) {
      final += Number(fee.finalSecurityDeposit);
    }
    if (fee.finalTransportFees > 0) {
      final += Number(fee.finalTransportFees);
    }

    if (fee.uniformCharges > 0) {
      final += Number(fee.uniformCharges);
    }

    if (fee.stationary > 0) {
      final += Number(fee.stationary);
    }


    if (fee.gstFee > 0) {
      final += Number(fee.gstFee);
    }

    if (fee.baseFeeGst > 0) {
      final += Number(fee.baseFeeGst);
    }

    fee.finalFee = Number(final.toFixed(2));
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
  service.calculateGstFee = function (fee, student) {
    fee.gstFee = 0;//annual-fee-gst
    fee.baseFeeGst = 0;

    if (fee.programId == 622614691413790 || (typeof student != 'undefined' && student.formalSchool) || fee.formalSchool) {
      fee.gstFee = Number(((Number(fee.finalAnnualFee)) * 0.18).toFixed(2));//annual-fee-gst
      fee.baseFeeGst = Number((((Number(fee.finalBaseFee) * 3)) * 0.18).toFixed(2));
    }

    // if(typeof student!='undefined' && student.formalSchool){
    //   fee.gstFee = Number(((Number(fee.finalAnnualFee)) * 0.18).toFixed(2));//annual-fee-gst
    //   fee.baseFeeGst = Number((((Number(fee.finalBaseFee) * 3)) * 0.18).toFixed(2));
    // }
    // else if (fee.formalSchool) {
    //   fee.gstFee = Number(((Number(fee.finalAnnualFee)) * 0.18).toFixed(2));//annual-fee-gst
    //   fee.baseFeeGst = Number((((Number(fee.finalBaseFee) * 3)) * 0.18).toFixed(2));

    // }
  }

  return service;
});