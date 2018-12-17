package com.synlabs.ipsaa.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.Holiday;
import com.synlabs.ipsaa.entity.center.QHoliday;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.HolidayRepository;
import com.synlabs.ipsaa.view.center.HolidayFilterRequest;
import com.synlabs.ipsaa.view.center.HolidayRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ttn on 10/6/17.
 */

@Service
public class HolidayService extends BaseService
{

  @Autowired
  private UserService userService;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private HolidayRepository holidayRepository;

  @Autowired
  private EntityManager entityManager;

  public List<Holiday> list(HolidayFilterRequest request)
  {
    if (request.getYear() == null)
    {
      throw new ValidationException("Please select Year.");
    }

    JPAQuery<Holiday> query = new JPAQuery<>(entityManager);
    QHoliday holiday = QHoliday.holiday;
    query.select(holiday).from(holiday);

    if (request.getCenterId() != null)
    {
      query.where(holiday.centers.any().id.eq(request.getCenterId()));
    }

    String dateString = String.format("01-%s-%s",
                                      request.getMonth() == null ? "01" : request.getMonth(),
                                      request.getYear());
    try
    {
      LocalDate localDate = LocalDate.fromDateFields(pareDate(dateString,"dd-MM-yyyy"));
      //System.out.println(localDate.plusMonths(12-request.getMonth()+1).toDate());
      query.where(holiday.holidayDate.between(localDate.toDate(),localDate.plusMonths(12-request.getMonth()+1).toDate()));
    }
    catch (ParseException e)
    {
      throw new ValidationException("Invalid Date " + dateString);
    }

    return query.fetch();
  }

  public void delete(HolidayRequest holidayRequest) throws Exception
  {
    Holiday holiday = holidayRepository.getOne(holidayRequest.getId());
    if (holiday.getHolidayDate().before(LocalDate.now().toDate()))
    {
      throw new ValidationException("Holiday can't be deleted, date has passed");
    }
    holidayRepository.delete(holidayRequest.getId());
  }

  public Holiday save(HolidayRequest holidayRequest)
  {
    List<Center> centers;
    if (!CollectionUtils.isEmpty(holidayRequest.getCenters()))
    {
      centers = centerRepository.findByIdIn(holidayRequest.getCenters().stream().map(c->unmask(c)).collect(Collectors.toList()));
    }
    else
    {
      centers = userService.getUserCenters();
    }

   // Holiday holiday;
//    holiday=holidayRepository.findByHolidayDateAndName(holidayRequest.getHolidayDate(),holidayRequest.getName());
//    System.out.println(holiday);
//    if(holiday == null)
      Holiday holiday = new Holiday();

    holiday.setId(holidayRequest.getId());
    holiday.setCenters(centers);
    holiday.setFloating(holidayRequest.isFloating());
    holiday.setOptional(holidayRequest.isOptional());
    holiday.setName(holidayRequest.getName().toUpperCase());
    holiday.setHolidayDate(holidayRequest.getHolidayDate());

   // System.out.println(holiday);

    return holidayRepository.save(holiday);
  }
}
