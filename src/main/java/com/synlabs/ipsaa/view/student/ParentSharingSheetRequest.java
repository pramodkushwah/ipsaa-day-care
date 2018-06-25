package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class ParentSharingSheetRequest extends PortalRequest
{
  private Long id;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date bedTime;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date wakeupTime;

  private String sleepCycle;

  private String sleepReason;

  private String wakeupMood;

  private String arrivalMood;

  private boolean bowelMovement;

  private boolean bathed;

  private boolean sponged;

  private String noticedIssue;

  private String sharing;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Date getBedTime()
  {
    return bedTime;
  }

  public void setBedTime(Date bedTime)
  {
    this.bedTime = bedTime;
  }

  public Date getWakeupTime()
  {
    return wakeupTime;
  }

  public void setWakeupTime(Date wakeupTime)
  {
    this.wakeupTime = wakeupTime;
  }

  public String getSleepCycle()
  {
    return sleepCycle;
  }

  public void setSleepCycle(String sleepCycle)
  {
    this.sleepCycle = sleepCycle;
  }

  public String getSleepReason()
  {
    return sleepReason;
  }

  public void setSleepReason(String sleepReason)
  {
    this.sleepReason = sleepReason;
  }

  public String getWakeupMood()
  {
    return wakeupMood;
  }

  public void setWakeupMood(String wakeupMood)
  {
    this.wakeupMood = wakeupMood;
  }

  public String getArrivalMood()
  {
    return arrivalMood;
  }

  public void setArrivalMood(String arrivalMood)
  {
    this.arrivalMood = arrivalMood;
  }

  public boolean isBowelMovement()
  {
    return bowelMovement;
  }

  public void setBowelMovement(boolean bowelMovement)
  {
    this.bowelMovement = bowelMovement;
  }

  public boolean isBathed()
  {
    return bathed;
  }

  public void setBathed(boolean bathed)
  {
    this.bathed = bathed;
  }

  public boolean isSponged()
  {
    return sponged;
  }

  public void setSponged(boolean sponged)
  {
    this.sponged = sponged;
  }

  public String getNoticedIssue()
  {
    return noticedIssue;
  }

  public void setNoticedIssue(String noticedIssue)
  {
    this.noticedIssue = noticedIssue;
  }

  public String getSharing()
  {
    return sharing;
  }

  public void setSharing(String sharing)
  {
    this.sharing = sharing;
  }

  public ParentSharingSheet toEntity(ParentSharingSheet pss)
  {
    pss.setBedTime(this.bedTime);
    pss.setWakeupTime(this.wakeupTime);
    pss.setSleepCycle(this.sleepCycle);
    pss.setSleepReason(this.sleepReason);
    pss.setWakeupMood(this.wakeupMood);
    pss.setArrivalMood(this.arrivalMood);
    pss.setBowelMovement(this.bowelMovement);
    pss.setBathed(this.bathed);
    pss.setSponged(this.sponged);
    pss.setNoticedIssue(this.noticedIssue);
    pss.setSharing(this.sharing);
    return pss;
  }
}
