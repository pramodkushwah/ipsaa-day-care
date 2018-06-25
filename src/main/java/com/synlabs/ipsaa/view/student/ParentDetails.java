package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.view.common.Response;

public class ParentDetails implements Response
{
  private Long   id;
  private String name;
  private String email;

  public ParentDetails(StudentParent parent)
  {
    id = mask(parent.getId());
    name = parent.getFullName();
    email = parent.getEmail();
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getEmail()
  {
    return email;
  }
}
