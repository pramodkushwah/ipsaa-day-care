package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.AttendanceStatus;
import com.synlabs.ipsaa.enums.Relationship;
import com.synlabs.ipsaa.view.common.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class StudentAttendanceResponse implements Response
{

  private Long id;

  private String fullName;

  private String program;

  private String group;

  private String center;

  private String expectedIn;

  private String expectedOut;

  private String actualIn;

  private String actualOut;

  private String status;

  private String parents;

  private String img;

  private final DateFormat formatter = new SimpleDateFormat("hh:mm a");

  public StudentAttendanceResponse(StudentAttendance attendance)
  {
    Student student = attendance.getStudent();
    this.id = mask(student.getId());
    this.program = student.getProgram().getName();
    this.group = student.getGroup().getName();
    this.fullName = student.getProfile().getFullName();
    this.center = student.getCenter().getName();
    this.status = attendance.getStatus().name() == null? AttendanceStatus.Absent.toString(): attendance.getStatus().name();
    this.actualIn = attendance.getCheckin() == null ? null : formatter.format(attendance.getCheckin());
    this.actualOut = attendance.getCheckout() == null ? null : formatter.format(attendance.getCheckout());
    this.expectedIn = student.getExpectedIn() == null ? null : formatter.format(student.getExpectedIn());
    this.expectedOut = student.getExpectedOut() == null ? null : formatter.format(student.getExpectedOut());
    this.img = student.getProfile().getImagePath();
    this.parents = (student.getParents() == null || student.getParents().isEmpty()) ? "NA" :
                   student.getParents()
                          .stream()
                          .filter(parent -> !parent.getRelationship().equals(Relationship.Guardian))
                          .map(StudentParent::getFullName)
                          .reduce((p1, p2) -> p1 + " / " + p2).get();

  }

  public Long getId()
  {
    return id;
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getProgram()
  {
    return program;
  }

  public String getGroup()
  {
    return group;
  }

  public String getCenter()
  {
    return center;
  }

  public String getParents()
  {
    return parents;
  }

  public String getExpectedIn()
  {
    return expectedIn;
  }

  public String getExpectedOut()
  {
    return expectedOut;
  }

  public String getImg()
  {
    return img;
  }

  public String getActualIn()
  {
    return actualIn;
  }

  public String getActualOut()
  {
    return actualOut;
  }

  public String getStatus()
  {
    return status;
  }
}
