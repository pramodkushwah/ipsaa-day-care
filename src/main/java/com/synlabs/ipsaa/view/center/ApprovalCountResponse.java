package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.view.common.Response;

public class ApprovalCountResponse implements Response
{
  private Long    id;
  private String  centerCode;
  private String  centerName;
  private Integer count;

  public ApprovalCountResponse(Center center)
  {
    this(center, 0);
  }

  public ApprovalCountResponse(Center center, Integer count)
  {
    this.id = center.getId();
    this.centerName = center.getName();
    this.centerCode = center.getCode();
    this.count = count;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public Integer getCount()
  {
    return count;
  }

  public Long getId()
  {
    return mask(id);
  }
}
