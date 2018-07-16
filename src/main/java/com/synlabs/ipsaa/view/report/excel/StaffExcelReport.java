package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.view.staff.StaffFilterRequest;
import com.synlabs.ipsaa.view.staff.StaffResponse;
import com.synlabs.ipsaa.view.staff.StaffSummaryResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


public class StaffExcelReport {
    List<EmployeeSalary> staff;

    StaffFilterRequest staffRequest;
    String path;
    public StaffExcelReport(List<EmployeeSalary> staff,StaffFilterRequest staffRequest,String path){
        this.staff=staff;
        this.path=path;
        this.staffRequest=staffRequest;
    }
    public File createExcel(){
        File file = new File(path + UUID.randomUUID() + ".xlsx");
        if (!file.exists())
        {
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                SXSSFWorkbook workbook = new SXSSFWorkbook();
                Sheet feeCollectionReportSheet = workbook.createSheet("response");// creating a blank sheet
                int rownum = 0;
                for (EmployeeSalary satff : this.staff)
                {
                    Row row = feeCollectionReportSheet.createRow(rownum++);
                    if (rownum == 1) {
                        setupheaders(row);
                        rownum++;
                    } else
                        createList(satff, row);
                }
                workbook.write(fileOutputStream);
                workbook.dispose();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;
    }
    private static void createList(EmployeeSalary staffR, Row row) // creating cells for each row
    {
        Cell cell = row.createCell(1);
        cell.setCellValue(staffR.getEmployee().getCostCenter().getName());

        cell = row.createCell(2);
        if(staffR.getEmployee().getFirstName()!=null)
            cell.setCellValue(staffR.getEmployee().getFirstName()+" "+staffR.getEmployee().getLastName());

        cell = row.createCell(3);
        if(staffR.getEmployee().getDesignation()!=null)
            cell.setCellValue(staffR.getEmployee().getDesignation());

        cell = row.createCell(5);
        if(staffR.getEmployee().getEmail()!=null)
            cell.setCellValue(staffR.getEmployee().getEmail());

        cell = row.createCell(6);
        if(staffR.getEmployee().getEid()!=null)
            cell.setCellValue(staffR.getEmployee().getEid());

        cell = row.createCell(7);
        if(staffR.getEmployee().getMobile()!=null)
            cell.setCellValue(staffR.getEmployee().getMobile());


        cell = row.createCell(9,Cell.CELL_TYPE_STRING);
        if(staffR.getEmployee().getProfile().getBan()!=null){
            System.out.println(staffR.getEmployee().getProfile().getBan());
            cell.setCellValue(staffR.getEmployee().getProfile().getBan());
        }


        cell = row.createCell(10,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getCtc()!=null){
            System.out.println(staffR.getCtc());
            cell.setCellValue((staffR.getCtc().intValue()));
        }
        cell = row.createCell(11,Cell.CELL_TYPE_STRING);
            cell.setCellValue((staffR.isPfd()));

        cell = row.createCell(12,Cell.CELL_TYPE_STRING);
        cell.setCellValue((staffR.isEsid()));

        cell = row.createCell(13,Cell.CELL_TYPE_STRING);
        cell.setCellValue((staffR.isProfd()));

        cell = row.createCell(14,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getBasic()!=null)
        cell.setCellValue((staffR.getBasic()).intValue());
        cell = row.createCell(15,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getHra()!=null)
        cell.setCellValue((staffR.getHra()).intValue());
        cell = row.createCell(16,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getConveyance()!=null)
        cell.setCellValue((staffR.getConveyance().intValue()));
        cell = row.createCell(17,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getBonus()!=null)
        cell.setCellValue((staffR.getBonus().intValue()));
        cell = row.createCell(18,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getGrossSalary()!=null)
        cell.setCellValue((staffR.getGrossSalary()).intValue());

        cell = row.createCell(19,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getTotalDeduction()!=null)
        cell.setCellValue((staffR.getTotalDeduction()).intValue());
        cell = row.createCell(20,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getTotalEarning()!=null)
        cell.setCellValue((staffR.getTotalEarning().intValue()));

        cell = row.createCell(21,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getSpecial()!=null)
            cell.setCellValue((staffR.getSpecial().intValue()));
    }
    public void setupheaders( Row row){
        Cell cell = row.createCell(1);
        cell.setCellValue("CostCenter");

        cell = row.createCell(2);
            cell.setCellValue("Employee Name");

        cell = row.createCell(3);
            cell.setCellValue("Designation");

        cell = row.createCell(5);
            cell.setCellValue("Email");

        cell = row.createCell(6);
            cell.setCellValue("Employee Eid");

        cell = row.createCell(7);
            cell.setCellValue("Employee Mobile");
        cell = row.createCell(9,Cell.CELL_TYPE_STRING);
            System.out.println("ACCOUNT NO");
        cell.setCellValue("ACCOUNT NO");

        cell = row.createCell(10,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(("Ctc"));

        cell = row.createCell(11,Cell.CELL_TYPE_STRING);
        cell.setCellValue(("Pfd"));

        cell = row.createCell(12,Cell.CELL_TYPE_STRING);
        cell.setCellValue(("Esid"));

        cell = row.createCell(13,Cell.CELL_TYPE_STRING);
        cell.setCellValue(("Profd"));

        cell = row.createCell(14,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Basic"));
        cell = row.createCell(15,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Hra"));
        cell = row.createCell(16,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Conveyance"));
        cell = row.createCell(17,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Bonus"));
        cell = row.createCell(18,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("GrossSalary"));
        cell = row.createCell(19,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("TotalDeduction"));
        cell = row.createCell(20,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("TotalEarning"));

        cell = row.createCell(21,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Special"));
    }
}
