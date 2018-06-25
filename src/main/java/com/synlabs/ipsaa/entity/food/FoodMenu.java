package com.synlabs.ipsaa.entity.food;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ttn on 2/7/17.
 */

@Entity
public class FoodMenu extends BaseEntity
{
  @Temporal(TemporalType.DATE)
  private Date date;

  @Column(length = 200)
  private String breakfast;

  @Column(length = 200)
  private String lunch;

  @Column(length = 200)
  private String dinner;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Center center;

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public String getBreakfast()
  {
    return breakfast;
  }

  public void setBreakfast(String breakfast)
  {
    this.breakfast = breakfast;
  }

  public String getLunch()
  {
    return lunch;
  }

  public void setLunch(String lunch)
  {
    this.lunch = lunch;
  }

  public String getDinner()
  {
    return dinner;
  }

  public void setDinner(String dinner)
  {
    this.dinner = dinner;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }
}
