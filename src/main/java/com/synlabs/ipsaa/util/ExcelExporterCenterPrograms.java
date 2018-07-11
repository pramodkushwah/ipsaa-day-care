package com.synlabs.ipsaa.util;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.view.staff.StaffResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporterCenterPrograms {

    List<CenterProgramFee> centers;

    public ExcelExporterCenterPrograms() {

    }
    public void createExcel(List<CenterProgramFee> centers){
        this.centers = centers;
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
        int rownum = 0;
        for (CenterProgramFee center : this.centers)
        {
            if(center.getCenter().getAddress().getCity().equals("Mumbai") ||center.getCenter().getAddress().getCity().equals("Gurgaon")) {
                Row row = sheet.createRow(rownum++);
                if (rownum == 1) {
                    setupheaders(row);
                    rownum++;
                } else
                    createList(center, row);
            }
        }
        try
        {
            FileOutputStream out = new FileOutputStream(new File("NewFile2.xlsx")); // file name with path
            workbook.write(out);
            out.close();

        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
    }
    private static void createList(CenterProgramFee centerprogram, Row row) // creating cells for each row
    {
            Cell cell = row.createCell(1);
            cell.setCellValue(centerprogram.getId());

        cell = row.createCell(2);
        if(centerprogram.getCenter()!=null)
            cell.setCellValue(centerprogram.getCenter().getName());


        cell = row.createCell(3);
        if(centerprogram.getProgram()!=null)
            cell.setCellValue(centerprogram.getProgram().getName());



        cell = row.createCell(4);
        if(centerprogram.getDeposit()>0)
            cell.setCellValue(centerprogram.getDeposit());

        cell = row.createCell(5);
            if(centerprogram.getFee()>0)
            cell.setCellValue(centerprogram.getFee());

            cell = row.createCell(6);
            if(centerprogram.getAnnualFee()>0)
            cell.setCellValue(centerprogram.getAnnualFee());

            cell = row.createCell(7);
            if(centerprogram.getCgst()!=null)
                cell.setCellValue(centerprogram.getCgst().toString());

            cell = row.createCell(8);
            if(centerprogram.getIgst()!=null)
                cell.setCellValue(centerprogram.getIgst().toString());


            cell = row.createCell(9);
            if(centerprogram.getSgst()!=null)
                cell.setCellValue(centerprogram.getSgst().toString());



    }
    public void setupheaders( Row row){
        Cell cell = row.createCell(1);
        cell.setCellValue("center program Id");

        cell = row.createCell(2);
        cell.setCellValue("Center Name");

        cell = row.createCell(3);
        cell.setCellValue("Program Name");



        cell = row.createCell(4);
        cell.setCellValue("Deposit");

        cell = row.createCell(5);
        cell.setCellValue("Fee");
        cell = row.createCell(6);
            cell.setCellValue(" AnnualFee");
        cell = row.createCell(7);
            cell.setCellValue("Cgst");
        cell = row.createCell(8);
            cell.setCellValue("Igst");
        cell = row.createCell(9);
            cell.setCellValue("Sgst");
        }
    }