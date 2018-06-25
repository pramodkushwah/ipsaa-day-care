package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.food.FoodMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by ttn on 2/7/17.
 */
public interface FoodMenuRepository extends Repository<FoodMenu, Long>, JpaRepository<FoodMenu, Long>, QueryDslPredicateExecutor<FoodMenu>
{
  Page<FoodMenu> findByCenterCodeAndDateBetween(String centerCode, Date dateFrom, Date dateTo, Pageable pageable);

  List<FoodMenu> findByCenterCodeAndDateBetween(String centerCode, Date dateFrom, Date dateTo);

  FoodMenu findByCenterCodeAndDate(String centerCode, Date date);

  List<FoodMenu> findByCenterInAndDateBetween(List<Center> centers, Date date, Date date1);
}
