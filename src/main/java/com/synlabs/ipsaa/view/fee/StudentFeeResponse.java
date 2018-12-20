package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.view.student.StudentSummaryResponse;

import java.math.BigDecimal;

import static com.synlabs.ipsaa.util.BigDecimalUtils.THREE;
import static com.synlabs.ipsaa.util.BigDecimalUtils.ZERO;

public class StudentFeeResponse extends StudentSummaryResponse
{
  private Long   id;
  private String comment;
  private String feeDuration;
  private boolean found = true;

  private BigDecimal adjust;
  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;


  private BigDecimal baseFee;
  private BigDecimal finalFee;
  private BigDecimal discount;
  private BigDecimal transportFee;

  private BigDecimal  discountBaseFee;
  private BigDecimal  discountAnnualCharges;
  private BigDecimal  discountAdmissionCharges;
  private BigDecimal  discountSecurityDeposit;

  private BigDecimal  finalBaseFee;
  private BigDecimal  finalAnnualFee;
  private BigDecimal  finalAdmissionCharges;
  private BigDecimal  finalSecurityDeposit;

  private BigDecimal  annualFee;
  private BigDecimal  admissionCharges;
  private BigDecimal  securityDeposit;
  private BigDecimal gstAmount;


  public StudentFeeResponse(StudentFee studentfee)
  {
    super(studentfee.getStudent());
    id = studentfee.getId();
    this.finalFee = studentfee.getFinalFee();
    this.comment = studentfee.getComment();
    this.discount = studentfee.getDiscount() == null ? new BigDecimal(0) : studentfee.getDiscount();
    this.feeDuration = studentfee.getFeeDuration().name();
    this.baseFee = studentfee.getBaseFee();
    this.transportFee = studentfee.getTransportFee();
    this.adjust = studentfee.getAdjust();
    this.sgst = studentfee.getSgst();
    this.cgst = studentfee.getCgst();
    this.igst = studentfee.getIgst();

    this.discountBaseFee=studentfee.getBaseFeeDiscount();
    this.discountAdmissionCharges=studentfee.getAddmissionFeeDiscount();
    this.discountAnnualCharges=studentfee.getAnnualFeeDiscount();
    this.discountSecurityDeposit=studentfee.getDepositFeeDiscount();

    this.finalAdmissionCharges=studentfee.getFinalAdmissionFee();
    this.finalAnnualFee=studentfee.getFinalAnnualCharges();
    this.finalBaseFee=studentfee.getFinalBaseFee().divide(THREE,2,BigDecimal.ROUND_CEILING); // chnage quaterly fee to monthly
    this.finalSecurityDeposit=studentfee.getFinalDepositFee();

    this.annualFee=studentfee.getAnnualCharges()==null?ZERO:studentfee.getAnnualCharges();
    this.admissionCharges=studentfee.getAdmissionFee()==null?ZERO:studentfee.getAdmissionFee();
    this.securityDeposit=studentfee.getDepositFee()==null?ZERO:studentfee.getDepositFee();
    this.gstAmount=studentfee.getGstAmount()==null?ZERO:studentfee.getGstAmount();
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setFeeDuration(String feeDuration) {
    this.feeDuration = feeDuration;
  }

  public void setFound(boolean found) {
    this.found = found;
  }

  public void setBaseFee(BigDecimal baseFee) {
    this.baseFee = baseFee;
  }

  public void setFinalFee(BigDecimal finalFee) {
    this.finalFee = finalFee;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public void setTransportFee(BigDecimal transportFee) {
    this.transportFee = transportFee;
  }

  public void setAdjust(BigDecimal adjust) {
    this.adjust = adjust;
  }

  public void setCgst(BigDecimal cgst) {
    this.cgst = cgst;
  }

  public void setSgst(BigDecimal sgst) {
    this.sgst = sgst;
  }

  public void setIgst(BigDecimal igst) {
    this.igst = igst;
  }

  public BigDecimal getDiscountBaseFee() {
    return discountBaseFee;
  }

  public void setDiscountBaseFee(BigDecimal discountBaseFee) {
    this.discountBaseFee = discountBaseFee;
  }

  public BigDecimal getDiscountAnnualCharges() {
    return discountAnnualCharges;
  }

  public void setDiscountAnnualCharges(BigDecimal discountAnnualCharges) {
    this.discountAnnualCharges = discountAnnualCharges;
  }

  public BigDecimal getDiscountAdmissionCharges() {
    return discountAdmissionCharges;
  }

  public void setDiscountAdmissionCharges(BigDecimal discountAdmissionCharges) {
    this.discountAdmissionCharges = discountAdmissionCharges;
  }

  public BigDecimal getDiscountSecurityDeposit() {
    return discountSecurityDeposit;
  }

  public void setDiscountSecurityDeposit(BigDecimal discountSecurityDeposit) {
    this.discountSecurityDeposit = discountSecurityDeposit;
  }

  public BigDecimal getFinalBaseFee() {
    return finalBaseFee;
  }

  public void setFinalBaseFee(BigDecimal finalBaseFee) {
    this.finalBaseFee = finalBaseFee;
  }

  public BigDecimal getFinalAnnualFee() {
    return finalAnnualFee;
  }

  public void setFinalAnnualFee(BigDecimal finalAnnualFee) {
    this.finalAnnualFee = finalAnnualFee;
  }

  public BigDecimal getFinalAdmissionCharges() {
    return finalAdmissionCharges;
  }

  public void setFinalAdmissionCharges(BigDecimal finalAdmissionCharges) {
    this.finalAdmissionCharges = finalAdmissionCharges;
  }

  public BigDecimal getFinalSecurityDeposit() {
    return finalSecurityDeposit;
  }

  public void setFinalSecurityDeposit(BigDecimal finalSecurityDeposit) {
    this.finalSecurityDeposit = finalSecurityDeposit;
  }

  public BigDecimal getAnnualFee() {
    return annualFee;
  }

  public void setAnnualFee(BigDecimal annualFee) {
    this.annualFee = annualFee;
  }

  public BigDecimal getAdmissionCharges() {
    return admissionCharges;
  }

  public void setAdmissionCharges(BigDecimal admissionCharges) {
    this.admissionCharges = admissionCharges;
  }

  public BigDecimal getSecurityDeposit() {
    return securityDeposit;
  }

  public void setSecurityDeposit(BigDecimal securityDeposit) {
    this.securityDeposit = securityDeposit;
  }
  public BigDecimal getGstAmount() {
    return gstAmount;
  }

  public void setGstAmount(BigDecimal gstAmount) {
    this.gstAmount = gstAmount;
  }

  public BigDecimal getIgst()
  {
    return igst;
  }

  public BigDecimal getCgst()
  {
    return cgst;
  }

  public BigDecimal getSgst()
  {
    return sgst;
  }

  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public Long getStudentId()
  {
    return super.getId();
  }

  public BigDecimal getTransportFee()
  {
    return transportFee;
  }

  public BigDecimal getBaseFee()
  {
    return baseFee;
  }

  public BigDecimal getDiscount()
  {
    return discount;
  }

  public StudentFeeResponse()
  {
    this.found = false;
  }

  @Override
  public Long getId()
  {
    return mask(id);
  }

  public BigDecimal getFinalFee()
  {
    return finalFee;
  }

  public String getComment()
  {
    return comment;
  }

  public String getFeeDuration()
  {
    return feeDuration;
  }

  public boolean isFound()
  {
    return found;
  }
}
