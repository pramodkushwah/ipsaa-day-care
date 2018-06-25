package com.synlabs.ipsaa.util;

public interface ITransformer<K, V>
{
  V transform(K input);
}
