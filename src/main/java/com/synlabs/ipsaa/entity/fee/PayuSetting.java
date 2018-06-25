package com.synlabs.ipsaa.entity.fee;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PayuSetting extends BaseEntity
{

  @ManyToOne(optional = false)
  private Center center;

  private boolean enabled;

  @Column(name = "payu_salt", length = 20)
  private String salt;

  @Column(name = "payu_key", length = 20)
  private String key;

  @Column(name = "payu_mid", length = 20)
  private String mid;

  @Column(name = "center_code", length = 20)
  private String centerCode;

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  public String getSalt()
  {
    return salt;
  }

  public void setSalt(String salt)
  {
    this.salt = salt;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getMid()
  {
    return mid;
  }

  public void setMid(String mid)
  {
    this.mid = mid;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }
}
