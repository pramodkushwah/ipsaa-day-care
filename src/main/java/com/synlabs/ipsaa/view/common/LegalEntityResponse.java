package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.LegalEntity;

/**
 * Created by sushil on 02-04-2018.
 */
public class LegalEntityResponse implements Response
{
  private Long   id;
  private String code;

  private String name;

  public LegalEntityResponse(LegalEntity legalEntity)
  {
    this.id = mask(legalEntity.getId());
    this.code = legalEntity.getCode();
    this.name = legalEntity.getName();
  }

  public Long getId()
  {
    return id;
  }

  public String getCode()
  {
    return code;
  }

  public String getName()
  {
    return name;
  }
}
