package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.programs.Program;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long>
{
  Program findByCode(String code);
  long countByCode(String code);

  //////////Avneet
  List<Program> findAllByOrderByIdAsc();
}
