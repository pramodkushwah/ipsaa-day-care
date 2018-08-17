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
    fee.setFinalBaseFee(fee.getFinalBaseFee().multiply(THREE));
    //fee.setTransportFee(fee.getTransportFee().multiply(THREE));

    BigDecimal totalFee = fee.getFinalBaseFee()
                          .add(fee.getTransportFee().multiply(THREE))
                          .add(fee.getFinalDepositFee())
                          .add(fee.getFinalAdmissionFee())
                          .add(fee.getFinalAnnualCharges())
                          .add(fee.getUniformCharges())
                          .add(fee.getStationary());
    BigDecimal gstAmmount;

    if(isGst){
      gstAmmount=calculateGST(fee.getFinalBaseFee(),fee.getFinalAnnualCharges(),GST.IGST);
      fee.setGstAmount(gstAmmount);
      totalFee=totalFee.add(gstAmmount);
    }else{
      fee.setGstAmount(ZERO);
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
  public static BigDecimal calculateFinalFee(StudentFeePaymentRequest fee,BigDecimal ratio)
  {
    BigDecimal finalRatio;
    if(ratio!=null)
    finalRatio=ratio;
    else{
      finalRatio=THREE;
    }
      fee.setFinalBaseFee(fee.getBaseFee().add(fee.getTransportFee()).multiply(finalRatio));

    BigDecimal totalFee = fee.getFinalBaseFee()
            .add(fee.getTransportFee().multiply(finalRatio))
            .add(fee.getFinalDepositFee())
            .add(fee.getFinalAdmissionFee())
            .add(fee.getFinalAnnualCharges())
            .add(fee.getUniformCharges())
            .add(fee.getStationary())
            .add(fee.getExtraCharge())
            .add(fee.getLatePaymentCharge());
    BigDecimal gstAmmount;

    if(fee.getIgst()!=null && fee.getIgst().intValue()!=0){
      gstAmmount=calculateGST(fee.getFinalBaseFee(),fee.getFinalAnnualCharges(),GST.IGST);
      fee.setGstAmount(gstAmmount);
      totalFee=totalFee.add(gstAmmount);
    }else{
      fee.setGstAmount(ZERO);
    }
    return totalFee;
  }

  public static void validateStudentFee(StudentFee fee, CenterProgramFee centerProgramFee)
  {

    fee.setFinalBaseFee(calculateDiscountAmmount(fee.getBaseFee(),fee.getBaseFeeDiscount(),fee.getFinalBaseFee(),"Base Fee"));

    fee.setFinalAnnualCharges(calculateDiscountAmmount(fee.getAnnualCharges(),fee.getAnnualFeeDiscount(),fee.getFinalAnnualCharges(),"Annual Charges"));

    fee.setFinalDepositFee(calculateDiscountAmmount(fee.getDepositFee(),fee.getDepositFeeDiscount(),fee.getFinalDepositFee(),"Deposit Fee"));

    fee.setFinalAdmissionFee(calculateDiscountAmmount(fee.getAdmissionFee(),fee.getAddmissionFeeDiscount(),fee.getFinalAdmissionFee(),"Addmission Fee"));

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

  private static BigDecimal calculateDiscountAmmount(BigDecimal ammount, BigDecimal discount,BigDecimal discountAmout,String discountName) {
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
            String.format(discountName+" calculation discount error![Request Final Fee=%s,Calculated Final Fee=%s]", discountAmout, finalAmount));
    }
    return finalAmount;
  }

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

  public static BigDecimal calculateFeeRatioForQuarter(Date admissionDate) {
    Calendar cal = Calendar. getInstance();
    cal.setTime(admissionDate);
    int currMonth = cal.get(Calendar.MONTH);
    currMonth=currMonth+1;// for removing 0 to one indexing
    int currDay = cal.get(Calendar.DATE);
    int quarterEnd=quarterEndMonth(getQuarter(currMonth));
    double feeMonthRatio=quarterEnd-currMonth;
    if(15<currDay){
      feeMonthRatio=feeMonthRatio+0.5;
    }else{
      feeMonthRatio=feeMonthRatio+1.00;
    }
    return new BigDecimal(feeMonthRatio);
  }
}
