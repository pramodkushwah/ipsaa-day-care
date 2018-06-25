package com.synlabs.ipsaa.view.inquiry;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.view.common.Response;

public class FollowUpReportResponse implements Response
{
  private Long   centerId;
  private String centerCode;
  private String centerName;
  private int    openFollowUps;
  private int    todayFollowUps;
  private int    previousFollowUps;
  private int    monthInquiries;

  public FollowUpReportResponse(Center center)
  {
    centerId = center.getId();
    centerName = center.getName();
    centerCode = center.getCode();
  }

  public int getMonthInquiries()
  {
    return monthInquiries;
  }

  public void setMonthInquiries(int monthInquiries)
  {
    this.monthInquiries = monthInquiries;
  }

  public int getOpenFollowUps()
  {
    return openFollowUps;
  }

  public void setOpenFollowUps(int openFollowUps)
  {
    this.openFollowUps = openFollowUps;
  }

  public int getTodayFollowUps()
  {
    return todayFollowUps;
  }

  public void setTodayFollowUps(int todayFollowUps)
  {
    this.todayFollowUps = todayFollowUps;
  }

  public int getPreviousFollowUps()
  {
    return previousFollowUps;
  }

  public void setPreviousFollowUps(int previousFollowUps)
  {
    this.previousFollowUps = previousFollowUps;
  }

  public Long getCenterId()
  {
    return centerId;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getCenterName()
  {
    return centerName;
  }

}
