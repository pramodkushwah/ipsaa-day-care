package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.inquiry.WebsiteInquiry;
import com.synlabs.ipsaa.enums.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebsiteInquiryRepository extends JpaRepository<WebsiteInquiry,Long> {
    List<WebsiteInquiry> findByStatus(InquiryStatus status);
}
