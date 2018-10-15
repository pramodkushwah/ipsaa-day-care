package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>
{
  List<Student> findByProgramCodeAndActiveIsTrue(String code);

  List<Student> findByActiveIsTrue();

  List<Student> findByCenterInAndActive(List<Center> centers, boolean active);

  Student findByIdAndCenterIn(Long studentId, List<Center> centers);

  List<Student> findByApprovalStatusAndCenterIn(ApprovalStatus new_approval, List<Center> centers);

  List<Student> findByActiveTrueAndApprovalStatusAndCenter(ApprovalStatus new_approval, Center center);

  List<Student> findByCenterAndProgram(Center center, Program program);

  List<Student> findByActiveTrueAndCenter(Center center);
  Student findByAdmissionNumber(String admissionNumber);

  List<Student> findByCenterInAndActiveOrderByIdAsc(List<Center> centers,boolean active);

  ////Avneet
  List<Student> findByCenterInAndActiveTrueAndCorporate(List<Center> centers,boolean corporate);

  List<Student> findByActiveTrueAndCenterIdOrderByIdAsc(Long centreId);
}
