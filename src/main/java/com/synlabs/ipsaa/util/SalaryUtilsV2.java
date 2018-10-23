package com.synlabs.ipsaa.util;

import static com.synlabs.ipsaa.util.BigDecimalUtils.FORTY;
import static com.synlabs.ipsaa.util.BigDecimalUtils.HUNDRED;
import static com.synlabs.ipsaa.util.BigDecimalUtils.TWELVE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.view.staff.EmployeePaySlipRequest;

public class SalaryUtilsV2 {
	public static BigDecimal ESI_PERCENT = new BigDecimal("1.75");
	public static BigDecimal PROFESSIONAL_TAX = new BigDecimal("200");

	public static BigDecimal TWALVEPERCENT = new BigDecimal("0.12");

	public static BigDecimal CONVEYANCE = new BigDecimal(1600);
	public static BigDecimal BOUNS = new BigDecimal(584);

	public static int PFE_LIMIT = 15000;
	public static int PFR_LIMIT = 15000;

	public static int ESI_LIMIT = 21000;

	public static BigDecimal calculateBasic(BigDecimal ctc, BigDecimal basic) {
	    BigDecimal basicSalary = new BigDecimal(0); ///incoming basic is checked for value other than 40% of CTC.
		return basicSalary= basic ==ctc.multiply(FORTY).divide(HUNDRED, 2) ?
                ctc.multiply(FORTY).divide(HUNDRED, 2) : basic;
	}

	public static BigDecimal calculateHra(BigDecimal basic) {
		return basic.multiply(FORTY).divide(HUNDRED, 2);
	}

	public static BigDecimal calculateSpecial(EmployeeSalary salary) {
		return calculateSpecial(salary.getCtc(), salary.getBasic(), salary.getHra(), salary.getConveyance(),
				salary.getBonus());
	}

	public static BigDecimal calculateSpecial(BigDecimal ctc, BigDecimal basic, BigDecimal hra, BigDecimal conveyance,
			BigDecimal bonus) {
		return ctc.subtract(basic).subtract(hra).subtract(conveyance).subtract(bonus);
	}

	// public static BigDecimal calculatePfe(EmployeeSalary salary)
	// {
	// if (salary.isEsid())
	// {
	// return ZERO;
	// }
	// return calculatePfe(salary.getBasic());
	// }

	public static BigDecimal calculatePfe(BigDecimal basic) {
		if (basic.intValue() <= PFE_LIMIT) {
			return basic.multiply(TWELVE).divide(HUNDRED, 2);
		}
		return new BigDecimal(PFE_LIMIT).multiply(TWELVE).divide(HUNDRED, 2);
	}

	// public static BigDecimal calculatePfr(EmployeeSalary salary)
	// {
	// if (salary.isEsid())
	// {
	// return ZERO;
	// }
	// return calculatePfr(salary.getBasic());
	// }

	public static BigDecimal calculatePfr(BigDecimal basic) {
		if (basic.intValue() <= PFE_LIMIT) {
			return basic.multiply(TWELVE).divide(HUNDRED, 2);
		}
		return new BigDecimal(PFE_LIMIT).multiply(TWELVE).divide(HUNDRED, 2);
	}

	public static BigDecimal calculateGross(EmployeeSalary salary) {
		return calculateGross(salary.getCtc(), salary.getBonus(), salary.getPfr());
	}

	public static BigDecimal calculateGross(BigDecimal ctc, BigDecimal bonus, BigDecimal pfr) {
		return ctc.subtract(bonus).subtract(pfr);
	}

	public static BigDecimal calculateEsi(EmployeeSalary salary, BigDecimal esiPercentage) {
		return calculateEsi(salary.isEsid(), salary.getGrossSalary(), esiPercentage);
	}

	public static BigDecimal calculateEsi(boolean esid, BigDecimal gross, BigDecimal esiPercentage) {
		if (!esid) {
			return BigDecimal.ZERO;
		}
		if (gross.intValue() > ESI_LIMIT) {
			return BigDecimal.ZERO;
		}
		return gross.multiply(esiPercentage).divide(HUNDRED, 2);
	}

	public static BigDecimal calculateTotalEaring(BigDecimal ctc, BigDecimal fixdMonthlyAllowance,
			BigDecimal otherAllowance) {
		if(fixdMonthlyAllowance==null) {
			fixdMonthlyAllowance = ZERO;
		}
		return ctc.add(fixdMonthlyAllowance).add(otherAllowance);
	}

	public static BigDecimal calculateTotalDeduction(BigDecimal pf, BigDecimal pfr, BigDecimal esi,
			BigDecimal professionalTex, BigDecimal otherDeduction, BigDecimal tds) {
		if(tds==null) {
			tds = ZERO;
		}
		if(otherDeduction==null) {
			otherDeduction = ZERO;
		}
		if(professionalTex==null) {
			professionalTex=ZERO;
		}
		return pf.add(pfr).add(esi).add(professionalTex).add(otherDeduction).add(tds);
	}

	public static BigDecimal calculateNetSalary(BigDecimal totalEaring, BigDecimal totalDeduction) {
		return totalEaring.subtract(totalDeduction);
	}

	public static EmployeeSalary calculateCTC(EmployeeSalary salary) {

		//salary.setProfessionalTax(salary.isProfd() ? SalaryUtilsV2.PROFESSIONAL_TAX : ZERO);
		salary.setProfessionalTax(salary.isProfd() ? salary.getProfessionalTax() : ZERO);

		// modify by shubham calculateGrossV2 by calculateGross

		salary.setBasic(SalaryUtilsV2.calculateBasic(salary.getCtc(),salary.getBasic()));
		salary.setHra(SalaryUtilsV2.calculateHra(salary.getBasic()));
		salary.setConveyance(SalaryUtilsV2.CONVEYANCE);
		salary.setBonus(SalaryUtilsV2.BOUNS);

		// must be calculate after ctc basic hra bouns conveyance
		salary.setSpecial(SalaryUtilsV2.calculateSpecial(salary));
		if (salary.isPfd()) {
			salary.setPfe(SalaryUtilsV2.calculatePfe(salary.getBasic()));
			salary.setPfr(SalaryUtilsV2.calculatePfr(salary.getBasic()));
		}else{
			salary.setPfe(ZERO);
			salary.setPfr(ZERO);
		}
		salary.setGrossSalary(SalaryUtilsV2.calculateGross(salary));

		if (salary.isEsid()) {
			salary.setEsi(SalaryUtilsV2.calculateEsi(salary, SalaryUtilsV2.ESI_PERCENT));
		}

		BigDecimal totalDeduction = SalaryUtilsV2.calculateTotalDeduction(salary.getPfe(), salary.getPfr(),
				salary.getEsi(), salary.getProfessionalTax(), ZERO, ZERO);
		salary.setTotalDeduction(totalDeduction);
		BigDecimal totalEaring = SalaryUtilsV2.calculateTotalEaring(salary.getCtc(), salary.getExtraMonthlyAllowance(),
				ZERO);
		salary.setTotalEarning(totalEaring);
		salary.setNetSalary(SalaryUtilsV2.calculateNetSalary(totalEaring, totalDeduction));
		// salary.update();
		return salary;
	}

	public static EmployeeSalary calculateCTC(EmployeeSalary salary, BigDecimal otherAllowance,
			BigDecimal extraDeduction, BigDecimal tds) {

		salary.setProfessionalTax(salary.isProfd() ? SalaryUtilsV2.PROFESSIONAL_TAX : ZERO);

		// modify by shubham calculateGrossV2 by calculateGross
		salary.setExtraMonthlyAllowance(salary.getExtraMonthlyAllowance());
		salary.setBasic(SalaryUtilsV2.calculateBasic(salary.getCtc(),salary.getBasic()));
		salary.setHra(SalaryUtilsV2.calculateHra(salary.getBasic()));
		salary.setConveyance(SalaryUtilsV2.CONVEYANCE);
		salary.setBonus(SalaryUtilsV2.BOUNS);

		// must be calculate after ctc basic hra bouns conveyance
		salary.setSpecial(SalaryUtilsV2.calculateSpecial(salary));
		if (salary.isPfd()) {
			salary.setPfe(SalaryUtilsV2.calculatePfe(salary.getBasic()));
			salary.setPfr(SalaryUtilsV2.calculatePfr(salary.getBasic()));
		}

		salary.setGrossSalary(SalaryUtilsV2.calculateGross(salary));

		if (salary.isEsid()) {
			salary.setEsi(SalaryUtilsV2.calculateEsi(salary, SalaryUtilsV2.ESI_PERCENT));
		}

		BigDecimal totalDeduction = SalaryUtilsV2.calculateTotalDeduction(salary.getPfe(), salary.getPfr(),
				salary.getEsi(), salary.getProfessionalTax(), extraDeduction, tds);
		salary.setTotalDeduction(totalDeduction);
		BigDecimal totalEaring = SalaryUtilsV2.calculateTotalEaring(salary.getCtc(), salary.getExtraMonthlyAllowance(),
				otherAllowance);
		salary.setTotalEarning(totalEaring);
		salary.setNetSalary(SalaryUtilsV2.calculateNetSalary(totalEaring, totalDeduction));
		// salary.update();
		return salary;
	}

	public static EmployeePaySlip updateAndCalculateCTC(EmployeePaySlip paySlip, EmployeePaySlipRequest request) {

		BigDecimal newRatio = ZERO;
		BigDecimal oldRatio = paySlip.getPresents().divide(paySlip.getTotalDays(),6, RoundingMode.HALF_UP);
		if (request.getPresents() != null) {
			newRatio = request.getPresents().divide(paySlip.getTotalDays(),6, RoundingMode.HALF_UP);
			paySlip.setPresents(request.getPresents());
		} else {
			newRatio = paySlip.getPresents().divide(paySlip.getTotalDays());
		}
		// fix dived by zero issu
		paySlip.setCtc(paySlip.getCtc().divide(oldRatio, 6, RoundingMode.CEILING).multiply(newRatio));

		//paySlip.setBasic(calculateBasic(paySlip.getCtc(),paySlip.getBasic()));
        paySlip.setBasic(paySlip.getBasic().divide(oldRatio,6,RoundingMode.CEILING).multiply(newRatio));
		paySlip.setHra(calculateHra(paySlip.getBasic()));
		paySlip.setConveyance(CONVEYANCE.multiply(newRatio));
		paySlip.setBonus(BOUNS.multiply(newRatio));
		paySlip.setSpecial(calculateSpecial(paySlip.getCtc(), paySlip.getBasic(), paySlip.getHra(),
				paySlip.getConveyance(), paySlip.getBonus()));

		if(paySlip.getExtraMonthlyAllowance()==null) {
			paySlip.setExtraMonthlyAllowance(ZERO);
		}else
			paySlip.setExtraMonthlyAllowance(
					paySlip.getExtraMonthlyAllowance().divide(oldRatio, 6, RoundingMode.CEILING).multiply(newRatio));

		// paySlip.setTds(paySlip.getTotalDeduction().subtract(paySlip.getTds()).add(request.getTds()));
		if (request.getTds() == null) {
			paySlip.setTds(ZERO);
		} else {
			paySlip.setTds(request.getTds());
		}
		if (request.getOtherAllowances() != null)
			paySlip.setOtherAllowances(request.getOtherAllowances());
		if (request.getOtherDeductions() != null)
			paySlip.setOtherDeductions(request.getOtherDeductions());

		if (paySlip.isPfd()) {
			paySlip.setPfe(SalaryUtilsV2.calculatePfe(paySlip.getBasic()));
			paySlip.setPfr(SalaryUtilsV2.calculatePfr(paySlip.getBasic()));
		}else{
			paySlip.setPfe(ZERO);
			paySlip.setPfr(ZERO);
		}

		paySlip.setGrossSalary(SalaryUtilsV2.calculateGross(paySlip.getCtc(), paySlip.getBonus(), paySlip.getPfr()));

		if (paySlip.isEsid()) {
			paySlip.setEsi(calculateEsi(paySlip.isEsid(), paySlip.getGrossSalary(), ESI_PERCENT));
		}else{
			paySlip.setEsi(ZERO);
		}

		BigDecimal totalDeduction = SalaryUtilsV2.calculateTotalDeduction(paySlip.getPfe(), paySlip.getPfr(),
				paySlip.getEsi(), paySlip.getProfessionalTax(), paySlip.getOtherDeductions(), paySlip.getTds());
		paySlip.setTotalDeduction(totalDeduction);

		BigDecimal totalEaring = SalaryUtilsV2.calculateTotalEaring(paySlip.getCtc(),
				paySlip.getExtraMonthlyAllowance(), paySlip.getOtherAllowances());
		paySlip.setTotalEarning(totalEaring);

		paySlip.setNetSalary(SalaryUtilsV2.calculateNetSalary(totalEaring, totalDeduction));

		return paySlip;
	}
}
