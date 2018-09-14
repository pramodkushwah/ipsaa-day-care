package com.synlabs.ipsaa.entity.student;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.FeeDuration;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity representing fee offered to a student
 */
@Entity
public class StudentFee extends BaseEntity
{
  // ---------------------------------------------------shubham------------------------------------------------------

  @Column(precision = 16, scale = 2)
  private BigDecimal annualCharges;
  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal finalAnnualCharges;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal admissionFee;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal finalAdmissionFee;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal finalBaseFee;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal baseFee;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal depositFee;
  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal finalDepositFee;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal baseFeeDiscount;
  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal annualFeeDiscount;
  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal addmissionFeeDiscount;
  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal depositFeeDiscount;

  @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
  private BigDecimal gstAmount;


  public BigDecimal getGstAmount() {
    return gstAmount;
  }

  public void setGstAmount(BigDecimal gstAmount) {
    this.gstAmount = gstAmount;
  }

  public BigDecimal getAnnualCharges() {
    return annualCharges;
  }

  public void setAnnualCharges(BigDecimal annualCharges) {
    this.annualCharges = annualCharges;
  }

  public BigDecimal getFinalBaseFee() {
    return finalBaseFee;
  }

  public void setFinalBaseFee(BigDecimal finalBaseFee) {
    this.finalBaseFee = finalBaseFee;
  }

  public BigDecimal getDepositFee() {
    return depositFee;
  }

  public void setDepositFee(BigDecimal depositFee) {
    this.depositFee = depositFee;
  }

  public BigDecimal getBaseFeeDiscount() {
    return baseFeeDiscount;
  }

  public void setBaseFeeDiscount(BigDecimal baseFeeDiscount) {
    this.baseFeeDiscount = baseFeeDiscount;
  }

  public BigDecimal getAnnualFeeDiscount() {
    return annualFeeDiscount;
  }

  public void setAnnualFeeDiscount(BigDecimal annualFeeDiscount) {
    this.annualFeeDiscount = annualFeeDiscount;
  }

  public BigDecimal getAddmissionFeeDiscount() {
    return addmissionFeeDiscount;
  }

  public void setAddmissionFeeDiscount(BigDecimal addmissionFeeDiscount) {
    this.addmissionFeeDiscount = addmissionFeeDiscount;
  }

  public BigDecimal getDepositFeeDiscount() {
    return depositFeeDiscount;
  }

  public void setDepositFeeDiscount(BigDecimal depositFeeDiscount) {
    this.depositFeeDiscount = depositFeeDiscount;
  }

  public BigDecimal getAdmissionFee() {
    return admissionFee;
  }

  public void setAdmissionFee(BigDecimal admissionFee) {
    this.admissionFee = admissionFee;
  }

  public BigDecimal getFinalAnnualCharges() {
    return finalAnnualCharges;
  }

  public void setFinalAnnualCharges(BigDecimal finalAnnualCharges) {
    this.finalAnnualCharges = finalAnnualCharges;
  }

  public BigDecimal getFinalAdmissionFee() {
    return finalAdmissionFee;
  }

  public void setFinalAdmissionFee(BigDecimal finalAdmissionFee) {
    this.finalAdmissionFee = finalAdmissionFee;
  }

  public BigDecimal getFinalDepositFee() {
    return finalDepositFee;
  }

  public void setFinalDepositFee(BigDecimal finalDepositFee) {
    this.finalDepositFee = finalDepositFee;
  }

  // ---------------------------------------------------shubham------------------------------------------------------
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Student student;

  @Column(precision = 16, scale = 2)
  private BigDecimal finalFee;

  @Column(precision = 16, scale = 2)
  private BigDecimal discount;


  @Column(precision = 16, scale = 2)
  private BigDecimal transportFee;

  @Column(precision = 16, scale = 6)
  private BigDecimal adjust;

  @Column(precision = 16, scale = 2)
  private BigDecimal cgst;

  @Column(precision = 16, scale = 2)
  private BigDecimal sgst;

  @Column(precision = 16, scale = 2)
  private BigDecimal igst;

  @Column(length = 200)
  private String comment;

  @Enumerated(EnumType.STRING)
  private FeeDuration feeDuration;

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public BigDecimal getIgst()
  {
    return igst;
  }

  public void setIgst(BigDecimal igst)
  {
    this.igst = igst;
  }

  public BigDecimal getCgst()
  {
    return cgst;
  }

  public void setCgst(BigDecimal cgst)
  {
    this.cgst = cgst;
  }

  public BigDecimal getSgst()
  {
    return sgst;
  }

  public void setSgst(BigDecimal sgst)
  {
    this.sgst = sgst;
  }

  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public void setAdjust(BigDecimal adjust)
  {
    this.adjust = adjust;
  }

  public Student getStudent()
  {
    return student;
  }

  public void setStudent(Student student)
  {
    this.student = student;
  }

  public BigDecimal getBaseFee()
  {
    return baseFee;
  }

  public void setBaseFee(BigDecimal baseFee)
  {
    this.baseFee = baseFee;
  }

  public BigDecimal getFinalFee()
  {
    return finalFee;
  }

  public void setFinalFee(BigDecimal finalFee)
  {
    this.finalFee = finalFee;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }
  public FeeDuration getFeeDuration()
  {
    return feeDuration;
  }

  public void setFeeDuration(FeeDuration feeDuration)
  {
    this.feeDuration = feeDuration;
  }

  public BigDecimal getTransportFee()
  {
    return transportFee;
  }

  public void setTransportFee(BigDecimal transportFee)
  {
    this.transportFee = transportFee;
  }
}
