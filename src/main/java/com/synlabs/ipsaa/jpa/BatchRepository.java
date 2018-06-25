package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.batch.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Long>
{
}
