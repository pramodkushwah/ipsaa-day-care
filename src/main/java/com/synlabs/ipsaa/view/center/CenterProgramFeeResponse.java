package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

/**
 * Created by itrs on 4/20/2017.
 */
public class CenterProgramFeeResponse implements Response
{
  private ProgramResponse program;
  private CenterResponse  center;

  private Integer fee;
  private Integer deposit;
  private Integer annualFee;
  private Integer admissionFee;

  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;

  public CenterProgramFeeResponse(CenterProgramFee centerProgramFee)
  {
    if (centerProgramFee == null)
    {
      return;
    }
    fee = centerProgramFee.getFee();
    admissionFee=centerProgramFee.getAddmissionFee()==null?0:centerProgramFee.getAddmissionFee().intValue();
    deposit = centerProgramFee.getDeposit();
    annualFee = centerProgramFee.getAnnualFee();
    program = centerProgramFee.getProgram() != null ? new ProgramResponse(centerProgramFee.getProgram()) : null;
    center = centerProgramFee.getCenter() != null ? new CenterResponse(centerProgramFee.getCenter()) : null;
    cgst = centerProgramFee.getCgst();
    sgst = centerProgramFee.getSgst();
    igst = centerProgramFee.getIgst();
  }

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

  public BigDecimal getCgst()
  {
    return cgst;
  }

  public BigDecimal getSgst()
  {
    return sgst;
  }

  public Integer getAnnualFee()
  {
    return annualFee;
  }

  public void setAnnualFee(Integer annualFee)
  {
    this.annualFee = annualFee;
  }

  public ProgramResponse getProgram()
  {
    return program;
  }

  public void setProgram(ProgramResponse program)
  {
    this.program = program;
  }

  public CenterResponse getCenter()
  {
    return center;
  }

  public void setCenter(CenterResponse center)
  {
    this.center = center;
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
}
