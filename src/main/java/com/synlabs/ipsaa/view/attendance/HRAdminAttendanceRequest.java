package com.synlabs.ipsaa.view.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class HRAdminAttendanceRequest implements Request
{
  private Long id;
  private Long employeeId;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date clockin;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date clockout;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date date;

  public EmployeeAttendance toEntity(EmployeeAttendance attendance)
  {
    attendance = attendance == null ? new EmployeeAttendance(): attendance;
    attendance.setAttendanceDate(date);
    attendance.setCheckout(clockout);
    attendance.setCheckin(clockin);
    return attendance;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getEmployeeId()
  {
    return unmask(employeeId);
  }

  public void setEmployeeId(Long employeeId)
  {
    this.employeeId = employeeId;
  }

  public Date getClockin()
  {
    return clockin;
  }

  public void setClockin(Date clockin)
  {
    this.clockin = clockin;
  }

  public Date getClockout()
  {
    return clockout;
  }

  public void setClockout(Date clockout)
  {
    this.clockout = clockout;
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
