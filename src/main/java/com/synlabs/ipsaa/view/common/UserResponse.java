package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.view.staff.StaffSummaryResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserResponse extends UserSummaryResponse
{
  private Long    id;
  private String  fullName;
  private String  phone;
  private String  type;
  private boolean active;
  private Set<String>  roles   = new HashSet<>();
  private List<String> centers = new ArrayList<>();
  private StaffSummaryResponse employee;

  public UserResponse(User user)
  {
    super(user);
    this.id = mask(user.getId());
    this.fullName = user.getName();
    this.phone = user.getPhone();
    this.type = user.getUserType().name();
    this.active = user.isActive();
    if (user.getEmployee() != null)
    {
      this.employee = new StaffSummaryResponse(user.getEmployee());
    }

    if (user.getRoles() != null)
    {
      user.getRoles().forEach(role -> {
        this.roles.add(role.getName());
      });
    }

    if (user.getCenters() != null)
    {
      user.getCenters().forEach(center -> {
        this.centers.add(center.getName());
      });
    }
  }

  public StaffSummaryResponse getEmployee()
  {
    return employee;
  }

  public Long getId()
  {
    return id;
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getPhone()
  {
    return phone;
  }

  public String getType()
  {
    return type;
  }

  public Set<String> getRoles()
  {
    return roles;
  }

  public boolean isActive()
  {
    return active;
  }

  public List<String> getCenters()
  {
    return centers;
  }
}
