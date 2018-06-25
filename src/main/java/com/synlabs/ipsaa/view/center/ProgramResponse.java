package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.view.common.Response;

import java.util.List;
import java.util.stream.Collectors;

public class ProgramResponse implements Response
{

  private Long id;
  private String code;
  private String name;
  private String description;

  private List<ProgramGroupResponse> groups;

  public ProgramResponse(Program program)
  {
    this.id = mask(program.getId());
    this.code = program.getCode();
    this.name = program.getName();
    this.description = program.getDescription();
    this.groups = program.getGroups() == null ? null : program.getGroups().stream().map(ProgramGroupResponse::new).collect(Collectors.toList());
  }

  public Long getId()
  {
    return id;
  }

  public String getCode()
  {
    return code;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public List<ProgramGroupResponse> getGroups()
  {
    return groups;
  }
}
