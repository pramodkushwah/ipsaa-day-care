package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.common.Request;

import java.util.ArrayList;
import java.util.List;

public class SlipEmailRequest implements Request
{
  private List<Long> slipIds = new ArrayList<>();
  private String body;
  private String subject;

  public List<Long> getSlipIds()
  {
    return slipIds;
  }

  public void setSlipIds(List<Long> slipIds)
  {
    this.slipIds = slipIds;
  }

  public String getBody()
  {
    return body;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject(String subject)
  {
    this.subject = subject;
  }
}
