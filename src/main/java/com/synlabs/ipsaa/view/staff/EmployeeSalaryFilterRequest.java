package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.view.common.PageRequest;

/**
 * Created by ttn on 22/5/17.
 */
public class EmployeeSalaryFilterRequest extends PageRequest
{
  private String centerCode;

  private String employerCode;

  public EmployeeSalaryFilterRequest()
  {

  }

  public EmployeeSalaryFilterRequest(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public String getEmployerCode()
  {
    return employerCode;
  }

  public void setEmployerCode(String employerCode)
  {
    this.employerCode = employerCode;
  }
}
