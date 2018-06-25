package com.synlabs.ipsaa.view.food;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.food.FoodMenu;
import com.synlabs.ipsaa.view.center.CenterSummaryResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

/**
 * Created by ttn on 2/7/17.
 */
public class FoodMenuResponse implements Response
{
  private Long id;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date date;

  private String breakfast;

  private String lunch;

  private String dinner;

  private CenterSummaryResponse center;

  public FoodMenuResponse(FoodMenu foodMenu)
  {
    this.id = mask(foodMenu.getId());
    this.date = foodMenu.getDate();
    this.breakfast = foodMenu.getBreakfast();
    this.lunch = foodMenu.getLunch();
    this.dinner = foodMenu.getDinner();
    this.center = new CenterSummaryResponse(foodMenu.getCenter());
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = mask(id);
  }

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

  public CenterSummaryResponse getCenter()
  {
    return center;
  }

  public void setCenter(CenterSummaryResponse center)
  {
    this.center = center;
  }
}
