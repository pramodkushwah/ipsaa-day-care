package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.common.AddressResponse;

public class ParentResponse implements Response
{
  private Long id;
  private boolean emergencyContact;
  private String firstName;
  private String lastName;
  private String fullName;
  private String mobile;
  private String secondaryNumbers;
  private String email;
  private String relationship;
  private String educationalQualification;
  private String occupation;
  private String organisation;
  private String designation;
  private boolean account;
  private boolean smsEnabled = false;
  private boolean emailEnabled = false;

  private AddressResponse residentialAddress;
  private AddressResponse officeAddress;


  public ParentResponse(StudentParent parent)
  {
    this.id=mask(parent.getId());
    this.emergencyContact = parent.isEmergencyContact();
    this.fullName = parent.getFullName();
    this.firstName = parent.getFirstName();
    this.lastName = parent.getLastName();
    this.mobile = parent.getMobile();
    this.secondaryNumbers = parent.getSecondaryNumbers();
    this.email = parent.getEmail();
    this.relationship = parent.getRelationship().name();
    this.educationalQualification = parent.getEducationalQualification();
    this.occupation = parent.getOccupation();
    this.organisation = parent.getOrganisation();
    this.designation = parent.getDesignation();
    this.residentialAddress = new AddressResponse(parent.getResidentialAddress());
    this.officeAddress = parent.getOfficeAddress() == null ? null : new AddressResponse(parent.getOfficeAddress());
    this.account= parent.isAccount();
    this.smsEnabled=parent.isSmsEnabled();
    this.emailEnabled=parent.isEmailEnabled();
  }

  public boolean isSmsEnabled()
  {
    return smsEnabled;
  }

  public boolean isEmailEnabled()
  {
    return emailEnabled;
  }

  public boolean isAccount()
  {
    return account;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public boolean isEmergencyContact()
  {
    return emergencyContact;
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getMobile()
  {
    return mobile;
  }

  public String getSecondaryNumbers()
  {
    return secondaryNumbers;
  }

  public String getEmail()
  {
    return email;
  }

  public String getRelationship()
  {
    return relationship;
  }

  public String getEducationalQualification()
  {
    return educationalQualification;
  }

  public String getOccupation()
  {
    return occupation;
  }

  public String getOrganisation()
  {
    return organisation;
  }

  public String getDesignation()
  {
    return designation;
  }

  public AddressResponse getResidentialAddress()
  {
    return residentialAddress;
  }

  public AddressResponse getOfficeAddress()
  {
    return officeAddress;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }
}
