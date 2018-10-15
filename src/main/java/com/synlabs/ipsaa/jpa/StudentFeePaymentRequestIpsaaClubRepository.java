package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentFeePaymentRequestIpsaaClubRepository  extends JpaRepository<StudentFeePaymentRequestIpsaaClub, Long> {
    StudentFeePaymentRequestIpsaaClub findByStudentId(Long id);
}
