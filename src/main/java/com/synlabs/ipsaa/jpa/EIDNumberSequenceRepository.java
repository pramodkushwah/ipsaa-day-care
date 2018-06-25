package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.common.EIDNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EIDNumberSequenceRepository extends JpaRepository<EIDNumberSequence, Long>
{
}
