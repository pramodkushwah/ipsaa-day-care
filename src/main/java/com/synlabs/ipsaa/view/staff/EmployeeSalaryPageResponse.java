package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.view.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ttn on 22/5/17.
 */
public class EmployeeSalaryPageResponse extends PageResponse
{
  private List<EmployeeSalaryResponse> employeeSalaryList = new ArrayList<>();

  public List<EmployeeSalaryResponse> getEmployeeSalaryList()
  {
    return employeeSalaryList;
  }
  public EmployeeSalaryPageResponse(int pageSize, int pageNumber, int totalPages, List<EmployeeSalaryResponse> employeeSalaryList)
  {
    super(pageSize, pageNumber, totalPages);
    this.employeeSalaryList = employeeSalaryList;
  }

  public EmployeeSalaryPageResponse(Page<EmployeeSalary> page)
  {
    super(page);
    this.employeeSalaryList = page.getContent().stream().map(EmployeeSalaryResponse::new).collect(Collectors.toList());

  }

}
