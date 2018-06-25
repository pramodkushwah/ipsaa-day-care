package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.center.Holiday;
import com.synlabs.ipsaa.view.center.HolidayResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ttn on 11/6/17.
 */
public class HolidayPageResponse extends PageResponse
{
  private List<HolidayResponse> holidayList = new ArrayList<>();

  public HolidayPageResponse(int pageSize, int pageNumber, int totalPages, List<HolidayResponse> holidayList)
  {
    super(pageSize, pageNumber, totalPages);
    this.holidayList = holidayList;
  }

  public HolidayPageResponse(Page<Holiday> page)
  {
    super(page);
    this.holidayList = page.getContent().stream().map(HolidayResponse::new).collect(Collectors.toList());

  }

  public List<HolidayResponse> getHolidayList()
  {
    return holidayList;
  }
}
