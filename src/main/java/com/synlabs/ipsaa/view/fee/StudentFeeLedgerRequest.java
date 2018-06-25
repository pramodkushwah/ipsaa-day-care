package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.common.Request;

public class StudentFeeLedgerRequest implements Request
{
  private Long studentId;

  private Long id;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public StudentFeeLedgerRequest(Long studentId)
  {
    this.studentId = studentId;
  }

  public Long getStudentId() {
    return unmask(studentId);
  }
}
