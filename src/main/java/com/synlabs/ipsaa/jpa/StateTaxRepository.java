package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.StateTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateTaxRepository extends JpaRepository<StateTax,Long> {
    List<StateTax> findByStateId(Long stateId);
}
