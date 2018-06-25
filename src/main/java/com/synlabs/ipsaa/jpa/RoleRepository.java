package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.common.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>
{
  Role getOneByName(String name);

  List<Role> findByNameNot(String hr_admin);
}
