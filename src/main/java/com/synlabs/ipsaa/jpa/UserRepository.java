package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.student.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{

  User findByEmailAndActiveTrue(String email);
  List<User> findDistinctByCentersInAndActiveTrue(List<Center> usercenters);
  User findOneByParent(StudentParent parent);

  User findByEmail(String email);

  long countDistinctByActiveTrueAndCentersIn(List<Center> centers);

  User findByEmployee(Employee currentEmployee);
}
