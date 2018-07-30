package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;

import java.math.BigDecimal;

import static com.synlabs.ipsaa.util.BigDecimalUtils.*;

public class SalaryUtilsV2
{
  public static BigDecimal ESI_PERCENT     = new BigDecimal("1.75");
  public static BigDecimal PROFESSIONAL_TAX = new BigDecimal("200");

  public static BigDecimal TWALVEPERCENT=new BigDecimal("0.12");

  public static BigDecimal CONVEYANCE =new BigDecimal(1600);
  public static BigDecimal BOUNS =new BigDecimal(584);

  public static int PFE_LIMIT = 15000;
  public static int PFR_LIMIT = 15000;

  public static int ESI_LIMIT = 21000;

  public static BigDecimal calculateBasic(BigDecimal ctc)
  {
    return ctc.multiply(FORTY).divide(HUNDRED, 2);
  }

  public static BigDecimal calculateHra(BigDecimal basic)
  {
    return basic.multiply(FORTY).divide(HUNDRED, 2);
  }

  public static BigDecimal calculateSpecial(EmployeeSalary salary)
  {
    return calculateSpecial(salary.getCtc(), salary.getBasic(), salary.getHra(), salary.getConveyance(), salary.getBonus());
  }
  public static BigDecimal calculateSpecial(BigDecimal ctc, BigDecimal basic, BigDecimal hra, BigDecimal conveyance, BigDecimal bonus)
  {
    return ctc.subtract(basic).subtract(hra).subtract(conveyance).subtract(bonus);
  }

//  public static BigDecimal calculatePfe(EmployeeSalary salary)
//  {
//    if (salary.isEsid())
//    {
//      return ZERO;
//    }
//    return calculatePfe(salary.getBasic());
//  }

  public static BigDecimal calculatePfe(BigDecimal basic)
  {
    if (basic.intValue() <= PFE_LIMIT)
    {
      return basic.multiply(TWELVE).divide(HUNDRED, 2);
    }
    return new BigDecimal(PFE_LIMIT).multiply(TWELVE).divide(HUNDRED, 2);
  }

//  public static BigDecimal calculatePfr(EmployeeSalary salary)
//  {
//    if (salary.isEsid())
//    {
//      return ZERO;
//    }
//    return calculatePfr(salary.getBasic());
//  }

  public static BigDecimal calculatePfr(BigDecimal basic)
  {
    if (basic.intValue() <= PFE_LIMIT)
    {
      return basic.multiply(TWELVE).divide(HUNDRED, 2);
    }
    return new BigDecimal(PFE_LIMIT).multiply(TWELVE).divide(HUNDRED, 2);
  }

  public static BigDecimal calculateGross(EmployeeSalary salary)
  {
    return calculateGross(salary.getCtc(),salary.getBonus(),salary.getPfr());
  }

  public static BigDecimal calculateGross(BigDecimal ctc, BigDecimal bonus, BigDecimal pfr)
  {
    return ctc.subtract(bonus).subtract(pfr);
  }

  public static BigDecimal calculateEsi(EmployeeSalary salary, BigDecimal esiPercentage)
  {
    return calculateEsi(salary.isEsid(), salary.getGrossSalary(), esiPercentage);
  }
  public static BigDecimal calculateEsi(boolean esid, BigDecimal gross, BigDecimal esiPercentage)
  {
    if (!esid)
    {
      return BigDecimal.ZERO;
    }
    if (gross.intValue() > ESI_LIMIT)
    {
      return BigDecimal.ZERO;
    }
    return gross.multiply(esiPercentage).divide(HUNDRED, 2);
  }

  public static BigDecimal calculateTotalEaring(BigDecimal ctc,BigDecimal fixdMonthlyAllowance,BigDecimal otherAllowance){
    return ctc.add(fixdMonthlyAllowance).add(otherAllowance);
  }
  public static BigDecimal calculateTotalDeduction(BigDecimal pf,BigDecimal pfr,BigDecimal esi,BigDecimal professionalTex,BigDecimal otherDeduction,BigDecimal tds){
    return pf.add(pfr).add(esi).add(professionalTex).add(otherDeduction).add(tds);
  }
  public static BigDecimal calculateNetSalary(BigDecimal totalEaring,BigDecimal totalDeduction){
    return totalEaring.subtract(totalDeduction);
  }
}
