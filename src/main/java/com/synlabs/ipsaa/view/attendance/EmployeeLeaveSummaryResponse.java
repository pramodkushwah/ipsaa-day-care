package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeLeave;
import com.synlabs.ipsaa.entity.staff.EmployeeLeaveSummary;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by sushil on 03-04-2018.
 */
public class EmployeeLeaveSummaryResponse
{
  private String eid;
  private String name;
  private String type;
  private String center;
  private String reportingManager;

  private int year;
  private int month;

  private BigDecimal count;

  public EmployeeLeaveSummaryResponse(EmployeeLeaveSummary summary)
  {
    this.eid = summary.getEmployee().getEid();
    this.name = summary.getEmployee().getName();
    this.year = summary.getYear();
    this.month = summary.getMonth();
    this.type = summary.getLeaveType().name();
    this.count = summary.getCount();
  }

   public BigDecimal getCount()
  {
    return count;
  }

  public String getEid()
  {
    return eid;
  }

  public String getName()
  {
    return name;
  }

  public int getYear()
  {
    return year;
  }

  public int getMonth()
  {
    return month;
  }

  public String getType()
  {
    return type;
  }

  public EmployeeLeaveSummaryResponse() {
  }

  public void setEid(String eid) {

    this.eid = eid;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCenter() {
    return center;
  }

  public void setCenter(String center) {
    this.center = center;
  }

  public String getReportingManager() {
    return reportingManager;
  }

  public void setReportingManager(String reportingManager) {
    this.reportingManager = reportingManager;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setCount(BigDecimal count) {
    this.count = count;
  }
}
