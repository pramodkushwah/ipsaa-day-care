package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;
import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long>, QueryDslPredicateExecutor<StudentAttendance>
{

  int countByStudentAndAttendanceDate(Student student, Date date);

  StudentAttendance findByStudentAndAttendanceDate(Student student, Date date);

  List<StudentAttendance> findByCenterAndAttendanceDateBetweenOrderByStudentAdmissionNumberAsc(Center center, Date from, Date to);
}
