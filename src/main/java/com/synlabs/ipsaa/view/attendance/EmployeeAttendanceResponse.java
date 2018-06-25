package com.synlabs.ipsaa.view.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.LeaveStatus;
import com.synlabs.ipsaa.enums.LeaveType;
import com.synlabs.ipsaa.view.common.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeAttendanceResponse implements Response
{

  private Long attendanceId;

  private Long id;

  private String center;

  private String eid;

  private String fullName;

  private String mobile;

  private String expectedIn;

  private String expectedOut;

  private String actualIn;

  private String actualOut;

  private String status;

  private String img;
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date   date;

  private boolean calendarHoliday = false;

  private boolean onLeave = false;

  private String holidayName;

  private LeaveType leaveType;

  private Boolean halfLeave;

  private Long leaveId;

  private LeaveStatus leaveStatus;

  public String getHolidayName()
  {
    return holidayName;
  }

  public LeaveType getLeaveType()
  {
    return leaveType;
  }

  private final DateFormat formatter = new SimpleDateFormat("hh:mm a");

  public EmployeeAttendanceResponse(EmployeeAttendance attendance)
  {
    Employee employee = attendance.getEmployee();
    this.attendanceId = mask(attendance.getId());
    this.id = mask(employee.getId());
    this.eid = employee.getEid();
    this.center = employee.getCostCenter().getName();
    this.fullName = employee.getName();
    this.mobile = employee.getMobile();
    this.expectedIn = employee.getExpectedIn() == null ? null : formatter.format(employee.getExpectedIn());
    this.expectedOut = employee.getExpectedOut() == null ? null : formatter.format(employee.getExpectedOut());
    this.img = employee.getProfile().getImagePath();

    this.actualIn = attendance.getCheckin() == null ? null : formatter.format(attendance.getCheckin());
    this.actualOut = attendance.getCheckout() == null ? null : formatter.format(attendance.getCheckout());
    this.status = attendance.getStatus().name();

    this.calendarHoliday = attendance.isCalendarHoliday();

    this.onLeave = attendance.isOnLeave();
    this.date = attendance.getAttendanceDate();
    this.holidayName = attendance.getHolidayName();
    this.leaveType = attendance.getLeaveType();
    this.halfLeave = attendance.getHalfLeave();
    this.leaveStatus  =attendance.getLeaveStatus();
    this.leaveId = attendance.getLeaveId();
  }

  public Long getLeaveId()
  {
    return leaveId;
  }

  public LeaveStatus getLeaveStatus()
  {
    return leaveStatus;
  }

  public Boolean getHalfLeave()
  {
    return halfLeave;
  }

  public Date getDate()
  {
    return date;
  }

  public String getImg()
  {
    return img;
  }

  public String getActualIn()
  {
    return actualIn;
  }

  public String getActualOut()
  {
    return actualOut;
  }

  public Long getAttendanceId()
  {
    return attendanceId;
  }

  public String getStatus()
  {
    return status;
  }

  public Long getId()
  {
    return id;
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getCenter()
  {
    return center;
  }

  public String getExpectedIn()
  {
    return expectedIn;
  }

  public String getExpectedOut()
  {
    return expectedOut;
  }

  public String getEid()
  {
    return eid;
  }

  public String getMobile()
  {
    return mobile;
  }

  public boolean isCalendarHoliday()
  {
    return calendarHoliday;
  }

  public boolean isOnLeave()
  {
    return onLeave;
  }
}
