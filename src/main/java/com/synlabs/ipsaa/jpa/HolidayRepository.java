package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

public interface HolidayRepository extends Repository<Holiday, Long>, JpaRepository<Holiday, Long>, QueryDslPredicateExecutor<Holiday>
{

  List<Holiday> findDistinctByCentersInAndHolidayDate(List<Center> userCenters, Date date);

  ////Avneet
  List<Holiday> findByCentersIdAndHolidayDateBetween(Long centerId,Date from,Date to);
}
