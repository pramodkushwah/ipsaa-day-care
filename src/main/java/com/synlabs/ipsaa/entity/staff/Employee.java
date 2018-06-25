package com.synlabs.ipsaa.entity.staff;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.EmployeeType;
import com.synlabs.ipsaa.enums.MaritalStatus;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Employee extends BaseEntity
{
  private Integer biometricId;

  @Column(nullable = false)
  private boolean active = true;

  @Column(nullable = false, length = 20)
  private String eid;

  @Column(length = 50)
  private String firstName;

  @Column(length = 50)
  private String lastName;

  @Column(nullable = false, length = 20)
  private String mobile;

  @Column(length = 50)
  private String secondaryNumbers;

  @Column(length = 50)
  private String email;

  @Column(length = 50)
  private String designation;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EmployeeType employeeType;

  @ManyToOne
  private Center costCenter;

  @ManyToOne
  private LegalEntity employer;

  @ManyToOne
  private Employee reportingManager;

  @Temporal(TemporalType.TIME)
  private Date expectedIn;

  @Temporal(TemporalType.TIME)
  private Date expectedOut;

  private Integer expectedHours;

  @OneToOne(cascade = CascadeType.ALL)
  private EmployeeProfile profile;

  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ApprovalStatus approvalStatus;

  private boolean payrollEnabled;

  private boolean attendanceEnabled;

  private String aadharNumber;

  public String getAadharNumber()
  {
    return aadharNumber;
  }

  public void setAadharNumber(String aadharNumber)
  {
    this.aadharNumber = aadharNumber;
  }

  public Integer getBiometricId()
  {
    return biometricId;
  }

  public void setBiometricId(Integer biometricId)
  {
    this.biometricId = biometricId;
  }

  public ApprovalStatus getApprovalStatus()
  {
    return approvalStatus;
  }

  public void setApprovalStatus(ApprovalStatus approvalStatus)
  {
    this.approvalStatus = approvalStatus;
  }

  public MaritalStatus getMaritalStatus()
  {
    return maritalStatus;
  }

  public void setMaritalStatus(MaritalStatus maritalStatus)
  {
    this.maritalStatus = maritalStatus;
  }

  public EmployeeProfile getProfile()
  {
    return profile;
  }

  public void setProfile(EmployeeProfile profile)
  {
    this.profile = profile;
  }

  public String getDesignation()
  {
    return designation;
  }

  public void setDesignation(String designation)
  {
    this.designation = designation;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getMobile()
  {
    return mobile;
  }

  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }

  public String getSecondaryNumbers()
  {
    return secondaryNumbers;
  }

  public void setSecondaryNumbers(String secondaryNumbers)
  {
    this.secondaryNumbers = secondaryNumbers;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public EmployeeType getEmployeeType()
  {
    return employeeType;
  }

  public void setEmployeeType(EmployeeType employeeType)
  {
    this.employeeType = employeeType;
  }

  public boolean isPayrollEnabled()
  {
    return payrollEnabled;
  }

  public void setPayrollEnabled(boolean payrollEnabled)
  {
    this.payrollEnabled = payrollEnabled;
  }

  public boolean isAttendanceEnabled()
  {
    return attendanceEnabled;
  }

  public void setAttendanceEnabled(boolean attendanceEnabled)
  {
    this.attendanceEnabled = attendanceEnabled;
  }

  public String getName()
  {
    return (firstName + (lastName == null ? "" : " " + lastName)).trim();
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public String getEid()
  {
    return eid;
  }

  public void setEid(String eid)
  {
    this.eid = eid;
  }

  public Center getCostCenter()
  {
    return costCenter;
  }

  public void setCostCenter(Center costCenter)
  {
    this.costCenter = costCenter;
  }

  public Date getExpectedIn()
  {
    return expectedIn;
  }

  public void setExpectedIn(Date expectedIn)
  {
    this.expectedIn = expectedIn;
  }

  public Date getExpectedOut()
  {
    return expectedOut;
  }

  public void setExpectedOut(Date expectedOut)
  {
    this.expectedOut = expectedOut;
  }

  public LegalEntity getEmployer()
  {
    return employer;
  }

  public void setEmployer(LegalEntity employer)
  {
    this.employer = employer;
  }

  public Employee getReportingManager()
  {
    return reportingManager;
  }

  public void setReportingManager(Employee reportingManager)
  {
    this.reportingManager = reportingManager;
  }

  public Integer getExpectedHours()
  {
    return expectedHours;
  }

  public void setExpectedHours(Integer expectedHours)
  {
    this.expectedHours = expectedHours;
  }

  @Transient
  public String getInTime()
  {
    String result = "";
    if (this.expectedIn != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
      result = sdf.format(this.expectedIn);
    }
    return result;
  }

  @Transient
  public String getOutTime()
  {
    String result = "";
    if (this.expectedOut != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
      result = sdf.format(this.expectedOut);
    }
    return result;
  }

  @Transient
  public String getCenterName()
  {
    return this.costCenter == null ? "" : this.costCenter.getName();
  }

  @Transient
  public String getType()
  {
    return this.employeeType == null ? "" : this.employeeType.name();
  }
}
