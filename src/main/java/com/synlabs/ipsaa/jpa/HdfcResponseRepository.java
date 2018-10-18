package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.fee.HdfcResponse;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HdfcResponseRepository extends JpaRepository<HdfcResponse, Long>
{

    List<HdfcResponse> findBySlipQuarterAndSlipYear(int quarter,int year);

}
