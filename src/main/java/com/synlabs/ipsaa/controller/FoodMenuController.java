package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.FoodMenuService;
import com.synlabs.ipsaa.view.food.FoodMenuFilterRequest;
import com.synlabs.ipsaa.view.food.FoodMenuPageResponse;
import com.synlabs.ipsaa.view.food.FoodMenuRequest;
import com.synlabs.ipsaa.view.food.FoodMenuResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.PARENT;
import static com.synlabs.ipsaa.service.BaseService.unmask;

/**
 * Created by ttn on 2/7/17.
 */

@RestController
@RequestMapping("/api/food/")
public class FoodMenuController
{

  @Autowired
  private FoodMenuService foodMenuService;

  /////////////
  @Secured(CENTER_WRITE)
  @PostMapping(path = "savelist")
  public List<FoodMenuResponse> save(@RequestBody List<FoodMenuRequest> list)
  {
    return foodMenuService.saveList(list).stream().map(FoodMenuResponse::new).collect(Collectors.toList());
  }

  @Secured(CENTER_WRITE)
  @PutMapping(path = "savelist")
  public List<FoodMenuResponse> updateList(@RequestBody List<FoodMenuRequest> list)
  {
    return foodMenuService.updateList(list).stream().map(FoodMenuResponse::new).collect(Collectors.toList());
  }

  @Secured(CENTER_WRITE)
  @PostMapping(path = "monthlylist")
  public List<FoodMenuResponse> monthlyList(@RequestBody FoodMenuFilterRequest request)
  {
    return foodMenuService.monthlyList(request).stream().map(FoodMenuResponse::new).collect(Collectors.toList());
  }
  @Secured((CENTER_WRITE))

  @PostMapping(path="upload")
  public ResponseEntity<Map> uploadFoodMenu(@RequestParam("file")MultipartFile file, @RequestParam ("month")int month,@RequestParam ("zone")long zoneId){
    zoneId=unmask(zoneId);
    try {
      Map<String, Object> map = foodMenuService.uploadData(file,month,zoneId);
      String isSuccess = (String)map.get("error");
      map.remove("error");
      if (isSuccess.equalsIgnoreCase("true"))
      {
        throw new ValidationException("file not uploaded");
      }
      return new ResponseEntity<>(map,HttpStatus.OK);
    } catch (Exception e) {
      throw new ValidationException("file not uploaded");
    }
  }

 }
