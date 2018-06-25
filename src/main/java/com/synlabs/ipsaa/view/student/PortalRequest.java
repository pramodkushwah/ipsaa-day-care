package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by itrs on 8/22/2017.
 */
public class PortalRequest implements Request
{
  private Long studentId;

  public PortalRequest(){}

  public PortalRequest(Long studentId)
  {
    this.studentId = studentId;
  }

  public Long getMaskedStudentId()
  {
    return studentId;
  }

  public Long getStudentId()
  {
    return unmask(studentId);
  }

  public void setStudentId(Long studentId)
  {
    this.studentId = studentId;
  }
}
