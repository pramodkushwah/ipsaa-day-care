package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.EmployeeProfile;
import com.synlabs.ipsaa.enums.Gender;
import com.synlabs.ipsaa.view.common.AddressResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

/**
 * Created by itrs on 4/13/2017.
 */
public class StaffProfileResponse implements Response
{
  private String imagePath;
  private Gender gender;

  private Date doj;
  private Date dob;
  private Date dol;

  private AddressResponse address;
  private AddressResponse permanentAddress;

  private String pan;
  private String uan;
  private String pfan;
  private String esin;
  private String pran;
  private String ban;
  // shubham
  private String  ifscCode;
  private String bankName;
  private String branchName;
  private String holderName;

  ///Avneet
  /////Avneet
  private String fatherName;
  private String spouseName;
  private String pState;




  public StaffProfileResponse(EmployeeProfile profile)
  {
    this.imagePath = profile.getImagePath();
    this.doj = profile.getDoj();
    this.dob = profile.getDob();
    this.dol = profile.getDol();
    this.gender = profile.getGender();
    this.address = new AddressResponse(profile.getAddress());
    this.permanentAddress = new AddressResponse(profile.getPermanentAddress());

    this.pan = profile.getPan();
    this.uan = profile.getUan();
    this.pfan = profile.getPfan();
    this.esin = profile.getEsin();
    this.pran = profile.getPran();
    this.ban = profile.getBan();
    // shubham
    this.ifscCode=profile.getIfscCode();
    this.bankName=profile.getBankName();
    this.branchName=profile.getBranchName();
    this.holderName=profile.getHolderName();

    //////Avneet
    this.spouseName=profile.getspouseName();
    this.fatherName=profile.getFatherName();
    this.pState=profile.getpState();
  }

  // shubham
  public String getIfscCode() {
    return ifscCode;
  }

  public String getBankName() {
    return bankName;
  }

  public String getBranchName() {
    return branchName;
  }
  public String getHolderName() {
    return holderName;
  }

  public String getBan()
  {
    return ban;
  }

  public String getPan()
  {
    return pan;
  }

  public String getUan()
  {
    return uan;
  }

  public String getPfan()
  {
    return pfan;
  }

  public String getEsin()
  {
    return esin;
  }

  public String getPran()
  {
    return pran;
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

  public AddressResponse getAddress()
  {
    return address;
  }

  public void setAddress(AddressResponse address)
  {
    this.address = address;
  }

  public AddressResponse getPermanentAddress()
  {
    return permanentAddress;
  }

  public void setPermanentAddress(AddressResponse permanentAddress)
  {
    this.permanentAddress = permanentAddress;
  }

    public String getFatherName() { return fatherName; }

    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getspouseName() { return spouseName; }

    public void setspouseName(String spouseName) { this.spouseName = spouseName; }

  public String getpState() { return pState; }

  public void setpState(String pState) { this.pState = pState; }
}
