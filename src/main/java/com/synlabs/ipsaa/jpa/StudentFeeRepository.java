package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.enums.FeeDuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentFeeRepository extends JpaRepository<StudentFee, Long>
{
  StudentFee findByStudent(Student student);
  StudentFee findByStudentId(Long id);
  List<StudentFee> findByStudentIn(List<Student> students);
  List<StudentFee> findByStudentCenterIdOrderByStudentProgramCode(Long centerId);


}
