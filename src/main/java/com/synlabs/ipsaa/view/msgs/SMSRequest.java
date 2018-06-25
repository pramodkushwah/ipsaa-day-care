package com.synlabs.ipsaa.view.msgs;

import com.synlabs.ipsaa.view.common.Request;

import java.util.Arrays;

public class SMSRequest implements Request
{

  private Long[] ids;
  private String smscontent;

  public String getSmscontent()
  {
    return smscontent;
  }

  public void setSmscontent(String smscontent)
  {
    this.smscontent = smscontent;
  }

  public Long[] getIds()
  {
    return Arrays.stream(ids).map(this::unmask).toArray(Long[]::new);
  }

  public void setIds(Long[] ids)
  {
    this.ids = ids;
  }
}
