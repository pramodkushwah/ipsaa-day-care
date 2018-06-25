package com.synlabs.ipsaa.view.batchimport;

import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.enums.EmployeeType;
import com.synlabs.ipsaa.enums.Gender;
import com.synlabs.ipsaa.enums.MaritalStatus;
import com.synlabs.ipsaa.enums.UserType;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class ImportEmployee
{
  private String eid;

  private String firstName;

  private String lastName;

  private Date doj;

  private String mobile;

  private String secondaryNumbers;

  private String email;

  private String designation;

  private String employeeType;

  private boolean payrollEnabled = false;

  private boolean attendanceEnabled = false;

  private String userType;

  private String costCenterName;

  private Date dob;

  private String gender;

  private String address;

  private String city;

  private String state;

  private String zipCode;

  private String phone;

  private String addressType;

  private String pAddress;

  private String pCity;

  private String pState;

  private String pZipCode;

  private String pPhone;

  private Date expectedIn;

  private Date expectedOut;

  private String maritalStatus;

  public String getMaritalStatus()
  {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus)
  {
    this.maritalStatus = maritalStatus;
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

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getpState()
  {
    return pState;
  }

  public void setpState(String pState)
  {
    this.pState = pState;
  }

  public Date getDob()
  {
    return dob;
  }

  public void setDob(Date dob)
  {
    this.dob = dob;
  }

  public String getGender()
  {
    return gender;
  }

  public void setGender(String gender)
  {
    this.gender = gender;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getZipCode()
  {
    return zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public void setAddressType(String addressType)
  {
    this.addressType = addressType;
  }

  public String getpAddress()
  {
    return pAddress;
  }

  public void setpAddress(String pAddress)
  {
    this.pAddress = pAddress;
  }

  public String getpCity()
  {
    return pCity;
  }

  public void setpCity(String pCity)
  {
    this.pCity = pCity;
  }

  public String getpZipCode()
  {
    return pZipCode;
  }

  public void setpZipCode(String pZipCode)
  {
    this.pZipCode = pZipCode;
  }

  public String getpPhone()
  {
    return pPhone;
  }

  public void setpPhone(String pPhone)
  {
    this.pPhone = pPhone;
  }

  public String getCostCenterName()
  {
    return costCenterName;
  }

  public void setCostCenterName(String costCenterName)
  {
    this.costCenterName = costCenterName;
  }

  public String getEid()
  {
    return eid;
  }

  public void setEid(String eid)
  {
    this.eid = eid;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName) throws Exception
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

  public Date getDoj()
  {
    return doj;
  }

  public void setDoj(Date doj)
  {
    this.doj = doj;
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

  public String getEmployeeType()
  {
    return employeeType;
  }

  public void setEmployeeType(String employeeType)
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

  public String getUserType()
  {
    return userType;
  }

  public void setUserType(String userType)
  {
    this.userType = userType;
  }

  public String validate()
  {

    if (StringUtils.isEmpty(this.firstName) || this.firstName.length() > 50)
    {
      return "Firstname is empty or length greater then 20.";
    }
    if (!StringUtils.isEmpty(this.lastName) && this.lastName.length() > 50)
    {
      return "Lastname length is greater than 20.";
    }
    if (doj == null)
    {
      return "DOJ not provided.";
    }

    if (this.mobile.contains("E")) {
      this.mobile = new BigDecimal(this.mobile).longValue() + "";
    }

    if (StringUtils.isEmpty(this.mobile) || this.mobile.length() > 10)
    {
      return "Mobile number not provided or length greater than 10.";
    }
    if (!StringUtils.isEmpty(this.secondaryNumbers) && this.secondaryNumbers.length() > 50)
    {
      return "Secondary Numbers Too long to handle.";
    }
    if (!StringUtils.isEmpty(this.designation) && this.designation.length() > 50)
    {
      return "Designation length greater than 50.";
    }
    if (!StringUtils.isEmpty(this.email) && this.email.length() > 50)
    {
      return "Email length is greater than 50.";
    }
    if (StringUtils.isEmpty(this.employeeType) || !EmployeeType.matches(this.employeeType))
    {
      return "Employee Type mismatch.";
    }
    if (!StringUtils.isEmpty(this.userType) && !UserType.matches(this.userType))
    {
      return "UserType mismatch.";
    }
    if (dob == null)
    {
      return "DOB not provided.";
    }
    if (StringUtils.isEmpty(this.gender) || !Gender.matches(this.gender))
    {
      return "Gender mismatch.";
    }
    if (StringUtils.isEmpty(this.address))
    {
      return "Address Line is necessary.";
    }
    if (StringUtils.isEmpty(this.city))
    {
      return "City in address is necessary.";
    }
    if (StringUtils.isEmpty(this.state))
    {
      return "State in address is necessary.";
    }
    if (StringUtils.isEmpty(this.zipCode))
    {
      return "ZipCode in address is necessary.";
    }
    if (StringUtils.isEmpty(this.phone))
    {
      return "Phone number on address required.";
    }
    if (StringUtils.isEmpty(this.pAddress))
    {
      return "Permanent address Line is necessary.";
    }
    if (StringUtils.isEmpty(this.pCity))
    {
      return "City in Permanent address is necessary.";
    }
    if (StringUtils.isEmpty(this.pState))
    {
      return "State in Permanent address is necessary.";
    }
    if (StringUtils.isEmpty(this.pZipCode))
    {
      return "ZipCode in Permanent address is necessary.";
    }
    if (StringUtils.isEmpty(this.pPhone))
    {
      return "Phone number on Permanent address required.";
    }
    if (this.expectedIn == null)
    {
      return "Expected In time not specified.";
    }
    if (this.expectedOut == null)
    {
      return "Expected Out time not specified.";
    }
    if(this.maritalStatus==null){
      return "MaritalStatus is required.";
    }
    return "OK";
  }

}
