package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFeePaymentRecordIpsaaClubRepository  extends JpaRepository<StudentFeePaymentRecordIpsaaClub,Long> {
}
