package com.synlabs.ipsaa.enums;

/**
 * Created by sushil on 11-04-2017.
 */
public enum AuditEvent
{
  ADD,
  DELETE,
  UPDATE;

  public String getCode()
  {
    return name().toLowerCase();
  }
}
