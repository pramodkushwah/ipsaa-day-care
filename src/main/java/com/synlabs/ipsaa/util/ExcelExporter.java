package com.synlabs.ipsaa.util;
import com.synlabs.ipsaa.view.staff.StaffResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.CellData;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class ExcelExporter {

    List<StaffResponse> staffs;

    public ExcelExporter() {

    }
    public void createExcel(List<StaffResponse> staffs){
        this.staffs = staffs;
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
        int rownum = 0;
        for (StaffResponse user : this.staffs)
        {
            Row row = sheet.createRow(rownum++);
            if(rownum == 1) {
                setupheaders(row);
                rownum++;
            }
                else
            createList(user, row);
        }
        try
        {
            FileOutputStream out = new FileOutputStream(new File("NewFile.xlsx")); // file name with path
            workbook.write(out);
            out.close();

        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
    }
    private static void createList(StaffResponse user, Row row) // creating cells for each row
    {
        Cell cell = row.createCell(1);

        if(user.getId()!=null)
        cell.setCellValue(user.getId());
        cell = row.createCell(2);
        if(user.getName()!=null)
        cell.setCellValue(user.getName());
        cell = row.createCell(3);
        if(user.getAadharNumber()!=null)
        cell.setCellValue(user.getAadharNumber());
        cell = row.createCell(4);
        if(user.getBiometricId()!=null)
        cell.setCellValue(user.getBiometricId());
        cell = row.createCell(5);
        if(user.getDesignation()!=null)
        cell.setCellValue(user.getDesignation());
        cell = row.createCell(6);
        if(user.getEid()!=null)
        cell.setCellValue(user.getEid());
        cell = row.createCell(7);
        if(user.getEmployerId()!=null)
        cell.setCellValue(user.getEmployerId());
        cell = row.createCell(8);
        if(user.getEmployerName()!=null)
        cell.setCellValue(user.getEmployerName());
        cell = row.createCell(9);
        if(user.getExpectedHours()!=null)
        cell.setCellValue(user.getExpectedHours());
        cell = row.createCell(10);
        if(user.getExpectedIn()!=null)
        cell.setCellValue(user.getExpectedIn().toString());
        cell = row.createCell(11);
        if(user.getExpectedOut()!=null)
        cell.setCellValue(user.getExpectedOut().toString());
        cell = row.createCell(13);
        if(user.getMobile()!=null)
        cell.setCellValue(user.getMobile());
        cell = row.createCell(14);
        if(user.getReportingManagerId()!=null)
        cell.setCellValue(user.getReportingManagerId());
        cell = row.createCell(15);
        if(user.getReportingManagerName()!=null)
        cell.setCellValue(user.getReportingManagerName());
        cell = row.createCell(16);
        if(user.getSecondaryNumbers()!=null)
        cell.setCellValue(user.getSecondaryNumbers());
        cell = row.createCell(17);
        if(user.getEmail()!=null)
        cell.setCellValue(user.getEmail());
        cell = row.createCell(18);
        if(user.getImagePath()!=null)
        cell.setCellValue(user.getImagePath());
        cell = row.createCell(19);
        if(user.getApprovalStatus()!=null)
        cell.setCellValue(user.getApprovalStatus().name());
        cell = row.createCell(20);
        if(user.getType()!=null)
        cell.setCellValue(user.getType().name());
        cell = row.createCell(21);
        if(user.getMaritalStatus()!=null)
        cell.setCellValue(user.getMaritalStatus().name());
        cell = row.createCell(22);
        cell.setCellValue(user.isAttendanceEnabled());
        cell = row.createCell(23);
        cell.setCellValue(user.isActive());
        cell = row.createCell(24);
        cell.setCellValue(user.isPayrollEnabled());
        cell = row.createCell(25);

        // profile
        if(user.getProfile()!=null){
            if(user.getProfile().getBan()!=null)
                cell.setCellValue(user.getProfile().getBan());
            cell = row.createCell(26);
            if(user.getProfile().getDob()!=null)
                cell.setCellValue(user.getProfile().getDob().toString());
            cell = row.createCell(27);
            if(user.getProfile().getDoj()!=null)
                cell.setCellValue(user.getProfile().getDoj());
            cell = row.createCell(28);
            if(user.getProfile().getDol()!=null)
                cell.setCellValue(user.getProfile().getDol());
            cell = row.createCell(29);
            if(user.getProfile().getEsin()!=null)
                cell.setCellValue(user.getProfile().getEsin());
            cell = row.createCell(30);
            if(user.getProfile().getPan()!=null)
                cell.setCellValue(user.getProfile().getPan());
            cell = row.createCell(31);
            if(user.getProfile().getPfan()!=null)
                cell.setCellValue(user.getProfile().getPfan());
            cell = row.createCell(32);
            if(user.getProfile().getPran()!=null)
                cell.setCellValue(user.getProfile().getPran());
            cell = row.createCell(33);
            if(user.getProfile().getUan()!=null)
                cell.setCellValue(user.getProfile().getUan());
            cell = row.createCell(34);
            if(user.getProfile().getGender().name()!=null)
                cell.setCellValue(user.getProfile().getGender().name());
            cell = row.createCell(35);
            if(user.getProfile().getImagePath()!=null)
                cell.setCellValue(user.getProfile().getImagePath());

        }




        // address
        cell = row.createCell(36);
        if(user.getProfile().getAddress()!=null){
            if(user.getProfile().getAddress().getAddress()!=null)
                cell.setCellValue(user.getProfile().getAddress().getAddress());
            cell = row.createCell(37);
            if(user.getProfile().getAddress().getCity()!=null)
                cell.setCellValue(user.getProfile().getAddress().getCity());
            cell = row.createCell(38);
            if(user.getProfile().getAddress().getPhone()!=null)
                cell.setCellValue(user.getProfile().getAddress().getPhone());
            cell = row.createCell(39);
            if(user.getProfile().getAddress().getState()!=null)
                cell.setCellValue(user.getProfile().getAddress().getState());
            cell = row.createCell(40);
            if(user.getProfile().getAddress()!=null)
                cell.setCellValue(user.getProfile().getAddress().getState());
            cell = row.createCell(41);
            if(user.getProfile().getAddress().getZipcode()!=null)
                cell.setCellValue(user.getProfile().getAddress().getZipcode());
            cell = row.createCell(42);
            if(user.getProfile().getAddress().getAddressType()!=null)
                cell.setCellValue(user.getProfile().getAddress().getAddressType().name());
        }


        // cost
        if(user.getCostCenter()!=null){
            if(user.getCostCenter().getCapacity()>0)
                cell = row.createCell(43);
            cell.setCellValue(user.getCostCenter().getCapacity());
            cell = row.createCell(44);
            if(user.getCostCenter().getCode()!=null)
                cell.setCellValue(user.getCostCenter().getCode());
            cell = row.createCell(45);
            if(user.getCostCenter().getId()!=null)
                cell.setCellValue(user.getCostCenter().getId());
            cell = row.createCell(46);
            if(user.getCostCenter().getName()!=null)
                cell.setCellValue(user.getCostCenter().getName());
            cell = row.createCell(47);
            if(user.getCostCenter().getType()!=null)
                cell.setCellValue(user.getCostCenter().getType());
            cell = row.createCell(48);
            cell.setCellValue(user.getCostCenter().isActive());
            cell = row.createCell(49);
            if(user.getCostCenter().getName()!=null)
                cell.setCellValue(user.getCostCenter().getZone().getName());
            if(user.getCostCenter().getZone()!=null)
                for(int i=0;i<user.getCostCenter().getZone().getCities().size();i++){
                    cell = row.createCell(50+i);
                    if(user.getCostCenter().getZone().getCities()!=null)
                        cell.setCellValue(user.getCostCenter().getZone().getCities().get(i).getName());
                }
        }

    }
    public void setupheaders( Row row){
        Cell cell = row.createCell(1);

            cell.setCellValue("user id");
        cell = row.createCell(2);
            cell.setCellValue("user Name");
        cell = row.createCell(3);
            cell.setCellValue("AadharNumber");
        cell = row.createCell(4);
            cell.setCellValue("BiometricId");
        cell = row.createCell(5);
            cell.setCellValue("Designation");
        cell = row.createCell(6);
            cell.setCellValue("Eid");
        cell = row.createCell(7);
            cell.setCellValue("EmployerId");
        cell = row.createCell(8);
            cell.setCellValue("EmployerName");
        cell = row.createCell(9);
            cell.setCellValue("ExpectedHours");
        cell = row.createCell(10);
            cell.setCellValue("ExpectedIn");
        cell = row.createCell(11);
            cell.setCellValue("ExpectedOut");

        cell = row.createCell(13);
            cell.setCellValue("Mobile");
        cell = row.createCell(14);
            cell.setCellValue("ReportingManager");
        cell = row.createCell(15);
            cell.setCellValue("ReportingManagerName");
        cell = row.createCell(16);
            cell.setCellValue("SecondaryNumbers");
        cell = row.createCell(17);
            cell.setCellValue("Email");
        cell = row.createCell(18);
            cell.setCellValue("ImagePath");
        cell = row.createCell(19);
            cell.setCellValue("ApprovalStatus");
        cell = row.createCell(20);
            cell.setCellValue("Type()");
        cell = row.createCell(21);
            cell.setCellValue("MaritalStatus()");
        cell = row.createCell(22);
        cell.setCellValue("AttendanceEnabled");
        cell = row.createCell(23);
        cell.setCellValue("Active");
        cell = row.createCell(24);
        cell.setCellValue("PayrollEnabled");
        cell = row.createCell(25);

        // profile
        //if(user.getProfile()!=null){


       // if(user.getProfile().getBan()!=null)
            cell.setCellValue("Ban");
        cell = row.createCell(26);
       // if(user.getProfile().getDob()!=null)
            cell.setCellValue("Dob");
        cell = row.createCell(27);
       // if(user.getProfile().getDoj()!=null)
            cell.setCellValue("Doj");

        cell = row.createCell(28);
       // if(user.getProfile().getDol()!=null)
            cell.setCellValue("Dol");
        cell = row.createCell(29);
      //  if(user.getProfile().getEsin()!=null)
            cell.setCellValue("Esin");
        cell = row.createCell(30);
      //  if(user.getProfile().getPan()!=null)
            cell.setCellValue("Pan");
        cell = row.createCell(31);
     //   if(user.getProfile().getPfan()!=null)
            cell.setCellValue("Pfan");
        cell = row.createCell(32);
    //    if(user.getProfile().getPran()!=null)
            cell.setCellValue("Pran");
        cell = row.createCell(33);
  //      if(user.getProfile().getUan()!=null)
            cell.setCellValue("Uan");
        cell = row.createCell(34);
//        if(user.getProfile().getGender().name()!=null)
            cell.setCellValue("Gender");
        cell = row.createCell(35);
            cell.setCellValue("ImagePath");




        // address
        cell = row.createCell(36);

            cell.setCellValue("Address");
        cell = row.createCell(37);
            cell.setCellValue("City");
        cell = row.createCell(38);
            cell.setCellValue("Phone");
        cell = row.createCell(39);
            cell.setCellValue("state");
        cell = row.createCell(40);
            cell.setCellValue("State");
        cell = row.createCell(41);
            cell.setCellValue("Zipcode");
        cell = row.createCell(42);
            cell.setCellValue("Address Type");

        // cost
            cell = row.createCell(43);
        cell.setCellValue("Capacity");
        cell = row.createCell(44);
            cell.setCellValue("Code");
        cell = row.createCell(45);
            cell.setCellValue("CostCenter");
        cell = row.createCell(46);
            cell.setCellValue("CostCenter Name");
        cell = row.createCell(47);
            cell.setCellValue("CostCenter Type");
        cell = row.createCell(48);
        cell.setCellValue("CostCenter Active");
        cell = row.createCell(49);
            cell.setCellValue("CostCenter  Zone Name");
                cell = row.createCell(50);
                    cell.setCellValue("CostCenter Zone Cities Name");
            }
    }