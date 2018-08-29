package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by itrs on 4/21/2017.
 */
public interface CenterProgramFeeRepository extends JpaRepository<CenterProgramFee,Long>
{
  CenterProgramFee findByProgramIdAndCenterId(Long programId,Long centerId);
  CenterProgramFee findByCenterId(Long centerId);
}
