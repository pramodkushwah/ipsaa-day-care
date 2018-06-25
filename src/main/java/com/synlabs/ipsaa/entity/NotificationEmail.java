package com.synlabs.ipsaa.entity;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.EmailNotificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by itrs on 7/13/2017.
 */
@Entity
public class NotificationEmail extends BaseEntity
{
  @Column(nullable = false,length = 50)
  private String email;

  @Enumerated(EnumType.STRING)
  private EmailNotificationType type;

  public EmailNotificationType getType()
  {
    return type;
  }

  public void setType(EmailNotificationType type)
  {
    this.type = type;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }
}
