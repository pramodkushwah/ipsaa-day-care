package com.synlabs.ipsaa.view.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by itrs on 4/14/2017.
 */
public class PageResponse<T extends Response>
{
  private int     pageSize;
  private int     pageNumber;
  private int     totalPages;
  private List<T> list;
  public PageResponse(int pageSize, int pageNumber, int totalPages)
  {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.totalPages = totalPages;
  }
  public PageResponse(){

  }

  public PageResponse(Page<T> page)
  {
    this.pageSize = page.getSize();
    this.pageNumber = page.getNumber() + 1;
    this.totalPages = page.getTotalPages();
  }

  public PageResponse(int pageSize, int pageNumber, int totalPages,List<T> list)
  {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.totalPages = totalPages;
    this.list=list;
  }

  public List<T> getList()
  {
    return list;
  }

  public int getPageSize()
  {
    return pageSize;
  }

  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  public int getTotalPages()
  {
    return totalPages;
  }

  public void setTotalPages(int totalPages)
  {
    this.totalPages = totalPages;
  }
}
