package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.jpa.*;
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
import java.util.List;

@Service
public class HdfcApiDetailService {
    @Autowired
    HdfcApiDetailRepository hdfcApiDetailRepository;
    @Autowired
    private StudentFeePaymentRepository slipRepository;
    @Autowired
    private StudentFeePaymentRequestIpsaaClubRepository IpsaaSlipRepository;
    @Autowired
    private CenterRepository centerRepository;

    private long DEFULT_GATEWAY_ID=33;

    public HdfcApiDetails getDetailsByCenter(Center center){
       return hdfcApiDetailRepository.findByCenter(center);
    }
    public HdfcApiDetails findByOrderId(String orderId){
        StudentFeePaymentRequest request= slipRepository.findOneByTnxid(orderId);
        if(request==null){

            StudentFeePaymentRequestIpsaaClub req=IpsaaSlipRepository.findOneByTnxid(orderId);
            return getDetailsByCenter(req.getStudent().getCenter());
        }else
            return getDetailsByCenter(request.getStudent().getCenter());
    }

    @Transactional
    public void upload() {
        String SAMPLE_XLSX_FILE_PATH ="C:/Users/shubham/Desktop/ipsaa/query/duplicate.xlsx";
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

                        String mname=currentCell.getStringCellValue();
                         currentCell = cellIterator.next();
                         Center center =centerRepository.findByCode(currentCell.getStringCellValue());
                         if(center!=null){
                             HdfcApiDetails details=new HdfcApiDetails();
                             details.setMerchantName(mname);
                             details.setCenter(center);
                             currentCell = cellIterator.next();
                             if(currentCell.getCellType()==Cell.CELL_TYPE_NUMERIC){
                                 details.setVsa(((int)currentCell.getNumericCellValue())+"");
                             }
                             else
                                 details.setVsa(Integer.parseInt(currentCell.getStringCellValue()+"")+"");

                             currentCell = cellIterator.next();
                             details.setAccessCode(currentCell.getStringCellValue());
                             currentCell=cellIterator.next();
                             details.setWorkingKey(currentCell.getStringCellValue());
                             currentCell=cellIterator.next();
                             details.setHdfcTid(((long)currentCell.getNumericCellValue())+"");
                             //System.out.println(details);
                             hdfcApiDetailRepository.save(details);
                         }else {
                             //System.out.println("center not found"+currentCell.getStringCellValue());
                         }
                 }
             }catch(Exception e){
                 e.printStackTrace();
             }
    }

    public HdfcApiDetailResponce save(HdfcApiDetailRequest request) {
        // add center left
       return new HdfcApiDetailResponce( hdfcApiDetailRepository.saveAndFlush(request.toEntity()));
    }

    public HdfcApiDetails findDefaultOne() {
        return hdfcApiDetailRepository.findOne(DEFULT_GATEWAY_ID);
    }

    public List<HdfcApiDetails> findAll() {
        return hdfcApiDetailRepository.findAll();
    }
}
