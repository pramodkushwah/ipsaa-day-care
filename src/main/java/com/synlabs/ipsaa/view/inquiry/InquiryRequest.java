package com.synlabs.ipsaa.view.inquiry;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.enums.InquiryType;
import com.synlabs.ipsaa.enums.LeadSource;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.view.common.AddressRequest;
import com.synlabs.ipsaa.view.common.Request;

import java.text.ParseException;

public class InquiryRequest implements Request
{
  private Long            id;
  private Long            centerId;
  private Long            programId;
  private Long            groupId;
  private String          inquiryNumber;
  private String          childFirstName;
  private String         childLastName;
  private String         childDob;
  private String         fatherFirstName;
  private String         fatherLastName;
  private String         fatherCompanyName;
  private String         fatherMobile;
  private String         fatherEmail;
  private String          motherFirstName;
  private String          motherLastName;
  private String          motherCompanyName;
  private String          motherMobile;
  private String          motherEmail;
  private String          secondaryNumbers;
  private String          whoVisited;
  private String          fromTime;
  private String          toTime;
  private String          feeOffer;
  private String          hobbies;
  private AddressRequest  address;
  private LeadSource      leadSource;
  private InquiryType     inquiryType;
  private String          inquiryDate;
  private CallDisposition status;

  private InquiryEventLogRequest log;

  public Inquiry toEntity(Inquiry inquiry)
  {
    if (inquiry == null)
    {
      inquiry = new Inquiry();
    }
    inquiry.setFirstName(childFirstName);
    inquiry.setLastName(childLastName);
    try
    {
      inquiry.setDob(parseDate(childDob));
    }
    catch (ParseException e)
    {
      throw new ValidationException("Invalid child dob.");
    }
    inquiry.setInquiryNumber(inquiryNumber);
    inquiry.setFatherFirstName(fatherFirstName);
    inquiry.setFatherLastName(fatherLastName);
    inquiry.setFatherCompanyName(fatherCompanyName);
    inquiry.setFatherMobile(fatherMobile);
    inquiry.setFatherEmail(fatherEmail);
    inquiry.setMotherFirstName(motherFirstName);
    inquiry.setMotherLastName(motherLastName);
    inquiry.setMotherCompanyName(motherCompanyName);
    inquiry.setMotherMobile(motherMobile);
    inquiry.setMotherEmail(motherEmail);
    inquiry.setSecondaryNumbers(secondaryNumbers);
    inquiry.setWhoVisited(whoVisited);
    inquiry.setFromTime(fromTime);
    inquiry.setToTime(toTime);
    inquiry.setFeeOffer(feeOffer);
    inquiry.setHobbies(hobbies);
    Address address = this.address==null?null:this.address.toEntity();
    if(this.address!=null) address.setAddressType(AddressType.Home);
    inquiry.setResidentialAddress(address);
    inquiry.setLeadSource(leadSource);
    inquiry.setInquiryType(inquiryType);
    try
    {
      inquiry.setInquiryDate(parseDate(inquiryDate));
    }
    catch (ParseException e)
    {
      throw new ValidationException("Invalid Inquiry Date.");
    }
    return inquiry;
  }

  public String getInquiryDate()
  {
    return inquiryDate;
  }

  public void setInquiryDate(String inquiryDate)
  {
    this.inquiryDate = inquiryDate;
  }

  public CallDisposition getStatus()
  {
    return status;
  }

  public void setStatus(CallDisposition status)
  {
    this.status = status;
  }

  public String getChildFirstName()
  {
    return childFirstName;
  }

  public void setChildFirstName(String childFirstName)
  {
    this.childFirstName = childFirstName;
  }

  public String getChildLastName()
  {
    return childLastName;
  }

  public void setChildLastName(String childLastName)
  {
    this.childLastName = childLastName;
  }

  public String getChildDob()
  {
    return childDob;
  }

  public void setChildDob(String childDob)
  {
    this.childDob = childDob;
  }

  public InquiryEventLogRequest getLog()
  {
    return log;
  }

  public void setLog(InquiryEventLogRequest log)
  {
    this.log = log;
  }

  public Long getUnmaskedId()
  {
    return unmask(id);
  }

  public Long getUnmaskedCetnerId()
  {
    return unmask(centerId);
  }

  public Long getUnmaskedProgramId()
  {
    return unmask(programId);
  }

  public Long getUnmaskedGroupId()
  {
    return unmask(groupId);
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getCenterId()
  {
    return centerId;
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId(Long programId)
  {
    this.programId = programId;
  }

  public Long getGroupId()
  {
    return groupId;
  }

  public void setGroupId(Long groupId)
  {
    this.groupId = groupId;
  }

  public String getInquiryNumber()
  {
    return inquiryNumber;
  }

  public void setInquiryNumber(String inquiryNumber)
  {
    this.inquiryNumber = inquiryNumber;
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

  public String getFatherCompanyName()
  {
    return fatherCompanyName;
  }

  public void setFatherCompanyName(String fatherCompanyName)
  {
    this.fatherCompanyName = fatherCompanyName;
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

  public String getMotherCompanyName()
  {
    return motherCompanyName;
  }

  public void setMotherCompanyName(String motherCompanyName)
  {
    this.motherCompanyName = motherCompanyName;
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

  public String getSecondaryNumbers()
  {
    return secondaryNumbers;
  }

  public void setSecondaryNumbers(String secondaryNumbers)
  {
    this.secondaryNumbers = secondaryNumbers;
  }

  public String getWhoVisited()
  {
    return whoVisited;
  }

  public void setWhoVisited(String whoVisited)
  {
    this.whoVisited = whoVisited;
  }

  public String getFromTime()
  {
    return fromTime;
  }

  public void setFromTime(String fromTime)
  {
    this.fromTime = fromTime;
  }

  public String getToTime()
  {
    return toTime;
  }

  public void setToTime(String toTime)
  {
    this.toTime = toTime;
  }

  public String getFeeOffer()
  {
    return feeOffer;
  }

  public void setFeeOffer(String feeOffer)
  {
    this.feeOffer = feeOffer;
  }

  public String getHobbies()
  {
    return hobbies;
  }

  public void setHobbies(String hobbies)
  {
    this.hobbies = hobbies;
  }

  public AddressRequest getAddress()
  {
    return address;
  }

  public void setAddress(AddressRequest address)
  {
    this.address = address;
  }

  public LeadSource getLeadSource()
  {
    return leadSource;
  }

  public void setLeadSource(LeadSource leadSource)
  {
    this.leadSource = leadSource;
  }

  public InquiryType getInquiryType()
  {
    return inquiryType;
  }

  public void setInquiryType(InquiryType inquiryType)
  {
    this.inquiryType = inquiryType;
  }
}
