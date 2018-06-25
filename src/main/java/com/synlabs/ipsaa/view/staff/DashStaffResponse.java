package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

/**
 * Created by itrs on 5/13/2017.
 */
public class DashStaffResponse implements Response
{
  private String     name;
  private String     designation;
  private String     mobile;
  private String     email;
  private BigDecimal basicSalary;

  public DashStaffResponse(EmployeeSalary employeeSalary){
    name=employeeSalary.getEmployee().getName();
    designation= employeeSalary.getEmployee().getDesignation();
    mobile=employeeSalary.getEmployee().getMobile();
    email=employeeSalary.getEmployee().getEmail();
    basicSalary =employeeSalary.getBasic();
  }

  public DashStaffResponse(Employee emp)
  {
    name=emp.getName();
    designation= emp.getDesignation();
    mobile=emp.getMobile();
    email=emp.getEmail();
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
}
