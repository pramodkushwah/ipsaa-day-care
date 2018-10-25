package com.synlabs.ipsaa.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.student.Student;

public interface StudentAttendanceRepository
		extends JpaRepository<StudentAttendance, Long>, QueryDslPredicateExecutor<StudentAttendance> {

	int countByStudentAndAttendanceDate(Student student, Date date);

	StudentAttendance findByStudentAndAttendanceDate(Student student, Date date);

	List<StudentAttendance> findByStudentAndCreatedDateBetweenAndExtraHoursNot(Student student, Date from, Date to,int value);
	List<StudentAttendance> findByCreatedDateBetween(Date from, Date to);
	List<StudentAttendance> findByCreatedDateBetweenAndStudent(Date from, Date to,Student student);

	List<StudentAttendance> findByCenterAndAttendanceDateBetweenOrderByStudentAdmissionNumberAsc(Center center,
			Date from, Date to);

	// Avneet
	List<StudentAttendance> findByStudentInAndAttendanceDateOrderByStudentIdAsc(List<Student> students, Date date);

	List<StudentAttendance> findByStudentCenterInAndStudentActiveTrueAndAttendanceDateAndCheckoutNotNull(
			List<Center> center, Date date);

}
