package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SharingSheetResponse implements Response
{

  private Long id;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date sharingDate;

  private Long studentId;

  private String studentName;

  private String notes;

  private String needs;

  private List<SharingSheetEntryResponse> entries;

  private String signedBy;

  public SharingSheetResponse()
  {
  }

  public SharingSheetResponse(SharingSheet ss)
  {
    this.id = mask(ss.getId());
    this.sharingDate = ss.getSharingDate();
    this.studentId = mask(ss.getStudent().getId());
    this.studentName = ss.getStudent().getName();
    this.notes = ss.getNotes();
    this.needs = ss.getNeeds();
    this.signedBy = ss.getCreatedBy().getFirstname();

    if (ss.getEntries() != null && !ss.getEntries().isEmpty()) {
      entries = ss.getEntries().stream().map(SharingSheetEntryResponse::new).collect(Collectors.toList());
    }
  }

  public Date getSharingDate()
  {
    return sharingDate;
  }

  public Long getId()
  {
    return id;
  }

  public Long getStudentId()
  {
    return studentId;
  }

  public String getStudentName()
  {
    return studentName;
  }

  public List<SharingSheetEntryResponse> getEntries()
  {
    return entries;
  }

  public String getNotes()
  {
    return notes;
  }

  public String getNeeds()
  {
    return needs;
  }

  public String getSignedBy()
  {
    return signedBy;
  }
}
