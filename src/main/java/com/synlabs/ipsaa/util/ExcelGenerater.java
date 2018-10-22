package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.view.report.excel.FeeCollectionExcelReport2;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExcelGenerater {
    List<Map<String, Object>> list;

    public ExcelGenerater(List<Map<String, Object>> list) {
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

    private boolean makeList(List<Map<String, Object>> list, int i, Sheet feeCollectionReportSheet) {
        int rowCount=i;
        for(Map<String ,Object > rowData:list){
            Row row = feeCollectionReportSheet.createRow(rowCount++);
            int count = 0;
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                Cell cell = row.createCell(count++);
                cell.setCellValue(entry.getValue()+"");
            }
        }
        return true;
    }

    private void createHeader(Map<String, Object> list, int i, Sheet feeCollectionReportSheet) {
        Row row = feeCollectionReportSheet.createRow(i);
        int count = 0;
        for (Map.Entry<String, Object> entry : list.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            Cell cell = row.createCell(count++);
            cell.setCellValue(entry.getKey().toUpperCase());
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
