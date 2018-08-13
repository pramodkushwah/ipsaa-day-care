package com.synlabs.ipsaa.entity.student;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.Relationship;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Student extends BaseEntity
{

  //TODO - admission Number = CENTER + ID
  @Column(length = 50, updatable = false)
  private String admissionNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Center center;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Program program;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private ProgramGroup group;

  @OneToOne(optional = false)
  private StudentProfile profile;

  @Column(nullable = false)
  private boolean active = true;

  @Temporal(TemporalType.TIME)
  private Date expectedIn;

  @Temporal(TemporalType.TIME)
  private Date expectedOut;

  private boolean corporate = false;

  /////Avneet
  private boolean formalSchool=false;

  private String schoolName;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "std_parent",
      joinColumns = @JoinColumn(name = "STUDENT_ID"),
      inverseJoinColumns = @JoinColumn(name = "PARENT_ID")
  )
  private List<StudentParent> parents=new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ApprovalStatus approvalStatus;

  public boolean isCorporate()
  {
    return corporate;
  }

  public void setCorporate(boolean corporate)
  {
    this.corporate = corporate;
  }

  public ApprovalStatus getApprovalStatus()
  {
    return approvalStatus;
  }

  public void setApprovalStatus(ApprovalStatus approvalStatus)
  {
    this.approvalStatus = approvalStatus;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public String getAdmissionNumber()
  {
    return admissionNumber;
  }

  public void setAdmissionNumber(String admissionNumber)
  {
    this.admissionNumber = admissionNumber;
  }

  public Program getProgram()
  {
    return program;
  }

  public void setProgram(Program program)
  {
    this.program = program;
  }

  public ProgramGroup getGroup()
  {
    return group;
  }

  public void setGroup(ProgramGroup group)
  {
    this.group = group;
  }

  public StudentProfile getProfile()
  {
    return profile;
  }

  public void setProfile(StudentProfile profile)
  {
    this.profile = profile;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
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

  public List<StudentParent> getParents()
  {
    return parents;
  }

  public void setParents(List<StudentParent> parents)
  {
    this.parents = parents;
  }

  @Transient
  public String getCenterName()
  {
    return this.center == null ? "" : this.center.getName();
  }

  @Transient
  public String getProgramName()
  {
    return this.program == null ? "" : this.program.getName();
  }

  @Transient
  public String getGroupName()
  {
    return this.group == null ? "" : this.group.getName();
  }

  @Transient
  public String getInTime()
  {
    String result = "";
    if (this.expectedIn != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
      result = sdf.format(this.expectedIn);
    }
    return result;
  }

  @Transient
  public String getOutTime()
  {
    String result = "";
    if (this.expectedOut != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
      result = sdf.format(this.expectedOut);
    }
    return result;
  }

  @Transient
  public StudentParent getFather()
  {
    StudentParent studentParent = null;
    if (this.parents != null && this.parents.size() > 0)
    {
      for (StudentParent parent : this.parents)
      {
        if (parent.getRelationship() == Relationship.Father)
        {
          studentParent = parent;
          break;
        }
      }
    }
    if (studentParent == null)
    {
      studentParent = new StudentParent();
    }
    return studentParent;
  }

  @Transient
  public StudentParent getMother()
  {
    StudentParent studentParent = null;
    if (this.parents != null && this.parents.size() > 0)
    {
      for (StudentParent parent : this.parents)
      {
        if (parent.getRelationship() == Relationship.Mother)
        {
          studentParent = parent;
          break;
        }
      }
    }
    if (studentParent == null)
    {
      studentParent = new StudentParent();
    }
    return studentParent;
  }

  @Transient
  public StudentParent getGuardian()
  {
    StudentParent studentParent = null;
    if (this.parents != null && this.parents.size() > 0)
    {
      for (StudentParent parent : this.parents)
      {
        if (parent.getRelationship() == Relationship.Guardian)
        {
          studentParent = parent;
          break;
        }
      }
    }
    if (studentParent == null)
    {
      studentParent = new StudentParent();
    }
    return studentParent;
  }

  @Override
  public String toString()
  {
    return "Student{" +
        "admissionNumber='" + admissionNumber + '\'' +
        "active"+active+
        ", profile=" + profile +
        '}';
  }

  @Transient
  public StudentParent getParent(Relationship relationship)
  {
    for (StudentParent parent : parents)
    {
      if (parent.getRelationship() == relationship)
      {
        return parent;
      }
    }
    return null;
  }

  @Transient
  public String getName()
  {
    return profile == null ? null : profile.getFullName();
  }

  @Transient
  public List<Address> getAllAddresses(){
    List<Address> addresses=new ArrayList<>();
    for (StudentParent parent:parents)
    {
      addresses.add(parent.getResidentialAddress());
      addresses.add(parent.getOfficeAddress());
    }
    return addresses;
  }

  public boolean isFormalSchool() {
    return formalSchool;
  }

  public void setFormalSchool(boolean formalSchool) { this.formalSchool = formalSchool; }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public String getSchoolName() {
    return schoolName;
  }

}
