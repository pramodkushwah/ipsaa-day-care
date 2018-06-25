package com.synlabs.ipsaa.entity.sharing;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.student.Student;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class ParentSharingSheet extends BaseEntity
{

  @ManyToOne
  private Student student;

  @Temporal(TemporalType.DATE)
  private Date sharingDate;

  @Temporal(TemporalType.TIME)
  private Date bedTime;

  @Temporal(TemporalType.TIME)
  private Date wakeupTime;

  @Column(length = 50)
  private String sleepCycle;

  @Column(length = 100)
  private String sleepReason;

  @Column(length = 50)
  private String wakeupMood;

  @Column(length = 50)
  private String arrivalMood;

  private boolean bowelMovement;

  private boolean bathed;

  private boolean sponged;

  @Column(length = 100)
  private String noticedIssue;

  @Column(length = 200)
  private String sharing;

  @OneToMany(mappedBy = "sharingSheet", cascade = CascadeType.PERSIST)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<ParentSharingSheetEntry> entries;

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

  public List<ParentSharingSheetEntry> getEntries()
  {
    return entries;
  }

  public void setEntries(List<ParentSharingSheetEntry> entries)
  {
    this.entries = entries;
  }

  public void addEntry(ParentSharingSheetEntry entry)
  {
    entries.add(entry);
  }
}
