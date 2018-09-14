package com.synlabs.ipsaa.view.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.EmployeeType;
import com.synlabs.ipsaa.enums.MaritalStatus;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.view.common.Request;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by itrs on 4/13/2017.
 */
public class StaffRequest implements Request
{
  private Long id;
  private Long employerId;
  private Long reportingManagerId;
  private Long costCenterId;

  private String eid;
  private String name;
  private String firstName;
  private String lastName;
  private String mobile;
  private String secondaryNumbers;
  private String email;
  private String designation;
  private String doj;
  private String aadharNumber;

  private boolean active;
  private Integer biometricId;
  private Integer expectedHours;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date expectedIn;

  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date expectedOut;

  private EmployeeType  type;
  private MaritalStatus maritalStatus;

  private StaffProfileRequest profile;

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

  public Long getReportingManagerId()
  {
    return unmask(reportingManagerId);
  }

  public void setReportingManagerId(Long reportingManagerId)
  {
    this.reportingManagerId = reportingManagerId;
  }

  public Long getEmployerId()
  {
    return unmask(employerId);
  }

  public void setEmployerId(Long employerId)
  {
    this.employerId = employerId;
  }

  public MaritalStatus getMaritalStatus()
  {
    return maritalStatus;
  }

  public void setMaritalStatus(MaritalStatus maritalStatus)
  {
    this.maritalStatus = maritalStatus;
  }

  public Long getCostCenterId()
  {
    return unmask(costCenterId);
  }

  public void setCostCenterId(Long costCenterId)
  {
    this.costCenterId = costCenterId;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = unmask(id);
  }

  public Long getMaskedId()
  {
    return id;
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

  public String getDoj()
  {
    return doj;
  }

  public void setDoj(String doj)
  {
    this.doj = doj;
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

  public StaffProfileRequest getProfile()
  {
    return profile;
  }

  public void setProfile(StaffProfileRequest profile)
  {
    this.profile = profile;
  }

  public Integer getExpectedHours()
  {
    return expectedHours;
  }

  public void setExpectedHours(Integer expectedHours)
  {
    this.expectedHours = expectedHours;
  }

  public Employee toEntity(Employee employee) throws ParseException
  {
    employee.setEid(eid);
    employee.setFirstName(firstName);
    employee.setLastName(lastName);
    employee.setEmployeeType(type);
    employee.setMobile(mobile);
    employee.setSecondaryNumbers(secondaryNumbers);
    employee.setEmail(email);
    employee.setDesignation(designation);
    employee.setExpectedIn(expectedIn);
    employee.setExpectedOut(expectedOut);
    employee.setMaritalStatus(maritalStatus);
    employee.setBiometricId(biometricId);
    if (employee.isNew() && profile != null)
    {
      employee.setProfile(profile.toEntity());
    }

    if (expectedHours == null)
    {
      if (expectedIn != null && expectedOut != null)
      {
        long millsec = expectedOut.getTime() - expectedIn.getTime();
        long hours = millsec / 3600000;
        employee.setExpectedHours((int) hours);
      }
      else
      {
        employee.setExpectedHours(0);
      }
    }
    else
    {
      employee.setExpectedHours(expectedHours);
    }
    employee.setAadharNumber(aadharNumber);

    return employee;
  }

  public Employee toEntity() throws ParseException
  {
    return toEntity(new Employee());
  }

  //-----------------------------shubham--------------------------------------------------------
  private String dol;

  public String getDol() {
    return dol;
  }

  public void setDol(String dol) {
    this.dol = dol;
  }
}
