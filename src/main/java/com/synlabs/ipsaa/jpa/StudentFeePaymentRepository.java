package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeePaymentRepository extends JpaRepository<StudentFeePaymentRequest, Long>
{
  List<StudentFeePaymentRequest> findByStudentId(Long studentId);
  List<StudentFeePaymentRequest> findByQuarterAndYear(int quarter,int year);

  StudentFeePaymentRequest findOneByStudentAndFeeDurationAndMonthAndYear(Student student, FeeDuration period, int month, int year);

  StudentFeePaymentRequest findOneByStudentAndFeeDurationAndQuarterAndYear(Student student, FeeDuration period, int quarter, int year);


  StudentFeePaymentRequest findOneByStudentAndFeeDurationAndYear(Student student, FeeDuration period, int year);

  StudentFeePaymentRequest findFirstByStudentOrderByCreatedDateDesc(Student student);

  StudentFeePaymentRequest findOneByTnxid(String tnxid);
  // -----------------------------------------shubham ---------------------------------------------------------
  List<StudentFeePaymentRequest> findByStudentApprovalStatusAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(ApprovalStatus ststus, FeeDuration period, int quarter, int year, String centerCode);
  List<StudentFeePaymentRequest> findByStudentApprovalStatusAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(ApprovalStatus ststus, FeeDuration period, int quarter, int year);


  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentApprovalStatusAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(ApprovalStatus ststus, FeeDuration period, int quarter, int year, String centerCode);
  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentApprovalStatusAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(ApprovalStatus ststus, FeeDuration period, int quarter, int year);
}
