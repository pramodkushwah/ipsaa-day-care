package com.synlabs.ipsaa.entity.attendance;

import com.google.common.base.MoreObjects;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.enums.AttendanceStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class StudentAttendance extends BaseAttendance
{

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Student student;

  public Student getStudent()
  {
    return student;
  }

  public void setStudent(Student student)
  {
    this.student = student;
  }

  @Override
  public String toString()
  {
    return MoreObjects.toStringHelper(this)
                      .add("student", student)
                      .add("attendanceDate",getAttendanceDate())
                      .toString();
  }
}
