package com.synlabs.ipsaa;

import com.synlabs.ipsaa.service.FoodMenuService;
import com.synlabs.ipsaa.view.food.FoodMenuFilterRequest;
import com.synlabs.ipsaa.view.food.FoodMenuPageResponse;
import com.synlabs.ipsaa.view.food.FoodMenuRequest;
import com.synlabs.ipsaa.view.food.FoodMenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.PARENT;

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
 }
