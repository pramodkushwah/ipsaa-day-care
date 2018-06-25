package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.view.common.Request;

public class StudentAttendanceRequest implements Request
{
  private Long studentId;

  public Long getStudentId()
  {
    return unmask(studentId);
  }

  public void setStudentId(Long studentId)
  {
    this.studentId = studentId;
  }
}
