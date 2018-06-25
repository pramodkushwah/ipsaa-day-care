package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.view.common.Request;

public class EmployeeAttendanceRequest implements Request
{
  private Long employeeId;

  public Long getEmployeeId()
  {
    return unmask(employeeId);
  }

  public void setEmployeeId(Long employeeId)
  {
    this.employeeId = employeeId;
  }
}
