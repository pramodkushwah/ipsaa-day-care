package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class StudentFeeRequestV2 implements Request
{
  private Long        id;
  private Long        centerId;
  private Long        studentId;
  private String      comment;
  private BigDecimal  baseFee;// monthly fee
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

  private BigDecimal  finalFee;
  private BigDecimal  transportFee;
  private BigDecimal  adjust;
  private FeeDuration feeDuration;
  private Boolean isProgramChange=false;

  public StudentFeeRequestV2()
  {
  }

  public StudentFeeRequestV2(Long centerId)
  {
    this.centerId = centerId;
  }

  public Boolean getProgramChange() {
    return isProgramChange;
  }

  public void setProgramChange(Boolean programChange) {
    isProgramChange = programChange;
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

  public StudentFee toEntity(StudentFee studentFee, CenterProgramFee fee)
  {
      if(studentFee==null || this.isProgramChange){
        if(studentFee==null) {
          studentFee = new StudentFee();
        }
      studentFee.setBaseFee(new BigDecimal(fee.getFee()));
      studentFee.setAdmissionFee(fee.getAddmissionFee());
      studentFee.setAnnualCharges(new BigDecimal(fee.getAnnualFee()));
      studentFee.setDepositFee(new BigDecimal(fee.getDeposit()));
    }else{
        studentFee.setDepositFee(securityDeposit==null?ZERO:securityDeposit);
        studentFee.setBaseFee(baseFee==null?ZERO:baseFee);
        studentFee.setAdmissionFee(admissionCharges==null?ZERO:admissionCharges);
        studentFee.setAnnualCharges(annualFee==null?ZERO:annualFee);
      }

    studentFee.setFinalFee(finalFee==null?ZERO:finalFee);
    studentFee.setComment(comment==null?"":comment);
    studentFee.setFeeDuration(getFeeDuration());
    studentFee.setTransportFee(transportFee == null ? ZERO : transportFee);

    studentFee.setFinalAdmissionFee(finalAdmissionCharges==null?ZERO:finalAdmissionCharges);
    studentFee.setFinalDepositFee(finalSecurityDeposit==null?ZERO:finalSecurityDeposit);
    studentFee.setFinalAnnualCharges(finalAnnualFee==null?ZERO:finalAnnualFee);
    studentFee.setFinalBaseFee(finalBaseFee==null?ZERO:finalBaseFee);

    studentFee.setBaseFeeDiscount(discountBaseFee == null ? ZERO : discountBaseFee);
    studentFee.setDiscount(ZERO);
    studentFee.setAddmissionFeeDiscount(discountAdmissionCharges==null?ZERO:discountAdmissionCharges);
    studentFee.setDepositFeeDiscount(discountSecurityDeposit==null?ZERO:discountSecurityDeposit);
    studentFee.setAnnualFeeDiscount(discountAnnualCharges==null?ZERO:discountAnnualCharges);
    studentFee.setAdjust(adjust == null ? ZERO : adjust);
    return studentFee;
  }

  public boolean isEmpty()
  {
    return baseFee == null
        && finalFee == null;
  }


  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public void setAdjust(BigDecimal adjust)
  {
    this.adjust = adjust;
  }

  public BigDecimal getTransportFee()
  {
    return transportFee;
  }

  public void setTransportFee(BigDecimal transportFee)
  {
    this.transportFee = transportFee;
  }

  public Long getMaskedId()
  {
    return id;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public Long getMaskedCenterId()
  {
    return centerId;
  }

  public Long getStudentId()
  {
    return unmask(studentId);
  }

  public Long getMaskedStudentId()
  {
    return studentId;
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public void setStudentId(Long studentId)
  {
    this.studentId = studentId;
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

  public FeeDuration getFeeDuration() {
    return feeDuration;
  }

  public void setFeeDuration(FeeDuration feeDuration) {
    this.feeDuration = feeDuration;
  }

  @Override
  public String toString()
  {
    return "StudentFeeRequest{" +
        "centerId=" + centerId +
        ", studentId=" + studentId +
        ", baseFee=" + baseFee +
        ", discount=" + discountBaseFee +
        ", finalFee=" + finalFee +
        ", comment='" + comment + '\'' +
        ", feeDuration=" + feeDuration +
        '}';
  }
}
