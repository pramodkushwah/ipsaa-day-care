package com.synlabs.ipsaa.entity.staff;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.LeaveStatus;
import com.synlabs.ipsaa.enums.LeaveType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EmployeeLeave extends BaseEntity
{
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Employee employee;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date date;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private LeaveStatus leaveStatus;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private LeaveType leaveType;

  @Column(length = 200)
  private String reason;

  private Boolean halfLeave;

  public Boolean getHalfLeave()
  {
    return halfLeave;
  }

  public void setHalfLeave(Boolean halfLeave)
  {
    this.halfLeave = halfLeave;
  }

  public Employee getEmployee()
  {
    return employee;
  }

  public void setEmployee(Employee employee)
  {
    this.employee = employee;
  }

  public LeaveStatus getLeaveStatus()
  {
    return leaveStatus;
  }

  public void setLeaveStatus(LeaveStatus leaveStatus)
  {
    this.leaveStatus = leaveStatus;
  }

  public LeaveType getLeaveType()
  {
    return leaveType;
  }

  public void setLeaveType(LeaveType leaveType)
  {
    this.leaveType = leaveType;
  }

  public String getReason()
  {
    return reason;
  }

  public void setReason(String reason)
  {
    this.reason = reason;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }
}
