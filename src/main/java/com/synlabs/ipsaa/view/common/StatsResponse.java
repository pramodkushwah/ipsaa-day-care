package com.synlabs.ipsaa.view.common;

public class StatsResponse implements Response
{
  private int              students;
  private int              corporateStudents;
  private int              parentUsers;
  private int              centers;
  private int              studentPresent;
  private int              staffPresent;
  private int              capacity;
  private int              staffCount;
  private FeeStatsResponse expectedFee;
  private FeeStatsResponse collectedFee;
  private int              openFollowups;
  private int              todayFollowups;
  private int              previousFollowups;
  private int              monthInquiries;

  private int users;

  private double utilisation;
  private int    staffCost;

  private int staffAbsent;
  private int staffOnLeave;

  public int getCorporateStudents()
  {
    return corporateStudents;
  }

  public void setCorporateStudents(int corporateStudents)
  {
    this.corporateStudents = corporateStudents;
  }

  public int getMonthInquiries()
  {
    return monthInquiries;
  }

  public void setMonthInquiries(int monthInquiries)
  {
    this.monthInquiries = monthInquiries;
  }

  public int getOpenFollowups()
  {
    return openFollowups;
  }

  public void setOpenFollowups(int openFollowups)
  {
    this.openFollowups = openFollowups;
  }

  public int getTodayFollowups()
  {
    return todayFollowups;
  }

  public void setTodayFollowups(int todayFollowups)
  {
    this.todayFollowups = todayFollowups;
  }

  public int getPreviousFollowups()
  {
    return previousFollowups;
  }

  public void setPreviousFollowups(int previousFollowups)
  {
    this.previousFollowups = previousFollowups;
  }

  public int getStudents()
  {
    return students;
  }

  public int getCenters()
  {
    return centers;
  }

  public int getStudentPresent()
  {
    return studentPresent;
  }

  public void setStudents(int students)
  {
    this.students = students;
  }

  public void setCenters(int centers)
  {
    this.centers = centers;
  }

  public void setStudentPresent(int studentPresent)
  {
    this.studentPresent = studentPresent;
  }

  public void setCapacity(int capacity)
  {
    this.capacity = capacity;
  }

  public int getCapacity()
  {
    return capacity;
  }

  public void setStaffCount(int staffCount)
  {
    this.staffCount = staffCount;
  }

  public int getStaffCount()
  {
    return staffCount;
  }

  public void setCollectedFee(FeeStatsResponse collectedFee)
  {
    this.collectedFee = collectedFee;
  }

  public FeeStatsResponse getCollectedFee()
  {
    return collectedFee;
  }

  public double getUtilisation()
  {
    return utilisation;
  }

  public void setUtilisation(double utilisation)
  {
    this.utilisation = utilisation;
  }

  public void setStaffCost(int staffCost)
  {
    this.staffCost = staffCost;
  }

  public int getStaffCost()
  {
    return staffCost;
  }

  public int getUsers()
  {
    return users;
  }

  public void setUsers(int users)
  {
    this.users = users;
  }

  public FeeStatsResponse getExpectedFee()
  {
    return expectedFee;
  }

  public void setExpectedFee(FeeStatsResponse expectedFee)
  {
    this.expectedFee = expectedFee;
  }

  public void setParentUsers(int parentUsers)
  {
    this.parentUsers = parentUsers;
  }

  public int getParentUsers()
  {
    return parentUsers;
  }

public int getStaffPresent() {
	return staffPresent;
}

public void setStaffPresent(int staffPresent) {
	this.staffPresent = staffPresent;
}

// -----------------------------------------------shubham --------------------------------------------------
  private int newJoinings;
  private int newLeavings;
  private long recruitmentCount;

  public long getRecruitmentCount() {
    return recruitmentCount;
  }

  public void setRecruitmentCount(long recruitmentCount) {
    this.recruitmentCount = recruitmentCount;
  }

  public int getNewLeavings() {
    return newLeavings;
  }

  public void setNewLeavings(int newLeavings) {
    this.newLeavings = newLeavings;
  }

  public int getNewJoinings() {
    return newJoinings;
  }

  public void setNewJoinings(int newJoinings) {
    this.newJoinings = newJoinings;
  }


  //////////////Avneet

  public int getStaffAbsent() { return staffAbsent; }

  public void setStaffAbsent(int staffAbsent) { this.staffAbsent = staffAbsent; }

  public int getStaffOnLeave() { return staffOnLeave; }

  public void setStaffOnLeave(int staffOnLeave) { this.staffOnLeave = staffOnLeave; }
}