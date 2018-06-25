package com.synlabs.ipsaa.view.food;

import com.synlabs.ipsaa.entity.food.FoodMenu;
import com.synlabs.ipsaa.view.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FoodMenuPageResponse extends PageResponse
{
  private List<FoodMenuResponse> foodMenuList = new ArrayList<>();

  public FoodMenuPageResponse(int pageSize, int pageNumber, int totalPages, List<FoodMenuResponse> foodMenuList)
  {
    super(pageSize, pageNumber, totalPages);
    this.foodMenuList = foodMenuList;
  }

  public FoodMenuPageResponse(Page<FoodMenu> page)
  {
    super(page);
    this.foodMenuList = page.getContent().stream().map(FoodMenuResponse::new).collect(Collectors.toList());

  }

  public List<FoodMenuResponse> getFoodMenuList()
  {
    return this.foodMenuList;
  }
}
