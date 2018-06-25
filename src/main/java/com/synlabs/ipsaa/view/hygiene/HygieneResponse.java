package com.synlabs.ipsaa.view.hygiene;

import com.synlabs.ipsaa.entity.hygiene.Hygiene;
import com.synlabs.ipsaa.view.center.CenterSummaryResponse;
import com.synlabs.ipsaa.view.common.Response;

/**
 * Created by itrs on 7/7/2017.
 */
public class HygieneResponse implements Response
{
  private Long                  id;
  private String                name;
  private CenterSummaryResponse center;

  public HygieneResponse(Hygiene hygiene)
  {
    id = hygiene.getId();
    name = hygiene.getName();
    center = new CenterSummaryResponse(hygiene.getCenter());
  }

  public Long getId()
  {
    return mask(id);
  }

  public String getName()
  {
    return name;
  }

  public CenterSummaryResponse getCenter()
  {
    return center;
  }
}
