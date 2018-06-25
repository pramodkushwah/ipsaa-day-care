package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.ProgramGroupRepository;
import com.synlabs.ipsaa.jpa.ProgramRepository;
import com.synlabs.ipsaa.view.center.ProgramGroupRequest;
import com.synlabs.ipsaa.view.center.ProgramRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramService extends BaseService
{

  @Autowired
  private ProgramRepository repository;

  @Autowired
  private ProgramGroupRepository groupRepository;

  public List<Program> listPrograms()
  {
    return repository.findAll();
  }

  public List<ProgramGroup> listGroups() {
    return groupRepository.findAll();
  }

  @Transactional
  public ProgramGroup saveGroup(ProgramGroup group)
  {

    if (StringUtils.isEmpty(group.getName())) {
      throw new ValidationException("Missing code");
    }

    if (StringUtils.isEmpty(group.getDescription())) {
      throw new ValidationException("Missing description");
    }

    return groupRepository.saveAndFlush(group);
  }

  public ProgramGroup getGroup(Long groupId)
  {
    return groupRepository.getOne(groupId);
  }

  @Transactional
  public ProgramGroup updateGroup(ProgramGroup group)
  {

    if (StringUtils.isEmpty(group.getName())) {
      throw new ValidationException("Missing code");
    }

    if (StringUtils.isEmpty(group.getDescription())) {
      throw new ValidationException("Missing description");
    }

    return groupRepository.saveAndFlush(group);
  }

  @Transactional
  public void deleteGroup(ProgramGroupRequest request)
  {
    groupRepository.delete(request.getId());
  }

  @Transactional
  public Program saveProgram(ProgramRequest request)
  {

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ValidationException("Missing code");
    }

    if (StringUtils.isEmpty(request.getName())) {
      throw new ValidationException("Missing name");
    }

    if (StringUtils.isEmpty(request.getDescription())) {
      throw new ValidationException("Missing description");
    }

    if (repository.countByCode(request.getCode()) != 0) {
      throw new ValidationException("Duplicate Program code");
    }

    Program program = request.toEntity();

    if (request.getGroups() != null && !request.getGroups().isEmpty()) {
      List<ProgramGroup> dbgroups = new ArrayList<>();
      for (ProgramGroupRequest groupRequest : request.getGroups()) {
        dbgroups.add(groupRepository.getOne(groupRequest.getId()));
      }
      program.setGroups(dbgroups);
    }

    return repository.saveAndFlush(program);
  }

  public Program getProgram(Long id)
  {
    return repository.getOne(id);
  }

  @Transactional
  public Program updateProgram(ProgramRequest request)
  {
    Program program = repository.getOne(request.getId());
    if (program == null) {
      throw new NotFoundException("Missing program");
    }

    if (StringUtils.isEmpty(request.getCode())) {
      throw new ValidationException("Missing code");
    }

    if (StringUtils.isEmpty(request.getName())) {
      throw new ValidationException("Missing name");
    }

    if (StringUtils.isEmpty(request.getDescription())) {
      throw new ValidationException("Missing description");
    }

    program = request.toEntity(program);
    if (request.getGroups() != null && !request.getGroups().isEmpty()) {
      List<ProgramGroup> dbgroups = new ArrayList<>();
      for (ProgramGroupRequest groupRequest : request.getGroups()) {
        dbgroups.add(groupRepository.getOne(groupRequest.getId()));
      }
      program.setGroups(dbgroups);
    }
    return repository.saveAndFlush(program);
  }

  @Transactional
  public void deleteProgram(ProgramRequest request)
  {
    repository.delete(request.getId());
  }
}
