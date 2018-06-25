package com.synlabs.ipsaa.entity.sharing;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.SharingSheetEntryType;
import com.synlabs.ipsaa.enums.SharingSheetFeedQty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ParentSharingSheetEntry extends BaseEntity
{

  @Column(length = 200)
  private String food;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private ParentSharingSheet sharingSheet;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SharingSheetEntryType entryType;

  @Enumerated(EnumType.STRING)
  private SharingSheetFeedQty feedQty;

  @Temporal(TemporalType.TIME)
  private Date entryAt;

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

  public ParentSharingSheet getSharingSheet()
  {
    return sharingSheet;
  }

  public void setSharingSheet(ParentSharingSheet sharingSheet)
  {
    this.sharingSheet = sharingSheet;
  }
}
