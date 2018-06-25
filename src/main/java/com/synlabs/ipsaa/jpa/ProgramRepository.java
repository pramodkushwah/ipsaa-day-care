package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.programs.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long>
{
  Program findByCode(String code);
  long countByCode(String code);
}
