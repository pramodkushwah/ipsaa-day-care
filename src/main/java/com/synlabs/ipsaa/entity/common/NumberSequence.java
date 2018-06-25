package com.synlabs.ipsaa.entity.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NumberSequence extends BaseEntity
{
  @Column(nullable = false)
  private long current = 1L;

  public long getCurrent()
  {
    return current;
  }

  public void setCurrent(long current)
  {
    this.current = current;
  }

  public long getNext()
  {
    return current++;
  }
}
