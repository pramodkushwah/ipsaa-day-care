package com.synlabs.ipsaa.view.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.EmployeeType;
import com.synlabs.ipsaa.enums.MaritalStatus;
import com.synlabs.ipsaa.view.center.CenterResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

public class StaffResponse implements Response
{

  private Long    id;
  private String  eid;
  private Long    employerId;
  private String  employerName;
  private Long    reportingManagerId;
  private String  reportingManagerName;
  private String  name;
  private String  firstName;
  private String  lastName;
  private String  mobile;
  private String  secondaryNumbers;
  private String  email;
  private boolean payrollEnabled;
  private boolean attendanceEnabled;

  private String        designation;
  private EmployeeType  type;
  private boolean       active;
  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date          expectedIn;
  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date          expectedOut;
  private MaritalStatus maritalStatus;
  private String        imagePath;
  private String        staffImageData;

  private StaffProfileResponse profile;
  private CenterResponse       costCenter;
  private Integer              biometricId;

  private ApprovalStatus approvalStatus;

  private Integer expectedHours;
  private String  aadharNumber;

  public StaffResponse(Employee employee)
  {
    this.id = mask(employee.getId());
    this.eid = employee.getEid();
    this.employerId = employee.getEmployer() == null ? null : mask(employee.getEmployer().getId());
    this.employerName = employee.getEmployer() == null ? null : employee.getEmployer().getName();
    this.reportingManagerId = mask(employee.getReportingManager() == null ? null : employee.getReportingManager().getId());
    this.reportingManagerName = employee.getReportingManager() == null ? null : employee.getReportingManager().getName();
    this.name = employee.getName();
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
    this.type = employee.getEmployeeType();
    this.mobile = employee.getMobile();
    this.email = employee.getEmail();
    this.designation = employee.getDesignation();
//    this.doj = employee.getDoj().toString();
    this.secondaryNumbers = employee.getSecondaryNumbers();
    this.active = employee.isActive();
    this.expectedIn = employee.getExpectedIn();
    this.expectedOut = employee.getExpectedOut();

    this.maritalStatus = employee.getMaritalStatus();
    this.approvalStatus = employee.getApprovalStatus();

    this.costCenter = employee.getCostCenter() == null ? null : new CenterResponse(employee.getCostCenter());
    this.profile = employee.getProfile() == null ? null : new StaffProfileResponse(employee.getProfile());
    this.imagePath = employee.getProfile().getImagePath();

    this.expectedHours = employee.getExpectedHours();
    this.payrollEnabled = employee.isPayrollEnabled();
    this.attendanceEnabled = employee.isAttendanceEnabled();
    this.biometricId = employee.getBiometricId();
    this.aadharNumber = employee.getAadharNumber();
  }

  public String getAadharNumber()
  {
    return aadharNumber;
  }

  public Integer getBiometricId()
  {
    return biometricId;
  }

  public boolean isAttendanceEnabled()
  {
    return attendanceEnabled;
  }

  public boolean isPayrollEnabled()
  {
    return payrollEnabled;
  }

  public Integer getExpectedHours()
  {
    return expectedHours;
  }

  public Long getReportingManagerId()
  {
    return reportingManagerId;
  }

  public String getReportingManagerName()
  {
    return reportingManagerName;
  }

  public Long getEmployerId()
  {
    return employerId;
  }

  public String getEmployerName()
  {
    return employerName;
  }

  public ApprovalStatus getApprovalStatus()
  {
    return approvalStatus;
  }

  public void setStaffImageData(String staffImageData)
  {
    this.staffImageData = staffImageData;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public MaritalStatus getMaritalStatus()
  {
    return maritalStatus;
  }

  public void setMaritalStatus(MaritalStatus maritalStatus)
  {
    this.maritalStatus = maritalStatus;
  }

  public String getStaffImageData()
  {
    return staffImageData;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getEid()
  {
    return eid;
  }

  public void setEid(String eid)
  {
    this.eid = eid;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
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

  public EmployeeType getType()
  {
    return type;
  }

  public void setType(EmployeeType type)
  {
    this.type = type;
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

  public String getDesignation()
  {
    return designation;
  }

  public void setDesignation(String designation)
  {
    this.designation = designation;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
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

  public StaffProfileResponse getProfile()
  {
    return profile;
  }

  public void setProfile(StaffProfileResponse profile)
  {
    this.profile = profile;
  }

  public CenterResponse getCostCenter()
  {
    return costCenter;
  }

  public void setCostCenter(CenterResponse costCenter)
  {
    this.costCenter = costCenter;
  }
}
