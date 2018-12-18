package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.GST;
import com.synlabs.ipsaa.ex.ValidationException;

import java.math.BigDecimal;

public class FeeUtils
{
  private static final String months[] = new String[] { "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };

  public static final double FEE_CALCULATION_TOLERANCE = 5.0;

  public static final BigDecimal ZERO    = BigDecimal.ZERO;
  public static final BigDecimal ONE     = BigDecimal.ONE;
  public static final BigDecimal THREE   = new BigDecimal(3);
  public static final BigDecimal TWELVE  = new BigDecimal(12);
  public static final BigDecimal HUNDRED = new BigDecimal(100);

  /**
   * month starting from 1 to 12
   * return
   *
   * @param month
   * @return empty string if month is negative or zero otherwise returns month name
   */
  public static String getMonthName(int month)
  {
    if (month <= 0)
    {
      return "";
    }
    month--;
    month = month % 12;
    return months[month];
  }

  public static String getMonth(StudentFeePaymentRequest studentFeePaymentRequest)
  {
    int year = studentFeePaymentRequest.getYear();
    switch (studentFeePaymentRequest.getFeeDuration())
    {
      case Monthly:
        return months[studentFeePaymentRequest.getMonth() - 1] + ", " + year;
      case Yearly:
        return "Jan, " + year + " to " + "Dec, " + year;
      case Quarterly:
        switch (studentFeePaymentRequest.getQuarter())
        {
          case 1:
            return "Jan, " + (year) + " to March, " + (year);
          case 2:
            return "April, " + year + " to June, " + year;
          case 3:
            return "July, " + year + " to Sep, " + year;
          case 4:
            return "Oct, " + year + " to Dec, " + year;
        }
    }
    return "";
  }
  public static String getMonth(StudentFeePaymentRequestIpsaaClub studentFeePaymentRequest)
  {
    if(studentFeePaymentRequest.getExpireDate()!=null)
    return studentFeePaymentRequest.getInvoiceDate()+" from "+studentFeePaymentRequest.getExpireDate();
    else return "";
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

  public static String getFYQuarter(int quarter)
  {
    switch (quarter)
    {
      case 1:
        return "FYQ4";// jan feb march
      case 2:
        return "FYQ1";// april may june
      case 3:
        return "FYQ2";// july aug sep
      case 4:
        return "FYQ3";// oct nov dec
    }
    return "";
  }

  public static BigDecimal calculateTotalFee(StudentFeePaymentRequest slip)
  {
    BigDecimal finalFee = ZERO;
    BigDecimal annualFee = slip.getAnnualFee() == null ? ZERO : slip.getAnnualFee();
    BigDecimal balance = slip.getBalance() == null ? ZERO : slip.getBalance();
    BigDecimal extraCharge = slip.getExtraCharge() == null ? ZERO : slip.getExtraCharge();
    BigDecimal latePaymentCharges = slip.getLatePaymentCharge() == null ? ZERO : slip.getLatePaymentCharge();
    BigDecimal deposit = slip.getDeposit() == null ? ZERO : slip.getDeposit();
    BigDecimal adjustAmount = slip.getAdjust() == null ? ZERO : slip.getAdjust();

    BigDecimal cgst = slip.getCgst() == null ? ZERO : slip.getCgst();
    BigDecimal sgst = slip.getSgst() == null ? ZERO : slip.getSgst();
    BigDecimal igst = slip.getIgst() == null ? ZERO : slip.getIgst();

    finalFee = finalFee.add(slip.getBaseFee())
                       .add(annualFee);

    BigDecimal cgstAmount = finalFee.multiply(cgst).divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
    BigDecimal sgstAmount = finalFee.multiply(sgst).divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
    BigDecimal igstAmount = finalFee.multiply(igst).divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);

    finalFee = finalFee
        .add(cgstAmount)
        .add(sgstAmount)
        .add(igstAmount)
        .setScale(0, BigDecimal.ROUND_DOWN);

    finalFee = finalFee
        .subtract(adjustAmount)
        .add(balance)
        .add(extraCharge)
        .add(latePaymentCharges)
        .add(deposit);
    return finalFee;
  }

  public static BigDecimal calculateGST(StudentFeePaymentRequest slip, GST type)
  {
    BigDecimal finalFee = ZERO;
    finalFee = finalFee.add(slip.getBaseFee());
    if (slip.getAnnualFee() != null)
    {
      finalFee = finalFee.add(slip.getAnnualFee());
    }

    BigDecimal gst = type == GST.CGST ? slip.getCgst() == null ? ZERO : slip.getCgst() :
                     type == GST.SGST ? slip.getSgst() == null ? ZERO : slip.getSgst() :
                     type == GST.IGST ? slip.getIgst() == null ? ZERO : slip.getIgst() : ZERO;
    finalFee = finalFee.multiply(gst)
                       .divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
    return finalFee;
  }

  public static BigDecimal calculateFinalFee(BigDecimal baseFee, FeeDuration duration, BigDecimal discount, BigDecimal adjust)
  {
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
    switch (duration)
    {
      case Monthly:
        finalFee = finalFee.multiply(BigDecimal.ONE);
        break;
      case Quarterly:
        finalFee = finalFee.multiply(THREE);
        break;
      case Yearly:
        finalFee = finalFee.multiply(TWELVE);
        break;
    }

    return finalFee;
  }

  public static BigDecimal calculateFinalFee(StudentFee studentFee)
  {
    studentFee.setBaseFeeDiscount(studentFee.getBaseFeeDiscount() == null ?
                           BigDecimal.ZERO :
                           studentFee.getBaseFeeDiscount());
    studentFee.setTransportFee(studentFee.getTransportFee() == null ?
                               BigDecimal.ZERO :
                               studentFee.getTransportFee());
    studentFee.setAdjust(studentFee.getAdjust() == null ?
                         BigDecimal.ZERO :
                         studentFee.getAdjust());

    BigDecimal baseFee = studentFee.getBaseFee();
    BigDecimal discount = studentFee.getBaseFeeDiscount();
    BigDecimal transportFee = studentFee.getTransportFee();
    BigDecimal adjust = studentFee.getAdjust();
    FeeDuration feeDuration = studentFee.getFeeDuration();

    BigDecimal finalFee = calculateFinalFee(baseFee, feeDuration, discount, adjust);
    switch(feeDuration){
    case Monthly:
    	transportFee = transportFee.multiply(BigDecimal.ONE);
        break;
      case Quarterly:
    	  transportFee = transportFee.multiply(THREE);
        break;
      case Yearly:
    	  transportFee = transportFee.multiply(TWELVE);
        break;
    }
    finalFee = finalFee.add(transportFee);
    return finalFee;
  }

  public static void validateStudentFee(StudentFee fee)
  {
    validateStudentFee(fee, true);
  }

  public static boolean validateStudentFee(StudentFee fee, boolean thrEx)
  {
    //calculate fee calculation diff
    BigDecimal calculateFinalFee = FeeUtils.calculateFinalFee(fee);
    BigDecimal diff = calculateFinalFee.subtract(fee.getFinalFee());

    //compare diff with tolerance
    if (Math.abs(diff.doubleValue()) >= FEE_CALCULATION_TOLERANCE)
    {
      if (thrEx)
      {
        throw new ValidationException(
            String.format("Final Fee calculation error![Request Final Fee=%s,Calculated Final Fee=%s]", fee.getFinalFee(), calculateFinalFee));
      }
      else
      {
        return false;
      }
    }
    return true;
  }

}
