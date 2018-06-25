package com.synlabs.ipsaa.entity.token;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by itrs on 5/2/2017.
 */
@Entity
public class PasswordResetToken extends BaseEntity
{
  @Column(length = 50, updatable = false)
  private String  token;

  private boolean expired;

  @Temporal(TemporalType.TIMESTAMP)
  private Date validTill;

  private String email;

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken(String token)
  {
    this.token = token;
  }

  public boolean isExpired()
  {
    return expired;
  }

  public void setExpired(boolean expired)
  {
    this.expired = expired;
  }

  public Date getValidTill()
  {
    return validTill;
  }

  public void setValidTill(Date validTill)
  {
    this.validTill = validTill;
  }
}
