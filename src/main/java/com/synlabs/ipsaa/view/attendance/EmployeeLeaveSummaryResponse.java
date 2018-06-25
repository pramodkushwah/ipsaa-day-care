package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.entity.staff.EmployeeLeaveSummary;

import java.math.BigDecimal;

/**
 * Created by sushil on 03-04-2018.
 */
public class EmployeeLeaveSummaryResponse
{
  private String eid;
  private String name;
  private String type;

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
}
