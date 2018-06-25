package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.common.AdmissionNumberSequence;
import com.synlabs.ipsaa.entity.common.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionNumberSequenceRepository extends JpaRepository<AdmissionNumberSequence, Long>
{
  Optional<AdmissionNumberSequence> getOneByCenterCode(String centerCode);
}
