package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.PayuSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayuSettingRepository extends JpaRepository<PayuSetting, Long>
{
  PayuSetting findOneByCenter(Center center);

  PayuSetting findFirstByKey(String key);
}
