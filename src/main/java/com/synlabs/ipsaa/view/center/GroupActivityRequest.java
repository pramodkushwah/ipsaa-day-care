package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.GroupActivity;
import com.synlabs.ipsaa.view.common.Request;

import java.text.ParseException;
import java.util.Date;

public class GroupActivityRequest implements Request
{
  private Long id;
  private Long   centerId;
  private Long   groupId;
  private String   date;
  private String activity;

  public Long getCenterId()
  {
    return centerId;
  }

  public GroupActivity toEntity(GroupActivity activity) throws ParseException
  {
    if (activity == null)
    {
      activity = new GroupActivity();
    }

    activity.setDate(parseDate(this.date));
    activity.setActivity(this.activity);
    return activity;
  }

  public Long getId()
  {
    return id;
  }

  public Long getUnmaskedId(){
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Long getUnmaskedCenterId(){
    return unmask(centerId);
  }

  public Long getGroupId()
  {
    return groupId;
  }

  public Long getUnmaskedGroupId(){
    return unmask(groupId);
  }

  public void setGroupId(Long groupId)
  {
    this.groupId = groupId;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate(String date)
  {
    this.date = date;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity(String activity)
  {
    this.activity = activity;
  }
}
