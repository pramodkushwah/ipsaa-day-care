package com.synlabs.ipsaa.util;
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
    List<LinkedHashMap<String,Object>> list=new ArrayList<>();

    public ExcelGenerater(List<LinkedHashMap<String,Object>>  list) {
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
                if(!list.isEmpty())
                {
                    createHeader(list.get(0), 0, feeCollectionReportSheet);
                    boolean isCreated = makeList(list, 1, feeCollectionReportSheet);
                    workbook.write(fileOutputStream);
                    workbook.dispose();
                }
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private boolean makeList(List<LinkedHashMap<String,Object>> list, int i, Sheet feeCollectionReportSheet) {
        int rowCount=i;

        for(LinkedHashMap<String,Object> col:list){
            Row row = feeCollectionReportSheet.createRow(rowCount++);
            int count = 0;
            for (Map.Entry<String, Object> entry : col.entrySet()){

                Cell cell = row.createCell(count++);
                cell.setCellValue(entry.getValue()+"");
            }
        }
        return true;
    }

    private void createHeader(LinkedHashMap<String,Object> list, int i, Sheet feeCollectionReportSheet) {
        Row row = feeCollectionReportSheet.createRow(i);
        int count = 0;

        for (Map.Entry<String, Object> entry : list.entrySet()){
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
