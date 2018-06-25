package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.sharing.SharingSheetEntry;
import com.synlabs.ipsaa.enums.SharingSheetEntryType;
import com.synlabs.ipsaa.enums.SharingSheetFeedQty;
import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class SharingSheetRequest implements Request
{

  private Long   id;

  private Long studentId;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date date;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getStudentId()
  {
    return unmask(studentId);
  }

  public void setStudentId(Long studentId)
  {
    this.studentId = studentId;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }
}
