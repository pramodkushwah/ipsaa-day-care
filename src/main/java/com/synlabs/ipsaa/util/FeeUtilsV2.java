package com.synlabs.ipsaa.util;

import static com.synlabs.ipsaa.util.BigDecimalUtils.THREE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.GST;
import com.synlabs.ipsaa.ex.ValidationException;

public class FeeUtilsV2 {
	private static final String months[] = new String[] { "Jan", "Feb", "March", "April", "May", "June", "July", "Aug",
			"Sep", "Oct", "Nov", "Dec" };
    public static BigDecimal CHEQUE_BOUNCE_CHARGE=new BigDecimal(200);

    private static BigDecimal HUNDRED = new BigDecimal(100);
	public static final double FEE_CALCULATION_TOLERANCE = 5.0;
	public static final double FEE_DISCOUNT_CALCULATION_TOLERANCE = 1.0;

	public static final long IPSAA_CLUB_PROGRAM_ID = 26;
	public static final long IPSAA_CLUB_REGULAR_PROGRAM_ID = 30;

	public static BigDecimal calculateGST(BigDecimal baseFee, BigDecimal annualFee, GST type) {
		BigDecimal finalFee = ZERO;
		finalFee = finalFee.add(baseFee);
		if (annualFee != null) {
			finalFee = finalFee.add(annualFee);
		}
		BigDecimal gst;
		if (type == GST.IGST) {
			gst = new BigDecimal(18);
		} else if (type == GST.CGST)
			gst = new BigDecimal(9);
		else
			gst = new BigDecimal(9);

		gst = finalFee.multiply(gst).divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
		return gst;
	}
	public static BigDecimal calculateGST(BigDecimal baseFee, GST type) {
		BigDecimal finalFee = ZERO;
		finalFee = finalFee.add(baseFee);
		BigDecimal gst;
		if (type == GST.IGST) {
			gst = new BigDecimal(18);
		} else if (type == GST.CGST)
			gst = new BigDecimal(9);
		else
			gst = new BigDecimal(9);
		gst = finalFee.multiply(gst).divide(HUNDRED, 2, BigDecimal.ROUND_CEILING);
		return gst;
	}
	public static BigDecimal calculateFinalFee(StudentFee fee, boolean isGst) {
		// fee.setTransportFee(fee.getTransportFee().multiply(THREE));

		fee.setFinalBaseFee(fee.getFinalBaseFee().multiply(THREE));
		BigDecimal totalFee = fee.getFinalBaseFee().add(fee.getTransportFee().multiply(THREE))
				.add(fee.getFinalDepositFee()).add(fee.getFinalAdmissionFee()).add(fee.getFinalAnnualCharges());
		BigDecimal gstAmmount;

		if (isGst) {
			gstAmmount = calculateGST(fee.getFinalBaseFee(), fee.getFinalAnnualCharges(), GST.IGST);
			fee.setGstAmount(gstAmmount);
			totalFee = totalFee.add(gstAmmount);
		} else {
			fee.setGstAmount(ZERO);
		}
		fee.setFinalFee(fee.getFinalFee().setScale(2, BigDecimal.ROUND_CEILING));
		totalFee = totalFee.setScale(2, BigDecimal.ROUND_CEILING);
		BigDecimal diff = totalFee.subtract(fee.getFinalFee());

		// compare diff with tolerance
		if (fee.getFinalFee().intValue() != 0) {
			if (Math.abs(diff.doubleValue()) >= FEE_CALCULATION_TOLERANCE) {
				throw new ValidationException(
						String.format("Final Fee calculation error![Request Final Fee=%s,Calculated Final Fee=%s]",
								fee.getFinalFee(), totalFee));
			}
		}

		return totalFee;
	}

	public static BigDecimal calculateSaveFinalFee(StudentFeePaymentRequest fee, BigDecimal ratio) {
		BigDecimal finalRatio;
		if (ratio != null)
			finalRatio = ratio;
		else {
			finalRatio = THREE;
		}
		fee.setFinalTransportFee(fee.getTransportFee().multiply(finalRatio));

		fee.setFinalBaseFee(calculateDiscountAmmount(fee.getBaseFee(), fee.getBaseFeeDiscount(),
				fee.getFinalBaseFee().divide(THREE, 2, BigDecimal.ROUND_CEILING), "Base Fee"));

		fee.setFinalBaseFee(fee.getFinalBaseFee().multiply(finalRatio).setScale(2, BigDecimal.ROUND_CEILING));

		return calculateFinalFee(fee);
	}

	public static BigDecimal calculateReGenrateFinalFee(StudentFeePaymentRequest fee, BigDecimal ratio) {
		BigDecimal finalRatio = THREE;
		if (ratio != null && ratio.doubleValue() > 0)
			finalRatio = ratio;

		fee.setFinalTransportFee(fee.getTransportFee().multiply(finalRatio));

		fee.setFinalBaseFee(calculateDiscountAmmount(fee.getBaseFee(), fee.getBaseFeeDiscount(),
				fee.getFinalBaseFee().divide(THREE, 2, BigDecimal.ROUND_CEILING), "Base Fee"));
		// need to re calculate the discount bcz finalBaseFee saves discount amount +
		// quarterly amount too
		fee.setFinalBaseFee(fee.getFinalBaseFee().multiply(finalRatio).setScale(2, BigDecimal.ROUND_CEILING));

		return calculateFinalFee(fee);
	}

	public static BigDecimal calculateFinalFee(StudentFeePaymentRequest fee) {

		BigDecimal totalFee = fee.getFinalBaseFee().add(fee.getFinalTransportFee()).add(fee.getFinalDepositFee())
				.add(fee.getFinalAdmissionFee()).add(fee.getFinalAnnualCharges()).add(fee.getUniformCharges())
				.add(fee.getStationary()).add(fee.getExtraCharge()).add(fee.getLatePaymentCharge())
				.add(fee.getAdjust());
		BigDecimal gstAmmount;

		if (fee.getIgst() != null && fee.getIgst().intValue() != 0) {
			gstAmmount = calculateGST(fee.getFinalBaseFee(), fee.getFinalAnnualCharges(), GST.IGST);
			fee.setGstAmount(gstAmmount);
			totalFee = totalFee.add(gstAmmount);
		} else {
			fee.setGstAmount(ZERO);
		}
		return totalFee;
	}

	public static void validateStudentFee(StudentFee fee, CenterProgramFee centerProgramFee) {

		fee.setFinalBaseFee(calculateDiscountAmmount(fee.getBaseFee(), fee.getBaseFeeDiscount(), fee.getFinalBaseFee(),
				"Base Fee"));

		fee.setFinalAnnualCharges(calculateDiscountAmmount(fee.getAnnualCharges(), fee.getAnnualFeeDiscount(),
				fee.getFinalAnnualCharges(), "Annual Charges"));

		fee.setFinalDepositFee(calculateDiscountAmmount(fee.getDepositFee(), fee.getDepositFeeDiscount(),
				fee.getFinalDepositFee(), "Deposit Fee"));

		fee.setFinalAdmissionFee(calculateDiscountAmmount(fee.getAdmissionFee(), fee.getAddmissionFeeDiscount(),
				fee.getFinalAdmissionFee(), "Addmission Fee"));

		if (fee.getStudent().isFormalSchool()) {
			fee.setIgst(new BigDecimal(18));
			fee.setFinalFee(calculateFinalFee(fee, true));
		} else if (centerProgramFee.getProgram().getId() == IPSAA_CLUB_PROGRAM_ID
				|| centerProgramFee.getProgram().getId() == IPSAA_CLUB_REGULAR_PROGRAM_ID) {
			// throw new ValidationException(String.format("can not save or update ipsaa
			// club studnet from here"));
			fee.setIgst(new BigDecimal(18));
			fee.setFinalFee(calculateFinalFee(fee, true));
		} else {
			fee.setIgst(new BigDecimal(0));
			fee.setFinalFee(calculateFinalFee(fee, false));
		}

	}

	private static BigDecimal calculateDiscountAmmount(BigDecimal baseAmount, BigDecimal discount,
			BigDecimal discountedAmount, String discountName) {
		BigDecimal discountAmount = ZERO;
		BigDecimal discountPercent = ZERO;
		baseAmount = baseAmount == null ? ZERO : baseAmount;
		discountedAmount = discountedAmount == null ? ZERO : discountedAmount;
		discountAmount = baseAmount.subtract(discountedAmount).multiply(HUNDRED);

		if(discount.intValue()<100)
			if(discountedAmount.intValue()==0){
				discountedAmount=baseAmount;
			}

		if (baseAmount.intValue() > 0)
			discountPercent = discountAmount.divide(baseAmount, 2, BigDecimal.ROUND_CEILING);

		if (discount != null && discount.intValue() > 0) {
			BigDecimal diff = discountPercent.subtract(discount);

			// compare diff with tolerance
			if (Math.abs(diff.doubleValue()) >= FEE_DISCOUNT_CALCULATION_TOLERANCE) {
				throw new ValidationException(String.format(
						discountName + " calculation discount error![Request discount=%s,Calculated discount=%s]",
						discount, discountPercent));
			}
		}
		return discountedAmount;
	}

	public static int quarterStartMonth(int quarter) {
		switch (quarter) {
		case 1:
			return 1;
		case 2: // first quater
			return 4;
		case 3:
			return 7;
		case 4:
			return 10;
		}
		return 0;
	}

	public static Map<String, Integer> getLastQuarter(int quarter, int year) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		switch (quarter) {
		case 1:
			map.put("year", --year);
			map.put("quarter", 4);
			return map;
		case 2: // first quater
			map.put("year", year);
			map.put("quarter", 1);
			return map;
		case 3:
			map.put("year", year);
			map.put("quarter", 2);
			return map;
		case 4:
			map.put("year", year);
			map.put("quarter", 3);
			return map;
		}
		return null;
	}

	public static int quarterEndMonth(int quarter) {
		switch (quarter) {
		case 1:
			return 3;
		case 2:
			return 6;
		case 3:
			return 9;
		case 4:
			return 12;
		}
		return 0;
	}

	public static Date quarterEndDate(int quarter, int year) {
		Calendar cal = Calendar.getInstance();
		switch (quarter) {
		case 1:
			cal.set(Calendar.MONTH, 3);
			cal.set(year, 3, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		case 2:
			cal.set(Calendar.MONTH, 6);
			cal.set(year, 6, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		case 3:

			cal.set(Calendar.MONTH, 9);
			cal.set(year, 9, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		case 4:
			cal.set(Calendar.MONTH, 12);
			cal.set(year, 12, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		}
		return null;
	}

	public static Date quarterStartDate(int quarter, int year) {
		Calendar cal = Calendar.getInstance();
		switch (quarter) {
		case 1:
			cal.set(Calendar.MONTH, 1);
			cal.set(year, 1, 1);
			return cal.getTime();
		case 2: // first quater
			cal.set(Calendar.MONTH, 4);
			cal.set(year, 4, 1);
			return cal.getTime();
		case 3:
			cal.set(Calendar.MONTH, 7);
			cal.set(year, 7, 1);
			return cal.getTime();
		case 4:
			cal.set(Calendar.MONTH, 10);
			cal.set(year, 10, 1);
			return cal.getTime();
		}
		return null;
	}

	public static int getQuarter() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		if (month >= 1 && month <= 3)
			return 1;
		else if (month >= 4 && month <= 6)
			return 2;
		else if (month >= 7 && month <= 9)
			return 3;
		else
			return 4;
	}

	public static int getQuarter(int month) {
		if (month >= 1 && month <= 3)
			return 1;
		else if (month >= 4 && month <= 6)
			return 2;
		else if (month >= 7 && month <= 9)
			return 3;
		else
			return 4;
	}

	public static BigDecimal calculateFeeRatioForQuarter(Date admissionDate,int quarter) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(admissionDate);
		int currMonth = cal.get(Calendar.MONTH);
		currMonth = currMonth + 1;// for removing 0 to one indexing
		int currDay = cal.get(Calendar.DATE);
		if(getQuarter()!=getQuarter(currMonth)){
			return THREE;
		}
		int quarterEnd = quarterEndMonth(getQuarter(currMonth));
		double feeMonthRatio = quarterEnd - currMonth;
		if (15 <= currDay) {
			feeMonthRatio = feeMonthRatio + 0.5;
		} else {
			feeMonthRatio = feeMonthRatio + 1.00;
		}
		return new BigDecimal(feeMonthRatio);
	}

	public static BigDecimal calculateNextFeeRatioForQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int currMonth = cal.get(Calendar.MONTH);
		currMonth = currMonth + 1;// for removing 0 to one indexing
		int currDay = cal.get(Calendar.DATE);
		int quarterEnd = quarterEndMonth(getQuarter(currMonth));
		double feeMonthRatio = quarterEnd - currMonth;
		if (15 >= currDay) {
			feeMonthRatio = feeMonthRatio + 0.5;
		} else {
			if (feeMonthRatio != 0)
				feeMonthRatio = feeMonthRatio + 1.00;
		}
		return new BigDecimal(feeMonthRatio);
	}

	public static BigDecimal calculateDiscountAmount(BigDecimal baseFee, BigDecimal baseFeeDiscount) {
		// worng way to do this
		return baseFee.subtract(baseFee.multiply(baseFeeDiscount).divide(HUNDRED));
	}

	public static BigDecimal calculateDiscount(BigDecimal baseFee, BigDecimal DiscountAmmount) {
		BigDecimal dis = baseFee.subtract(DiscountAmmount);
		return dis.divide(baseFee, 6).multiply(HUNDRED);
	}
}
