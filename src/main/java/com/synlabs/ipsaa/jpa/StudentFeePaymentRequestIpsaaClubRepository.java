package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.enums.FeeDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeePaymentRequestIpsaaClubRepository  extends JpaRepository<StudentFeePaymentRequestIpsaaClub, Long> {
    List<StudentFeePaymentRequestIpsaaClub> findByStudentIdOrderByCreatedDateDesc(Long id);
    List<StudentFeePaymentRequestIpsaaClub> findByMonthAndYearOrderByCreatedDateDesc(int month,int year);
    List<StudentFeePaymentRequestIpsaaClub> findByIsExpireIsFalseOrderByCreatedDateDesc();
    List<StudentFeePaymentRequestIpsaaClub> findByIsExpireIsFalseAndStudentCenterCodeOrderByCreatedDateDesc(String code);

    StudentFeePaymentRequestIpsaaClub findOneByTnxid(String tnxid);
    List<StudentFeePaymentRequestIpsaaClub> findByStudentProgramIdAndStudentCorporateIsFalseAndIsExpireIsFalseAndMonthAndYearAndStudentCenterCode(long id,int month, int year, String centerCode);
    List<StudentFeePaymentRequestIpsaaClub> findByStudentProgramIdAndStudentCorporateIsFalseAndIsExpireIsFalseAndMonthAndYear(long id, int month, int year);

}
