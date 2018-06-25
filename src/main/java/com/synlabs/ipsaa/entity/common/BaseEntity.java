package com.synlabs.ipsaa.entity.common;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by sushil on 12-08-2016.
 */
@MappedSuperclass
public abstract class BaseEntity extends AbstractAuditable<User, Long>
{

  @PrePersist
  private void preCreate()
  {
    this.setCreatedDate(DateTime.now());

    User current = getCurrentUser();
    if (current != null)
    {
      this.setCreatedBy(current);
    }
  }

  @PreUpdate
  private void preUpdate()
  {
    this.setLastModifiedDate(DateTime.now());
    User current = getCurrentUser();
    if (current != null)
    {
      this.setLastModifiedBy(current);
    }

  }

  private User getCurrentUser()
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated())
    {
      return null;
    }

    if ((authentication.getPrincipal() instanceof CurrentUser))
    {
      return ((CurrentUser) authentication.getPrincipal()).getUser();
    }
    return null;
  }

  public void setId(Long Id)
  {
    super.setId(Id);
  }
}
