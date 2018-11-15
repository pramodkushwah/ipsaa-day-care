package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeePaymentRecordIpsaaClubRepository  extends JpaRepository<StudentFeePaymentRecordIpsaaClub,Long> {
    List<StudentFeePaymentRecord> findByRequest(StudentFeePaymentRequestIpsaaClub slip);
}
