package com.synlabs.ipsaa.view.inquiry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.view.common.AddressResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InquiryResponse implements Response
{

  private Long            id;
  private String          inquiryNumber;
  private String          centerName;
  private String          centerCode;
  private Long            centerId;
  private String          programName;
  private String          programCode;
  private Long            programId;
  private String          childFirstName;
  private String          childLastName;
  @JsonFormat(pattern="yyyy-MM-dd")
  private Date            childDob;
  private Long            groupId;
  private String          groupName;
  private String          type;
  private String          fatherFirstName;
  private String          fatherLastName;
  private String          fatherCompanyName;
  private String          fatherMobile;
  private String          fatherEmail;
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
  private AddressResponse address;
  private String          leadSource;
  private String          inquiryType;
  private Date            inquiryDate;
  private CallDisposition status;

  private List<InquiryEventLogResponse> logs=new ArrayList<>();

  public InquiryResponse(Inquiry inquiry)
  {
    this.id = mask(inquiry.getId());
    this.inquiryNumber=inquiry.getInquiryNumber();

    this.centerName = inquiry.getCenter() == null ? null : inquiry.getCenter().getName();
    this.centerCode = inquiry.getCenter() == null ? null : inquiry.getCenter().getCode();
    this.centerId = inquiry.getCenter() == null ? null : mask(inquiry.getCenter().getId());

    this.programCode = inquiry.getProgram() == null ? null : inquiry.getProgram().getCode();
    this.programName = inquiry.getProgram() == null ? null : inquiry.getProgram().getName();
    this.programId = inquiry.getProgram() == null ? null : mask(inquiry.getProgram().getId());

    this.groupName = inquiry.getGroup() == null ? null : inquiry.getGroup().getName();
    this.groupId = inquiry.getGroup() == null ? null : mask(inquiry.getGroup().getId());

    this.childFirstName = inquiry.getFirstName();
    this.childLastName = inquiry.getLastName();
    this.childDob = inquiry.getDob();

    this.type = inquiry.getInquiryType().name();
    this.fatherFirstName = inquiry.getFatherFirstName();
    this.fatherLastName = inquiry.getFatherLastName();
    this.fatherCompanyName = inquiry.getFatherCompanyName();
    this.fatherMobile = inquiry.getFatherMobile();
    this.fatherEmail = inquiry.getFatherEmail();

    this.motherFirstName = inquiry.getMotherFirstName();
    this.motherLastName = inquiry.getMotherLastName();
    this.motherCompanyName = inquiry.getMotherCompanyName();
    this.motherMobile = inquiry.getMotherMobile();
    this.motherEmail = inquiry.getMotherEmail();

    this.secondaryNumbers = inquiry.getSecondaryNumbers();
    this.whoVisited = inquiry.getWhoVisited();
    this.fromTime = inquiry.getFromTime();
    this.toTime = inquiry.getToTime();
    this.feeOffer = inquiry.getFeeOffer();
    this.hobbies = inquiry.getHobbies();

    this.address = new AddressResponse(inquiry.getResidentialAddress());

    this.leadSource = inquiry.getLeadSource().name();
    this.inquiryType = inquiry.getInquiryType().name();

    this.status = inquiry.getStatus();
    this.inquiryDate = inquiry.getInquiryDate();

    for (InquiryEventLog eventLog:inquiry.getLogs())
    {
      this.logs.add(new InquiryEventLogResponse(eventLog));
    }
  }

  public Date getInquiryDate()
  {
    return inquiryDate;
  }

  public CallDisposition getStatus()
  {
    return status;
  }

  public Long getCenterId()
  {
    return centerId;
  }

  public Long getProgramId()
  {
    return programId;
  }

  public String getInquiryNumber()
  {
    return inquiryNumber;
  }

  public String getChildFirstName()
  {
    return childFirstName;
  }

  public String getChildLastName()
  {
    return childLastName;
  }

  public Date getChildDob()
  {
    return childDob;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getProgramName()
  {
    return programName;
  }

  public String getProgramCode()
  {
    return programCode;
  }

  public Long getGroupId()
  {
    return groupId;
  }

  public List<InquiryEventLogResponse> getLogs()
  {
    return logs;
  }

  public Long getId()
  {
    return id;
  }

  public String getType()
  {
    return type;
  }

  public String getFatherFirstName()
  {
    return fatherFirstName;
  }

  public String getFatherLastName()
  {
    return fatherLastName;
  }

  public String getFatherCompanyName()
  {
    return fatherCompanyName;
  }

  public String getFatherMobile()
  {
    return fatherMobile;
  }

  public String getFatherEmail()
  {
    return fatherEmail;
  }

  public String getMotherFirstName()
  {
    return motherFirstName;
  }

  public String getMotherLastName()
  {
    return motherLastName;
  }

  public String getMotherCompanyName()
  {
    return motherCompanyName;
  }

  public String getMotherMobile()
  {
    return motherMobile;
  }

  public String getMotherEmail()
  {
    return motherEmail;
  }

  public String getSecondaryNumbers()
  {
    return secondaryNumbers;
  }

  public String getWhoVisited()
  {
    return whoVisited;
  }

  public String getFromTime()
  {
    return fromTime;
  }

  public String getToTime()
  {
    return toTime;
  }

  public String getFeeOffer()
  {
    return feeOffer;
  }

  public String getHobbies()
  {
    return hobbies;
  }

  public AddressResponse getAddress()
  {
    return address;
  }

  public String getLeadSource()
  {
    return leadSource;
  }

  public String getInquiryType()
  {
    return inquiryType;
  }
}
