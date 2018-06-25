package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.programs.GroupActivity;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface GroupActivityRepository extends JpaRepository<GroupActivity,Long>
{
  List<GroupActivity> findByCenterInAndGroupInAndDateBetween(Set<Center> centers, List<ProgramGroup> groups, Date date, Date date1);

  List<GroupActivity> findByCenterAndGroupAndDateBetween(Center center, ProgramGroup group, Date date, Date date1);
}
