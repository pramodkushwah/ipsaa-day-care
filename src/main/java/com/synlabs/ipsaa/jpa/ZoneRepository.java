package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long>
{
  Zone findOneByName(String zone);
  int countByName(String name);
}
