package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.food.FoodMenu;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.FoodMenuRepository;
import com.synlabs.ipsaa.jpa.UserRepository;
import com.synlabs.ipsaa.view.batchimport.ImportMonthlySalary;
import com.synlabs.ipsaa.view.food.FoodMenuFilterRequest;
import com.synlabs.ipsaa.view.food.FoodMenuRequest;
import com.synlabs.ipsaa.view.staff.ErrorPayslipResponce;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ttn on 2/7/17.
 */

@Service
public class FoodMenuService extends BaseService
{
  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FoodMenuRepository foodMenuRepository;

  @Transactional
  public List<FoodMenu> saveList(List<FoodMenuRequest> list)
  {
    Set<String> centerCodes = new HashSet<>();
    Map<String, Center> centerMap = new HashMap<>();
    List<FoodMenu> foodMenus = new ArrayList<>();
    for (FoodMenuRequest menu : list)
    {
      centerCodes.add(menu.getCenterCode());
    }
    for (String centerCode : centerCodes)
    {
      Center center = centerRepository.findByCode(centerCode);
      if (center == null)
      {
        throw new ValidationException(String.format("Center[code=%s] not found", centerCode));
      }
      centerMap.put(centerCode, center);
    }
    for (FoodMenuRequest menu : list)
    {
      FoodMenu foodMenu = null;
      try
      {
        foodMenu = menu.toEntity(null);
      }
      catch (ParseException e)
      {
        e.printStackTrace();
        throw new ValidationException(String.format("Invalid Date Format[%s]", menu.getDate()));
      }
      foodMenu.setCenter(centerMap.get(menu.getCenterCode()));
      foodMenuRepository.saveAndFlush(foodMenu);
      foodMenus.add(foodMenu);
    }
    return foodMenus;
  }

  @Transactional
  public List<FoodMenu> updateList(List<FoodMenuRequest> list)
  {
    List<FoodMenu> foodMenus = new ArrayList<>();
    for (FoodMenuRequest menu : list)
    {
      if (menu.getId() == null)
      {
        throw new ValidationException("FoodMenu id is null.");
      }
      FoodMenu one = foodMenuRepository.findOne(menu.getId());
      if (one == null)
      {
        throw new ValidationException(String.format("FoodMenu[id=%s] not fount.", menu.getMaskedId()));
      }
      try
      {
        menu.toEntity(one);
      }
      catch (ParseException e)
      {
        e.printStackTrace();
        throw new ValidationException(String.format("Invalid Date Format[%s]", menu.getDate()));
      }
      foodMenus.add(one);
      foodMenuRepository.saveAndFlush(one);
    }
    return foodMenus;
  }

  public List<FoodMenu> monthlyList(FoodMenuFilterRequest request)
  {
    if (request.getYear() == null)
    {
      throw new ValidationException("Year not found in request.");
    }
    if (request.getMonth() == null)
    {
      throw new ValidationException("Month not found in request.");
    }

    if (request.getUnmaskedZoneId() == null)
    {
      throw new ValidationException("Zoneid  not found in request.");
    }

    //calculate start and end date of month
    Calendar c = Calendar.getInstance();
    c.set(request.getYear().intValue(), request.getMonth().intValue(), 1, 0, 0);
    LocalDate start = LocalDate.fromDateFields(c.getTime());
    LocalDate end = start.dayOfMonth().withMaximumValue();

    List<FoodMenu> list;
    List<Center> centers = getUserCenters().stream().filter(center -> center.getZone().getId().equals(request.getUnmaskedZoneId())).collect(Collectors.toList());
    list = foodMenuRepository.findByCenterInAndDateBetween(centers, start.toDate(), end.toDate());

    return list;
  }

  @Autowired
  private ExcelImportService excelImportService;
  public Map<String,Object> uploadData(MultipartFile file, int month,int year, Long zoneId,String centerCode) {
   boolean isCenter=centerCode==null?false:true;
   Center center=null;
   if(isCenter){
    center= centerRepository.findByCode(centerCode);
    if(center==null)
      throw new ValidationException("center not found");
   }
    Calendar c = Calendar.getInstance();
    c.set(year,month, 1, 0, 0);
    LocalDate start = LocalDate.fromDateFields(c.getTime());
    LocalDate end = start.dayOfMonth().withMaximumValue();

    boolean errorInFile = false;
    Map<String, Object> statusMap = new HashMap<>();
    statusMap.put("error", "false");
    List<FoodMenuRequest> menus = excelImportService.importFoodMenuRecords(file);
    if(!menus.isEmpty()){
      for(FoodMenuRequest menu:menus) {

        if(isCenter){
            FoodMenu foodMenu= foodMenuRepository.findByCenterCodeAndDate(centerCode,start.toDate());
            if(foodMenu==null){
               foodMenu=new FoodMenu();
            }
          foodMenu.setLunch(menu.getLunch());
          foodMenu.setBreakfast(menu.getBreakfast());
          foodMenu.setDinner(menu.getDinner());
          foodMenu.setCenter(center);
          foodMenuRepository.saveAndFlush(foodMenu);
        }

        else {

        }
      }
    }
     statusMap.put("error", "true");
    return statusMap;
  }
}
