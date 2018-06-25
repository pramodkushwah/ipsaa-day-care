package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.NotificationEmail;
import com.synlabs.ipsaa.enums.EmailNotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by itrs on 7/13/2017.
 */
@Repository
public interface NotificationEmailRepository extends JpaRepository<NotificationEmail,Long>
{
  List<NotificationEmail> findByTypeIn(EmailNotificationType[] types);

  List<NotificationEmail> findByType(EmailNotificationType type);
}
