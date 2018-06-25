package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.Role;
import com.synlabs.ipsaa.view.common.PrivilegeRequest;
import com.synlabs.ipsaa.view.common.Request;

import java.util.HashSet;
import java.util.Set;

public class RoleRequest implements Request
{
  private Long id;
  private String name;
  private Set<PrivilegeRequest> privileges = new HashSet<>();

  public RoleRequest()
  {
  }

  public RoleRequest(Long id)
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

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Set<PrivilegeRequest> getPrivileges()
  {
    return privileges;
  }

  public void setPrivileges(Set<PrivilegeRequest> privileges)
  {
    this.privileges = privileges;
  }

  public Role toEntity(Role role) {
    if (role == null) role = new Role();
    role.setName(this.name);
    return role;
  }

  public Role toEntity()
  {
      return toEntity(new Role());
  }
}
