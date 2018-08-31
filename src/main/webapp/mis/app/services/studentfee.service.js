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
    fee.finalBaseFee = fee.discountBaseFee
      ? (fee.finalBaseFee / 3).toFixed(2)
      : fee.baseFee | 0;

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
    var final = 0;
    if (fee.finalAnnualFee > 0) {
      final += Number(fee.finalAnnualFee);
    }
    if (fee.finalAdmissionCharges > 0) {
      final += Number(fee.finalAdmissionCharges);
    }
    if (fee.finalBaseFee > 0) {
      final += Number(fee.finalBaseFee * 3);
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

    fee.finalFee = final;
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
    if(typeof student!='undefined' && student.formalSchool){
      fee.gstFee = Number(((Number(fee.finalAnnualFee)) * 0.18).toFixed(2));//annual-fee-gst
      fee.baseFeeGst = Number((((Number(fee.finalBaseFee) * 3)) * 0.18).toFixed(2));
    }
    else if (fee.formalSchool) {
      fee.gstFee = Number(((Number(fee.finalAnnualFee)) * 0.18).toFixed(2));//annual-fee-gst
      fee.baseFeeGst = Number((((Number(fee.finalBaseFee) * 3)) * 0.18).toFixed(2));

      // } else {
      //   if (fee.program.code === 'ICLUB' || fee.program.code === 'CLUBREG') {
      //     fee.gstFee = (fee.finalAnnualFee + fee.finalBaseFee * 3) * 0.18;
      //   } else {
      //     fee.gstFee = 0;
      //   }
    }
    else
      fee.gstFee = 0;//annual-fee-gst
  }

  return service;
});