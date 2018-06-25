package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import com.synlabs.ipsaa.view.common.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sushil on 06-07-2017.
 */
public class ParentSharingSheetResponse implements Response
{

  private Long id;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date sharingDate;

  private Long studentId;

  private String studentName;

  private String signedBy;

  private String bedTime;

  private String wakeupTime;

  private String sleepCycle;

  private String sleepReason;

  private String wakeupMood;

  private String arrivalMood;

  private boolean bowelMovement;

  private boolean bathed;

  private boolean sponged;

  private String noticedIssue;

  private String sharing;

  private List<SharingSheetEntryResponse> entries;

  private final DateFormat formatter = new SimpleDateFormat("hh:mm a");

  public ParentSharingSheetResponse()
  {
  }

  public ParentSharingSheetResponse(ParentSharingSheet ss)
  {
    this.id = mask(ss.getId());
    this.sharingDate = ss.getSharingDate();
    this.studentId = mask(ss.getStudent().getId());
    this.studentName = ss.getStudent().getName();
    this.signedBy = ss.getCreatedBy().getFirstname();
    this.bedTime = ss.getBedTime() == null ? null : formatter.format(ss.getBedTime());
    this.wakeupTime = ss.getWakeupTime() == null ? null : formatter.format(ss.getWakeupTime());
    this.sleepCycle = ss.getSleepCycle();
    this.sleepReason = ss.getSleepReason();
    this.wakeupMood = ss.getWakeupMood();
    this.arrivalMood = ss.getArrivalMood();
    this.bowelMovement = ss.isBowelMovement();
    this.bathed = ss.isBathed();
    this.sponged = ss.isSponged();
    this.noticedIssue = ss.getNoticedIssue();
    this.sharing = ss.getSharing();

    if (ss.getEntries() != null && !ss.getEntries().isEmpty())
    {
      entries = ss.getEntries().stream().map(SharingSheetEntryResponse::new).collect(Collectors.toList());
    }
  }

  public Long getId()
  {
    return id;
  }

  public Date getSharingDate()
  {
    return sharingDate;
  }

  public Long getStudentId()
  {
    return studentId;
  }

  public String getStudentName()
  {
    return studentName;
  }

  public String getSignedBy()
  {
    return signedBy;
  }

  public String getBedTime()
  {
    return bedTime;
  }

  public String getWakeupTime()
  {
    return wakeupTime;
  }

  public String getSleepCycle()
  {
    return sleepCycle;
  }

  public String getSleepReason()
  {
    return sleepReason;
  }

  public String getWakeupMood()
  {
    return wakeupMood;
  }

  public String getArrivalMood()
  {
    return arrivalMood;
  }

  public boolean isBowelMovement()
  {
    return bowelMovement;
  }

  public boolean isBathed()
  {
    return bathed;
  }

  public boolean isSponged()
  {
    return sponged;
  }

  public String getNoticedIssue()
  {
    return noticedIssue;
  }

  public String getSharing()
  {
    return sharing;
  }

  public List<SharingSheetEntryResponse> getEntries()
  {
    return entries;
  }

  public DateFormat getFormatter()
  {
    return formatter;
  }
}
