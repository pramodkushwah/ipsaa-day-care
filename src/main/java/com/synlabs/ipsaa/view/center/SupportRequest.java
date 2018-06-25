package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.City;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.student.PortalRequest;

/**
 * Created by sushil on 27-03-2017.
 */
public class SupportRequest extends PortalRequest
{
  private Long id;

  private String title;

  private String description;

  public SupportRequest() {}

  public SupportRequest(Long id)
  {
    this.id = unmask(id);
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = unmask(id);
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
}
