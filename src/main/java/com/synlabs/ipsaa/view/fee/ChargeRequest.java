package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by itrs on 4/19/2017.
 */
public class ChargeRequest implements Request
{
  private Long id;
  private String name;


  public Long getId()
  {
    return unmask(id);
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

}
