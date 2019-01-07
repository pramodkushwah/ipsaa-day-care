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
  private Integer baseFee;    // due to angular 2
  private Integer deposit;
  private Integer securityDeposit;  // due to angular 2
  private Integer annualFee;
  private Integer admissionFee;
  private Integer admissionCharges;   // due to angular 2


  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;
  private Long id;

  public CenterProgramFeeResponse(CenterProgramFee centerProgramFee)
  {
    if (centerProgramFee == null)
    {
      return;
    }
    id= mask(centerProgramFee.getId());
    fee = centerProgramFee.getFee();
    baseFee= centerProgramFee.getFee();
    admissionFee =centerProgramFee.getAddmissionFee()==null?0:centerProgramFee.getAddmissionFee().intValue();
    admissionCharges =centerProgramFee.getAddmissionFee()==null?0:centerProgramFee.getAddmissionFee().intValue();
    deposit = centerProgramFee.getDeposit();
    securityDeposit = centerProgramFee.getDeposit();
    annualFee = centerProgramFee.getAnnualFee();
    cgst = centerProgramFee.getCgst();
    sgst = centerProgramFee.getSgst();
    igst = centerProgramFee.getIgst();
    program = centerProgramFee.getProgram() != null ? new ProgramResponse(centerProgramFee.getProgram()) : null;
    center = centerProgramFee.getCenter() != null ? new CenterResponse(centerProgramFee.getCenter()) : null;
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

  public Long getId() { return id; }

  public void setId(Long id) { this.id = id; }

  public Integer getSecurityDeposit() { return securityDeposit; }

  public void setSecurityDeposit(Integer securityDeposit) {
    this.deposit = securityDeposit;
    this.securityDeposit = securityDeposit; }

  public Integer getBaseFee() { return baseFee; }

  public void setBaseFee(Integer baseFee) { this.baseFee = baseFee; }

  public Integer getAdmissionCharges() { return admissionCharges; }

  public void setAdmissionCharges(Integer admissionCharges) { this.admissionCharges = admissionCharges; }
}
