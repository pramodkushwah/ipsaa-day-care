package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ParentSharingSheetRepository extends JpaRepository<ParentSharingSheet, Long>
{
  ParentSharingSheet findByStudentIdAndSharingDate(Long studentId, Date date);

  ParentSharingSheet findByStudentIdAndId(Long studentId, Long id);
}
