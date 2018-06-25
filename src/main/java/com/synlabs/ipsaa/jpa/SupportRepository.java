package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.enums.QueryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<SupportQuery, Long>
{
  List<SupportQuery> findByStatusNotOrderByCreatedDateAsc(QueryStatus status);
  List<SupportQuery> findByParent(StudentParent parent);
  List<SupportQuery> findAllByOrderByCreatedDateAsc();

  SupportQuery findByParentAndId(StudentParent parent, Long id);
}
