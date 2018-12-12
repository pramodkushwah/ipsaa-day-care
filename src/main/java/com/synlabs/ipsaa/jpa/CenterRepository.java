package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterRepository extends JpaRepository<Center, Long>
{
  Center getOneByName(String center);

  Center findByCode(String code);

  List<Center> findByIdIn(List<Long> codes);
  List<Center> findByActiveIsTrue();

  long countByCode(String code);
}
