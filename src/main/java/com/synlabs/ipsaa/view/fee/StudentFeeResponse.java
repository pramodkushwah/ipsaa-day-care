package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.view.student.StudentSummaryResponse;

import java.math.BigDecimal;

public class StudentFeeResponse extends StudentSummaryResponse
{
  private Long   id;
  private String comment;
  private String feeDuration;
  private boolean found = true;
  private BigDecimal baseFee;
  private BigDecimal finalFee;
  private BigDecimal discount;
  private BigDecimal transportFee;
  private BigDecimal adjust;
  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;

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
