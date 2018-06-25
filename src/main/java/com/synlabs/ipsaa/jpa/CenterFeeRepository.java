package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterFeeRepository extends JpaRepository<CenterProgramFee, Long>
{
  List<CenterProgramFee> findByCenterId(Long centerId);
}
