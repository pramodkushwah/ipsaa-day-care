package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

public class StudentFeeRequestV2 implements Request
{
  private Long        id;
  private Long        centerId;
  private Long        studentId;
  private Long        centerProgramFeeId;
  private String      comment;
  private BigDecimal  baseFee;// monthly fee
  private BigDecimal  discount;

  private BigDecimal  annualFee;
  private BigDecimal  admissionCharges;
  private BigDecimal  securityDeposit;
  private BigDecimal  uniformCharges;
  private BigDecimal  stationary;

  private BigDecimal  finalFee;
  private BigDecimal  transportFee;
  private BigDecimal  adjust;
  private FeeDuration feeDuration;


  public StudentFeeRequestV2()
  {
  }

  public StudentFeeRequestV2(Long centerId)
  {
    this.centerId = centerId;
  }

  public Long getCenterProgramFeeId() {
    return unmask(centerProgramFeeId);
  }

  public void setCenterProgramFeeId(Long centerProgramFeeId) {
    this.centerProgramFeeId = centerProgramFeeId;
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

  public BigDecimal getUniformCharges() {
    return uniformCharges;
  }

  public void setUniformCharges(BigDecimal uniformCharges) {
    this.uniformCharges = uniformCharges;
  }

  public BigDecimal getStationary() {
    return stationary;
  }

  public void setStationary(BigDecimal stationary) {
    this.stationary = stationary;
  }

  public StudentFee toEntity(StudentFee studentFee)
  {
    if (studentFee == null)
    {
      studentFee = new StudentFee();
    }
    studentFee.setUniformCharges(uniformCharges);
    studentFee.setStationary(stationary);
    studentFee.setAdmissionFee(admissionCharges);
    studentFee.setAnnualCharges(annualFee);
    studentFee.setFinalDepositFee(securityDeposit);
    studentFee.setBaseFee(baseFee);
    studentFee.setFinalFee(finalFee);
    studentFee.setComment(comment);
    studentFee.setFeeDuration(getFeeDuration());
    studentFee.setTransportFee(transportFee == null ? BigDecimal.ZERO : transportFee);
    studentFee.setDiscount(discount == null ? BigDecimal.ZERO : discount);
    studentFee.setAdjust(adjust == null ? BigDecimal.ZERO : adjust);
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

  public BigDecimal getDiscount()
  {
    return discount;
  }

  public void setDiscount(BigDecimal discount)
  {
    this.discount = discount;
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
        ", discount=" + discount +
        ", finalFee=" + finalFee +
        ", comment='" + comment + '\'' +
        ", feeDuration=" + feeDuration +
        '}';
  }
}
