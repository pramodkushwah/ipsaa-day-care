package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.service.EmployeeService;
import com.synlabs.ipsaa.service.StudentService;
import com.synlabs.ipsaa.service.UserService;
import com.synlabs.ipsaa.view.center.CenterListRequest;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.center.CenterResponse;
import com.synlabs.ipsaa.view.center.CenterResponseV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

@RestController
@RequestMapping("/api/center/")
public class CenterController
{
  @Autowired
  private CenterService centerService;


  @GetMapping
  @Secured(CENTER_READ)
  public List<CenterResponse> list(@RequestParam(required = false, name = "zone") String zone,
                                   @RequestParam(required = false, name = "city") String city)
  {
    return centerService.list(new CenterListRequest(zone, city)).stream().map(CenterResponse::new).collect(Collectors.toList());
  }

  @GetMapping("all")
  @Secured(ADMIN_CENTER_LIST_READ)
  public List<CenterResponse> allCenters()
  {
    return centerService.listAll().stream().map(CenterResponse::new).collect(Collectors.toList());
  }

  @Secured(CENTER_WRITE)
  @PostMapping
  public CenterResponse createCenter(@RequestBody @Validated CenterRequest request, BindingResult result)
  {
    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    return new CenterResponse(centerService.createCenter(request));
  }

  @Secured(CENTER_WRITE)
  @PutMapping
  public CenterResponse updateCenter(@RequestBody @Validated CenterRequest request, BindingResult result)
  {
    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }

    Center center = centerService.updateCenter(request);
    return new CenterResponse(center);
  }

  @Secured(CENTER_DELETE)
  @DeleteMapping(path = "{centerId}")
  public void deleteCenter(@PathVariable Long centerId)
  {
    centerService.deleteCenter(new CenterRequest(centerId));
  }

  @GetMapping(path= "{stateId}")
  public List<CenterResponse> getCentersByState(@PathVariable(name = "stateId")  Long stateId){
     return centerService.getCenterByState(stateId).stream().map(CenterResponse::new).collect(Collectors.toList());
  }
}
