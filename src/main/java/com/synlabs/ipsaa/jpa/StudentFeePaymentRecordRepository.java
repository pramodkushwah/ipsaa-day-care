package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sushil on 12/12/2017.
 */
public interface StudentFeePaymentRecordRepository extends JpaRepository<StudentFeePaymentRecord, Long>
{
  StudentFeePaymentRecord findByTxnid(String txnid);

  List<StudentFeePaymentRecord> findByRequest(StudentFeePaymentRequest slip);
}
