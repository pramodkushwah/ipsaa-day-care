package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.ProgramService;
import com.synlabs.ipsaa.view.center.ProgramGroupRequest;
import com.synlabs.ipsaa.view.center.ProgramGroupResponse;
import com.synlabs.ipsaa.view.center.ProgramRequest;
import com.synlabs.ipsaa.view.center.ProgramResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.PROGRAM_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.PROGRAM_WRITE;

@RestController
@RequestMapping("/api/")
public class ProgramController
{
  @Autowired
  private ProgramService programService;

  @GetMapping(path = "program")
  @Secured(PROGRAM_READ)
  public List<ProgramResponse> listPrograms()
  {
    return programService.listPrograms().stream().map(ProgramResponse::new).collect(Collectors.toList());
  }

  @Secured(PROGRAM_WRITE)
  @PostMapping(path = "program")
  public ProgramResponse createProgram(@RequestBody ProgramRequest request)
  {
    return new ProgramResponse(programService.saveProgram(request));
  }

  @Secured(PROGRAM_WRITE)
  @PutMapping(path = "program")
  public ProgramResponse updateProgram(@RequestBody ProgramRequest request)
  {
    return new ProgramResponse(programService.updateProgram(request));
  }

  @Secured(PROGRAM_WRITE)
  @DeleteMapping(path = "program/{programId}")
  public void deleteProgram(@PathVariable Long programId)
  {
    programService.deleteProgram(new ProgramRequest(programId));
  }

  @Secured(PROGRAM_READ)
  @GetMapping(path = "group")
  public List<ProgramGroupResponse> listGroups()
  {
    return programService.listGroups().stream().map(ProgramGroupResponse::new).collect(Collectors.toList());
  }

  @Secured(PROGRAM_WRITE)
  @PostMapping(path = "group")
  public ProgramGroupResponse createGroup(@RequestBody @Validated ProgramGroupRequest request, BindingResult result)
  {

    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    return new ProgramGroupResponse(programService.saveGroup(request.toEntity()));
  }

  @Secured(PROGRAM_WRITE)
  @PutMapping(path = "group")
  public ProgramGroupResponse updateGroup(@RequestBody @Valid ProgramGroupRequest request, BindingResult result)
  {
    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    ProgramGroup group = programService.getGroup(request.getId());

    if (group == null)
    {
      throw new NotFoundException("Missing group");
    }

    return new ProgramGroupResponse(programService.updateGroup(request.toEntity(group)));
  }

  @Secured(PROGRAM_WRITE)
  @DeleteMapping(path = "group/{groupId}")
  public void deleteGroup(@PathVariable Long groupId)
  {
    programService.deleteGroup(new ProgramGroupRequest(groupId));
  }
}
