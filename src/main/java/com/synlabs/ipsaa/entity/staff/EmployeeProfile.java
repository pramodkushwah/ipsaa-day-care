package com.synlabs.ipsaa.entity.staff;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.Gender;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class EmployeeProfile extends BaseEntity
{

  @Column(length = 200)
  private String imagePath;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date doj;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date dob;

  @Temporal(TemporalType.DATE)
  private Date dol;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Gender gender;

  @Embedded
  private Address address;

  @Embedded
  private Address permanentAddress;

  @Column(length = 20)
  private String pan;

  @Column(length = 20)
  //Universal Account Number
  private String uan;

  @Column(length = 20)
  //PF Account Number
  private String pfan;

  @Column(length = 20)
  //ESI Number
  private String esin;

  @Column(length = 20)
  //PR Account Number
  private String pran;

  @Column(length = 20)
  //Bank Account Number
  private String ban;
  // shubham
  private String  ifscCode;
  private String bankName;
  private String branchName;
  private String holderName;
  //
  public String getBan()
  {
    return ban;
  }

  public void setBan(String ban)
  {
    this.ban = ban;
  }

  public String getPan()
  {
    return pan;
  }

  public void setPan(String pan)
  {
    this.pan = pan;
  }

  public String getUan()
  {
    return uan;
  }

  public void setUan(String uan)
  {
    this.uan = uan;
  }

  public String getPfan()
  {
    return pfan;
  }

  public void setPfan(String pfan)
  {
    this.pfan = pfan;
  }

  public String getEsin()
  {
    return esin;
  }

  public void setEsin(String esin)
  {
    this.esin = esin;
  }

  public String getPran()
  {
    return pran;
  }

  public void setPran(String pran)
  {
    this.pran = pran;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }

  public Date getDoj()
  {
    return doj;
  }

  public void setDoj(Date doj)
  {
    this.doj = doj;
  }

  public Date getDob()
  {
    return dob;
  }

  public void setDob(Date dob)
  {
    this.dob = dob;
  }

  public Date getDol()
  {
    return dol;
  }

  public void setDol(Date dol)
  {
    this.dol = dol;
  }

  public Gender getGender()
  {
    return gender;
  }

  public void setGender(Gender gender)
  {
    this.gender = gender;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }

  public Address getPermanentAddress()
  {
    return permanentAddress;
  }

  public void setPermanentAddress(Address permanentAddress)
  {
    this.permanentAddress = permanentAddress;
  }

  @Transient
  public String getGenderType()
  {
    return this.gender != null ? this.gender.name() : "";
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
  public String getDateOfJoining()
  {
    String result = "";
    if (this.doj != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      result = sdf.format(this.doj);
    }
    return result;
  }

  @Transient
  public String getDateOfLeaving()
  {
    String result = "";
    if (this.dol != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      result = sdf.format(this.dol);
    }
    return result;
  }

  @Transient
  public Address getCAddress()
  {
    return this.address == null ? new Address() : this.address;
  }

  @Transient
  public Address getPAddress()
  {
    return this.permanentAddress == null ? new Address() : this.permanentAddress;
  }
// shubham

  public String getIfscCode() {
    return ifscCode;
  }
  public void setIfscCode(String ifscCode) {
    this.ifscCode = ifscCode;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public String getHolderName() {
    return holderName;
  }

  public void setHolderName(String holderName) {
    this.holderName = holderName;
  }
  // shubh
}


//TODO question
//what all information do we need to capture for a employee

// Wife/Daugher of
// religion
//  Marital Status
//  Qualification
//
