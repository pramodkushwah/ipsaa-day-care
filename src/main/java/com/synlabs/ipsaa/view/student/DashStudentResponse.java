package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

public class DashStudentResponse implements Response
{
  private boolean present;
  private String  name;
  private String  program;
  private String  group;
  private Date    checkout;
  private Date    checkin;
  private boolean corporate;

  public DashStudentResponse(Student student)
  {
    name = student.getName();
    program = student.getProgramName();
    group = student.getGroupName();
    corporate = student.isCorporate();
    present = false;
  }

  public DashStudentResponse(StudentAttendance attendance)
  {
    this(attendance.getStudent());
    present = true;
    checkin = attendance.getCheckin();
    checkout = attendance.getCheckout();
  }

  public boolean isCorporate()
  {
    return corporate;
  }

  public boolean getPresent()
  {
    return present;
  }

  public String getName()
  {
    return name;
  }

  public String getProgram()
  {
    return program;
  }

  public String getGroup()
  {
    return group;
  }

  public Date getCheckout()
  {
    return checkout;
  }

  public Date getCheckin()
  {
    return checkin;
  }
}
