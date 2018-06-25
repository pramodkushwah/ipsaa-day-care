package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.enums.PhotoType;

import javax.persistence.*;

@Entity
public class GalleryPhoto extends BaseEntity
{
  @Enumerated(EnumType.STRING)
  private PhotoType type;

  @Column(length = 50,nullable = false)
  private String imagePath;

  @ManyToOne(optional = false)
  private Center center;

  @ManyToOne
  private Student student;

  private String comment;

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public PhotoType getType()
  {
    return type;
  }

  public void setType(PhotoType type)
  {
    this.type = type;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public Student getStudent()
  {
    return student;
  }

  public void setStudent(Student student)
  {
    this.student = student;
  }
}
