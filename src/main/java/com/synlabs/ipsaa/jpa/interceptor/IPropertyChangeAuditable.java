package com.synlabs.ipsaa.jpa.interceptor;

import com.synlabs.ipsaa.entity.common.BaseEntity;

public interface IPropertyChangeAuditable
{
  BaseEntity auditAddition();
  BaseEntity auditRemoval();
  BaseEntity auditPropertyChange(String propertyName, String oldValue, String newValue);
}
