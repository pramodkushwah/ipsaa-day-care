package com.synlabs.ipsaa.view.support;

import com.synlabs.ipsaa.entity.support.SupportQueryEntry;
import com.synlabs.ipsaa.view.common.Response;

public class SupportReplyResponse implements Response
{
  private String description;
  private String created;
  private String repliedBy;

  public SupportReplyResponse() {}

  public SupportReplyResponse(SupportQueryEntry entry)
  {
    this.description = entry.getDescription();
    this.created = entry.getCreatedDate().toString("yyyy-MM-dd HH:mm");
    this.repliedBy = entry.getCreatedBy().getName();
  }

  public String getCreated()
  {
    return created;
  }

  public String getDescription()
  {
    return description;
  }

  public String getRepliedBy()
  {
    return repliedBy;
  }
}
