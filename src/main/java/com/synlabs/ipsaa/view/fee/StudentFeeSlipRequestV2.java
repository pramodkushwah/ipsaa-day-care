package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

public class StudentFeeSlipRequestV2 implements Request
{
  private Long id;
  private Long   studentId;
  private BigDecimal extraCharge;
  private BigDecimal latePaymentCharge;
  private String centerCode;
  private String period;
  private int    month;
  private int    quarter;
  private int    year;
  private String reportType;
  private Boolean confirm;

  public Long getId() {
    return unmask(id);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStudentId() {
    return unmask(studentId);
  }
  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public BigDecimal getExtraCharge() {
    return extraCharge;
  }

  public void setExtraCharge(BigDecimal extraCharge) {
    this.extraCharge = extraCharge;
  }

  public BigDecimal getLatePaymentCharge() {
    return latePaymentCharge;
  }

  public void setLatePaymentCharge(BigDecimal latePaymentCharge) {
    this.latePaymentCharge = latePaymentCharge;
  }

  public String getReportType()
  {
    return reportType;
  }

  public void setReportType(String reportType)
  {
    this.reportType = reportType;
  }


  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public String getPeriod()
  {
    return period;
  }

  public void setPeriod(String period)
  {
    this.period = period;
  }

  public int getMonth()
  {
    return month;
  }

  public void setMonth(int month)
  {
    this.month = month;
  }

  public int getQuarter()
  {
    return quarter;
  }

  public void setQuarter(int quarter)
  {
    this.quarter = quarter;
  }

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

  public Boolean getConfirm() {
    return confirm;
  }
  public void setConfirm(Boolean confirm) {
    this.confirm = confirm;
  }
}
