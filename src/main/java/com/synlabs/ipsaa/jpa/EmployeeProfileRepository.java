package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.staff.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by itrs on 4/14/2017.
 */
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long>
{
}
