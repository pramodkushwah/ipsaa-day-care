package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheetEntry;
import com.synlabs.ipsaa.entity.sharing.SharingSheetEntry;
import com.synlabs.ipsaa.enums.SharingSheetEntryType;
import com.synlabs.ipsaa.enums.SharingSheetFeedQty;
import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class SharingSheetEntryRequest extends SharingSheetRequest
{

  private Long   id;

  private Long entryId;

  private String food;

  private String entryType;

  private String feedQty;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date entryAt;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date entryFrom;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date entryTo;

  private String notes;
  private String needs;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getEntryType()
  {
    return entryType;
  }

  public void setEntryType(String entryType)
  {
    this.entryType = entryType;
  }

  public String getFeedQty()
  {
    return feedQty;
  }

  public void setFeedQty(String feedQty)
  {
    this.feedQty = feedQty;
  }

  public Date getEntryAt()
  {
    return entryAt;
  }

  public void setEntryAt(Date entryAt)
  {
    this.entryAt = entryAt;
  }

  public Date getEntryFrom()
  {
    return entryFrom;
  }

  public void setEntryFrom(Date entryFrom)
  {
    this.entryFrom = entryFrom;
  }

  public Date getEntryTo()
  {
    return entryTo;
  }

  public void setEntryTo(Date entryTo)
  {
    this.entryTo = entryTo;
  }

  public String getNotes()
  {
    return notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  public String getFood()
  {
    return food;
  }

  public void setFood(String food)
  {
    this.food = food;
  }

  public Long getEntryId()
  {
    return entryId;
  }

  public void setEntryId(Long entryId)
  {
    this.entryId = entryId;
  }

  public SharingSheetEntry toEntity()
  {
    SharingSheetEntry sse = new SharingSheetEntry();
    sse.setFood(this.food);
    sse.setEntryAt(this.getEntryAt());
    sse.setEntryFrom(this.getEntryFrom());
    sse.setEntryTo(this.getEntryTo());
    sse.setEntryType(SharingSheetEntryType.valueOf(this.getEntryType()));
    sse.setFeedQty(this.getFeedQty() == null ? null : SharingSheetFeedQty.valueOf(this.getFeedQty()));
    sse.setNotes(this.getNotes());
    return sse;
  }

  public String getNeeds()
  {
    return needs;
  }

  public void setNeeds(String needs)
  {
    this.needs = needs;
  }

  public ParentSharingSheetEntry toParentEntity()
  {
    ParentSharingSheetEntry sse = new ParentSharingSheetEntry();
    sse.setFood(this.food);
    sse.setEntryAt(this.getEntryAt());
    sse.setEntryType(SharingSheetEntryType.valueOf(this.getEntryType()));
    sse.setFeedQty(this.getFeedQty() == null ? null : SharingSheetFeedQty.valueOf(this.getFeedQty()));
    return sse;
  }
}
