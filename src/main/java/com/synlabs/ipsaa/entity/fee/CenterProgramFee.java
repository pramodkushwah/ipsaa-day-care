package com.synlabs.ipsaa.entity.fee;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Center profile represents programs, groups and fee structure for a center
 */
@Entity
public class CenterProgramFee extends BaseEntity
{
// shubham-------------------------------------------------shubham---------------------------------------------------------
private BigDecimal addmissionFee;

  public BigDecimal getAddmissionFee() {
    return addmissionFee;
  }

  public void setAddmissionFee(BigDecimal addmissionFee) {

    this.addmissionFee = addmissionFee;
  }
// shubham-------------------------------------------------shubham---------------------------------------------------------
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Center center;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Program program;

  private int fee;

  private int deposit;

  private int annualFee;

  @Column(precision = 16, scale = 2)
  private BigDecimal cgst;

  @Column(precision = 16, scale = 2)
  private BigDecimal sgst;

  @Column(precision = 16, scale = 2)
  private BigDecimal igst;

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

  public int getAnnualFee()
  {
    return annualFee;
  }

  public void setAnnualFee(int annualFee)
  {
    this.annualFee = annualFee;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public int getFee()
  {
    return fee;
  }

  public void setFee(int fee)
  {
    this.fee = fee;
  }

  public int getDeposit()
  {
    return deposit;
  }

  public void setDeposit(int deposit)
  {
    this.deposit = deposit;
  }

  public Program getProgram()
  {
    return program;
  }

  public void setProgram(Program program)
  {
    this.program = program;
  }
}
