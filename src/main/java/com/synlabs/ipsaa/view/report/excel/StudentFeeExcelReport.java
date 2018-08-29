package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class StudentFeeExcelReport {
    List<StudentFeePaymentRequest> staff;

    StudentFeeRepository studentFeeRepository;
    public StudentFeeExcelReport(List<StudentFeePaymentRequest> staff, StudentFeeRepository studentFeeRepository){
        this.staff=staff;
        this.studentFeeRepository=studentFeeRepository;
    }
    public void createExcel(){
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank sheet
        int rownum = 0;
        for (StudentFeePaymentRequest satff : this.staff)
        {
                Row row = sheet.createRow(rownum++);
                if (rownum == 1) {
                    setupheaders(row);
                    rownum++;
                } else
                    createList(satff, row);
        }
        try
        {
            FileOutputStream out = new FileOutputStream(new File("studentSlipReport.xlsx")); // file name with path
            workbook.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void createList(StudentFeePaymentRequest staffR, Row row) // creating cells for each row
    {   StudentFee fee=null;
        if(staffR.getStudent()!=null)
        fee = studentFeeRepository.findByStudent(staffR.getStudent());
        Cell cell = row.createCell(1);
        cell.setCellValue(staffR.getStudent().getCenter().getName());

        cell = row.createCell(2);
        if(staffR.getStudent().getName()!=null)
            cell.setCellValue(staffR.getStudent().getName());

        cell = row.createCell(3);
        if(staffR.getStudent().getProgram().getName()!=null)
            cell.setCellValue(staffR.getStudent().getProgram().getName());

        cell = row.createCell(4);
        if(staffR.getStudent().getGroupName()!=null)
            cell.setCellValue(staffR.getStudent().getGroupName());


        cell = row.createCell(5,Cell.CELL_TYPE_STRING);
        if(staffR.getBaseFee()!=null)
            cell.setCellValue(staffR.getBaseFee().intValue());
//
//        cell = row.createCell(10,Cell.CELL_TYPE_NUMERIC);
//        if(staffR.get!=null){
//            System.out.println(staffR.getTransportFee().intValue());
//            cell.setCellValue((staffR.getTransportFee().intValue()));
//        }

        cell = row.createCell(6,Cell.CELL_TYPE_STRING);
        if(staffR.getAdjust()!=null)
            cell.setCellValue((staffR.getAdjust().intValue()));


        cell = row.createCell(7,Cell.CELL_TYPE_STRING);
        if(staffR.getAnnualFee()!=null)
        cell.setCellValue((staffR.getAnnualFee().intValue()));

        cell = row.createCell(8,Cell.CELL_TYPE_STRING);
        if(staffR.getBalance()!=null)
        cell.setCellValue((staffR.getBalance()).intValue());

        cell = row.createCell(9,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getExtraCharge()!=null)
        cell.setCellValue((staffR.getExtraCharge()).intValue());

        cell = row.createCell(10,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getDeposit()!=null)
        cell.setCellValue((staffR.getDeposit()).intValue());

//        cell = row.createCell(16,Cell.CELL_TYPE_NUMERIC);
//        if(staffR.getDiscount!=null)
//        cell.setCellValue((staffR.getConveyance().intValue()));

        cell = row.createCell(11,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getCgst()!=null)
        cell.setCellValue((staffR.getCgst().intValue()));

        cell = row.createCell(12,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getSgst()!=null)
        cell.setCellValue((staffR.getSgst()).intValue());

        cell = row.createCell(13,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getTotalFee()!=null)
        cell.setCellValue((staffR.getTotalFee()).intValue());

        cell = row.createCell(14,Cell.CELL_TYPE_NUMERIC);
        if(staffR.getTotalFee()!=null)
            cell.setCellValue((staffR.getPaymentStatus().name()));

        cell = row.createCell(15,Cell.CELL_TYPE_NUMERIC);
        if(fee!=null && fee.getTransportFee()!=null)
            cell.setCellValue((fee.getTransportFee().intValue()));
    }
    public void setupheaders( Row row){
        Cell cell = row.createCell(1);
        cell.setCellValue("CenterName");

        cell = row.createCell(2);
            cell.setCellValue("Student name");

        cell = row.createCell(3);
            cell.setCellValue("Program Name");

        cell = row.createCell(4);
            cell.setCellValue("GroupName");

        cell = row.createCell(5,Cell.CELL_TYPE_STRING);
            cell.setCellValue("BaseFee");
//
//        cell = row.createCell(10,Cell.CELL_TYPE_NUMERIC);
//        if(staffR.get!=null){
//            System.out.println(staffR.getTransportFee().intValue());
//            cell.setCellValue((staffR.getTransportFee().intValue()));
//        }

        cell = row.createCell(6,Cell.CELL_TYPE_STRING);
            cell.setCellValue(("Adjust"));


        cell = row.createCell(7,Cell.CELL_TYPE_STRING);

            cell.setCellValue(("AnnualFee"));

        cell = row.createCell(8,Cell.CELL_TYPE_STRING);
        cell.setCellValue("Balance");

        cell = row.createCell(9,Cell.CELL_TYPE_NUMERIC);

            cell.setCellValue(("ExtraCharge"));

        cell = row.createCell(10,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(("Deposit"));

//        cell = row.createCell(16,Cell.CELL_TYPE_NUMERIC);
//        if(staffR.getDiscount!=null)
//        cell.setCellValue((staffR.getConveyance().intValue()));

        cell = row.createCell(11,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(("Cgst"));

        cell = row.createCell(12,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(("Sgst"));

        cell = row.createCell(13,Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(("TotalFee"));
        cell = row.createCell(14,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Status"));
        cell = row.createCell(15,Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(("Transport Fee"));

    }
}
