package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.util.LongObfuscator;

public interface Response
{
  default Long mask(final Long number)
  {
    return number != null ? LongObfuscator.INSTANCE.obfuscate(number) : null;
  }
}
