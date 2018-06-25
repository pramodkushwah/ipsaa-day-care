package com.synlabs.ipsaa.ftl.model;

/**
 * Created by itrs on 7/17/2017.
 */
public class ApprovalModel
{
  private String centerName;
  private int staffApprovalCount;
  private int studentApprovalCount;

  public ApprovalModel(String centerName)
  {
    this.centerName = centerName;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public void setCenterName(String centerName)
  {
    this.centerName = centerName;
  }

  public int getStaffApprovalCount()
  {
    return staffApprovalCount;
  }

  public void setStaffApprovalCount(int staffApprovalCount)
  {
    this.staffApprovalCount = staffApprovalCount;
  }

  public int getStudentApprovalCount()
  {
    return studentApprovalCount;
  }

  public void setStudentApprovalCount(int studentApprovalCount)
  {
    this.studentApprovalCount = studentApprovalCount;
  }
}
