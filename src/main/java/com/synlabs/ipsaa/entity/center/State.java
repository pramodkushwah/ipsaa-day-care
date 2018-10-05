package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
public class State extends BaseEntity {

    @Column(nullable = false, length=20, unique=true)
    private String name;

    @OneToMany(mappedBy = "state")
    private Set<City> cities;

    @OneToMany(mappedBy = "state")
    private List<StateTax> tax;

    public String getName() { return name.toUpperCase(); }

    public void setName(String name) { this.name = name; }

    public Set<City> getCities() { return cities; }

    public void setCities(Set<City> cities) { this.cities = cities; }

    public List<StateTax> getTax() { return tax; }

    public void setTax(List<StateTax> tax) { this.tax = tax; }


}
