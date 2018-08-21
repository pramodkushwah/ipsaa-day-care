package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

/**
 * Created by itrs on 4/20/2017.
 */
public class CenterProgramFeeRequest implements Request
{
  private Long       id;
  private Long       programId;
  private Long       centerId;
  private Integer    fee;
  private Integer    deposit;
  private Integer    annualFee;
  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;
  private Integer admissionFee;

  public Integer getAdmissionFee() {
    return admissionFee;
  }

  public void setAdmissionFee(Integer admissionFee) {
    this.admissionFee = admissionFee;
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

  public Integer getAnnualFee()
  {
    return annualFee;
  }

  public void setAnnualFee(Integer annualFee)
  {
    this.annualFee = annualFee;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getProgramId()
  {
    return unmask(programId);
  }

  public Long getMaskedProgramId()
  {
    return programId;
  }

  public Long getMaskedCenterId()
  {
    return programId;
  }

  public void setProgramId(Long programId)
  {
    this.programId = programId;
  }

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Integer getFee()
  {
    return fee;
  }

  public void setFee(Integer fee)
  {
    this.fee = fee;
  }

  public Integer getDeposit()
  {
    return deposit;
  }

  public void setDeposit(Integer deposit)
  {
    this.deposit = deposit;
  }

  public CenterProgramFee toEntity(CenterProgramFee fee)
  {
    fee = fee == null ? new CenterProgramFee() : fee;
    fee.setFee(this.fee);
    fee.setAnnualFee(this.annualFee);
    fee.setDeposit(this.deposit);
    fee.setCgst(this.cgst);
    fee.setSgst(this.sgst);
    fee.setIgst(this.igst);
    fee.setAddmissionFee(new BigDecimal(this.admissionFee));
    return fee;
  }

  @Override
  public String toString()
  {
    return "CenterProgramFeeRequest{" +
        "id=" + id +
        ", programId=" + programId +
        ", centerId=" + centerId +
        ", fee=" + fee +
        ", deposit=" + deposit +
        '}';
  }
}
