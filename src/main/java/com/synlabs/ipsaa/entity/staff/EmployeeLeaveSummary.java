package com.synlabs.ipsaa.entity.staff;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.LeaveType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class EmployeeLeaveSummary extends BaseEntity
{
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Employee employee;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private LeaveType leaveType;

  private int month;

  private int year;

  @Column(precision = 16, scale = 2)
  private BigDecimal count;

  public BigDecimal getCount()
  {
    return count;
  }

  public void setCount(BigDecimal count)
  {
    this.count = count;
  }

  public Employee getEmployee()
  {
    return employee;
  }

  public void setEmployee(Employee employee)
  {
    this.employee = employee;
  }

  public LeaveType getLeaveType()
  {
    return leaveType;
  }

  public void setLeaveType(LeaveType leaveType)
  {
    this.leaveType = leaveType;
  }

  public int getMonth()
  {
    return month;
  }

  public void setMonth(int month)
  {
    this.month = month;
  }

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }
}
