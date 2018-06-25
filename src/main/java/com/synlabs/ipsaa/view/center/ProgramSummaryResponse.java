package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.view.common.Response;

import java.util.List;
import java.util.stream.Collectors;

public class ProgramSummaryResponse implements Response
{

  private Long id;
  private String code;
  private String name;
  private String description;

  private List<String> groups;

  public ProgramSummaryResponse(Program program)
  {
    this.id = mask(program.getId());
    this.code = program.getCode();
    this.name = program.getName();
    this.description = program.getDescription();
    this.groups = program.getGroups() == null ? null : program.getGroups().stream().map(ProgramGroup::getName).collect(Collectors.toList());
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

  public List<String> getGroups()
  {
    return groups;
  }
}
