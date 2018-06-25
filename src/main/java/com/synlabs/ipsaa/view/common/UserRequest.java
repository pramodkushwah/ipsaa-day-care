package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.enums.UserType;

import java.util.HashSet;
import java.util.Set;

public class UserRequest implements Request
{
  private Long   id;
  private String firstname;
  private String lastname;
  private String type;
  private String phone;
  private String email;

  private boolean active;
  private Long    empId;

  private Set<String> roles   = new HashSet<>();
  private Set<String> centers = new HashSet<>();

  public UserRequest()
  {
  }

  public Long getEmpId()
  {
    return unmask(empId);
  }

  public void setEmpId(Long empId)
  {
    this.empId = empId;
  }

  public UserRequest(Long id)
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

  public String getFirstname()
  {
    return firstname;
  }

  public void setFirstname(String firstname)
  {
    this.firstname = firstname;
  }

  public String getLastname()
  {
    return lastname;
  }

  public void setLastname(String lastname)
  {
    this.lastname = lastname;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public Set<String> getRoles()
  {
    return roles;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public void setRoles(Set<String> roles)
  {
    this.roles = roles;
  }

  public Set<String> getCenters()
  {
    return centers;
  }

  public void setCenters(Set<String> centers)
  {
    this.centers = centers;
  }

  public User toEntity(User user)
  {
    if (user == null)
    {
      user = new User();
    }
    user.setActive(this.active);
    user.setEmail(this.email);
    user.setFirstname(this.firstname);
    user.setLastname(this.lastname);
    user.setPhone(this.phone);
    user.setUserType(UserType.valueOf(this.type));
    return user;
  }

  public User toEntity()
  {
    return toEntity(new User());
  }
}
