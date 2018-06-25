package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface SharingSheetRepository extends JpaRepository<SharingSheet, Long>
{
  SharingSheet findByStudentIdAndSharingDate(Long studentId, Date date);
}
