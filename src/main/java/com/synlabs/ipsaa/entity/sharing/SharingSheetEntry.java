package com.synlabs.ipsaa.entity.sharing;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.SharingSheetEntryType;
import com.synlabs.ipsaa.enums.SharingSheetFeedQty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SharingSheetEntry extends BaseEntity
{

  @Column(length = 200)
  private String food;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private SharingSheet sharingSheet;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SharingSheetEntryType entryType;

  @Column(nullable = true)
  @Enumerated(EnumType.STRING)
  private SharingSheetFeedQty feedQty;

  @Temporal(TemporalType.TIME)
  private Date entryAt;

  @Temporal(TemporalType.TIME)
  private Date entryFrom;

  @Temporal(TemporalType.TIME)
  private Date entryTo;

  @Column(length = 500)
  private String notes;

  public SharingSheet getSharingSheet()
  {
    return sharingSheet;
  }

  public void setSharingSheet(SharingSheet sharingSheet)
  {
    this.sharingSheet = sharingSheet;
  }

  public SharingSheetEntryType getEntryType()
  {
    return entryType;
  }

  public void setEntryType(SharingSheetEntryType entryType)
  {
    this.entryType = entryType;
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

  public SharingSheetFeedQty getFeedQty()
  {
    return feedQty;
  }

  public void setFeedQty(SharingSheetFeedQty feedQty)
  {
    this.feedQty = feedQty;
  }

  public String getFood()
  {
    return food;
  }

  public void setFood(String food)
  {
    this.food = food;
  }
}
