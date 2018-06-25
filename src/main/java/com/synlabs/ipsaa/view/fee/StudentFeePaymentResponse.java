package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

public class StudentFeePaymentResponse implements Response
{
  private Long        id;
  private String      txnid;
  private PaymentMode paymentMode;
  private BigDecimal  paidAmount;
  private String      paymentDate;
  private Boolean     confirmed;

  public StudentFeePaymentResponse(StudentFeePaymentRecord payment) {
    this.id = mask(payment.getId());
    this.paidAmount = payment.getPaidAmount();
    this.txnid = payment.getTxnid();
    this.paymentDate = payment.getPaymentDate() == null ? null : payment.getPaymentDate().toString();
    this.paymentMode = payment.getPaymentMode();
    this.confirmed = payment.getConfirmed();
  }

  public Boolean getConfirmed()
  {
    return confirmed;
  }

  public Long getId()
  {
    return id;
  }

  public String getTxnid()
  {
    return txnid;
  }

  public PaymentMode getPaymentMode()
  {
    return paymentMode;
  }

  public BigDecimal getPaidAmount()
  {
    return paidAmount;
  }

  public String getPaymentDate()
  {
    return paymentDate;
  }
}
