package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class GalleryFilterRequest implements Request
{
  private Long centerId;
  private Long studentId;
  private Date from;
  private Date to;

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Long getStudentId()
  {
    return unmask(studentId);
  }

  public void setStudentId(Long studentId)
  {
    this.studentId = studentId;
  }

  public Date getFrom()
  {
    return from;
  }

  public void setFrom(Date from)
  {
    this.from = from;
  }

  public Date getTo()
  {
    return to;
  }

  public void setTo(Date to)
  {
    this.to = to;
  }
}
