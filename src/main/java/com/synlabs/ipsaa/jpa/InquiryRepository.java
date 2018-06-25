package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.enums.CallDisposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>
{
  Inquiry findByInquiryNumber(String inquiryNumber);

  List<Inquiry> findByCenterAndStatusNotIn(Center center, List<CallDisposition> callDispositions);

  List<Inquiry> findByCenterInAndStatusNotIn(List<Center> centers, List<CallDisposition> callDispositions);

  int countByCenterInAndInquiryDateBetween(List<Center> centers, Date monthStart, Date monthEnd);
}
