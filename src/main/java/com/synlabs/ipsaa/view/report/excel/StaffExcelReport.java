package com.synlabs.ipsaa.view.report.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.jpa.EmployeePaySlipRepository;
import com.synlabs.ipsaa.view.staff.StaffFilterRequest;

public class StaffExcelReport {
	List<EmployeeSalary> staff;

	StaffFilterRequest staffRequest;
	String path;
	int year;
	int month;
	private String er_code;
	private EmployeePaySlipRepository employeePaySlipRepository;

	public StaffExcelReport(List<EmployeeSalary> staff, StaffFilterRequest staffRequest, String path,
			EmployeePaySlipRepository employeePaySlipRepository,String er_code) {
		this.staff = staff;
		this.path = path;
		this.staffRequest = staffRequest;
		this.employeePaySlipRepository = employeePaySlipRepository;
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
				int rownum = 0;
				boolean isCreateList = true;
				Row row = null;
				if (rownum == 0) {
					row=feeCollectionReportSheet.createRow(0);
					setupheaders(row);
					rownum++;
				}
				for (EmployeeSalary satffSalary : this.staff) {
					if(isCreateList==true) {
						row = feeCollectionReportSheet.createRow(rownum++);
					}
					isCreateList = createList(satffSalary, row,cellStyle);
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

	private boolean createList(EmployeeSalary staffR, Row row, CellStyle style) // creating cells for each row
	{
		style.setDataFormat((short)14);				//////////sets excel built-in date format
		EmployeePaySlip slip = employeePaySlipRepository.findOneByEmployeeAndMonthAndYear(staffR.getEmployee(), month,
				year);
		int index=0;
		if (slip != null) {
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

			cell = row.createCell(index++);
			if (staffR.getEmployee().getProfile().getDateOfJoining() != null) {
				cell.setCellValue(staffR.getEmployee().getProfile().getDoj());
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
				System.out.println(staffR.getCtc());
				cell.setCellValue((staffR.getEmployee().getCenterName()));
			}
			if (staffR.getEmployee().getDesignation() != null) {
				cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue((staffR.getEmployee().getDesignation()));
			}

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((slip.isPfd()?"YES":"NO"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((slip.isEsid()?"YES":"NO"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((slip.isProfd()?"YES":"NO"));

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);// o to 11
			cal.set(Calendar.YEAR, year);
			int totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((totalDays));

			if (slip.getPresents() != null)
				cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue((slip.getPresents().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getCtc() != null)
				cell.setCellValue((slip.getCtc()).intValue());

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getBasic() != null)
				cell.setCellValue((slip.getBasic().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getHra() != null)
				cell.setCellValue((slip.getHra().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getConveyance() != null)
				cell.setCellValue((slip.getConveyance()).intValue());

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getBonus() != null)
				cell.setCellValue((slip.getBonus()).intValue());

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getSpecial() != null)
				cell.setCellValue((slip.getSpecial().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (slip.getGrossSalary() != null)
				cell.setCellValue((slip.getGrossSalary().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getPfe() != null)
				cell.setCellValue((slip.getPfe().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getPfr() != null)
				cell.setCellValue((slip.getPfr().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getEsi() != null)
				cell.setCellValue((slip.getEsi().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getProfessionalTax() != null)
				cell.setCellValue((slip.getProfessionalTax().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getNetSalary() != null)
				cell.setCellValue((slip.getNetSalary().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getOtherAllowances() != null)
				cell.setCellValue((slip.getOtherAllowances().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING); // other d
			if (slip.getOtherDeductions() != null)
				cell.setCellValue((slip.getOtherDeductions().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);

			if (slip.getExtraMonthlyAllowance() != null)
				cell.setCellValue((slip.getExtraMonthlyAllowance().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getTds() != null)
				cell.setCellValue((slip.getTds().intValue()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getNetSalary() != null)
				cell.setCellValue((slip.getNetSalary().intValue()));
			// comment
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (slip.getComment() != null)
				cell.setCellValue((slip.getComment()));

			cell = row.createCell(index++, Cell.CELL_TYPE_BOOLEAN);
			cell.setCellValue(slip.isLock()?"YES":"NO");
			
			return true;
		}
		return false;
	}

	public void setupheaders(Row row) {
		int index=0;
		Cell cell = row.createCell(index++);
		cell.setCellValue("Serial No");

		cell = row.createCell(index++);
		cell.setCellValue("Company Name");

		cell = row.createCell(index++);
		cell.setCellValue("Employee First Name");

		cell = row.createCell(index++);
		cell.setCellValue("Employee Last Name");

		cell = row.createCell(index++);
		cell.setCellValue("Date of Joining");
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
		cell.setCellValue(("ESI"));

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
