package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.view.common.PageResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itrs on 4/14/2017.
 */
public class StudentPageResponse extends PageResponse
{
  private List<StudentSummaryResponse> students = new ArrayList<>();

  public StudentPageResponse(int pageSize, int pageNumber, int totalPages, List<StudentSummaryResponse> students)
  {
    super(pageSize, pageNumber, totalPages);
    this.students = students;
  }

  public List<StudentSummaryResponse> getStudents()
  {
    return students;
  }
}
