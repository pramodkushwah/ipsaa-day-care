package com.synlabs.ipsaa.view.common;

import org.springframework.data.domain.Pageable;

/**
 * Created by itrs on 4/12/2017.
 */
public class PageRequest implements Request
{
  private Integer pageNumber;
  private Integer pageSize;

  public PageRequest(Integer pageNumber, Integer pageSize)
  {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
  }

  public Integer getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  public Integer getPageSize()
  {
    return pageSize;
  }

  public void setPageSize(Integer pageSize)
  {
    this.pageSize = pageSize;
  }

  public PageRequest(){}

  public Pageable getPageable(){
    return new org.springframework.data.domain.PageRequest(pageNumber-1,pageSize);
  }

}
