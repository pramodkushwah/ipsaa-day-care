package com.synlabs.ipsaa.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.entity.inquiry.QInquiryEventLog;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.view.inquiry.InquiryEventLogFilterRequest;
import com.synlabs.ipsaa.view.inquiry.InquiryReportRequest;
import com.synlabs.ipsaa.view.inquiry.InquiryRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class InquiryService extends BaseService
{

  @Autowired
  private InquiryRepository inquiryRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private ProgramRepository programRepository;

  @Autowired
  private ProgramGroupRepository programGroupRepository;

  @Autowired
  private InquiryEventLogRepository logRepository;

  @Autowired
  private EntityManager entityManager;

  @Value("${ipsaa.export.directory}")
  private String exportDir;

  @Transactional
  public Inquiry saveInquiry(InquiryRequest request)
  {
    if (hasCenter(request.getUnmaskedCetnerId()) == null)
    {
      throw new ValidationException(String.format("Center[id=%s] not found.", request.getCenterId()));
    }
    Center center = centerRepository.findOne(request.getUnmaskedCetnerId());
    Program program = programRepository.findOne(request.getUnmaskedProgramId());
    if (program == null)
    {
      throw new ValidationException(String.format("Program[id=%s] not found.", request.getProgramId()));
    }
    ProgramGroup group = programGroupRepository.findOne(request.getUnmaskedGroupId());
    if (group == null)
    {
      throw new ValidationException(String.format("Group[id=%s] not found.", request.getGroupId()));
    }
    Inquiry inquiry = inquiryRepository.findByInquiryNumber(request.getInquiryNumber());
    if (inquiry != null)
    {
      throw new ValidationException(String.format("Duplicate Inquiry Number[%s]", request.getInquiryNumber()));
    }
    inquiry = request.toEntity(null);
    inquiry.setCenter(center);
    inquiry.setProgram(program);
    inquiry.setGroup(group);
    inquiry.setStatus(CallDisposition.NewInquiry);
    inquiryRepository.saveAndFlush(inquiry);
    if (!request.getLog().isEmpty())
    {
      InquiryEventLog eventLog = request.getLog().toEntity(null);
      eventLog.setInquiry(inquiry);
      eventLog.setDone(false);
      inquiry.setStatus(eventLog.getCallDisposition());
      inquiryRepository.saveAndFlush(inquiry);
      logRepository.saveAndFlush(eventLog);
    }
    return inquiry;
  }

  @Transactional
  public Inquiry updateInquiry(InquiryRequest request)
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Inquiry id is null");
    }
    Inquiry inquiry = inquiryRepository.findOne(request.getUnmaskedId());
    if (inquiry == null)
    {
      throw new ValidationException(String.format("Inquiry[id=%s] not found", request.getId()));
    }
    List<InquiryEventLog> logs = inquiry.getLogs();
    request.toEntity(inquiry);
    if (request.getLog() != null && !request.getLog().isEmpty())
    {
      //Done all previous logs
      for (InquiryEventLog log : logs)
      {
        if (!log.isDone())
        {
          log.setDone(true);
          logRepository.saveAndFlush(log);
        }
      }
      InquiryEventLog eventLog = request.getLog().toEntity(null);
      eventLog.setInquiry(inquiry);
      eventLog.setDone(false);
      inquiry.setStatus(eventLog.getCallDisposition());
      logRepository.saveAndFlush(eventLog);
    }
    return inquiryRepository.saveAndFlush(inquiry);
  }

  public List<Inquiry> list(InquiryRequest request)
  {
    Long centerId = request.getUnmaskedCetnerId();
    if (centerId != null)
    {
      if (hasCenter(centerId) == null)
      {
        throw new ValidationException(String.format("Unauthorized access to center[%s] user[%s]", centerId, getUser().getEmail()));
      }
      Center center = centerRepository.findOne(centerId);
      return inquiryRepository.findByCenterAndStatusNotIn(center,
                                                          Arrays.asList(new CallDisposition[] {
                                                              CallDisposition.NotInterested, CallDisposition.Enrolled
                                                          }));
    }
    else
    {
      return inquiryRepository.findByCenterInAndStatusNotIn(getUserCenters(),
                                                            Arrays.asList(new CallDisposition[] {
                                                                CallDisposition.NotInterested, CallDisposition.Enrolled
                                                            }));
    }
  }

  public Inquiry getInquiry(InquiryRequest request)
  {
    if (request.getId() == null && StringUtils.isEmpty(request.getInquiryNumber()))
    {
      throw new ValidationException("Empty inquiry request.");
    }
    if (request.getId() != null)
    {
      Inquiry inquiry = inquiryRepository.findOne(request.getUnmaskedId());
      if (inquiry == null)
      {
        throw new ValidationException(String.format("Inquiry[id=%s] not found", request.getId()));
      }
      return inquiry;
    }
    else
    {
      Inquiry inquiry = inquiryRepository.findByInquiryNumber(request.getInquiryNumber());
      if (inquiry == null)
      {
        throw new ValidationException(String.format("Inquiry[inquiryNumber=%s] not found", request.getInquiryNumber()));
      }
      return inquiry;
    }
  }

  public List<InquiryEventLog> getFollowUps(InquiryEventLogFilterRequest request)
  {
    JPAQuery<InquiryEventLog> query = new JPAQuery<>(entityManager);
    QInquiryEventLog eventLog = QInquiryEventLog.inquiryEventLog;

    query.select(eventLog).from(eventLog);
    query.where(eventLog.done.isFalse());

//    adding call dispositions for followup
    query.where(eventLog.callDisposition.in(CollectionUtils.isEmpty(request.getDispositions())
                                            ? Arrays.asList(CallDisposition.Callback, CallDisposition.Followup)
                                            : request.getDispositions()));

    List<String> centerCodes = new ArrayList<>();
    if (CollectionUtils.isEmpty(request.getCenterCodes()))
    {
      getUserCenters().forEach((center) -> {
        centerCodes.add(center.getCode());
      });
    }
    else
    {
      request.getCenterCodes().forEach((centerCode) -> {
        if (!hasCenter(centerCode))
        {
          throw new ValidationException(String.format("Center[code=%s] not found for user[email=%s]", centerCode, getUser().getEmail()));
        }
        centerCodes.add(centerCode);
      });
    }
    query.where(eventLog.inquiry.center.code.in(centerCodes));

    if (request.getDate() != null)
    {
      LocalDate localDate = LocalDate.fromDateFields(request.getDate());
      query.where(eventLog.callBack.after(localDate.toDate()));
      query.where(eventLog.callBack.before(localDate.plusDays(1).toDate()));
    }
    else if (request.getFrom() != null || request.getTo() != null)
    {
      if (request.getFrom() != null)
      {
        query.where(eventLog.callBack.after(LocalDate.fromDateFields(request.getFrom()).toDate()));
      }
      if (request.getTo() != null)
      {
        query.where(eventLog.callBack.before(LocalDate.fromDateFields(request.getTo()).plusDays(1).toDate()));
      }
    }
    return query.fetch();
  }

  public File inquiryReport(InquiryReportRequest request) throws IOException
  {
    if (request.getFrom() == null)
    {
      throw new ValidationException("From date is required.");
    }
    if (request.getTo() == null)
    {
      throw new ValidationException("To date is required.");
    }
    Center center = hasCenter(request.getCenterId());
    if (center == null)
    {
      throw new ValidationException(String.format("Cannot locate Center[id=%s]", request.getMaskedCenterId()));
    }
    request.setCenterCode(center.getCode());
    Date from = LocalDate.fromDateFields(request.getFrom()).toDate();
    Date to = LocalDate.fromDateFields(request.getTo()).plusDays(1).toDate();
    List<Inquiry> inquiries = logRepository.findByCenterAndDate(center, from, to);

    //    2. adding fee to sheet
    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    createStyle(workbook);
    int rowNumber = 0;
    Sheet inquirySheet = workbook.createSheet("Inquiry");
    Row row = inquirySheet.createRow(rowNumber++);
    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Sr. No.");
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Enquiry Number");
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Date");
    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Status");
    row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Father's Name");
    row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Father's Mobile");
    row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Mother's Name");
    row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Mother's Mobile");
    row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Child's Name");
    row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Group");
    row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Enquiry Type");
    row.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("How they heard about ipsaa");
    row.createCell(12, Cell.CELL_TYPE_STRING).setCellValue("Comments");

    int count = 1;
    for (Inquiry inquiry : inquiries)
    {
      row = inquirySheet.createRow(rowNumber++);
      row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(count++);
      row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getInquiryNumber());
      row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(formatDate(inquiry.getInquiryDate()));
      row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getStatus() != null ? inquiry.getStatus().toString() : "");
      row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getFatherName());
      row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getFatherMobile());
      row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getMotherName());
      row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getMotherMobile());
      row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getChildName());
      row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getGroup()==null? " " :inquiry.getGroup().getName());
      row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getInquiryType().toString());
      row.createCell(11, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getLeadSource().toString());
      row.createCell(12, Cell.CELL_TYPE_STRING).setCellValue(inquiry.getFirstLog() == null ? ""
                                                                                           : inquiry.getFirstLog().getComment() == null ? ""
                                                                                                                                        : inquiry.getFirstLog().getComment());
    }

    workbook.write(fileOutputStream);
    workbook.dispose();
    return file;
  }
}
