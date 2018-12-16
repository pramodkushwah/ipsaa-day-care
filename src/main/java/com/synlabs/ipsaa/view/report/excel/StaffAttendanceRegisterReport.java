package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.util.Utils;
import com.synlabs.ipsaa.view.attendance.AttendanceReportRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class StaffAttendanceRegisterReport {

    List<StaffAttendanceReport> reports;
    String path;
    AttendanceReportRequest request;

    public StaffAttendanceRegisterReport(List<StaffAttendanceReport> report, String path,AttendanceReportRequest request){
        this.reports=report;
        this.path= path;
        this.request=request;
    }

    public File createExcel(){
        File file= new File(path+UUID.randomUUID() + ".xlsx");
        if(!file.exists()){
            try{
                file.createNewFile();
                FileOutputStream fileOutputStream= new FileOutputStream(file);
                SXSSFWorkbook workbook= new SXSSFWorkbook();
                Sheet attendanceRegisterSheet= workbook.createSheet("Attendance Register");

                ///To access other excel features which are not in SXSSF class.
                CreationHelper creationHelper= workbook.getCreationHelper();
                CellStyle cellStyle= workbook.createCellStyle();
                cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-mmm-yy"));

                int rownum=0;
                boolean isCreateList= true;
                Row row= null;
                Date from= request.getFrom();
                Date to= request.getTo();
                if(rownum == 0) {
                    Set<Date> dates = Utils.datesBetween(from, to, true);
                    row = attendanceRegisterSheet.createRow(0);
                    setUpHeaders(row, dates, cellStyle);
                    rownum++;
                }
                for(StaffAttendanceReport report: this.reports){
                        if(isCreateList == true)
                            row= attendanceRegisterSheet.createRow(rownum++);
                        isCreateList=createList(report,row,cellStyle);
                }
                //int rowNUm=workbook.getSheetAt(0).getPhysicalNumberOfRows();
                workbook.write(fileOutputStream);
                workbook.dispose();
                return file;
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return file;

    }

    public void setUpHeaders(Row row,Set<Date> dates,CellStyle style){

        int index=0;
        Cell cell= row.createCell(index++);
        cell.setCellValue("S.No");

        cell= row.createCell(index++);
        cell.setCellValue("Eid");
        cell= row.createCell(index++);
        cell.setCellValue("First Name");
        cell= row.createCell(index++);
        cell.setCellValue("Last Name");
        cell= row.createCell(index++);
        cell.setCellValue("Designation");
        cell= row.createCell(index++);
        cell.setCellValue("Center");
        cell= row.createCell(index++);
        cell.setCellValue("State");
        cell= row.createCell(index++);
        cell.setCellValue("Date of Joining");
        cell= row.createCell(index++);
        cell.setCellValue("Date Of Leaving");

       for(Date d:dates){
           cell= row.createCell(index++);
           cell.setCellValue(d);
           cell.setCellStyle(style);
       }

        cell= row.createCell(index++);
        cell.setCellValue("Presents");
        cell= row.createCell(index++);
        cell.setCellValue("Absents");
        cell= row.createCell(index++);
        cell.setCellValue("Leaves Taken");
        cell= row.createCell(index++);
        cell.setCellValue("Week Offs");
        cell= row.createCell(index++);
        cell.setCellValue("Holidays");
        cell= row.createCell(index++);
        cell.setCellValue("Half Days");

        cell= row.createCell(index++);
        cell.setCellValue("Total Paid Days");
//        cell= row.createCell(index++);
//        cell.setCellValue("Overtime");

    }

    public boolean createList(StaffAttendanceReport report,Row row,CellStyle style){

        int index=0;
        System.out.println(report.getEmployee().getName()+" "+report.getPresents()+" "+report.getHolidays()+" "+report.getWeekoff());
        System.out.println(index);
        if(report!= null){

            Cell cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(row.getRowNum());

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(report.getEmployee().getEid());

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(report.getEmployee().getFirstName());

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            String lname=report.getEmployee().getLastName();
            if(lname != null)
             cell.setCellValue(lname);

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(report.getEmployee().getDesignation());

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(report.getEmployee().getCostCenter().getName());

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(report.getEmployee().getProfile().getpState());

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            cell.setCellValue(report.getEmployee().getProfile().getDoj());
            cell.setCellStyle(style);

            cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
            if(report.getEmployee().getProfile().getDol() != null)
                cell.setCellValue(report.getEmployee().getProfile().getDol());
            cell.setCellStyle(style);

            for(Map.Entry<Date,String> dates:report.getCalendar().entrySet()){
                cell= row.createCell(index++,Cell.CELL_TYPE_STRING);
                cell.setCellValue(dates.getValue());
            }

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getPresents().floatValue());

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getAbsents().intValue());

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getLeaves().floatValue());

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getWeekoff().intValue());

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getHolidays().intValue());

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getHalfdays().intValue());

            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(report.getPaidDays().floatValue());   // Total paid days

//            cell= row.createCell(index++,Cell.CELL_TYPE_NUMERIC);
//            cell.setCellValue(report.getOverTime().longValue());
            System.out.println(row);
            return true;

        }
        return false;
    }
}
