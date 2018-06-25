package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Holiday extends BaseEntity
{
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "holiday_center",
          joinColumns = { @JoinColumn(name = "HOLIDAY_ID", referencedColumnName = "ID") },
          inverseJoinColumns = { @JoinColumn(name = "CENTER_ID", referencedColumnName = "ID") })
  private List<Center> centers;

  @Column(nullable = false, length = 50)
  private String name;

  private boolean floating;

  private boolean optional;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date holidayDate;

  public List<Center> getCenters()
  {
    return centers;
  }

  public void setCenters(List<Center> centers)
  {
    this.centers = centers;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean isFloating()
  {
    return floating;
  }

  public void setFloating(boolean floating)
  {
    this.floating = floating;
  }

  public boolean isOptional()
  {
    return optional;
  }

  public void setOptional(boolean optional)
  {
    this.optional = optional;
  }

  public Date getHolidayDate()
  {
    return holidayDate;
  }

  public void setHolidayDate(Date holidayDate)
  {
    this.holidayDate = holidayDate;
  }
}

//TODO - floating or optional needed?
//TODO model leave and leave plan