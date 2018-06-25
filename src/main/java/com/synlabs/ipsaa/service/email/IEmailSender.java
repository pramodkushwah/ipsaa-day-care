package com.synlabs.ipsaa.service.email;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.ftl.model.ApprovalModel;
import com.synlabs.ipsaa.view.msgs.MessageEmail;

import java.util.List;

public interface IEmailSender
{

  Boolean sendClockIn(StudentAttendance attendance, String... to);

  Boolean sendClockOut(StudentAttendance attendance, String... to);

  Boolean sendPassword(String password, String to);

  Boolean sendMessage(MessageEmail message);

  Boolean sendResetPassword(String token, String to);

  Boolean sendSupportQueryEmail(SupportQuery query);

  Boolean sendStudentApprovalEmail(Student student);

  Boolean sendStaffApprovalEmail(Employee employee);

  Boolean sendPendingApprovalEmail(List<ApprovalModel> approvalModels);

  Boolean sendStudentDeleteEmail(Student student);

  Boolean sendStaffDeleteEmail(Employee employee);
}
