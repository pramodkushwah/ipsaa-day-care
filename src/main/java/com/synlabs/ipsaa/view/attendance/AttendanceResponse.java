package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.view.common.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AttendanceResponse implements Response
{

  private Long studentId;
  private Long employeeId;
  private String center;
  private String attendanceDate;
  private String checkin;
  private String checkout;
  private String status;

  private final DateFormat formatter = new SimpleDateFormat("hh:mm a");

  public AttendanceResponse(StudentAttendance attendance)
  {
    this.studentId = mask(attendance.getStudent().getId());
    this.center = attendance.getCenter().getName();
    this.attendanceDate = attendance.getAttendanceDate().toString();
    this.checkin = formatter.format(attendance.getCheckin());
    this.checkout = attendance.getCheckout() == null ? null : formatter.format(attendance.getCheckout());
    this.status = attendance.getStatus().name();
  }


  public AttendanceResponse(EmployeeAttendance attendance)
  {
    this.studentId = mask(attendance.getEmployee().getId());
    this.center = attendance.getCenter().getName();
    this.attendanceDate = attendance.getAttendanceDate().toString();
    this.checkin = attendance.getCheckin().toString();
    this.checkout = attendance.getCheckout().toString();
    this.status = attendance.getStatus().name();
  }

  public Long getStudentId()
  {
    return studentId;
  }

  public Long getEmployeeId()
  {
    return employeeId;
  }

  public String getCenter()
  {
    return center;
  }

  public String getAttendanceDate()
  {
    return attendanceDate;
  }

  public String getCheckin()
  {
    return checkin;
  }

  public String getCheckout()
  {
    return checkout;
  }

  public String getStatus()
  {
    return status;
  }


}
