package com.synlabs.ipsaa.view.report.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.jpa.EmployeePaySlipRepository;
import com.synlabs.ipsaa.view.staff.StaffFilterRequest;

public class StaffExcelReport {
	List<EmployeePaySlip> staff;

	StaffFilterRequest staffRequest;
	String path;
	int year;
	int month;
	private String er_code;

	public StaffExcelReport(List<EmployeePaySlip> staff, StaffFilterRequest staffRequest, String path,
			String er_code) {
		this.staff = staff;
		this.path = path;
		this.staffRequest = staffRequest;
		this.year = staffRequest.getYear();
		this.month = staffRequest.getMonth();
		this.er_code=er_code;
	}

	public File createExcel() {
		File file = new File(path + UUID.randomUUID() + ".xlsx");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				SXSSFWorkbook workbook = new SXSSFWorkbook();
				Sheet feeCollectionReportSheet = workbook.createSheet("SalarySlip");// creating a blank sheet
				CellStyle cellStyle=workbook.createCellStyle();
				CreationHelper creationHelper = workbook.getCreationHelper();
				cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
						"dd-mmm-yy"));


				int rownum = 0;
				boolean isCreateList = true;
				Row row = null;
				if (rownum == 0) {
					row=feeCollectionReportSheet.createRow(0);
					setupheaders(row);
					rownum++;
				}
				for (EmployeePaySlip satffSalary : this.staff) {
					if(isCreateList==true) {
						row = feeCollectionReportSheet.createRow(rownum++);
					}
					isCreateList = createList(satffSalary, row, cellStyle );
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

	private boolean createList(EmployeePaySlip staffR, Row row, CellStyle style) // creating cells for each row
	{
		//System.out.println(staffR.getEmployee().getName());
		//EmployeePaySlip slip = employeePaySlipRepository.findOneByEmployeeAndMonthAndYear(staffR.getEmployee(), month,year);
		int index=0;
		if (staffR != null) {
			Cell cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue(row.getRowNum());

			cell = row.createCell(index++);
			if (staffR.getEmployee().getEid() != null)
				cell.setCellValue(staffR.getEmployee().getEid());

			cell = row.createCell(index++);
			cell.setCellValue(staffR.getEmployee().getFirstName());
			cell = row.createCell(index++);
			if (staffR.getEmployee().getLastName() != null)
				cell.setCellValue(staffR.getEmployee().getLastName());


			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getEmployer().getName() != null) {
				cell.setCellValue(staffR.getEmployee().getEmployer().getName());
			}

			cell = row.createCell(index++);
			if (staffR.getEmployee().getProfile().getDoj() != null) {
				cell.setCellValue(staffR.getEmployee().getProfile().getDoj());
				cell.setCellStyle(style);

			}

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getProfile().getDol() != null) {
				cell.setCellValue(staffR.getEmployee().getProfile().getDol());
				cell.setCellStyle(style);
			}

//			cell = row.createCell(5);
//			if (staffR.getEmployee().getCostCenter() != null && staffR.getEmployee().getCostCenter().getName() != null)
//				cell.setCellValue(staffR.getEmployee().getCostCenter().getName());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getProfile().getBan() != null) {
				//System.out.println(staffR.getEmployee().getProfile().getBan());
				cell.setCellValue(staffR.getEmployee().getProfile().getBan());
			}
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getProfile().getHolderName() != null)
				cell.setCellValue((staffR.getEmployee().getProfile().getHolderName()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getProfile().getIfscCode() != null)
				cell.setCellValue((staffR.getEmployee().getProfile().getIfscCode()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getProfile().getBankName() != null)
				cell.setCellValue((staffR.getEmployee().getProfile().getBankName()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEmployee().getProfile().getBranchName() != null)
				cell.setCellValue((staffR.getEmployee().getProfile().getBranchName()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getEmployee().getCenterName() != null) {
				//System.out.println(staffR.getCtc());
				cell.setCellValue((staffR.getEmployee().getCenterName()));
			}

			///pState addded
			cell=row.createCell(index++,Cell.CELL_TYPE_STRING);
			if(staffR.getEmployee().getProfile().getpState() != null){
				cell.setCellValue(staffR.getEmployee().getProfile().getpState());
			}


			if (staffR.getEmployee().getDesignation() != null) {
				cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue((staffR.getEmployee().getDesignation()));
			}

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((staffR.isPfd()?"YES":"NO"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((staffR.isEsid()?"YES":"NO"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((staffR.isProfd()?"YES":"NO"));

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);// o to 11
			cal.set(Calendar.YEAR, year);
			int totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((totalDays));

			if (staffR.getPresents() != null)
				cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((staffR.getPresents().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getCtc() != null)
				cell.setCellValue((staffR.getCtc()).intValue());

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getBasic() != null)
				cell.setCellValue((staffR.getBasic().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getHra() != null)
				cell.setCellValue((staffR.getHra().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getConveyance() != null)
				cell.setCellValue((staffR.getConveyance()).intValue());

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getBonus() != null)
				cell.setCellValue((staffR.getBonus()).intValue());

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getSpecial() != null)
				cell.setCellValue((staffR.getSpecial().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (staffR.getGrossSalary() != null)
				cell.setCellValue((staffR.getGrossSalary().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getPfe() != null)
				cell.setCellValue((staffR.getPfe().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getPfr() != null)
				cell.setCellValue((staffR.getPfr().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getEsi() != null)
				cell.setCellValue((staffR.getEsi().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getGrossSalary() != null)
				cell.setCellValue((staffR.getGrossSalary().doubleValue()*4.75/100));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getProfessionalTax() != null)
				cell.setCellValue((staffR.getProfessionalTax().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getNetSalary() != null)
				cell.setCellValue((staffR.getNetSalary().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getOtherAllowances() != null)
				cell.setCellValue((staffR.getOtherAllowances().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING); // other d
			if (staffR.getOtherDeductions() != null)
				cell.setCellValue((staffR.getOtherDeductions().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);

			if (staffR.getExtraMonthlyAllowance() != null)
				cell.setCellValue((staffR.getExtraMonthlyAllowance().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getTds() != null)
				cell.setCellValue((staffR.getTds().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getNetSalary() != null)
				cell.setCellValue((staffR.getNetSalary().intValue()));
			// comment
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (staffR.getComment() != null)
				cell.setCellValue((staffR.getComment()));

			cell = row.createCell(index++, Cell.CELL_TYPE_BOOLEAN);
			cell.setCellValue(staffR.isLock()?"YES":"NO");
			
			return true;
		}
		return false;
	}

	public void setupheaders(Row row) {
		int index=0;
		Cell cell = row.createCell(index++);
		cell.setCellValue("Serial No");

		cell = row.createCell(index++);
		cell.setCellValue("Eid");

		cell = row.createCell(index++);
		cell.setCellValue("Employee First Name");

		cell = row.createCell(index++);
		cell.setCellValue("Employee Last Name");

		cell = row.createCell(index++);
		cell.setCellValue("Employer");

		cell = row.createCell(index++);
		cell.setCellValue("Date of Joining");

		cell = row.createCell(index++);
		cell.setCellValue("Date of Leaving");
//
//		cell = row.createCell(5);
//		cell.setCellValue("Cost Center");

		cell = row.createCell(index++);
		cell.setCellValue("Bank Account Number");

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Name As Per Bank");

		cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(("IFSC Code"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Bank Name"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Branch Name"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Cost Center"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("PState"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Designation"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("PF Deduction"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Esi Deduction"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Professional Tax Deduction"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Total Days"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Present Days"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("CTC Monthly"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Basic"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("HRA"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Conveyance Allowance"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Bonus"));
		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Special Allowance"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Gross Salary"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Employee PF"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Employer PF"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("ESI 1.75%"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("ESI 4.75%"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Professional Tax"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Net Salary"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Other Allowance"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Other Deduction"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("Extra Monthly Fixed Allowance"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("TDS"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("NET Salary"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue(("REMARK"));

		cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
		cell.setCellValue("LOCK");
	}
}
