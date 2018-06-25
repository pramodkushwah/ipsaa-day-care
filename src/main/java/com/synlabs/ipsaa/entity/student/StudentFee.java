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
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Student student;

  @Column(precision = 16, scale = 2)
  private BigDecimal baseFee;

  @Column(precision = 9, scale = 6)
  private BigDecimal discount;

  @Column(precision = 16, scale = 2)
  private BigDecimal finalFee;

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

  public BigDecimal getTransportFee()
  {
    return transportFee;
  }

  public void setTransportFee(BigDecimal transportFee)
  {
    this.transportFee = transportFee;
  }
}
