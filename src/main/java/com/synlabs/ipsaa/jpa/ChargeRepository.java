package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.fee.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, Long>
{
}
