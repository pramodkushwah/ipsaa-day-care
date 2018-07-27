package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;

import java.math.BigDecimal;

import static com.synlabs.ipsaa.util.BigDecimalUtils.*;

public class SalaryUtils
{
  public static BigDecimal ESI_PERCENT     = new BigDecimal("1.75");
  public static BigDecimal PROFESSIONAL_TAX = new BigDecimal("200");

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

  public static BigDecimal calculatePfe(EmployeeSalary salary)
  {
    if (salary.isEsid())
    {
      return ZERO;
    }
    return calculatePfe(salary.getBasic());
  }

  public static BigDecimal calculatePfe(BigDecimal basic)
  {
    if (basic.intValue() <= PFE_LIMIT)
    {
      return basic.multiply(TWELVE).divide(HUNDRED, 2);
    }
    return new BigDecimal(PFE_LIMIT).multiply(TWELVE).divide(HUNDRED, 2);
  }

  public static BigDecimal calculatePfr(EmployeeSalary salary)
  {
    if (salary.isEsid())
    {
      return ZERO;
    }
    return calculatePfr(salary.getBasic());
  }

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
    return calculateGross(salary.getBasic(), salary.getHra(), salary.getConveyance(), salary.getSpecial(), salary.getPfr());
  }

  public static BigDecimal calculateGross(BigDecimal basic, BigDecimal hra, BigDecimal conveyance, BigDecimal special, BigDecimal pfr)
  {
    return basic.add(hra).add(conveyance).add(special).subtract(pfr);
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
  //-----------------------------------------------------shubham-----------------------------------------------------
  public static BigDecimal calculateGrossV2(EmployeeSalary salary)
  {
    return salary.getBasic().add(salary.getHra()).add(salary.getConveyance()).add(salary.getSpecial()).add(salary.getExtraMonthlyAllowance()).subtract(salary.getPfr());
  }
  public static BigDecimal calculateGrossV2(BigDecimal extraMonthlyAllowance,BigDecimal basic, BigDecimal hra, BigDecimal conveyance, BigDecimal special, BigDecimal pfr)
  {
    return basic.add(hra).add(conveyance).add(special).add(extraMonthlyAllowance).subtract(pfr);
  }
}
