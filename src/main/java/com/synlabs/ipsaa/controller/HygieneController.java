package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.HygieneService;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.hygiene.HygieneRequest;
import com.synlabs.ipsaa.view.hygiene.HygieneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

/**
 * Created by itrs on 7/7/2017.
 */
@RestController
@RequestMapping("/api/hygiene/")
public class HygieneController
{
  @Autowired
  private HygieneService hygieneService;

  @GetMapping("/{centerId}")
  @Secured(HYGIENE_READ)
  public List<HygieneResponse> list(@PathVariable(name = "centerId") Long centerId){
    CenterRequest request=new CenterRequest();
    request.setId(centerId);
    return hygieneService.list(request).stream().map(HygieneResponse::new).collect(Collectors.toList());
  }

  @PostMapping
  @Secured(HYGIENE_WRITE)
  public HygieneResponse save(@RequestBody HygieneRequest request){
    return new HygieneResponse(hygieneService.save(request));
  }

  @PutMapping
  @Secured(HYGIENE_WRITE)
  public HygieneResponse update(@RequestBody HygieneRequest request){
    return new HygieneResponse(hygieneService.update(request));
  }

  @DeleteMapping("/{id}")
  @Secured(HYGIENE_WRITE)
  public void delete(@PathVariable(name = "id") Long id){
    HygieneRequest request=new HygieneRequest();
    request.setId(id);
    hygieneService.delete(request);
  }
}
