package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.fee.HdfcResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HdfcResponseRepository extends JpaRepository<HdfcResponse, Long>
{
}
