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
  private Boolean     active;

  private BigDecimal uniformPaidAmount;
  private BigDecimal stationaryPaidAmount;
  private BigDecimal annualPaidAmount;
  private BigDecimal addmissionPaidAmount;
  private BigDecimal depositPaidAmount;
  private BigDecimal programPaidAmount;
  private BigDecimal transportPaidAmount;

  public StudentFeePaymentResponse(StudentFeePaymentRecord payment) {
    this.id = mask(payment.getId());
    this.active=payment.getActive();
    this.paidAmount = payment.getPaidAmount();
    this.txnid = payment.getTxnid();
    this.paymentDate = payment.getPaymentDate() == null ? null : payment.getPaymentDate().toString();
    this.paymentMode = payment.getPaymentMode();
    this.confirmed = payment.getConfirmed();

    this.uniformPaidAmount=payment.getUniformPaidAmount();
    this.stationaryPaidAmount=payment.getStationaryPaidAmount();
    this.annualPaidAmount=payment.getAnnualPaidAmount();
    this.addmissionPaidAmount=payment.getAddmissionPaidAmount() ;
    this.depositPaidAmount=payment.getDepositPaidAmount();
    this.programPaidAmount=payment.getProgramPaidAmount();
    this.transportPaidAmount=payment.getTransportPaidAmount();
  }

  public Boolean getActive() {
    return active;
  }
  public void setActive(Boolean active) {
    this.active = active;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTxnid(String txnid) {
    this.txnid = txnid;
  }

  public void setPaymentMode(PaymentMode paymentMode) {
    this.paymentMode = paymentMode;
  }

  public void setPaidAmount(BigDecimal paidAmount) {
    this.paidAmount = paidAmount;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public void setConfirmed(Boolean confirmed) {
    this.confirmed = confirmed;
  }

  public BigDecimal getUniformPaidAmount() {
    return uniformPaidAmount;
  }

  public void setUniformPaidAmount(BigDecimal uniformPaidAmount) {
    this.uniformPaidAmount = uniformPaidAmount;
  }

  public BigDecimal getStationaryPaidAmount() {
    return stationaryPaidAmount;
  }

  public void setStationaryPaidAmount(BigDecimal stationaryPaidAmount) {
    this.stationaryPaidAmount = stationaryPaidAmount;
  }

  public BigDecimal getAnnualPaidAmount() {
    return annualPaidAmount;
  }

  public void setAnnualPaidAmount(BigDecimal annualPaidAmount) {
    this.annualPaidAmount = annualPaidAmount;
  }

  public BigDecimal getAddmissionPaidAmount() {
    return addmissionPaidAmount;
  }

  public void setAddmissionPaidAmount(BigDecimal addmissionPaidAmount) {
    this.addmissionPaidAmount = addmissionPaidAmount;
  }

  public BigDecimal getDepositPaidAmount() {
    return depositPaidAmount;
  }

  public void setDepositPaidAmount(BigDecimal depositPaidAmount) {
    this.depositPaidAmount = depositPaidAmount;
  }

  public BigDecimal getProgramPaidAmount() {
    return programPaidAmount;
  }

  public void setProgramPaidAmount(BigDecimal programPaidAmount) {
    this.programPaidAmount = programPaidAmount;
  }

  public BigDecimal getTransportPaidAmount() {
    return transportPaidAmount;
  }

  public void setTransportPaidAmount(BigDecimal transportPaidAmount) {
    this.transportPaidAmount = transportPaidAmount;
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
