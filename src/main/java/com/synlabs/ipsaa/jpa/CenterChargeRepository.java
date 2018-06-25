package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.CenterCharge;
import com.synlabs.ipsaa.entity.fee.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterChargeRepository extends JpaRepository<CenterCharge, Long>
{
  List<CenterCharge> findByCenterId(Long centerId);
  CenterCharge findByCenterAndCharge(Center center, Charge charge);
}
