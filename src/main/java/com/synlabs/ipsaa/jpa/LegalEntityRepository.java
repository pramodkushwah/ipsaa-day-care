package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.common.LegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long>
{
  LegalEntity findByCode(String employerCode);
}
