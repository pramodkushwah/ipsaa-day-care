package com.synlabs.ipsaa.entity.inquiry;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.enums.InquiryType;
import com.synlabs.ipsaa.enums.LeadSource;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sushil on 10/07/2017.
 */
@Entity
public class Inquiry extends BaseEntity
{
  @Column(length = 50, nullable = false, unique = true)
  private String inquiryNumber;

  @ManyToOne
  private Center center;

  @ManyToOne
  private ProgramGroup group;

  @ManyToOne
  private Program program;

  @Column(length = 50)
  private String firstName;

  @Column(length = 50)
  private String lastName;

  @Temporal(TemporalType.DATE)
  private Date dob;

  @Column(nullable = false, length = 50)
  private String fatherFirstName;

  @Column(length = 50)
  private String fatherLastName;

  @Column(length = 50)
  private String fatherCompanyName;

  @Column(nullable = false, length = 20)
  private String fatherMobile;

  @Column(length = 50)
  private String fatherEmail;

  @Column(nullable = false, length = 50)
  private String motherFirstName;

  @Column(length = 50)
  private String motherLastName;

  @Column(length = 50)
  private String motherCompanyName;

  @Column(nullable = false, length = 20)
  private String motherMobile;

  @Column(length = 50)
  private String motherEmail;

  @Column(length = 50)
  private String secondaryNumbers;

  @Column(length = 50)
  private String whoVisited;

  @Column(length = 20)
  private String fromTime;

  @Column(length = 20)
  private String toTime;

  @Column(length = 200)
  private String feeOffer;

  @Column(length = 100)
  private String hobbies;

  @Temporal(TemporalType.DATE)
  private Date inquiryDate;

  @Enumerated(EnumType.STRING)
  private CallDisposition status;

  @Embedded
  private Address residentialAddress;

  @Enumerated(EnumType.STRING)
  private LeadSource leadSource;

  @Enumerated(EnumType.STRING)
  private InquiryType inquiryType;

  @OneToMany(mappedBy = "inquiry")
  private List<InquiryEventLog> logs=new ArrayList<>();

  public Date getInquiryDate()
  {
    return inquiryDate;
  }

  public void setInquiryDate(Date inquiryDate)
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

  public List<InquiryEventLog> getLogs()
  {
    return logs;
  }

  public void setLogs(List<InquiryEventLog> logs)
  {
    this.logs = logs;
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

  public Date getDob()
  {
    return dob;
  }

  public void setDob(Date dob)
  {
    this.dob = dob;
  }

  public String getInquiryNumber()
  {
    return inquiryNumber;
  }

  public void setInquiryNumber(String inquiryNumber)
  {
    this.inquiryNumber = inquiryNumber;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public ProgramGroup getGroup()
  {
    return group;
  }

  public void setGroup(ProgramGroup group)
  {
    this.group = group;
  }

  public Program getProgram()
  {
    return program;
  }

  public void setProgram(Program program)
  {
    this.program = program;
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

  public Address getResidentialAddress()
  {
    return residentialAddress;
  }

  public void setResidentialAddress(Address residentialAddress)
  {
    this.residentialAddress = residentialAddress;
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

  @Transient
  public String getChildName(){
    return getFirstName()+" "+getLastName();
  }

  @Transient
  public String getFatherName()
  {
    return fatherFirstName + " " + fatherLastName;
  }

  @Transient
  public String getMotherName()
  {
    return motherFirstName + " " + motherLastName;
  }

  @Transient
  public InquiryEventLog getFirstLog()
  {
    InquiryEventLog result = null;
    for (InquiryEventLog log : logs)
    {
      if (result == null)
      {
        result = log;
      }
      else if (log.getCreatedDate().isAfter(result.getCreatedDate()))
      {
        result = log;
      }
    }
    return result;
  }

}
