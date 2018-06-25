package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

public class CenterFeeResponse implements Response
{
  private CenterSummaryResponse center;
  private ProgramResponse       program;

  private int  fee;
  private int  deposit;
  private int  annualFee;
  private Long id;

  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;

  public CenterFeeResponse()
  {
  }

  public CenterFeeResponse(CenterProgramFee fee)
  {
    this.id = mask(fee.getId());
    this.center = new CenterSummaryResponse(fee.getCenter());
    this.program = new ProgramResponse(fee.getProgram());
    this.fee = fee.getFee();
    this.annualFee = fee.getAnnualFee();
    this.deposit = fee.getDeposit();
    this.cgst = fee.getCgst();
    this.sgst = fee.getSgst();
    this.igst = fee.getIgst();
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

  public int getAnnualFee()
  {
    return annualFee;
  }

  public Long getId()
  {
    return id;
  }

  public CenterSummaryResponse getCenter()
  {
    return center;
  }

  public ProgramResponse getProgram()
  {
    return program;
  }

  public int getFee()
  {
    return fee;
  }

  public int getDeposit()
  {
    return deposit;
  }
}
