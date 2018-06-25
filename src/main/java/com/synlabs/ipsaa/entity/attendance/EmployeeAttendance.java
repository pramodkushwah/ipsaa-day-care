package com.synlabs.ipsaa.entity.attendance;

import com.synlabs.ipsaa.entity.staff.Employee;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class EmployeeAttendance extends BaseAttendance
{
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Employee employee;

  @Transient
  private boolean calendarHoliday = false;

  @Transient
  private boolean onLeave = false;

  @Transient
  private Long leaveId;

  public Long getLeaveId()
  {
    return leaveId;
  }

  public void setLeaveId(Long leaveId)
  {
    this.leaveId = leaveId;
  }

  public Employee getEmployee()
  {
    return employee;
  }

  public void setEmployee(Employee employee)
  {
    this.employee = employee;
  }

  public boolean isCalendarHoliday()
  {
    return calendarHoliday;
  }

  public void setCalendarHoliday(boolean calendarHoliday)
  {
    this.calendarHoliday = calendarHoliday;
  }

  public boolean isOnLeave()
  {
    return onLeave;
  }

  public void setOnLeave(boolean onLeave)
  {
    this.onLeave = onLeave;
  }
}
