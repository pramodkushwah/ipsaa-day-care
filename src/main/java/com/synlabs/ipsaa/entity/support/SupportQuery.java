package com.synlabs.ipsaa.entity.support;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.QueryStatus;

import javax.persistence.*;
import java.util.List;

@Entity
public class SupportQuery extends BaseEntity
{

  @Column(length = 200, nullable = false)
  private String title;

  @Column(length = 2000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private StudentParent parent;

  @ManyToOne(fetch = FetchType.LAZY)
  private Student student;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Center center;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private QueryStatus status = QueryStatus.New;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "supportQuery", cascade = CascadeType.ALL)
  @OrderBy("createdDate ASC")
  private List<SupportQueryEntry> replies;

  public Student getStudent()
  {
    return student;
  }

  public void setStudent(Student student)
  {
    this.student = student;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public StudentParent getParent()
  {
    return parent;
  }

  public void setParent(StudentParent parent)
  {
    this.parent = parent;
  }

  public QueryStatus getStatus()
  {
    return status;
  }

  public void setStatus(QueryStatus status)
  {
    this.status = status;
  }

  public List<SupportQueryEntry> getReplies()
  {
    return replies;
  }

  public void setReplies(List<SupportQueryEntry> replies)
  {
    this.replies = replies;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public void addReply(SupportQueryEntry reply)
  {
    this.replies.add(reply);
  }
}
