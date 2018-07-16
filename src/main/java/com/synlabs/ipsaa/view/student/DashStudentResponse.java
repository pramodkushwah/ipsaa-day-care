package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;
import java.util.List;

public class DashStudentResponse implements Response
{
  private boolean present;
  private String  name;
  private String  program;
  private String  group;
  private Date    checkout;
  private Date    checkin;
  private Date expectedOut;
  private Date expectedIn;

private boolean corporate;

  public DashStudentResponse(Student student)
  {
    name = student.getName();
    program = student.getProgramName();
    group = student.getGroupName();
    corporate = student.isCorporate();
    expectedIn  = student.getExpectedIn();
    expectedOut = student.getExpectedOut();
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
  
  public Date getExpectedOut() {
	return expectedOut;
	}
	
	public void setExpectedOut(Date expectedOut) {
		this.expectedOut = expectedOut;
	}
	
	public Date getExpectedIn() {
		return expectedIn;
	}
	
	public void setExpectedIn(Date expectedIn) {
		this.expectedIn = expectedIn;
	}
}
