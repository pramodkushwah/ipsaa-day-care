package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.view.staff.StaffFilterRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class StaffReport {
	List<Employee> staff;

	StaffFilterRequest staffRequest;
	String path;


	public StaffReport(List<Employee> staff, String path) {
		this.staff = staff;
		this.path = path;
	}

	public File createExcel() {
		File file = new File(path + UUID.randomUUID() + ".xlsx");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				SXSSFWorkbook workbook = new SXSSFWorkbook();
				Sheet feeCollectionReportSheet = workbook.createSheet("SalarySlip");// creating a blank sheet
				int rownum = 0;
				boolean isCreateList = true;
				Row row = null;
				for (Employee emp : this.staff) {
					if(isCreateList==true) {
						row = feeCollectionReportSheet.createRow(rownum++);
					} 
					if (rownum == 1) {
						setupheaders(row);
						//rownum++;
					} else
						isCreateList = createList(emp, row);
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

	private boolean createList(Employee employee, Row row) // creating cells for each row
	{
		int index=0;
		if (employee != null) {
			Cell cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue(row.getRowNum()+1);

			cell = row.createCell(index++);
			if (employee.getEid() != null)
				cell.setCellValue(employee.getEid());

			cell = row.createCell(index++);
			cell.setCellValue(employee.getFirstName());
			cell = row.createCell(index++);
			if (employee.getLastName() != null)
				cell.setCellValue(employee.getLastName());

			cell = row.createCell(index++);
			if (employee.getEmail() != null)
				cell.setCellValue(employee.getEmail());

			cell = row.createCell(index++);
			if (employee.getEmployer() != null)
				cell.setCellValue(employee.getEmployer().getName());


			cell = row.createCell(index++);
			if (employee.getProfile().getGender() != null)
				cell.setCellValue(employee.getProfile().getGender().name());


			cell = row.createCell(index++);											///Father's name added
			if (employee.getProfile().getFatherName() != null)
				cell.setCellValue(employee.getProfile().getFatherName());

			cell = row.createCell(index++);
			if (employee.getMaritalStatus() != null)
				cell.setCellValue(employee.getMaritalStatus().name());

			cell = row.createCell(index++);										////Husband's name added
			if (employee.getProfile().getspouseName() != null)
				cell.setCellValue(employee.getProfile().getspouseName());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getDateOfJoining() != null) {
				cell.setCellValue(employee.getProfile().getDateOfJoining());
			}
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getDateOfLeaving() != null) {
				cell.setCellValue(employee.getProfile().getDateOfLeaving());
			}
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getDateOfBirth() != null) {
				cell.setCellValue(employee.getProfile().getDateOfBirth());
			}

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getMobile() != null)
				cell.setCellValue(employee.getMobile());

			cell = row.createCell(index++);
			if (employee.getCostCenter() != null && employee.getCostCenter().getName() != null)
				cell.setCellValue(employee.getCostCenter().getName());

			cell = row.createCell(index++);
			if (employee.getEmployeeType() != null)
				cell.setCellValue(employee.getEmployeeType().name());


			cell = row.createCell(index++);
			if (employee.getExpectedIn() != null)
				cell.setCellValue(employee.getExpectedIn().toString());


			cell = row.createCell(index++);
			if (employee.getExpectedOut() != null)
				cell.setCellValue(employee.getExpectedOut().toString());


			cell = row.createCell(index++);
			if (employee.getExpectedHours() != null)
				cell.setCellValue(employee.getExpectedHours());


			cell = row.createCell(index++);
			if (employee.getReportingManager() != null)
				cell.setCellValue(employee.getReportingManager().getName());

			cell = row.createCell(index++);
			if (employee.getBiometricId() != null)
				cell.setCellValue(employee.getBiometricId());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getBan() != null) {
				//System.out.println(staffR.getEmployee().getProfile().getBan());
				cell.setCellValue(employee.getProfile().getBan());
			}
			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getHolderName() != null)
				cell.setCellValue((employee.getProfile().getHolderName()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getIfscCode() != null)
				cell.setCellValue((employee.getProfile().getIfscCode()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getBankName() != null)
				cell.setCellValue((employee.getProfile().getBankName()));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if (employee.getProfile().getBranchName() != null)
				cell.setCellValue((employee.getProfile().getBranchName()));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
			if (employee.getCenterName() != null) {
				cell.setCellValue((employee.getCenterName()));
			}

			if (employee.getDesignation() != null) {
				cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue((employee.getDesignation()));
			}

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getAadharNumber()!=null)
			cell.setCellValue(employee.getAadharNumber());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getPan()!=null)
				cell.setCellValue(employee.getProfile().getPan());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getEsin()!=null)
				cell.setCellValue(employee.getProfile().getEsin());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getPran()!=null)
				cell.setCellValue(employee.getProfile().getPran());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getAddress()!=null)
				cell.setCellValue(employee.getProfile().getAddress().getAddress());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getAddress()!=null)
				cell.setCellValue(employee.getProfile().getAddress().getCity());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getAddress()!=null)
				cell.setCellValue(employee.getProfile().getAddress().getState());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getAddress()!=null)
				cell.setCellValue(employee.getProfile().getAddress().getZipcode());

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			if(employee.getProfile().getAddress()!=null)
				cell.setCellValue(employee.getProfile().getAddress().getPhone());

			return true;
		}
		return false;
	}

	public void setupheaders(Row row) {
		int index=0;
			Cell cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
			cell.setCellValue(row.getRowNum());

			cell = row.createCell(index++);
				cell.setCellValue("Eid");

			cell = row.createCell(index++);
				cell.setCellValue("FirstName");

			cell = row.createCell(index++);
				cell.setCellValue("LastName");

			cell = row.createCell(index++);
				cell.setCellValue("Email");

				cell = row.createCell(index++);
			cell.setCellValue("Employer");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Gender");

			cell = row.createCell(index++);							///Added
				cell.setCellValue("Father's Name");

			cell = row.createCell(index++,Cell.CELL_TYPE_STRING);
				cell.setCellValue("MaritalStatus");

			cell = row.createCell(index++);							///Added
				cell.setCellValue("Spouse's Name");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("DateOfJoining");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("DateOfLeaving");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("DateOfBirth");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Mobile");

			cell = row.createCell(index++);
				cell.setCellValue("CostCenter");

			cell = row.createCell(index++);
				cell.setCellValue("EmployeeType");

			cell = row.createCell(index++);
				cell.setCellValue("ExpectedIn");

			cell = row.createCell(index++);
				cell.setCellValue("ExpectedOut");

			cell = row.createCell(index++);
				cell.setCellValue("ExpectedHours");

			cell = row.createCell(index++);
				cell.setCellValue("ReportingManager");

			cell = row.createCell(index++);
				cell.setCellValue("BiometricId");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				//System.out.println(staffR.getEmployee().getProfile().getBan());
				cell.setCellValue("Ban");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(("HolderName"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(("IfscCode"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(("BankName"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(("BranchName"));

			cell = row.createCell(index++, Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(("CenterName"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(("Designation"));

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("AadharNumber");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Pan");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Esin");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Pran");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Address");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("City");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("State");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Zipcode");

			cell = row.createCell(index++, Cell.CELL_TYPE_STRING);
				cell.setCellValue("Phone");

			}
}
