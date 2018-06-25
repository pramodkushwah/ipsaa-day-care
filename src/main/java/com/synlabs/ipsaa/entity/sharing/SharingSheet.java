package com.synlabs.ipsaa.entity.sharing;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentParent;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class SharingSheet extends BaseEntity
{

  @OneToMany(mappedBy = "sharingSheet")
  @Fetch(value = FetchMode.SUBSELECT)
  private List<SharingSheetEntry> entries;

  @ManyToOne
  private Student student;

  @Temporal(TemporalType.DATE)
  private Date sharingDate;

  private boolean readByParent;

  @Column(length = 500)
  private String notes;

  @Column(length = 500)
  private String needs;

  public List<SharingSheetEntry> getEntries()
  {
    return entries;
  }

  public void setEntries(List<SharingSheetEntry> entries)
  {
    this.entries = entries;
  }

  public Student getStudent()
  {
    return student;
  }

  public void setStudent(Student student)
  {
    this.student = student;
  }

  public Date getSharingDate()
  {
    return sharingDate;
  }

  public void setSharingDate(Date sharingDate)
  {
    this.sharingDate = sharingDate;
  }

  public boolean isReadByParent()
  {
    return readByParent;
  }

  public void setReadByParent(boolean readByParent)
  {
    this.readByParent = readByParent;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  public String getNotes()
  {
    return notes;
  }

  public void setNeeds(String needs)
  {
    this.needs = needs;
  }

  public String getNeeds()
  {
    return needs;
  }
}
