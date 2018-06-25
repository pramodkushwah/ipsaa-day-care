package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.EmployeeType;
import com.synlabs.ipsaa.view.common.Response;

/**
 * Created by itrs on 4/13/2017.
 */
public class StaffSummaryResponse implements Response
{
  private Long   id;
  private String employer;
  private Long   employerId;
  private String employerName;
  private String employerCode;
  private Long   reportingManagerId;
  private String reportingManagerName;
  private String eid;
  private String name;
  private String designation;
  private String email;
  private String mobile;
  private String centerCode;
  private String centerName;

  private EmployeeType type;

  public StaffSummaryResponse()
  {
  }

  public StaffSummaryResponse(Employee employee)
  {
    this(employee, false);
  }

  public StaffSummaryResponse(Employee employee, boolean onlyName)
  {
    this.eid = employee.getEid();
    this.id = mask(employee.getId());
    this.name = employee.getName();
    if (onlyName)
    {
      return;
    }
    this.employer = employee.getEmployer() == null ? null : employee.getEmployer().getName();
    this.employerId = employee.getEmployer() == null ? null : mask(employee.getEmployer().getId());
    this.employerName = employee.getEmployer() == null ? null : employee.getEmployer().getName();
    this.employerCode = employee.getEmployer() == null ? null : employee.getEmployer().getCode();
    this.reportingManagerId = mask(employee.getReportingManager() == null ? null : employee.getReportingManager().getId());
    this.reportingManagerName = employee.getReportingManager() == null ? null : employee.getReportingManager().getName();
    this.designation = employee.getDesignation();
    this.email = employee.getEmail();
    this.mobile = employee.getMobile();
    this.type = employee.getEmployeeType();
    this.centerName = employee.getCostCenter() == null ? null : employee.getCostCenter().getName();
    this.centerCode = employee.getCostCenter() == null ? null : employee.getCostCenter().getCode();
    this.employer = employee.getEmployer() == null ? "" : employee.getEmployer().getName();
  }


  public String getEmployerCode()
  {
    return employerCode;
  }

  public String getEmployerName()
  {
    return employerName;
  }

  public String getReportingManagerName()
  {
    return reportingManagerName;
  }

  public Long getEmployerId()
  {
    return employerId;
  }

  public Long getReportingManagerId()
  {
    return reportingManagerId;
  }

  public EmployeeType getType()
  {
    return type;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getCenterName()
  {
    return centerName;
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

  public String getDesignation()
  {
    return designation;
  }

  public void setDesignation(String designation)
  {
    this.designation = designation;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getMobile()
  {
    return mobile;
  }

  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }

  public String getEmployer()
  {
    return employer;
  }

  public void setEmployer(String employer)
  {
    this.employer = employer;
  }
}
