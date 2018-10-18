package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.City;
import com.synlabs.ipsaa.entity.center.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long>
{
  City findOneByName(String name);
  List<City> findByZoneName(String zoneName);
  int countByName(String name);

  ////Avneet
  List<City> findByState(State state);
}
