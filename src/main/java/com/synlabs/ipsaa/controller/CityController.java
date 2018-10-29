package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.view.center.CityRequest;
import com.synlabs.ipsaa.view.center.CityResponse;
import com.synlabs.ipsaa.view.center.StateRequest;
import com.synlabs.ipsaa.view.center.StateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;

@RestController
@RequestMapping("/api/city/")
public class CityController
{
  @Autowired
  private CenterService centerService;

  @GetMapping
  @Secured(CENTER_READ)
  public List<CityResponse> list(@RequestParam(required = false, name = "zone") String zone)
  {
    return centerService.listCities(new CityRequest(zone)).stream().map(CityResponse::new).collect(Collectors.toList());
  }

  @GetMapping("{cityId}")
  @Secured(CENTER_READ)
  public CityResponse get(@PathVariable("cityId") Long id)
  {
    CityRequest request = new CityRequest();
    request.setId(id);
    return new CityResponse(centerService.getCity(request));
  }

  @PostMapping
  @Secured(CENTER_WRITE)
  public CityResponse save(@RequestBody CityRequest request)
  {
    return new CityResponse(centerService.saveCity(request));
  }

  @PutMapping
  @Secured(CENTER_WRITE)
  public CityResponse update(@RequestBody CityRequest request)
  {
    return new CityResponse(centerService.updateCity(request));
  }

  @DeleteMapping("{cityId}")
  @Secured(CENTER_WRITE)
  public void delete(@PathVariable("cityId") Long id)
  {
    CityRequest request = new CityRequest();
    request.setId(id);
    centerService.deleteCity(request);
  }

  @GetMapping("state/{stateId}")
  @Secured(CENTER_READ)
  public List<CityResponse> getByState(@PathVariable("stateId") Long stateId){
    return centerService.getCityByState(stateId).stream().map(CityResponse::new).collect(Collectors.toList());
  }


}
