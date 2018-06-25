package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramGroupRepository extends JpaRepository<ProgramGroup, Long>
{
  ProgramGroup findByName(String name);
}
