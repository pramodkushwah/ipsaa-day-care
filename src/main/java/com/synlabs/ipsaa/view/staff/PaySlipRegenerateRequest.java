package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.view.common.Request;

public class PaySlipRegenerateRequest implements Request
{
  private Long id;
  private Long legalEntityId;

  private Integer month;
  private Integer year;

  public void validate()
  {
    if (month == null || month < 1 || month > 13)
    {
      throw new ValidationException("Invalid Month");
    }

    if (year == null)
    {
      throw new ValidationException("Invalid Year");
    }

    if (id == null && legalEntityId == null)
    {
      throw new ValidationException("Legal entity or slip id is required.");
    }
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Integer getMonth()
  {
    return month;
  }

  public void setMonth(Integer month)
  {
    this.month = month;
  }

  public Integer getYear()
  {
    return year;
  }

  public void setYear(Integer year)
  {
    this.year = year;
  }

  public Long getLegalEntityId()
  {
    return unmask(legalEntityId);
  }

  public void setLegalEntityId(Long legalEntityId)
  {
    this.legalEntityId = legalEntityId;
  }
}
