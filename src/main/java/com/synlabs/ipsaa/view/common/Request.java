package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.util.LongObfuscator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Request
{
  String dateFormat = "yyyy-MM-dd";
  String dateTimeFormat="yyyy-MM-dd HH:mm z";

  default Long unmask(final Long number)
  {
    return number != null ? LongObfuscator.INSTANCE.unobfuscate(number) : null;
  }

  default Date parseDate(String string, String dateFormat) throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    return sdf.parse(string);
  }

  default Date parseDate(String string) throws ParseException
  {
    return parseDate(string, dateFormat);
  }
}