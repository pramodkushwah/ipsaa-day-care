package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.util.NumberToWordConverter;

import java.math.BigDecimal;
import java.util.Date;

public class FeePaymentRecordView
{
  private BigDecimal  paidAmount;
  private Date        paymentDate;
  private PaymentMode paymentMode;
  private BigDecimal  dueAmount;
  private String paidAmountWords;

  public FeePaymentRecordView(StudentFeePaymentRecord payment)
  {
    paidAmount = payment.getPaidAmount();
    paymentDate = payment.getPaymentDate();
    paymentMode = payment.getPaymentMode();
    paidAmountWords = NumberToWordConverter.convert(paidAmount.longValue());
  }

  public String getPaidAmountWords()
  {
    return paidAmountWords;
  }

  public BigDecimal getDueAmount()
  {
    return dueAmount;
  }

  public void setDueAmount(BigDecimal dueAmount)
  {
    this.dueAmount = dueAmount;
  }

  public PaymentMode getPaymentMode()
  {
    return paymentMode;
  }

  public BigDecimal getPaidAmount()
  {
    return paidAmount;
  }

  public Date getPaymentDate()
  {
    return paymentDate;
  }
}
