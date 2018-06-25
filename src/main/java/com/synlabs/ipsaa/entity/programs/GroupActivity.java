package com.synlabs.ipsaa.entity.programs;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class GroupActivity extends BaseEntity
{
  @ManyToOne
  private Center center;

  @ManyToOne
  private ProgramGroup group;

  @Column(length = 500)
  private String activity;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date date;

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public ProgramGroup getGroup()
  {
    return group;
  }

  public void setGroup(ProgramGroup group)
  {
    this.group = group;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity(String activity)
  {
    this.activity = activity;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }
}
