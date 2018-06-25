package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.hygiene.Hygiene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by itrs on 7/7/2017.
 */
@Repository
public interface HygieneRepository extends JpaRepository<Hygiene,Long>
{
  List<Hygiene> findByCenter(Center center);
}
