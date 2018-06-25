package com.synlabs.ipsaa.entity.student;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.FamilyType;
import com.synlabs.ipsaa.enums.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class StudentProfile extends BaseEntity
{
  //year of admission
  private int year;

  @Temporal(TemporalType.DATE)
  private Date admissionDate;

  @Column(length = 200)
  private String imagePath;

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(length = 50)
  private String lastName;

  @Column(length = 20)
  private String nickName;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date dob;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Gender gender;

  @Column(length = 20)
  private String nationality;

  @Column(length = 20)
  private String bloodGroup;

  @Column(nullable = false, length = 1000)
  //few words about the child
  private String profile;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private FamilyType familyType;

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

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

  public Date getAdmissionDate()
  {
    return admissionDate;
  }

  public void setAdmissionDate(Date admissionDate)
  {
    this.admissionDate = admissionDate;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
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

  public Gender getGender()
  {
    return gender;
  }

  public void setGender(Gender gender)
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

  public FamilyType getFamilyType()
  {
    return familyType;
  }

  public void setFamilyType(FamilyType familyType)
  {
    this.familyType = familyType;
  }

  @Transient
  public String getFullName()
  {
    return (firstName + (lastName==null?"":" "+lastName)).trim();
  }

  @Transient
  public String getGenderType()
  {
    return this.gender != null ? this.gender.name() : "";
  }

  @Transient
  public String getFamily()
  {
    return this.familyType != null ? this.familyType.name() : "";
  }

  @Transient
  public String getDateOfBirth()
  {
    String result = "";
    if (this.dob != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      result = sdf.format(this.dob);
    }
    return result;
  }

  @Transient
  public String getAdmDate()
  {
    String result = "";
    if (this.admissionDate != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      result = sdf.format(this.admissionDate);
    }
    return result;
  }

  @Override
  public String toString()
  {
    return "StudentProfile{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        '}';
  }
}
