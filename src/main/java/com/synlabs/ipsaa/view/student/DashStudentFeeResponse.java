package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

public class DashStudentFeeResponse implements Response
{
  private String     name;
  private String     program;
  private String     group;
  private String     center;
  private BigDecimal finalFee;
  private BigDecimal baseFee;
  private String     feeDuration;
  private BigDecimal discount;

  public DashStudentFeeResponse(Student student)
  {
    name = student.getName();
    program = student.getProgramName();
    group = student.getGroupName();
    center = student.getCenterName();
  }

  public DashStudentFeeResponse(StudentFee studentfee)
  {
    Student student = studentfee.getStudent();
    name = student.getName();
    program = student.getProgramName();
    group = student.getGroupName();
    center = student.getCenterName();
    this.finalFee = studentfee.getFinalFee();
    this.discount = studentfee.getDiscount() == null ? new BigDecimal(0) : studentfee.getDiscount();
    this.feeDuration = studentfee.getFeeDuration().name();
    this.baseFee = studentfee.getBaseFee();
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getProgram()
  {
    return program;
  }

  public void setProgram(String program)
  {
    this.program = program;
  }

  public String getGroup()
  {
    return group;
  }

  public void setGroup(String group)
  {
    this.group = group;
  }

  public String getCenter()
  {
    return center;
  }

  public void setCenter(String center)
  {
    this.center = center;
  }

  public BigDecimal getFinalFee()
  {
    return finalFee;
  }

  public void setFinalFee(BigDecimal finalFee)
  {
    this.finalFee = finalFee;
  }

  public BigDecimal getBaseFee()
  {
    return baseFee;
  }

  public void setBaseFee(BigDecimal baseFee)
  {
    this.baseFee = baseFee;
  }

  public String getFeeDuration()
  {
    return feeDuration;
  }

  public void setFeeDuration(String feeDuration)
  {
    this.feeDuration = feeDuration;
  }

  public BigDecimal getDiscount()
  {
    return discount;
  }

  public void setDiscount(BigDecimal discount)
  {
    this.discount = discount;
  }
}
