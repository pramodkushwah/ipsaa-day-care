package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by itrs on 4/6/2017.
 */
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long>
{
}
