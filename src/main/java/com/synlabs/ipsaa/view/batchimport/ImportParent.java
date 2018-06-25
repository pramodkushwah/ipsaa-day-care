package com.synlabs.ipsaa.view.batchimport;

import com.synlabs.ipsaa.enums.Relationship;
import org.springframework.util.StringUtils;

/**
 * Created by abhishek on 22/4/17.
 */
public class ImportParent
{
  private String admissionNumber;
  private String relationship;
  private String firstName;
  private String lastName;
  private String mobile;
  private String secondaryNumbers;
  private String email;
  private String occupation;
  private String organisation;
  private String designation;
  private String educationalQualification;
  private String rAddress;
  private String rCity;
  private String rState;
  private String rZipCode;
  private String rPhone;
  private String oAddress;
  private String oCity;
  private String oState;
  private String oZipCode;
  private String oPhone;
  private boolean emergencyContact = false;

  public boolean isEmergencyContact()
  {
    return emergencyContact;
  }

  public void setEmergencyContact(boolean emergencyContact)
  {
    this.emergencyContact = emergencyContact;
  }

  public String getAdmissionNumber()
  {
    return admissionNumber;
  }

  public void setAdmissionNumber(String admissionNumber)
  {
    this.admissionNumber = admissionNumber;
  }

  public String getRelationship()
  {
    return relationship;
  }

  public void setRelationship(String relationship)
  {
    this.relationship = relationship;
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

  public String getOccupation()
  {
    return occupation;
  }

  public void setOccupation(String occupation)
  {
    this.occupation = occupation;
  }

  public String getOrganisation()
  {
    return organisation;
  }

  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  public String getDesignation()
  {
    return designation;
  }

  public void setDesignation(String designation)
  {
    this.designation = designation;
  }

  public String getEducationalQualification()
  {
    return educationalQualification;
  }

  public void setEducationalQualification(String educationalQualification)
  {
    this.educationalQualification = educationalQualification;
  }

  public String getrAddress()
  {
    return rAddress;
  }

  public void setrAddress(String rAddress)
  {
    this.rAddress = rAddress;
  }

  public String getrCity()
  {
    return rCity;
  }

  public void setrCity(String rCity)
  {
    this.rCity = rCity;
  }

  public String getrState()
  {
    return rState;
  }

  public void setrState(String rState)
  {
    this.rState = rState;
  }

  public String getrZipCode()
  {
    return rZipCode;
  }

  public void setrZipCode(String rZipCode)
  {
    this.rZipCode = rZipCode;
  }

  public String getrPhone()
  {
    return rPhone;
  }

  public void setrPhone(String rPhone)
  {
    this.rPhone = rPhone;
  }

  public String getoAddress()
  {
    return oAddress;
  }

  public void setoAddress(String oAddress)
  {
    this.oAddress = oAddress;
  }

  public String getoCity()
  {
    return oCity;
  }

  public void setoCity(String oCity)
  {
    this.oCity = oCity;
  }

  public String getoState()
  {
    return oState;
  }

  public void setoState(String oState)
  {
    this.oState = oState;
  }

  public String getoZipCode()
  {
    return oZipCode;
  }

  public void setoZipCode(String oZipCode)
  {
    this.oZipCode = oZipCode;
  }

  public String getoPhone()
  {
    return oPhone;
  }

  public void setoPhone(String oPhone)
  {
    this.oPhone = oPhone;
  }

  public String validate()
  {
    if (StringUtils.isEmpty(this.admissionNumber))
    {
      return "Admission Number can't be empty.(Parent)";
    }
    if (StringUtils.isEmpty(this.firstName))
    {
      return "First Name can't be empty.(Parent)";
    }
    if (!StringUtils.isEmpty(this.firstName) && this.firstName.length() > 20)
    {
      return "First Name can't have length more than 20.(Parent)";
    }
    if (!StringUtils.isEmpty(this.lastName) && this.lastName.length() > 20)
    {
      return "Last Name can't have length more than 20.(Parent)";
    }
    if (StringUtils.isEmpty(this.mobile) || this.mobile.length() > 10)
    {
      return "Mobile number not provided or length greater than 10.(Parent)";
    }
    if (!StringUtils.isEmpty(this.secondaryNumbers) && this.secondaryNumbers.length() > 50)
    {
      return "Too long to handle.(Parent)";
    }
    if (!StringUtils.isEmpty(this.email) && this.email.length() > 50)
    {
      return "Email length is greater than 50.(Parent)";
    }
    if (StringUtils.isEmpty(this.relationship) || !Relationship.matches(this.relationship))
    {
      return "Relationship mismatch.(Parent)";
    }
    if (StringUtils.isEmpty(this.rAddress))
    {
      return "Residence address Line is necessary.(Parent)";
    }
    if (StringUtils.isEmpty(this.rCity))
    {
      return "City in Residence address is necessary.(Parent)";
    }
    if (StringUtils.isEmpty(this.rState))
    {
      return "State in Residence address is necessary.(Parent)";
    }
    if (StringUtils.isEmpty(this.rZipCode))
    {
      return "ZipCode in Residence address is necessary.(Parent)";
    }
    if (StringUtils.isEmpty(this.rPhone))
    {
      return "Phone number on Residence address required.(Parent)";
    }
    return "OK";
  }

}
