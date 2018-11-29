package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.entity.student.StudentProfile;
import com.synlabs.ipsaa.enums.FamilyType;
import com.synlabs.ipsaa.enums.Gender;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.fee.StudentFeeRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeRequestV2;

import java.text.ParseException;
import java.time.*;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class StudentRequest implements Request
{
  private Long   id;
  private String admissionNumber;
  private String firstName;
  private String lastName;
  private String nickName;

  private String admissionDate;
  private String dob;
  private String nationality;
  private String gender;
  private String bloodGroup;
  private String familyType;
  private String profile;

  private Long    programId;
  private Long    centerId;
  private Long    groupId;
  private Boolean active;
  private Long    siblingId;
  private boolean corporate;

  //Avneet
  private boolean formalSchool;
  private String schoolName;

  private StudentFeeRequestV2 fee;


  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date expectedIn;
  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date expectedOut;


  public StudentFeeRequestV2 getFee()
  {
    return fee;
  }

  public void setFee(StudentFeeRequestV2 fee)
  {
    this.fee = fee;
  }

  public boolean isCorporate()
  {
    return corporate;
  }

  public void setCorporate(boolean corporate)
  {
    this.corporate = corporate;
  }

  private List<ParentRequest> parents;

  public Long getSiblingId()
  {
    return unmask(siblingId);
  }

  public Long getMaskedSiblingId()
  {
    return siblingId;
  }

  public void setSiblingId(Long siblingId)
  {
    this.siblingId = siblingId;
  }

  public Long getMaskedId()
  {
    return id;
  }

  public Date getExpectedIn()
  {
    return expectedIn;
  }

  public void setExpectedIn(Date expectedIn)
  {
    this.expectedIn = expectedIn;
  }

  public Date getExpectedOut()
  {
    return expectedOut;
  }

  public void setExpectedOut(Date expectedOut)
  {
    this.expectedOut = expectedOut;
  }

  public Boolean getActive()
  {
    return active;
  }

  public void setActive(Boolean active)
  {
    this.active = active;
  }

  public String getAdmissionNumber()
  {
    return admissionNumber;
  }

  public void setAdmissionNumber(String admissionNumber)
  {
    this.admissionNumber = admissionNumber;
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

  public String getNickName()
  {
    return nickName;
  }

  public void setNickName(String nickName)
  {
    this.nickName = nickName;
  }

  public String getAdmissionDate()
  {
    return admissionDate;
  }

  public void setAdmissionDate(String admissionDate)
  {
    this.admissionDate = admissionDate;
  }

  public String getDob()
  {
    return dob;
  }

  public void setDob(String dob)
  {
    this.dob = dob;
  }

  public String getNationality()
  {
    return nationality;
  }

  public void setNationality(String nationality)
  {
    this.nationality = nationality;
  }

  public String getGender()
  {
    return gender;
  }

  public void setGender(String gender)
  {
    this.gender = gender;
  }

  public String getBloodGroup()
  {
    return bloodGroup;
  }

  public void setBloodGroup(String bloodGroup)
  {
    this.bloodGroup = bloodGroup;
  }

  public String getFamilyType()
  {
    return familyType;
  }

  public void setFamilyType(String familyType)
  {
    this.familyType = familyType;
  }

  public String getProfile()
  {
    return profile;
  }

  public void setProfile(String profile)
  {
    this.profile = profile;
  }

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId(Long programId)
  {
    this.programId = unmask(programId);
  }

  public Long getCenterId()
  {
    return centerId;
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = unmask(centerId);
  }

  public Long getGroupId()
  {
    return groupId;
  }

  public void setGroupId(Long groupId)
  {
    this.groupId = unmask(groupId);
  }

  public List<ParentRequest> getParents()
  {
    return parents;
  }

  public void setParents(List<ParentRequest> parents)
  {
    this.parents = parents;
  }

  public boolean isFormalSchool() { return formalSchool; }

  public void setFormalSchool(boolean formalSchool) { this.formalSchool = formalSchool; }

  public String getSchoolName() { return schoolName; }

  public void setSchoolName(String schoolName) { this.schoolName = schoolName;  }

  public StudentRequest()
  {
  }

  public StudentRequest(Long id)
  {
    this.id = id;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Student toEntity(Student student) throws ParseException
  {
    Instant instant = Instant.ofEpochMilli(expectedIn.getTime());
    LocalTime expIn = LocalDateTime.ofInstant(instant,ZoneId.systemDefault()).toLocalTime();
    instant = Instant.ofEpochMilli(expectedOut.getTime());

    if(expectedIn.after(expectedOut)){
      throw new ValidationException("please enter expected in/out in 24 hours system");
    }
    if(expIn.getHour()<7){
      throw new ValidationException("expected in can not be less then 7");
    }

    student.setExpectedIn(expectedIn);
    student.setExpectedOut(expectedOut);

    student.setActive(this.active);

    StudentProfile studentProfile = student.isNew() ? new StudentProfile() : student.getProfile();
    studentProfile.setProfile(getProfile());
    studentProfile.setBloodGroup(getBloodGroup());
    studentProfile.setFirstName(getFirstName());
    studentProfile.setLastName(getLastName());
    studentProfile.setNationality(getNationality());
    studentProfile.setNickName(getNickName());
    studentProfile.setGender(Gender.valueOf(getGender()));
    student.setCorporate(isCorporate());
    student.setFormalSchool(isFormalSchool());

    if(student.isFormalSchool()){
      if(getSchoolName()!=null)
        student.setSchoolName(getSchoolName());
      else
        throw new ValidationException("Please Add School name");
    }

    studentProfile.setDob(parseDate(getDob()));
    studentProfile.setAdmissionDate(parseDate(getAdmissionDate()));

    studentProfile.setFamilyType(FamilyType.valueOf(getFamilyType()));

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parseDate(getAdmissionDate()));
    studentProfile.setYear(calendar.get(Calendar.YEAR));

//  TODO : Workon ImagePath
//  studentProfile.setImagePath();

    student.setProfile(studentProfile);

    if (student.isNew())
    {
      student.setParents(new ArrayList<>());
      if (parents != null)
      {
        for (ParentRequest parentRequest : parents)
        {
          StudentParent studentParent = parentRequest.toEntity();
          student.getParents().add(studentParent);
        }
      }
    }

    return student;
  }

  public Student toEntity() throws ParseException
  {
    return toEntity(new Student());
  }

  @Override
  public String toString()
  {
    return "StudentRequest{" +
        "id=" + id +
        ", admissionNumber='" + admissionNumber + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", nickName='" + nickName + '\'' +
        ", admissionDate='" + admissionDate + '\'' +
        ", dob='" + dob + '\'' +
        ", nationality='" + nationality + '\'' +
        ", gender='" + gender + '\'' +
        ", bloodGroup='" + bloodGroup + '\'' +
        ", familyType='" + familyType + '\'' +
        ", profile='" + profile + '\'' +
        ", programId='" + programId + '\'' +
        ", centerId='" + centerId + '\'' +
        ", groupId='" + groupId + '\'' +
        ", parents=" + parents +
        '}';
  }

}
