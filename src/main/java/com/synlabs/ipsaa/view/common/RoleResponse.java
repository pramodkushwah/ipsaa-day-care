package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.Role;
import com.synlabs.ipsaa.view.common.PrivilegeResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleResponse implements Response
{

  private Long                   id;
  private String                 name;
  private Set<PrivilegeResponse> privileges;

  public RoleResponse(Role role)
  {

    this.id = mask(role.getId());
    this.name = role.getName();

    if (role.getPrivileges() != null)
    {
      this.privileges = role.getPrivileges().stream().map(PrivilegeResponse::new).collect(Collectors.toSet());
    }
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public Set<PrivilegeResponse> getPrivileges()
  {
    return privileges;
  }
}
