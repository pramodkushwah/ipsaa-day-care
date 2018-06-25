package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.enums.CallDisposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface InquiryEventLogRepository extends JpaRepository<InquiryEventLog, Long>
{
  List<InquiryEventLog> findByDoneFalseAndInquiryCenterInAndCallDispositionIn(List<Center> centers, List<CallDisposition> callDispositions);

  List<InquiryEventLog> findByDoneFalseAndInquiryCenterAndCallDispositionIn(Center center, List<CallDisposition> callDispositions);

  @Query(value = "select distinct inquiry from InquiryEventLog as log join log.inquiry as inquiry where inquiry.center = ?1 and log.createdDate between ?2 and ?3")
  List<Inquiry> findByCenterAndDate(Center center,Date from ,Date to);
}
