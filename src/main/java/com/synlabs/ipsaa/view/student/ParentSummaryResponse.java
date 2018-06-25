package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.view.common.AddressResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.ArrayList;

public class ParentSummaryResponse implements Response
{
  private String name;
  private String mobile;
  private String email;
  private String relationship;
  private boolean account;
  private ArrayList<String> students=new ArrayList<>();
  private String organisation;

  public ParentSummaryResponse(StudentParent parent)
  {
    this.name = parent.getFullName();
    this.mobile = parent.getMobile();
    this.email = parent.getEmail();
    this.relationship = parent.getRelationship().name();
    this.account= parent.isAccount();
    this.organisation=parent.getOrganisation();
    for (Student student:parent.getStudents())
    {
      students.add(student.getName());
    }
  }

  public String getOrganisation()
  {
    return organisation;
  }

  public String getName()
  {
    return name;
  }

  public String getMobile()
  {
    return mobile;
  }

  public String getEmail()
  {
    return email;
  }

  public String getRelationship()
  {
    return relationship;
  }

  public boolean isAccount()
  {
    return account;
  }

  public ArrayList<String> getStudents()
  {
    return students;
  }
}
