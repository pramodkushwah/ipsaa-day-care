package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheetEntry;
import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import com.synlabs.ipsaa.entity.sharing.SharingSheetEntry;
import com.synlabs.ipsaa.view.common.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharingSheetEntryResponse implements Response
{

  private String food;

  private String entryType;

  private String feedQty;

  private String entryAt;

  private String entryFrom;

  private String entryTo;

  private String notes;

  private final DateFormat formatter = new SimpleDateFormat("hh:mm a");

  public SharingSheetEntryResponse(SharingSheetEntry sse)
  {

    this.food = sse.getFood();
    this.entryType = sse.getEntryType() == null ? null : sse.getEntryType().name();
    this.feedQty = sse.getFeedQty() == null ? null : sse.getFeedQty().name();
    this.entryAt = sse.getEntryAt() == null ? null : formatter.format(sse.getEntryAt());
    this.entryFrom = sse.getEntryFrom() == null ? null : formatter.format(sse.getEntryFrom());
    this.entryTo = sse.getEntryTo() == null ? null : formatter.format(sse.getEntryTo());
    this.notes = sse.getNotes();
  }

  public SharingSheetEntryResponse(ParentSharingSheetEntry sse)
  {
    this.food = sse.getFood();
    this.entryType = sse.getEntryType() == null ? null : sse.getEntryType().name();
    this.feedQty = sse.getFeedQty() == null ? null : sse.getFeedQty().name();
    this.entryAt = sse.getEntryAt() == null ? null : formatter.format(sse.getEntryAt());
  }

  public String getFood()
  {
    return food;
  }

  public String getEntryType()
  {
    return entryType;
  }

  public String getFeedQty()
  {
    return feedQty;
  }

  public String getEntryAt()
  {
    return entryAt;
  }

  public String getEntryFrom()
  {
    return entryFrom;
  }

  public String getEntryTo()
  {
    return entryTo;
  }

  public String getNotes()
  {
    return notes;
  }
}
