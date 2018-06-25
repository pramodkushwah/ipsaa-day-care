package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class AttendancePullRequest implements Request
{
  private Date to;
  private Date from;
  private Boolean dryRun;

  public Boolean getDryRun()
  {
    return dryRun;
  }

  public void setDryRun(Boolean dryRun)
  {
    this.dryRun = dryRun;
  }

  public Date getTo()
  {
    return to;
  }

  public void setTo(Date to)
  {
    this.to = to;
  }

  public Date getFrom()
  {
    return from;
  }

  public void setFrom(Date from)
  {
    this.from = from;
  }
}
