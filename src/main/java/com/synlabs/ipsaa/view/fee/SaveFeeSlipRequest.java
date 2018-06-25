package com.synlabs.ipsaa.view.fee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sushil on 27-04-2017.
 */
public class SaveFeeSlipRequest implements Request
{
  private Long        id;
  private BigDecimal  extraCharge;
  private BigDecimal  latePaymentCharge;
  private String      comments;
  private PaymentMode paymentMode;
  private String      txnid;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date        paymentDate;
  private BigDecimal  deposit;
  private BigDecimal  annualFee;
  private BigDecimal  paidAmount;
  private String      slipSerial;
  private String      receiptSerial;
  private BigDecimal  balance;
  private BigDecimal  adjust;
  private Boolean confirmed = false;

  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public void setAdjust(BigDecimal adjust)
  {
    this.adjust = adjust;
  }

  public BigDecimal getBalance()
  {
    return balance;
  }

  public void setBalance(BigDecimal balance)
  {
    this.balance = balance;
  }

  public Boolean getConfirmed()
  {
    return confirmed;
  }

  public void setConfirmed(Boolean confirmed)
  {
    this.confirmed = confirmed;
  }

  public String getSlipSerial()
  {
    return slipSerial;
  }

  public void setSlipSerial(String slipSerial)
  {
    this.slipSerial = slipSerial;
  }

  public String getReceiptSerial()
  {
    return receiptSerial;
  }

  public void setReceiptSerial(String receiptSerial)
  {
    this.receiptSerial = receiptSerial;
  }

  public BigDecimal getDeposit()
  {
    return deposit;
  }

  public void setDeposit(BigDecimal deposit)
  {
    this.deposit = deposit;
  }

  public BigDecimal getAnnualFee()
  {
    return annualFee;
  }

  public void setAnnualFee(BigDecimal annualFee)
  {
    this.annualFee = annualFee;
  }

  public PaymentMode getPaymentMode()
  {
    return paymentMode;
  }

  public void setPaymentMode(PaymentMode paymentMode)
  {
    this.paymentMode = paymentMode;
  }

  public String getTxnid()
  {
    return txnid;
  }

  public void setTxnid(String txnid)
  {
    this.txnid = txnid;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public BigDecimal getExtraCharge()
  {
    return extraCharge;
  }

  public void setExtraCharge(BigDecimal extraCharge)
  {
    this.extraCharge = extraCharge;
  }

  public BigDecimal getLatePaymentCharge()
  {
    return latePaymentCharge;
  }

  public void setLatePaymentCharge(BigDecimal latePaymentCharge)
  {
    this.latePaymentCharge = latePaymentCharge;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments(String comments)
  {
    this.comments = comments;
  }

  public Date getPaymentDate()
  {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate)
  {
    this.paymentDate = paymentDate;
  }

  public BigDecimal getPaidAmount()
  {
    return paidAmount;
  }

  public void setPaidAmount(BigDecimal paidAmount)
  {
    this.paidAmount = paidAmount;
  }
}
