package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.programs.GroupActivity;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.GroupActivityRepository;
import com.synlabs.ipsaa.jpa.ProgramGroupRepository;
import com.synlabs.ipsaa.view.center.GroupActivityRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupActivityService extends BaseService
{
  @Autowired
  private GroupActivityRepository groupActivityRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private ProgramGroupRepository programGroupRepository;

  @Value("${ipsaa.activity.mis.days}")
  private int days = 10;

  public GroupActivity save(GroupActivityRequest request)
  {
    if (request.getCenterId() == null)
    {
      throw new ValidationException("Center is missing.");
    }
    if (request.getGroupId() == null)
    {
      throw new ValidationException("Group is missing.");
    }
    if (StringUtils.isEmpty(request.getActivity()))
    {
      throw new ValidationException("Activity is missing.");
    }
    Center center = centerRepository.findOne(request.getUnmaskedCenterId());
    if (center == null)
    {
      throw new ValidationException(String.format("Center[id = %s] not found.", request.getCenterId()));
    }

    Set<Center> centers = new HashSet<>(getUserCenters());
    if (!centers.contains(center))
    {
      throw new ValidationException(String.format("Unauthorized access to center[%s] user[%s]", request.getCenterId(), getUser().getEmail()));
    }

    ProgramGroup group = programGroupRepository.findOne(request.getUnmaskedGroupId());
    if (group == null)
    {
      throw new ValidationException(String.format("Group[id=%s] not found", request.getGroupId()));
    }

    GroupActivity groupActivity = null;
    try
    {
      groupActivity = request.toEntity(null);
    }
    catch (ParseException e)
    {
      throw new ValidationException("incorrect date format [yyyy-MM-dd]");
    }
    groupActivity.setCenter(center);
    groupActivity.setGroup(group);

    return groupActivityRepository.saveAndFlush(groupActivity);
  }

  public GroupActivity update(GroupActivityRequest request)
  {
    if (request.getUnmaskedId() == null)
    {
      throw new ValidationException("Activity Id missing in request");
    }
    GroupActivity groupActivity = groupActivityRepository.findOne(request.getUnmaskedId());
    if (groupActivity == null)
    {
      throw new ValidationException(String.format("GroupActivity[%s] not found.", request.getId()));
    }
    try
    {
      groupActivityRepository.saveAndFlush(request.toEntity(groupActivity));
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return groupActivity;
  }

  public List<GroupActivity> list(GroupActivityRequest request)
  {
    Set<Center> centers = new HashSet<>(getUserCenters());
    if (request.getCenterId() != null)
    {
      centers = centers.stream().filter(c -> c.getId().equals(request.getUnmaskedCenterId())).collect(Collectors.toSet());
    }
    if (centers.size() == 0)
    {
      throw new ValidationException(String.format("Unauthorized access to center[%s] user[%s]", request.getUnmaskedCenterId(), getUser().getEmail()));
    }
    List<ProgramGroup> groups = programGroupRepository.findAll();
    if (request.getGroupId() != null)
    {
      groups = groups.stream().filter(f -> f.getId().equals(request.getUnmaskedGroupId())).collect(Collectors.toList());
    }

    //calculate start and end date
    LocalDate end = LocalDate.now();
    LocalDate start = end.minusDays(days);

    return groupActivityRepository.findByCenterInAndGroupInAndDateBetween(centers, groups, start.toDate(), end.toDate());
  }
}
