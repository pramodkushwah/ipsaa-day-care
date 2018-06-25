package com.synlabs.ipsaa.view.common;

public class UserPasswordChangeRequest implements Request
{
  private Long id;

  private String password;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }
}
