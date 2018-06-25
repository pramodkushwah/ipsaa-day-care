package com.synlabs.ipsaa.entity.common;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role extends BaseEntity
{

  @Column(name = "uniq")
  private boolean unique;

  private boolean internal;

  @Column(length = 500)
  private String dashboard;

  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_privilege",
      joinColumns = { @JoinColumn(name = "ROLE_ID") },
      inverseJoinColumns = { @JoinColumn(name = "PRIV_ID") })
  private Set<Privilege> privileges;

  private Integer priority;

  public Role()
  {
  }

  public Role(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Set<Privilege> getPrivileges()
  {
    return privileges;
  }

  public void setPrivileges(Set<Privilege> privileges)
  {
    this.privileges = privileges;
  }

  public Integer getPriority()
  {
    return priority;
  }

  public void setPriority(Integer priority)
  {
    this.priority = priority;
  }

  public void addPrivilege(Privilege privilege)
  {
    if (privileges == null) privileges = new HashSet<>();
    privileges.add(privilege);
  }

  public boolean isUnique()
  {
    return unique;
  }

  public void setUnique(boolean unique)
  {
    this.unique = unique;
  }

  public boolean isInternal()
  {
    return internal;
  }

  public void setInternal(boolean internal)
  {
    this.internal = internal;
  }

  public String getDashboard()
  {
    return dashboard;
  }

  public void setDashboard(String dashboard)
  {
    this.dashboard = dashboard;
  }
}
