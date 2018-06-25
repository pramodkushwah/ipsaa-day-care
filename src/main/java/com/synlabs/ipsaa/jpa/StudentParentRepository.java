package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentParentRepository extends JpaRepository<StudentParent, Long>
{
}
