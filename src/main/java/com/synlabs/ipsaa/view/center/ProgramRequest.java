package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.view.common.Request;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.Set;

public class ProgramRequest implements Request
{
  private Long id;

  @NotBlank
  @Size(min = 2,max = 20)
  private String code;

  @NotBlank
  @Size(min = 2,max = 50)
  private String name;

  @NotBlank
  @Size(min = 2,max = 200)
  private String description;

  private Set<ProgramGroupRequest> groups;

  public ProgramRequest()
  {
  }

  public ProgramRequest(Long id)
  {
    this.id = id;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public Program toEntity()
  {
    return toEntity(null);
  }

  public Set<ProgramGroupRequest> getGroups()
  {
    return groups;
  }

  public void setGroups(Set<ProgramGroupRequest> groups)
  {
    this.groups = groups;
  }

  public Program toEntity(Program program)
  {
    if (program == null) program = new Program();
    program.setCode(this.code);
    program.setName(this.name);
    program.setDescription(this.description);

    return program;
  }
}
