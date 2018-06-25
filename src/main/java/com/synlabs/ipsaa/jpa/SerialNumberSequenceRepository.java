package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.common.SerialNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerialNumberSequenceRepository extends JpaRepository<SerialNumberSequence,Long>
{
  SerialNumberSequence findOneByCenterCodeAndType(String centerCode, String type);
}
