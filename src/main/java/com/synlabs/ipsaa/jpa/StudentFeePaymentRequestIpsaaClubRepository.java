package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeePaymentRequestIpsaaClubRepository  extends JpaRepository<StudentFeePaymentRequestIpsaaClub, Long> {
    List<StudentFeePaymentRequestIpsaaClub> findByStudentIdOrderByCreatedDateDesc(Long id);
    List<StudentFeePaymentRequestIpsaaClub> findByMonthAndYearOrderByCreatedDateDesc(int month,int year);
}
