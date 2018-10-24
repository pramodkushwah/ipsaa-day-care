package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.view.report.excel.FeeCollectionExcelReport2;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelGenerater {
    List<Stack<Pair<String, Object>>> list;

    public ExcelGenerater(List<Stack<Pair<String, Object>>> list) {
        this.list = list;
    }
    public File create(String exportDir, String sheetName) {
        File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                SXSSFWorkbook workbook = new SXSSFWorkbook();
                createStyle(workbook);
                Sheet feeCollectionReportSheet = workbook.createSheet(sheetName);// creating a blank sheet
                createHeader(list.get(0), 0, feeCollectionReportSheet);
                boolean isCreated = makeList(list, 1, feeCollectionReportSheet);
                workbook.write(fileOutputStream);
                workbook.dispose();
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private boolean makeList(List<Stack<Pair<String, Object>>> list, int i, Sheet feeCollectionReportSheet) {
        int rowCount=i;
        for(Stack<Pair<String, Object>> rowData:list){
            Row row = feeCollectionReportSheet.createRow(rowCount++);
            int count = 0;
            while(!rowData.isEmpty()){
                Pair<String,Object> data=rowData.pop();
                //System.out.println("Key = " + data.getKey() + ", Value = " + data.getValue());
                Cell cell = row.createCell(count++);
                cell.setCellValue(data.getValue()+"");
            }
        }
        return true;
    }

    private void createHeader(Stack<Pair<String, Object>> list, int i, Sheet feeCollectionReportSheet) {
        Row row = feeCollectionReportSheet.createRow(i);
        Stack<Pair<String, Object>> l= (Stack<Pair<String, Object>>) list.clone();
        int count = 0;
        while (!l.isEmpty()){
            Pair<String,Object> value=l.pop();
            //System.out.println("Key = " + value.getKey() + ", Value = " +  value.getValue());
            Cell cell = row.createCell(count++);
            cell.setCellValue(value.getKey().toUpperCase());
        }
    }

    protected void createStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
    }
}
