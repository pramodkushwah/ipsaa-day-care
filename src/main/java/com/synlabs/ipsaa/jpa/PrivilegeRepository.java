package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.common.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>
{
  Optional<Privilege> findByName(String name);

  List<Privilege> findByNameNot(String hr_admin);
}
