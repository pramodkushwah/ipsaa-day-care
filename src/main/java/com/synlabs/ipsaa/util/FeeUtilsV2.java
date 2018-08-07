package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.GST;
import com.synlabs.ipsaa.ex.ValidationException;
import sun.util.resources.cldr.CalendarData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static com.synlabs.ipsaa.util.BigDecimalUtils.THREE;
import static com.synlabs.ipsaa.util.FeeUtils.FEE_CALCULATION_TOLERANCE;
import static java.math.BigDecimal.ZERO;

public class FeeUtilsV2
{
  private static final String months[] = new String[] { "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };

  private static BigDecimal HUNDRED=new BigDecimal(100);
  public static final double FEE_CALCULATION_TOLERANCE = 5.0;
  public static final double FEE_DISCOUNT_CALCULATION_TOLERANCE = 1.0;

  public static final int IPSAA_CLUB_PROGRAM_ID=26;
  public static final int IPSAA_CLUB_REGULAR_PROGRAM_ID=30;


  public static BigDecimal calculateGST(BigDecimal baseFee,BigDecimal annualFee, GST type)
  {
    BigDecimal finalFee = ZERO;
    finalFee = finalFee.add(baseFee);
    if (annualFee != null)
    {
      finalFee = finalFee.add(annualFee);
    }
    BigDecimal gst;
    if(type==GST.IGST){
      gst=new BigDecimal(18);
    }else if(type==GST.CGST)
      gst=new BigDecimal(9);
    else gst=new BigDecimal(9);

    gst = finalFee.multiply(gst)
            .divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
    return gst;
  }

  public static BigDecimal calculateFinalFee(StudentFee fee,boolean isGst)
  {

    BigDecimal totalFee = fee.getFinalBaseFee()
                          .add(fee.getFinalDepositFee())
                          .add(fee.getFinalAdmissionFee())
                          .add(fee.getFinalAnnualCharges())
                          .add(fee.getUniformCharges())
                          .add(fee.getStationary())
                          .add(fee.getTransportFee());
    BigDecimal gstAmmount;

    if(isGst){
      gstAmmount=calculateGST(fee.getFinalBaseFee(),fee.getFinalAnnualCharges(),GST.IGST);
      fee.setGstAmount(gstAmmount);
      totalFee.add(gstAmmount);
    }

    BigDecimal diff = totalFee.subtract(fee.getFinalFee());

    //compare diff with tolerance
    if (Math.abs(diff.doubleValue()) >= FEE_CALCULATION_TOLERANCE)
    {
      throw new ValidationException(
              String.format("Final Fee calculation discount error![Request Final Fee=%s,Calculated Final Fee=%s]", fee.getFinalFee(), totalFee));
    }
    return totalFee;
  }

  private static BigDecimal calculateFinalFee(BigDecimal baseFee, BigDecimal discount, BigDecimal adjust, BigDecimal transportFee,BigDecimal ratio) {
    BigDecimal finalFee = baseFee;
    if (discount != null && !discount.equals(ZERO))
    {
      BigDecimal discountAmount = baseFee.multiply(discount)
              .divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
      finalFee = baseFee.subtract(discountAmount);
    }
    if (adjust != null && !adjust.equals(ZERO))
    {
      finalFee = finalFee.add(adjust);
    }
    finalFee=finalFee.add(transportFee);
    finalFee = finalFee.multiply(ratio);
    return finalFee;
  }

  public static void validateStudentFee(StudentFee fee, CenterProgramFee centerProgramFee)
  {
    fee.setBaseFee(new BigDecimal(centerProgramFee.getFee()));
    fee.setAdmissionFee(centerProgramFee.getAddmissionFee());
    fee.setDepositFee(new BigDecimal(centerProgramFee.getDeposit()));
    fee.setAnnualCharges(new BigDecimal(centerProgramFee.getAnnualFee()));


    fee.setFinalBaseFee(calculateDiscountAmmount(fee.getBaseFee(),fee.getBaseFeeDiscount(),fee.getFinalBaseFee()));

    fee.setFinalAnnualCharges(calculateDiscountAmmount(fee.getAnnualCharges(),fee.getAnnualFeeDiscount(),fee.getFinalAnnualCharges()));

    fee.setFinalDepositFee(calculateDiscountAmmount(fee.getDepositFee(),fee.getDepositFeeDiscount(),fee.getFinalDepositFee()));

    fee.setFinalAdmissionFee(calculateDiscountAmmount(fee.getAdmissionFee(),fee.getAddmissionFeeDiscount(),fee.getFinalAdmissionFee()));

    if(fee.getStudent().isFormalSchool()){
     fee.setIgst(new BigDecimal(18));
     fee.setFinalFee(calculateFinalFee(fee,true));
   }
   else if(centerProgramFee.getProgram().getId()==IPSAA_CLUB_PROGRAM_ID ||centerProgramFee.getProgram().getId()==IPSAA_CLUB_REGULAR_PROGRAM_ID){
     fee.setIgst(new BigDecimal(18));
     fee.setFinalFee(calculateFinalFee(fee,true));
   }else {
     fee.setIgst(new BigDecimal(0));
     fee.setFinalFee(calculateFinalFee(fee,false));
   }

  }

  private static BigDecimal calculateDiscountAmmount(BigDecimal ammount, BigDecimal discount,BigDecimal discountAmout) {
    BigDecimal finalAmount=ZERO;
    ammount=ammount==null?ZERO:ammount;

    if (discount != null && !discount.equals(ZERO))
    {
      BigDecimal calculateDiscount = ammount.multiply(discount)
              .divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);

      finalAmount= ammount.subtract(calculateDiscount);
    }else{
      finalAmount= ammount;
    }
    BigDecimal diff = discountAmout.subtract(finalAmount);

    //compare diff with tolerance
    if (Math.abs(diff.doubleValue()) >= FEE_DISCOUNT_CALCULATION_TOLERANCE)
    {
        throw new ValidationException(
            String.format("Final Fee calculation discount error![Request Final Fee=%s,Calculated Final Fee=%s]", discountAmout, finalAmount));
    }
    return finalAmount;
  }

//  public static boolean validateStudentFee(StudentFee fee, boolean thrEx,CenterProgramFee centerProgramFee)
//  {
//    BigDecimal feeRatio=calculateFeeRatioForQuarter(fee.getStudent().getProfile().getAdmissionDate());
//    BigDecimal calculateFinalFee=ZERO;
//    if(fee.getStudent().isFormalSchool()){
//      calculateFinalFee= FeeUtilsV2.calculateFinalFee(fee,true,feeRatio);
//    }
//    else if(!fee.getStudent().isFormalSchool() && centerProgramFee.getProgram().getId()==26 ||centerProgramFee.getProgram().getId()==30 ){
//      calculateFinalFee= FeeUtilsV2.calculateFinalFee(fee,true,feeRatio);
//      // calculate ratio
//    }else{
//      calculateFinalFee= FeeUtilsV2.calculateFinalFee(fee,false,feeRatio);
//    }
//    BigDecimal diff = calculateFinalFee.subtract(fee.getFinalFee());
//
//    //compare diff with tolerance
//    if (Math.abs(diff.doubleValue()) >= FEE_CALCULATION_TOLERANCE)
//    {
//      if (thrEx)
//      {
//        throw new ValidationException(
//            String.format("Final Fee calculation error![Request Final Fee=%s,Calculated Final Fee=%s]", fee.getFinalFee(), calculateFinalFee));
//      }
//      else
//      {
//        return false;
//      }
//    }
//    return true;
//  }
  //  public static BigDecimal calculateFinalFee(StudentFee studentFee,boolean isGst,BigDecimal ratio)
//  {
//    studentFee.setDiscount(studentFee.getDiscount() == null ?
//                           ZERO :
//                           studentFee.getDiscount());
//    studentFee.setTransportFee(studentFee.getTransportFee() == null ?
//                               ZERO :
//                               studentFee.getTransportFee());
//    studentFee.setAdjust(studentFee.getAdjust() == null ?
//                         ZERO :
//                         studentFee.getAdjust());
//    studentFee.setUniformCharges(studentFee.getUniformCharges()==null?ZERO:studentFee.getUniformCharges());
//    studentFee.setStationary(studentFee.getStationary()==null?ZERO:studentFee.getStationary());
//    studentFee.setAdmissionFee(studentFee.getAdmissionFee()==null?ZERO:studentFee.getAdmissionFee());
//
//    BigDecimal baseFee = studentFee.getBaseFee();
//    BigDecimal discount = studentFee.getDiscount();
//    BigDecimal transportFee = studentFee.getTransportFee();
//    BigDecimal adjust = studentFee.getAdjust();
//    BigDecimal annualFee=studentFee.getAnnualCharges();
//    BigDecimal uniform=studentFee.getUniformCharges();
//    BigDecimal stationary=studentFee.getStationary();
//    BigDecimal addmissionFee=studentFee.getAdmissionFee();
//    BigDecimal deposit=studentFee.getFinalDepositFee();
//
//    BigDecimal totalFee = calculateFinalFee(baseFee, discount, adjust,transportFee,ratio);
//    if(isGst)
//    studentFee.setFinalFee(calculateGST(totalFee,annualFee,GST.IGST));
//    else
//      studentFee.setFinalFee(totalFee.add(annualFee));
//    BigDecimal finalFee=studentFee.getFinalFee().add(addmissionFee).add(uniform).add(stationary).add(deposit);
//    studentFee.setFinalFee(finalFee);
//    return finalFee;
//  }

  public static int quarterStartMonth(int quarter)
  {
    switch (quarter)
    {
      case 1:
        return 1;
      case 2:  // first quater
        return 4;
      case 3:
        return 7;
      case 4:
        return 10;
    }
    return 0;
  }

  public static int quarterEndMonth(int quarter)
  {
    switch (quarter)
    {
      case 1:
        return 3;
      case 2:
        return 6;
      case 3:
        return 9;
      case 4:
        return 12;
    }
    return 0;
  }
  public static int getQuarter(int month){
    if(month>=1 && month<=3)
      return 1;
    else if(month>=4 && month<=6)
      return 2;
    else if(month>=7 && month<=10)
      return 3;
    else
      return 4;
  }

  private static BigDecimal calculateFeeRatioForQuarter(Date admissionDate) {
    Calendar cal = Calendar. getInstance();
    cal.setTime(admissionDate);
    int currMonth = cal.get(Calendar.MONTH);
    currMonth=currMonth+1;// for removing 0 to one indexing
    int currDay = cal.get(Calendar.DATE);

    double feeMonthRatio=quarterEndMonth(getQuarter(currMonth))-currMonth;
    if(currDay>15){
      feeMonthRatio=feeMonthRatio+0.5;
    }
    return new BigDecimal(feeMonthRatio);
  }
}
