package com.synlabs.ipsaa.view.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.view.center.CenterSummaryResponse;
import com.synlabs.ipsaa.view.center.ProgramGroupResponse;
import com.synlabs.ipsaa.view.center.ProgramResponse;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.fee.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentResponse implements Response
{

  private Long id;

  private Boolean active;
  private String  fullName;
  private String  firstName;
  private String  lastName;

  private String admissionNumber;

  private CenterSummaryResponse center;

  private ProgramResponse program;

  private ProgramGroupResponse group;

  private String admissionDate;
  private String imagePath;
  private String nickName;
  private String dob;
  private String gender;
  private String nationality;
  private String bloodGroup;

  private String profile;
  private String familyType;

  private boolean corporate;

  private List<ParentResponse> parents;
  private String               studentImageData;
  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date                 expectedIn;
  @JsonFormat(pattern = "HH:mm", timezone = "IST")
  private Date                 expectedOut;

  private ApprovalStatus approvalStatus;

  /////Avneet
  private boolean formalSchool;
  private String schoolName;
private StudentFeeResponse fee;

public  StudentResponse(StudentFee fee){
  Student student =fee.getStudent();

  this.fee=new StudentFeeResponse(fee);

  this.id = mask(student.getId());
  this.active = student.isActive();
  this.admissionNumber = student.getAdmissionNumber();
  this.center = new CenterSummaryResponse(student.getCenter());
  this.program = new ProgramResponse(student.getProgram());
  this.group = new ProgramGroupResponse(student.getGroup());
  this.fullName = student.getProfile().getFullName();
  this.firstName = student.getProfile().getFirstName();
  this.lastName = student.getProfile().getLastName();
  this.admissionDate = student.getProfile().getAdmissionDate().toString();
  this.imagePath = student.getProfile().getImagePath();
  this.nickName = student.getProfile().getNickName();
  this.dob = student.getProfile().getDob().toString();
  this.gender = student.getProfile().getGender().name();
  this.nationality = student.getProfile().getNationality();
  this.bloodGroup = student.getProfile().getBloodGroup();
  this.profile = student.getProfile().getProfile();
  this.familyType = student.getProfile().getFamilyType().name();
  this.expectedIn = student.getExpectedIn();
  this.expectedOut = student.getExpectedOut();
  this.approvalStatus = student.getApprovalStatus();
  this.corporate = student.isCorporate();
  this.formalSchool=student.isFormalSchool();
  this.schoolName=student.getSchoolName();
  if (student.getParents() != null && !student.getParents().isEmpty())
  {
    this.parents = new ArrayList<>(student.getParents().size());
    for (StudentParent parent : student.getParents())
    {
      this.parents.add(new ParentResponse(parent));
    }
  }

}
  public StudentResponse(Student student)
  {
    this.id = mask(student.getId());
    this.active = student.isActive();
    this.admissionNumber = student.getAdmissionNumber();
    this.center = new CenterSummaryResponse(student.getCenter());
    this.program = new ProgramResponse(student.getProgram());
    this.group = new ProgramGroupResponse(student.getGroup());
    this.fullName = student.getProfile().getFullName();
    this.firstName = student.getProfile().getFirstName();
    this.lastName = student.getProfile().getLastName();
    this.admissionDate = student.getProfile().getAdmissionDate().toString();
    this.imagePath = student.getProfile().getImagePath();
    this.nickName = student.getProfile().getNickName();
    this.dob = student.getProfile().getDob().toString();
    this.gender = student.getProfile().getGender().name();
    this.nationality = student.getProfile().getNationality();
    this.bloodGroup = student.getProfile().getBloodGroup();
    this.profile = student.getProfile().getProfile();
    this.familyType = student.getProfile().getFamilyType().name();
    this.expectedIn = student.getExpectedIn();
    this.expectedOut = student.getExpectedOut();
    this.approvalStatus = student.getApprovalStatus();
    this.corporate = student.isCorporate();
    this.formalSchool=student.isFormalSchool();
    this.schoolName=student.getSchoolName();
    if (student.getParents() != null && !student.getParents().isEmpty())
    {
      this.parents = new ArrayList<>(student.getParents().size());
      for (StudentParent parent : student.getParents())
      {
        this.parents.add(new ParentResponse(parent));
      }
    }
  }

  public StudentFeeResponse getFee() {
    return fee;
  }

  public boolean isCorporate()
  {
    return corporate;
  }

  public ApprovalStatus getApprovalStatus()
  {
    return approvalStatus;
  }

  public Boolean getActive()
  {
    return active;
  }

  public Date getExpectedIn()
  {
    return expectedIn;
  }

  public Date getExpectedOut()
  {
    return expectedOut;
  }

  public Long getId()
  {
    return id;
  }

  public String getAdmissionNumber()
  {
    return admissionNumber;
  }

  public CenterSummaryResponse getCenter()
  {
    return center;
  }

  public ProgramResponse getProgram()
  {
    return program;
  }

  public ProgramGroupResponse getGroup()
  {
    return group;
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getAdmissionDate()
  {
    return admissionDate;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public String getNickName()
  {
    return nickName;
  }

  public String getDob()
  {
    return dob;
  }

  public String getGender()
  {
    return gender;
  }

  public String getNationality()
  {
    return nationality;
  }

  public String getBloodGroup()
  {
    return bloodGroup;
  }

  public String getProfile()
  {
    return profile;
  }

  public String getFamilyType()
  {
    return familyType;
  }

  public List<ParentResponse> getParents()
  {
    return parents;
  }

  public void setStudentImageData(String studentImageData)
  {
    this.studentImageData = studentImageData;
  }

  public String getStudentImageData()
  {
    return studentImageData;
  }

  public boolean isFormalSchool() { return formalSchool; }

  public String getSchoolName() { return schoolName; }
}
