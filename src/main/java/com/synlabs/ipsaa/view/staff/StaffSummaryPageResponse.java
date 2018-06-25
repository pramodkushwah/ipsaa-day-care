package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.view.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by itrs on 4/14/2017.
 */
public class StaffSummaryPageResponse extends PageResponse
{
  private List<StaffSummaryResponse> stafflist = new ArrayList<>();

  public StaffSummaryPageResponse(int pageSize, int pageNumber, int totalPages, List<StaffSummaryResponse> staffSummaries)
  {
    super(pageSize, pageNumber, totalPages);
    this.stafflist = staffSummaries;
  }

  public StaffSummaryPageResponse(Page<Employee> page)
  {
    super(page);
    this.stafflist = page.getContent().stream().map(StaffSummaryResponse::new).collect(Collectors.toList());

  }

  public List<StaffSummaryResponse> getStafflist()
  {
    return stafflist;
  }
}
