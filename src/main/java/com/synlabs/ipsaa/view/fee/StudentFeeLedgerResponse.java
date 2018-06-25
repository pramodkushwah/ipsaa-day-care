package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StudentFeeLedgerResponse implements Response
{
  private Long                            id;
  private int                             month;
  private String                          monthName;
  private int                             year;
  private BigDecimal                      baseFee;
  private BigDecimal                      extraCharge;
  private BigDecimal                      latePaymentCharge;
  private BigDecimal                      totalFee;
  private String                          invoiceDate;
  private String                          paymentStatus;
  private String                          slipSerial;
  private BigDecimal                      deposit;
  private BigDecimal                      annualFee;
  private BigDecimal                      balance;
  private BigDecimal                      adjust;
  private String                          autoComments;
  private List<StudentFeePaymentResponse> payments;

  public StudentFeeLedgerResponse(StudentFeePaymentRequest slip)
  {
    this.id = slip.getId();
    this.month = slip.getMonth();
    this.year = slip.getYear();
    this.baseFee = slip.getBaseFee();
    this.extraCharge = slip.getExtraCharge();
    this.latePaymentCharge = slip.getLatePaymentCharge();
    this.totalFee = slip.getTotalFee();
    this.invoiceDate = slip.getInvoiceDate().toString();
    this.paymentStatus = slip.getPaymentStatus().name();
    this.monthName = FeeUtils.getMonth(slip);
    this.slipSerial = slip.getSlipSerial();
    this.deposit = slip.getDeposit();
    this.annualFee = slip.getAnnualFee();
    this.balance = slip.getBalance();
    this.adjust = slip.getAdjust();

    this.adjust = this.adjust == null ? BigDecimal.ZERO : this.adjust;
    this.balance = this.balance == null ? BigDecimal.ZERO : this.balance;
    this.autoComments = slip.getAutoComments();
    if (slip.getPayments() != null && !slip.getPayments().isEmpty())
    {
      payments = new ArrayList<>(slip.getPayments().size());
      slip.getPayments().forEach(p -> {
        payments.add(new StudentFeePaymentResponse(p));
      });
    }
  }

  public String getAutoComments()
  {
    return autoComments;
  }

  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public BigDecimal getBalance()
  {
    return balance;
  }

  public BigDecimal getDeposit()
  {
    return deposit;
  }

  public BigDecimal getAnnualFee()
  {
    return annualFee;
  }

  public String getSlipSerial()
  {
    return slipSerial;
  }

  public Long getId()
  {
    return mask(id);
  }

  public int getMonth()
  {
    return month;
  }

  public int getYear()
  {
    return year;
  }

  public BigDecimal getBaseFee()
  {
    return baseFee;
  }

  public BigDecimal getExtraCharge()
  {
    return extraCharge;
  }

  public BigDecimal getLatePaymentCharge()
  {
    return latePaymentCharge;
  }

  public BigDecimal getTotalFee()
  {
    return totalFee;
  }

  public String getInvoiceDate()
  {
    return invoiceDate;
  }

  public String getPaymentStatus()
  {
    return paymentStatus;
  }

  public String getMonthName()
  {
    return monthName;
  }

  public List<StudentFeePaymentResponse> getPayments()
  {
    return payments;
  }
}
