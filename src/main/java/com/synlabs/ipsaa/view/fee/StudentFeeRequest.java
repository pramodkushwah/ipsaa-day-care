package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

public class StudentFeeRequest implements Request
{
  private Long        id;
  private Long        centerId;
  private Long        studentId;
  private String      comment;
  private BigDecimal  baseFee;
  private BigDecimal  discount;
  private BigDecimal  finalFee;
  private BigDecimal  transportFee;
  private BigDecimal  adjust;
  private BigDecimal  cgst;
  private BigDecimal  sgst;
  private BigDecimal  igst;
  private FeeDuration feeDuration;

  public StudentFeeRequest()
  {
  }

  public StudentFeeRequest(Long centerId)
  {
    this.centerId = centerId;
  }

  public StudentFee toEntity(StudentFee studentFee)
  {
    if (studentFee == null)
    {
      studentFee = new StudentFee();
    }
    studentFee.setBaseFee(baseFee);
    studentFee.setFinalFee(finalFee);
    studentFee.setComment(comment);
    studentFee.setFeeDuration(getFeeDuration());
    studentFee.setTransportFee(transportFee == null ? BigDecimal.ZERO : transportFee);
    studentFee.setDiscount(discount == null ? BigDecimal.ZERO : discount);
    studentFee.setAdjust(adjust == null ? BigDecimal.ZERO : adjust);
    studentFee.setCgst(cgst);
    studentFee.setSgst(sgst);
    studentFee.setIgst(igst);
    return studentFee;
  }

  public boolean isEmpty()
  {
    return baseFee == null
        && finalFee == null
        && feeDuration == null;
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

  public FeeDuration getFeeDuration()
  {
    return feeDuration;
  }

  public void setFeeDuration(FeeDuration feeDuration)
  {
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
