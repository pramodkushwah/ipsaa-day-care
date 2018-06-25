package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.enums.Relationship;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.common.AddressRequest;

/**
 * Created by itrs on 4/4/2017.
 */
public class ParentRequest implements Request
{
  private Long           id;
  private String         firstName;
  private String         lastName;
  private String         mobile;
  private String         email;
  private String         educationalQualification;
  private String         occupation;
  private String         designation;
  private String         organisation;
  private AddressRequest residentialAddress;
  private AddressRequest officeAddress;
  private String         secondaryNumbers;
  private String         relationship;
  private boolean account=false;
  private boolean smsEnabled;
  private boolean emailEnabled;

  public boolean isSmsEnabled()
  {
    return smsEnabled;
  }

  public void setSmsEnabled(boolean smsEnabled)
  {
    this.smsEnabled = smsEnabled;
  }

  public boolean isEmailEnabled()
  {
    return emailEnabled;
  }

  public void setEmailEnabled(boolean emailEnabled)
  {
    this.emailEnabled = emailEnabled;
  }

  public boolean isAccount()
  {
    return account;
  }

  public void setAccount(boolean account)
  {
    this.account = account;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public Long getMaskedId(){
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getRelationship()
  {
    return relationship;
  }

  public void setRelationship(String relationship)
  {
    this.relationship = relationship;
  }

  public String getSecondaryNumbers()
  {
    return secondaryNumbers;
  }

  public void setSecondaryNumbers(String secondaryNumbers)
  {
    this.secondaryNumbers = secondaryNumbers;
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

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getEducationalQualification()
  {
    return educationalQualification;
  }

  public void setEducationalQualification(String educationalQualification)
  {
    this.educationalQualification = educationalQualification;
  }

  public String getOccupation()
  {
    return occupation;
  }

  public void setOccupation(String occupation)
  {
    this.occupation = occupation;
  }

  public String getDesignation()
  {
    return designation;
  }

  public void setDesignation(String designation)
  {
    this.designation = designation;
  }

  public String getOrganisation()
  {
    return organisation;
  }

  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  public AddressRequest getResidentialAddress()
  {
    return residentialAddress;
  }

  public void setResidentialAddress(AddressRequest residentialAddress)
  {
    this.residentialAddress = residentialAddress;
  }

  public AddressRequest getOfficeAddress()
  {
    return officeAddress;
  }

  public void setOfficeAddress(AddressRequest officeAddress)
  {
    this.officeAddress = officeAddress;
  }

  public StudentParent toEntity()
  {
    return toEntity(new StudentParent());
  }

  public StudentParent toEntity(StudentParent parent)
  {
    if (parent == null)
    {
      parent = new StudentParent();
    }
    parent.setSmsEnabled(smsEnabled);
    parent.setEmailEnabled(emailEnabled);
    parent.setFirstName(firstName);
    parent.setLastName(lastName);
    parent.setMobile(mobile);
    parent.setSecondaryNumbers(secondaryNumbers);
    parent.setEmail(email);
    parent.setEducationalQualification(educationalQualification);
    parent.setOccupation(occupation);
    parent.setOrganisation(organisation);
    parent.setDesignation(designation);
    parent.setRelationship(Relationship.valueOf(relationship));
    parent.setAccount(account);
    if (officeAddress != null)
    {
      Address officeAddressEntity = officeAddress.toEntity();
      officeAddressEntity.setAddressType(AddressType.Office);
      parent.setOfficeAddress(officeAddressEntity);
    }
    if (residentialAddress != null)
    {
      Address residentialAddressEntity = residentialAddress.toEntity();
      residentialAddressEntity.setAddressType(AddressType.Home);
      parent.setResidentialAddress(residentialAddressEntity);
    }
    return parent;
  }

  @Override
  public String toString()
  {
    return "ParentRequest{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", mobile='" + mobile + '\'' +
        ", email='" + email + '\'' +
        ", educationalQualification='" + educationalQualification + '\'' +
        ", occupation='" + occupation + '\'' +
        ", designation='" + designation + '\'' +
        ", organisation='" + organisation + '\'' +
        ", residentialAddress=" + residentialAddress +
        ", officeAddress=" + officeAddress +
        ", secondaryNumbers='" + secondaryNumbers + '\'' +
        ", relationship='" + relationship + '\'' +
        '}';
  }
}
