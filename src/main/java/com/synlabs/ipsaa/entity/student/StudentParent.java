package com.synlabs.ipsaa.entity.student;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.Relationship;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
public class StudentParent extends BaseEntity
{

  private boolean emergencyContact;

  private boolean smsEnabled = false;

  private boolean emailEnabled = false;

  //TODO can be multiple kids in school!!
  @ManyToMany(fetch = FetchType.LAZY,mappedBy = "parents")
  private List<Student> students=new ArrayList<>();

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(length = 50)
  private String lastName;

  @Column(nullable = false, length = 20)
  private String mobile;

  @Column(length = 50)
  private String secondaryNumbers;

  @Column(length = 50)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Relationship relationship;

  @Column(length = 200)
  private String educationalQualification;

  @Column(length = 50)
  private String occupation;

  @Column(length = 50)
  private String organisation;

  @Column(length = 50)
  private String designation;

  @Embedded
  private Address residentialAddress;

  @Embedded
  private Address officeAddress;

  @Column(nullable = false)
  private boolean account = false;

  public StudentParent()
  {
  }

  public boolean isAccount()
  {
    return account;
  }

  public void setAccount(boolean account)
  {
    this.account = account;
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

  public List<Student> getStudents()
  {
    return students;
  }

  public void setStudents(List<Student> students)
  {
    this.students = students;
  }

  public boolean isEmergencyContact()
  {
    return emergencyContact;
  }

  public void setEmergencyContact(boolean emergencyContact)
  {
    this.emergencyContact = emergencyContact;
  }

  public Relationship getRelationship()
  {
    return relationship;
  }

  public void setRelationship(Relationship relationship)
  {
    this.relationship = relationship;
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

  public Address getResidentialAddress()
  {
    return residentialAddress;
  }

  public void setResidentialAddress(Address residentialAddress)
  {
    this.residentialAddress = residentialAddress;
  }

  public Address getOfficeAddress()
  {
    return officeAddress;
  }

  public void setOfficeAddress(Address officeAddress)
  {
    this.officeAddress = officeAddress;
  }

  public String getSecondaryNumbers()
  {
    return secondaryNumbers;
  }

  public void setSecondaryNumbers(String secondaryNumbers)
  {
    this.secondaryNumbers = secondaryNumbers;
  }

  @Transient
  public String getFullName()
  {
    return (firstName + (lastName == null ? "" : " " + lastName)).trim();
  }

  @Transient
  public Address getOAddress()
  {
    return this.officeAddress == null ? new Address() : this.officeAddress;
  }

  @Transient
  public Address getRAddress()
  {
    return this.residentialAddress == null ? new Address() : this.residentialAddress;
  }

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

  public boolean hasStudent(Student student){
    return (new HashSet<>(getStudents())).contains(student);
  }
}

