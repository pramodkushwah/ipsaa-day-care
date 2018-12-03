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
  private Integer securityDeposit;
  private Integer annualFee;
  private Integer admissionCharges;

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
    admissionCharges =centerProgramFee.getAddmissionFee()==null?0:centerProgramFee.getAddmissionFee().intValue();
    securityDeposit = centerProgramFee.getDeposit();
    annualFee = centerProgramFee.getAnnualFee();
    program = centerProgramFee.getProgram() != null ? new ProgramResponse(centerProgramFee.getProgram()) : null;
    center = centerProgramFee.getCenter() != null ? new CenterResponse(centerProgramFee.getCenter()) : null;
    cgst = centerProgramFee.getCgst();
    sgst = centerProgramFee.getSgst();
    igst = centerProgramFee.getIgst();
  }

  public Integer getAdmissionCharges() {
    return admissionCharges;
  }

  public void setAdmissionCharges(Integer admissionCharges) {
    this.admissionCharges = admissionCharges;
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

  public Integer getSecurityDeposit()
  {
    return securityDeposit;
  }

  public void setSecurityDeposit(Integer securityDeposit)
  {
    this.securityDeposit = securityDeposit;
  }
}
