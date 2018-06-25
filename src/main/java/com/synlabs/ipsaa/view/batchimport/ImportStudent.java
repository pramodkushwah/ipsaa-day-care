package com.synlabs.ipsaa.view.batchimport;

import com.synlabs.ipsaa.enums.FamilyType;
import com.synlabs.ipsaa.enums.Gender;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by abhishek on 22/4/17.
 */
public class ImportStudent
{

  private String admissionNumber;
  private String centerCode;
  private String programCode;
  private String groupName;
  private boolean active = true;
  private Date   admissionDate;
  private String firstName;
  private String lastName;
  private String nickName;
  private Date   dob;
  private String gender;
  private String nationality;
  private String bloodGroup;
  private String profile;
  private String familyType;
  private Date   expectedIn;
  private Date   expectedOut;

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

  public String getAdmissionNumber()
  {
    return admissionNumber;
  }

  public void setAdmissionNumber(String admissionNumber)
  {
    this.admissionNumber = admissionNumber;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public String getProgramCode()
  {
    return programCode;
  }

  public void setProgramCode(String programCode)
  {
    this.programCode = programCode;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName(String groupName)
  {
    this.groupName = groupName;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public Date getAdmissionDate()
  {
    return admissionDate;
  }

  public void setAdmissionDate(Date admissionDate)
  {
    this.admissionDate = admissionDate;
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

  public String getNickName()
  {
    return nickName;
  }

  public void setNickName(String nickName)
  {
    this.nickName = nickName;
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

  public String getNationality()
  {
    return nationality;
  }

  public void setNationality(String nationality)
  {
    this.nationality = nationality;
  }

  public String getBloodGroup()
  {
    return bloodGroup;
  }

  public void setBloodGroup(String bloodGroup)
  {
    this.bloodGroup = bloodGroup;
  }

  public String getProfile()
  {
    return profile;
  }

  public void setProfile(String profile)
  {
    this.profile = profile;
  }

  public String getFamilyType()
  {
    return familyType;
  }

  public void setFamilyType(String familyType)
  {
    this.familyType = familyType;
  }

  public String validate()
  {
    if (StringUtils.isEmpty(this.admissionNumber))
    {
      return "Admission Number can't be empty.";
    }
    if (StringUtils.isEmpty(this.centerCode))
    {
      return "Center Code can't be empty.";
    }
    if (StringUtils.isEmpty(this.programCode))
    {
      return "Program Code can't be empty.";
    }
    if (StringUtils.isEmpty(this.groupName))
    {
      return "Program Group Name can't be empty.";
    }
    if (this.admissionDate == null)
    {
      return "Admission Date can't be null.";
    }
    if (StringUtils.isEmpty(this.firstName))
    {
      return "First Name can't be empty.";
    }
    if (!StringUtils.isEmpty(this.firstName) && this.firstName.length() > 20)
    {
      return "First Name can't have length more than 20.";
    }
    if (!StringUtils.isEmpty(this.lastName) && this.lastName.length() > 20)
    {
      return "Last Name can't have length more than 20.";
    }
    if (!StringUtils.isEmpty(this.nickName) && this.nickName.length() > 20)
    {
      return "Nick Name can't have length more than 20.";
    }
    if (this.dob == null)
    {
      return "Date of Birth can't be null.";
    }
    if (StringUtils.isEmpty(this.gender) || !Gender.matches(this.gender))
    {
      return "Gender mismatch.";
    }
    if (!StringUtils.isEmpty(this.familyType) && !FamilyType.matches(this.familyType))
    {
      return "Family Type mismatch.";
    }
    if (this.expectedIn == null)
    {
      return "Expected In time not specified.";
    }
    if (this.expectedOut == null)
    {
      return "Expected Out time not specified.";
    }
    return "OK";
  }
}
