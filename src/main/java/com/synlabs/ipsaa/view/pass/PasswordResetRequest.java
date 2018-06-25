package com.synlabs.ipsaa.view.pass;

import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by itrs on 5/3/2017.
 */
public class PasswordResetRequest implements Request
{
  private String token;
  private String password;

  public String getToken()
  {
    return token;
  }

  public void setToken(String token)
  {
    this.token = token;
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
