package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.NotificationEmail;
import com.synlabs.ipsaa.enums.EmailNotificationType;
import com.synlabs.ipsaa.jpa.NotificationEmailRepository;
import com.synlabs.ipsaa.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by itrs on 7/13/2017.
 */
@Service
public class NotificationEmailService extends BaseService
{
  @Autowired
  private NotificationEmailRepository notificationEmailRepository;

  public List<String> getEmails()
  {
    List<String> emails = new ArrayList<>();
    List<NotificationEmail> all = notificationEmailRepository.findAll();
    for (NotificationEmail notificationEmail : all)
    {
      emails.add(notificationEmail.getEmail());
    }
    return emails;
  }

  public String[] notificationEmailList(EmailNotificationType[] types)
  {
    Set<String> set = new HashSet<>();
    if (types != null && types.length > 0)
    {
      List<NotificationEmail> all = notificationEmailRepository.findByTypeIn(types);
      if (!CollectionUtils.isEmpty(all))
      {
        all.forEach(i -> {
          set.add(i.getEmail());
        });
      }
    }
    return set.toArray(new String[0]);
  }
  public String[] notificationEmailList(EmailNotificationType type)
  {
    Set<String> set = new HashSet<>();
    if (type != null)
    {
      List<NotificationEmail> all = notificationEmailRepository.findByType(type);
      if (!CollectionUtils.isEmpty(all))
      {
        all.forEach(i -> {
          set.add(i.getEmail());
        });
      }
    }
    return set.toArray(new String[0]);
  }

}
