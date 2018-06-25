package com.synlabs.ipsaa.entity.common;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Created by sushil on 29-02-2016.
 */

public class CurrentUser extends User {

  private com.synlabs.ipsaa.entity.common.User user;

  public CurrentUser(com.synlabs.ipsaa.entity.common.User user, String[] roles) {
    super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(roles));
    this.user = user;
  }

  public com.synlabs.ipsaa.entity.common.User getUser() {
    return user;
  }

  public void setUser(com.synlabs.ipsaa.entity.common.User user) {
    this.user = user;
  }
}
