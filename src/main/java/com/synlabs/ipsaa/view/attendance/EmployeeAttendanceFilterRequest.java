package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.view.common.Request;

import javax.validation.constraints.NotNull;

public class EmployeeAttendanceFilterRequest implements Request
{
  private Long empId;

  @NotNull(message = "Year is required.")
  private Integer year;

  @NotNull(message = "Month is required.")
  private Integer month;

  public Long getEmpId()
  {
    return unmask(empId);
  }

  public void setEmpId(Long empId)
  {
    this.empId = empId;
  }

  public Integer getYear()
  {
    return year;
  }

  public void setYear(Integer year)
  {
    this.year = year;
  }

  public Integer getMonth()
  {
    return month;
  }

  public void setMonth(Integer month)
  {
    this.month = month;
  }
}
