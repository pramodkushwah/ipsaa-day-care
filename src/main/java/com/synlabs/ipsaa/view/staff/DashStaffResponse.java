package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by itrs on 5/13/2017.
 */
public class DashStaffResponse implements Response
{
  private boolean present;
  private String     name;
  private String     designation;
  private String     mobile;
  private String     email;
  private BigDecimal basicSalary;
  private Date checkin;
  private Date checkout;
  private Date expectedIn;
  private Date expectedOut;

  public DashStaffResponse(EmployeeSalary employeeSalary){
    name=employeeSalary.getEmployee().getName();
    designation= employeeSalary.getEmployee().getDesignation();
    mobile=employeeSalary.getEmployee().getMobile();
    email=employeeSalary.getEmployee().getEmail();
    basicSalary =employeeSalary.getBasic();
  }
  
  public DashStaffResponse(EmployeeSalary employeeSalary,EmployeeAttendance employeeAttandence,Employee emp){
    name=employeeSalary.getEmployee().getName();
    designation= employeeSalary.getEmployee().getDesignation();
    mobile=employeeSalary.getEmployee().getMobile();
    email=employeeSalary.getEmployee().getEmail();
    basicSalary =employeeSalary.getBasic();
    present = true;
    checkin = employeeAttandence.getCheckin();
    checkout = employeeAttandence.getCheckout();
    expectedIn = emp.getExpectedIn();
    expectedOut = emp.getExpectedOut();
  }
  
  

  public DashStaffResponse(Employee emp)
  {
    name=emp.getName();
    designation= emp.getDesignation();
    mobile=emp.getMobile();
    email=emp.getEmail();
    expectedIn  = emp.getExpectedIn();
    expectedOut = emp.getExpectedOut();
  }

  public String getName()
  {
    return name;
  }

  public String getDesignation()
  {
    return designation;
  }

  public String getMobile()
  {
    return mobile;
  }

  public String getEmail()
  {
    return email;
  }

  public BigDecimal getBasicSalary()
  {
    return basicSalary;
  }


public Date getCheckin() {
	return checkin;
}

public void setCheckin(Date checkin) {
	this.checkin = checkin;
}

public Date getCheckout() {
	return checkout;
}

public void setCheckout(Date checkout) {
	this.checkout = checkout;
}

public Date getExpectedIn() {
	return expectedIn;
}

public void setExpectedIn(Date expectedIn) {
	this.expectedIn = expectedIn;
}

public Date getExpectedOut() {
	return expectedOut;
}

public void setExpectedOut(Date expectedOut) {
	this.expectedOut = expectedOut;
}

public boolean isPresent() {
	return present;
}

public void setPresent(boolean present) {
	this.present = present;
}
  
  
}
