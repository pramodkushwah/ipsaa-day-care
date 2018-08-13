package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StudentFeeSlipResponse implements Response
{
  private Long id;

  private String     fullName;
  private String     group;
  private String     program;
  private int        month;
  private int        quarter;
  private int        year;
  private BigDecimal fee;
  private BigDecimal extraCharge;
  private BigDecimal latePaymentCharge;
  private BigDecimal totalFee;
  private String     invoiceDate;
  private String     status;
  private String     comments;
  private String     feeDuration;
  private BigDecimal deposit;
  private BigDecimal annualFee;
  private BigDecimal payableAmount;
  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;
  private BigDecimal balance;
  private BigDecimal adjust;
  private String     autoComments;
  private boolean generateActive;

  private List<StudentFeePaymentResponse> payments;

  public BigDecimal getIgst()
  {
    return igst;
  }

  public StudentFeeSlipResponse(StudentFeePaymentRequest slip)
  {

    this.id = slip.getId();
    this.fullName = slip.getStudent().getProfile().getFullName();
    this.group = slip.getStudent().getGroup().getName();
    this.program = slip.getStudent().getProgram().getName();
    this.month = slip.getMonth();
    this.quarter = slip.getQuarter();
    this.year = slip.getYear();
    this.extraCharge = slip.getExtraCharge();
    this.latePaymentCharge = slip.getLatePaymentCharge();
    this.totalFee = slip.getTotalFee();
    this.fee = slip.getBaseFee();
    this.status = slip.getPaymentStatus().name();
    this.feeDuration = slip.getFeeDuration().name();
    this.comments = slip.getComments();
    this.invoiceDate = slip.getInvoiceDate() == null ? null : slip.getInvoiceDate().toString();
    this.deposit = slip.getDeposit();
    this.annualFee = slip.getAnnualFee();
    this.payableAmount = this.totalFee;
    this.sgst = slip.getSgst();
    this.cgst = slip.getCgst();
    this.igst = slip.getIgst();
    this.balance = slip.getBalance();
    this.adjust = slip.getAdjust();
    this.generateActive = slip.isGenerateActive();

    this.adjust = this.adjust == null ? BigDecimal.ZERO : this.adjust;
    this.balance = this.balance == null ? BigDecimal.ZERO : this.balance;
    this.autoComments = slip.getAutoComments();
    if (slip.getPayments() != null && !slip.getPayments().isEmpty())
    {
      payments = new ArrayList<>(slip.getPayments().size());
      slip.getPayments().forEach(payment -> {
        payments.add(new StudentFeePaymentResponse(payment));
        this.payableAmount = this.payableAmount.subtract(payment.getPaidAmount());
      });
    }
  }

  public boolean isGenerateActive() {
	return generateActive;
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

  public BigDecimal getCgst()
  {
    return cgst;
  }

  public BigDecimal getSgst()
  {
    return sgst;
  }

  public BigDecimal getPayableAmount()
  {
    return payableAmount;
  }

  public BigDecimal getDeposit()
  {
    return deposit;
  }

  public BigDecimal getAnnualFee()
  {
    return annualFee;
  }

  public Long getId()
  {
    return mask(id);
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getGroup()
  {
    return group;
  }

  public String getProgram()
  {
    return program;
  }

  public int getMonth()
  {
    return month;
  }

  public int getQuarter()
  {
    return quarter;
  }

  public int getYear()
  {
    return year;
  }

  public BigDecimal getFee()
  {
    return fee;
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

  public String getStatus()
  {
    return status;
  }

  public String getComments()
  {
    return comments;
  }

  public String getFeeDuration()
  {
    return feeDuration;
  }

  public List<StudentFeePaymentResponse> getPayments()
  {
    return payments;
  }

  @Override
  public String toString()
  {
    return "StudentFeeSlipResponse{" +
        "id=" + id +
        ", fullName='" + fullName + '\'' +
        ", group='" + group + '\'' +
        ", program='" + program + '\'' +
        ", month=" + month +
        ", quarter=" + quarter +
        ", year=" + year +
        ", fee=" + fee +
        ", extraCharge=" + extraCharge +
        ", latePaymentCharge=" + latePaymentCharge +
        ", totalFee=" + totalFee +
        ", invoiceDate='" + invoiceDate + '\'' +
        ", status='" + status + '\'' +
        ", comment='" + comments + '\'' +
        ", feeDuration='" + feeDuration + '\'' +
        '}';
  }
}
