package com.synlabs.ipsaa.view.batchimport;

import com.synlabs.ipsaa.enums.FamilyType;
import com.synlabs.ipsaa.enums.Gender;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by abhishek on 22/4/17.
 */
public class ImportStudentFull
{

  private String sNo;
  private String fullName;
  private String age;
  private String address;
  private String area;

  private String motherFirstName;
  private String motherLastName;
  private String motherOccupation;
  private String motherQualification;
  private String motherCompany;
  private String motherWorkAddress;
  private String motherMobile;
  private String motherEmail;


  private String fatherFirstName;
  private String fatherLastName;
  private String fatherOccupation;
  private String fatherQualification;
  private String fatherCompany;
  private String fatherWorkAddress;
  private String fatherMobile;
  private String fatherEmail;
  
  
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
  private String city;
  private String state;
  private String zipCode;

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
    return centerCode == null ? null : centerCode.trim();
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
    return profile == null ? "NA" : profile;
  }

  public void setProfile(String profile)
  {
    this.profile = profile;
  }

  public String getFamilyType()
  {
    return familyType == null ?  FamilyType.Nuclear.name() : familyType;
  }

  public void setFamilyType(String familyType)
  {
    this.familyType = familyType;
  }

  public String getsNo()
  {
    return sNo;
  }

  public void setsNo(String sNo)
  {
    this.sNo = sNo;
  }

  public String getFullName()
  {
    return fullName;
  }

  public void setFullName(String fullName)
  {
    this.fullName = fullName;
  }

  public String getAge()
  {
    return age;
  }

  public void setAge(String age)
  {
    this.age = age;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getArea()
  {
    return area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }

  public String getMotherFirstName()
  {
    return motherFirstName;
  }

  public void setMotherFirstName(String motherFirstName)
  {
    this.motherFirstName = motherFirstName;
  }

  public String getMotherLastName()
  {
    return motherLastName;
  }

  public void setMotherLastName(String motherLastName)
  {
    this.motherLastName = motherLastName;
  }

  public String getMotherOccupation()
  {
    return motherOccupation;
  }

  public void setMotherOccupation(String motherOccupation)
  {
    this.motherOccupation = motherOccupation;
  }

  public String getMotherQualification()
  {
    return motherQualification;
  }

  public void setMotherQualification(String motherQualification)
  {
    this.motherQualification = motherQualification;
  }

  public String getMotherCompany()
  {
    return motherCompany;
  }

  public void setMotherCompany(String motherCompany)
  {
    this.motherCompany = motherCompany;
  }

  public String getMotherWorkAddress()
  {
    return motherWorkAddress;
  }

  public void setMotherWorkAddress(String motherWorkAddress)
  {
    this.motherWorkAddress = motherWorkAddress;
  }

  public String getMotherMobile()
  {
    return motherMobile;
  }

  public void setMotherMobile(String motherMobile)
  {
    this.motherMobile = motherMobile;
  }

  public String getMotherEmail()
  {
    return motherEmail;
  }

  public void setMotherEmail(String motherEmail)
  {
    this.motherEmail = motherEmail;
  }

  public String getFatherFirstName()
  {
    return fatherFirstName;
  }

  public void setFatherFirstName(String fatherFirstName)
  {
    this.fatherFirstName = fatherFirstName;
  }

  public String getFatherLastName()
  {
    return fatherLastName;
  }

  public void setFatherLastName(String fatherLastName)
  {
    this.fatherLastName = fatherLastName;
  }

  public String getFatherOccupation()
  {
    return fatherOccupation;
  }

  public void setFatherOccupation(String fatherOccupation)
  {
    this.fatherOccupation = fatherOccupation;
  }

  public String getFatherQualification()
  {
    return fatherQualification;
  }

  public void setFatherQualification(String fatherQualification)
  {
    this.fatherQualification = fatherQualification;
  }

  public String getFatherCompany()
  {
    return fatherCompany;
  }

  public void setFatherCompany(String fatherCompany)
  {
    this.fatherCompany = fatherCompany;
  }

  public String getFatherWorkAddress()
  {
    return fatherWorkAddress;
  }

  public void setFatherWorkAddress(String fatherWorkAddress)
  {
    this.fatherWorkAddress = fatherWorkAddress;
  }

  public String getFatherMobile()
  {
    return fatherMobile;
  }

  public void setFatherMobile(String fatherMobile)
  {
    this.fatherMobile = fatherMobile;
  }

  public String getFatherEmail()
  {
    return fatherEmail;
  }

  public void setFatherEmail(String fatherEmail)
  {
    this.fatherEmail = fatherEmail;
  }

  public String validate()
  {

    if (!StringUtils.isEmpty(this.fullName)) {
      String[] names = fullName.split(" ");

      if (names.length < 1) return "Missing FirstName";
      firstName  = names[0];
      if (names.length >1) lastName = names[1];
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
    if (!StringUtils.isEmpty(this.firstName) && this.firstName.length() > 50)
    {
      return "First Name can't have length more than 20.";
    }
    if (!StringUtils.isEmpty(this.lastName) && this.lastName.length() > 50)
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

    if ("M".equalsIgnoreCase(this.gender)) this.gender = Gender.Male.name();
    if ("F".equalsIgnoreCase(this.gender)) this.gender = Gender.Female.name();
    if (StringUtils.isEmpty(this.gender) || !Gender.matches(this.gender))
    {
      return "Gender mismatch.";
    }
    if (!StringUtils.isEmpty(this.familyType) && !FamilyType.matches(this.familyType))
    {
      return "Family Type mismatch.";
    }
//    if (this.expectedIn == null)
//    {
//      return "Expected In time not specified.";
//    }
//    if (this.expectedOut == null)
//    {
//      return "Expected Out time not specified.";
//    }

    return validateParents();
  }

  private String validateParents()
  {

    //mother
    if (StringUtils.isEmpty(this.motherFirstName))
    {
      return "First Name can't be empty.(Mother)";
    }
    if (!StringUtils.isEmpty(this.motherFirstName) && this.motherFirstName.length() > 50)
    {
      return "First Name can't have length more than 20.(Mother)";
    }
    if (!StringUtils.isEmpty(this.motherLastName) && this.motherLastName.length() > 50)
    {
      return "Last Name can't have length more than 20.(Mother)";
    }

    if (StringUtils.isEmpty(this.motherMobile))
    {
      return "Mobile number not provided(Mother)";
    }

    if (this.motherMobile.contains("E")) {
      this.motherMobile = new BigDecimal(this.motherMobile).longValue() + "";
    }

    if (this.motherMobile.length() > 10)
    {
      return "Mobile number not provided or length greater than 10.(Mother)";
    }

    if (!StringUtils.isEmpty(this.motherEmail) && this.motherEmail.length() > 50)
    {
      return "Email length is greater than 50.(Mother)";
    }

    if (StringUtils.isEmpty(this.address))
    {
      return "Address is necessary";
    }

  //father
    if (StringUtils.isEmpty(this.fatherFirstName))
    {
      return "First Name can't be empty.(Father)";
    }
    if (!StringUtils.isEmpty(this.fatherFirstName) && this.fatherFirstName.length() > 50)
    {
      return "First Name can't have length more than 20.(Father)";
    }
    if (!StringUtils.isEmpty(this.fatherLastName) && this.fatherLastName.length() > 50)
    {
      return "Last Name can't have length more than 20.(Father)";
    }


    if (StringUtils.isEmpty(this.fatherMobile))
    {
      return "Mobile number not provided(Father)";
    }

    if (this.fatherMobile.contains("E")) {
      this.fatherMobile = new BigDecimal(this.fatherMobile).longValue() + "";
    }

    if (this.fatherMobile.length() > 10)
    {
      return "Mobile number not provided or length greater than 10.(Father)";
    }

    if (!StringUtils.isEmpty(this.fatherEmail) && this.fatherEmail.length() > 50)
    {
      return "Email length is greater than 50.(Father)";
    }

    return "OK";
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getState()
  {
    return state;
  }

  public String getZipCode()
  {
    return zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  public void setState(String state)
  {
    this.state = state;
  }
}
