package com.synlabs.ipsaa.view.food;

import com.synlabs.ipsaa.entity.food.FoodMenu;
import com.synlabs.ipsaa.view.common.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ttn on 2/7/17.
 */
public class FoodMenuRequest implements Request
{
  private Long id;

  private String date;

  private String breakfast;

  private String lunch;

  private String dinner;

  private String centerCode;

  public FoodMenu toEntity(FoodMenu foodMenu) throws ParseException
  {
    if(foodMenu==null){
      foodMenu=new FoodMenu();
    }
    foodMenu.setBreakfast(breakfast);
    foodMenu.setLunch(lunch);
    foodMenu.setDinner(dinner);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    foodMenu.setDate(sdf.parse(date));
    return foodMenu;
  }

  public Long getId()
  {
    return unmask(id);
  }
  public Long getMaskedId(){
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate(String date)
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

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  @Override
  public String toString()
  {
    return "FoodMenuRequest{" +
        "id=" + id +
        ", date=" + date +
        ", breakfast='" + breakfast + '\'' +
        ", lunch='" + lunch + '\'' +
        ", dinner='" + dinner + '\'' +
        ", centerCode='" + centerCode + '\'' +
        '}';
  }
}
