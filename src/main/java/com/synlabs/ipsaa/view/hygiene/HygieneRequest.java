package com.synlabs.ipsaa.view.hygiene;

import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by itrs on 7/7/2017.
 */
public class HygieneRequest implements Request
{
  private Long id;
  private String name;
  private Long CenterId;

  public Long getId()
  {
    return unmask(id);
  }

  public Long getMaskedId(){
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Long getCenterId()
  {
    return unmask(CenterId);
  }

  public Long getMaskedCenterId(){
    return getCenterId();
  }

  public void setCenterId(Long centerId)
  {
    CenterId = centerId;
  }
}
