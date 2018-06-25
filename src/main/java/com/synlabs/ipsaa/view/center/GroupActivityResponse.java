package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.GroupActivity;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

public class GroupActivityResponse implements Response
{
  private Long                  id;
  private CenterSummaryResponse center;
  private ProgramGroupResponse  group;
  private String                activity;
  private Date                  date;

  public GroupActivityResponse(GroupActivity activity)
  {
    this.id = mask(activity.getId());
    this.center = new CenterSummaryResponse(activity.getCenter());
    this.group = new ProgramGroupResponse(activity.getGroup());
    this.activity = activity.getActivity();
    this.date = activity.getDate();
  }

  public Long getUnmaskedId()
  {
    return id;
  }

  public Long getId()
  {
    return id;
  }

  public CenterSummaryResponse getCenter()
  {
    return center;
  }

  public ProgramGroupResponse getGroup()
  {
    return group;
  }

  public String getActivity()
  {
    return activity;
  }

  public Date getDate()
  {
    return date;
  }
}
