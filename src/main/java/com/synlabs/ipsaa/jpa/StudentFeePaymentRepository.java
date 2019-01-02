package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
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
  List<StudentFeePaymentRequest> findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(FeeDuration period, int quarter, int year, String centerCode);
  List<StudentFeePaymentRequest> findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(FeeDuration period, int quarter, int year);

  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(ApprovalStatus ststus, FeeDuration period, int quarter, int year, String centerCode);
  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear( FeeDuration period, int quarter, int year);

  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(FeeDuration period, int quarter, int year, String centerCode);
  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentProgramIdIsNot( FeeDuration period, int quarter, int year,long id);



  // ipsaa
  List<StudentFeePaymentRequest> findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCodeAndStudentProgramIdIsNot(FeeDuration period, int quarter, int year, String centerCode,long id);
  List<StudentFeePaymentRequest> findByStudentCorporateIsFalseAndStudentActiveIsTrueAndFeeDurationAndQuarterAndYearAndStudentProgramIdIsNot(FeeDuration period, int quarter, int year,long id);
  List<StudentFeePaymentRequest> findByStudentCorporateIsFalseAndStudentActiveIsTrueAndFeeDurationAndQuarterAndYearAndStudentCenterCodeAndStudentProgramIdIsNot(FeeDuration period, int quarter, int year, String centerCode,long id);

  List<StudentFeePaymentRequest> findByStudentAndFeeDurationAndQuarterNotAndYearNot(Student student, FeeDuration period,int quarter, int year);
  StudentFeePaymentRequest findByStudentAndFeeDurationAndQuarterAndYear(Student student, FeeDuration period,int quarter, int year);

  List<StudentFeePaymentRequest> findByStudentAndFeeDuration(Student student, FeeDuration period);
  int countByStudentAndFeeDuration(Student student, FeeDuration period);

    int countByStudentId(Long id);

//  /////Avneet
//  List<StudentFeePaymentRequest> findByStudentCenterIdInAndQuarterAndYear(List<Long> ids, int quarter,int year);

  StudentFeePaymentRequest findOneByStudentAndFeeDurationAndIsExpireIsFalse(Student student, FeeDuration period);
}
