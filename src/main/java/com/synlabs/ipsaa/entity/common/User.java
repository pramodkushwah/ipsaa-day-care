package com.synlabs.ipsaa.entity.common;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.UserType;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User extends BaseEntity
{

  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email;

  @Column(name = "password_hash", nullable = false, length = 100)
  private String passwordHash;

  @Column(name = "active", nullable = false)
  private boolean active;

  @Column(name = "phone", nullable = false, length = 20)
  private String phone;

  @Column(name = "firstname", nullable = false, length = 20)
  private String firstname;

  @Column(name = "lastname", length = 20)
  private String lastname;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLogin;

  @Enumerated(EnumType.STRING)
  private UserType userType;

  @Column(length = 200)
  private String imagePath;

  @ManyToOne(fetch = FetchType.EAGER)
  private Employee employee;

  @ManyToOne(fetch = FetchType.LAZY)
  private StudentParent parent;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = { @JoinColumn(name = "USER_ID") },
      inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
  private List<Role> roles=new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_center",
      joinColumns = { @JoinColumn(name = "USER_ID") },
      inverseJoinColumns = { @JoinColumn(name = "CENTER_ID") })
  @OrderBy("address.city,code")
  private List<Center> centers;

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPasswordHash()
  {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash)
  {
    this.passwordHash = passwordHash;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
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

  public Date getLastLogin()
  {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin)
  {
    this.lastLogin = lastLogin;
  }

  public UserType getUserType()
  {
    return userType;
  }

  public void setUserType(UserType userType)
  {
    this.userType = userType;
  }

  public Employee getEmployee()
  {
    return employee;
  }

  public void setEmployee(Employee employee)
  {
    this.employee = employee;
  }

  public StudentParent getParent()
  {
    return parent;
  }

  public void setParent(StudentParent parent)
  {
    this.parent = parent;
  }

  public List<Role> getRoles()
  {
    return roles;
  }

  public void setRoles(List<Role> roles)
  {
    this.roles = roles;
  }

  public List<Center> getCenters()
  {
    return centers;
  }

  public void setCenters(List<Center> centers)
  {
    this.centers = centers;
  }

  public Set<String> getPrivileges()
  {
    Set<String> usrPrivileges = new HashSet<>();
    for (Role userRole : roles)
    {
      for (Privilege privilege : userRole.getPrivileges())
      {
        usrPrivileges.add(privilege.getName());
      }
    }
    return usrPrivileges;
  }

  public boolean hasPrivilege(String priv)
  {

    if (StringUtils.isEmpty(priv))
    {
      return true;
    }

    for (Role userRole : roles)
    {
      for (Privilege privilege : userRole.getPrivileges())
      {
        if (privilege.getName().equals(priv))
        {
          return true;
        }
      }
    }

    return false;
  }

  public String getName()
  {
    return (firstname + " " + lastname).trim();
  }

  public void addRole(Role role)
  {
    if (roles == null)
    {
      roles = new ArrayList<>();
    }
    roles.add(role);
  }

  public void addCenter(Center center)
  {
    if (centers == null)
    {
      centers = new ArrayList<>();
    }
    centers.add(center);
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }
}
