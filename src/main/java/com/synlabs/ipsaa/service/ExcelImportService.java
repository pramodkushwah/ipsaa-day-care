package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.view.batchimport.ImportEmployee;
import com.synlabs.ipsaa.view.batchimport.ImportSalary;
import com.synlabs.ipsaa.view.batchimport.ImportMonthlySalary;
import org.jxls.reader.ReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishek on 9/4/17.
 */

@Service
public class ExcelImportService
{
  private static final Logger logger = LoggerFactory.getLogger(ExcelImportService.class);

  private final String IMPORT_EMPLOYEE_FILE = "staff.xml";

  private final String IMPORT_SALARY_FILE = "salary.xml";

  private final String IMPORT_STUDENT_FILE = "student-one.xml";

  private final String IMPORT_MONTHLY_SALARY_FILE = "monthly-salary.xml";

  private void fileProcessor(MultipartFile file, String filePath, Map<String, ArrayList> beans)
  {
    try
    {
      ReaderBuilder.buildFromXML(ExcelImportService.class.getClassLoader().getResourceAsStream(filePath)).read(new ByteArrayInputStream(file.getBytes()), beans);
    }
    catch (Exception exp)
    {
      logger.error("Error importing records : {}", exp);
    }
  }

  public List<ImportEmployee> importEmployeeRecords(MultipartFile file)
  {
    Map<String, ArrayList> beans = new HashMap<>();
    beans.put("employees", new ArrayList<ImportEmployee>());
    fileProcessor(file, IMPORT_EMPLOYEE_FILE, beans);
    return beans.get("employees");
  }

  public List<ImportSalary> importSalaryRecords(MultipartFile file)
  {
    Map<String, ArrayList> beans = new HashMap<>();
    beans.put("salaries", new ArrayList<ImportEmployee>());
    fileProcessor(file, IMPORT_SALARY_FILE, beans);
    return beans.get("salaries");
  }

  public List<ImportMonthlySalary> importMonthlySalaryRecords(MultipartFile file)
  {
    Map<String, ArrayList> beans = new HashMap<>();
    beans.put("monthlysalaries", new ArrayList<ImportMonthlySalary>());
    fileProcessor(file, IMPORT_MONTHLY_SALARY_FILE, beans);
    return beans.get("monthlysalaries");
  }

  public Map<String, ArrayList> importStudentRecords(MultipartFile file)
  {
    Map<String, ArrayList> beans = new HashMap<>();
    beans.put("students", new ArrayList<ImportEmployee>());
//    beans.put("parents", new ArrayList<ImportParent>());
    fileProcessor(file, IMPORT_STUDENT_FILE, beans);
    return beans;
  }

}
