package com.synlabs.ipsaa.entity.audit;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.AuditEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class AuditHistory extends BaseEntity
{

  @Column(length = 10, name = "action", nullable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private AuditEvent action;

  @Column(length = 50, nullable = false, updatable = false)
  private String objectName;

  @Column(length = 50, nullable = false, updatable = false)
  private String fieldName;

  @Column(length = 100, updatable = false)
  private String newValue;

  @Column(length = 100, updatable = false)
  private String oldValue;

  @Column(nullable = false, updatable = false)
  private Long objectId;

  public AuditEvent getAction()
  {
    return action;
  }

  public void setAction(AuditEvent action)
  {
    this.action = action;
  }

  public String getObjectName()
  {
    return objectName;
  }

  public void setObjectName(String objectName)
  {
    this.objectName = objectName;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }

  public String getNewValue()
  {
    return newValue;
  }

  public void setNewValue(String newValue)
  {
    this.newValue = newValue;
  }

  public String getOldValue()
  {
    return oldValue;
  }

  public void setOldValue(String oldValue)
  {
    this.oldValue = oldValue;
  }

  public Long getObjectId()
  {
    return objectId;
  }

  public void setObjectId(Long objectId)
  {
    this.objectId = objectId;
  }
}
