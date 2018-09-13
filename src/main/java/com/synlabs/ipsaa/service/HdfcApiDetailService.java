package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.jpa.HdfcApiDetailRepository;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRepository;
import com.synlabs.ipsaa.view.hdfcGateWayDetails.HdfcApiDetailRequest;
import com.synlabs.ipsaa.view.hdfcGateWayDetails.HdfcApiDetailResponce;
import org.apache.poi.ss.usermodel.*;
import org.jxls.common.CellData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

@Service
public class HdfcApiDetailService {
    @Autowired
    HdfcApiDetailRepository hdfcApiDetailRepository;
    @Autowired
    private StudentFeePaymentRepository slipRepository;

    public HdfcApiDetails getDetailsByCenter(Center center){
       return hdfcApiDetailRepository.findByCenter(center);
    }
    public HdfcApiDetails findByOrderId(String orderId){
        StudentFeePaymentRequest request= slipRepository.findOneByTnxid(orderId);
        return getDetailsByCenter(request.getStudent().getCenter());
    }

    @Transactional
    public void upload() {
        String SAMPLE_XLSX_FILE_PATH ="C:/Users/shubham/Desktop/ipsaa/query/Live2.xlsx";
             try{
                 File file = new File(SAMPLE_XLSX_FILE_PATH);
                 FileInputStream inputStream = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(inputStream);
                 workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
                 Sheet sheet = workbook.getSheetAt(0);
                 Iterator<Row> iterator = sheet.iterator();
                 while (iterator.hasNext()) {

                     Row currentRow = iterator.next();
                     Iterator<Cell> cellIterator = currentRow.iterator();

                         Cell currentCell = cellIterator.next();
                         if(currentCell.getStringCellValue().equals("Merchant Name"))
                             continue;
                         currentCell = cellIterator.next();
                         HdfcApiDetails details=new HdfcApiDetails();
                         currentCell = cellIterator.next();
                         details.setMerchantName(currentCell.getStringCellValue());
                         currentCell = cellIterator.next();
                         details.setVsa(currentCell.getStringCellValue()+"");
                         currentCell = cellIterator.next();
                         if(currentCell.getCellType()==Cell.CELL_TYPE_NUMERIC)
                         details.setAccessCode(currentCell.getNumericCellValue()+"");
                         else
                             details.setAccessCode(currentCell.getStringCellValue());
                         currentCell=cellIterator.next();
                         details.setWorkingKey(currentCell.getStringCellValue());
                         currentCell=cellIterator.next();
                         details.setHdfcTid(currentCell.getStringCellValue());
                     System.out.println(details);
                     hdfcApiDetailRepository.save(details);
                 }
             }catch(Exception e){
                 e.printStackTrace();
             }
    }

    public HdfcApiDetailResponce save(HdfcApiDetailRequest request) {
        // add center left
       return new HdfcApiDetailResponce( hdfcApiDetailRepository.saveAndFlush(request.toEntity()));
    }
}
