package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Zone extends BaseEntity
{
  @Column(nullable = false, length = 50)
  private String name;

  @OneToMany(mappedBy = "zone")
  private Set<City> cities;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Set<City> getCities()
  {
    return cities;
  }

  public void setCities(Set<City> cities)
  {
    this.cities = cities;
  }
}
