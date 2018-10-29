package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State,Long> {

    int countByName(String name);

    State findOneByName(String name);

    List<State> findByCitiesZoneId(Long zoneId);
}
