package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HdfcApiDetailRepository extends JpaRepository<HdfcApiDetails,Long> {
   HdfcApiDetails findByCenter(Center center);
}
