package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.entity.support.SupportQueryEntry;
import com.synlabs.ipsaa.enums.QueryStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.SupportRepository;
import com.synlabs.ipsaa.view.center.SupportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SupportService extends BaseService
{

  @Autowired
  private SupportRepository repository;

  public List<SupportQuery> supportQueryList()
  {
    return repository.findByStatusNotOrderByCreatedDateAsc(QueryStatus.Closed);
  }

  public List<SupportQuery> supportQueryListAll()
  {
    return repository.findAllByOrderByCreatedDateAsc();
  }

  public SupportQuery supportQuery(SupportRequest request)
  {
    return repository.getOne(request.getId());
  }

  public SupportQuery postReply(SupportRequest request)
  {

    if (StringUtils.isEmpty(request.getDescription())) {
      throw new ValidationException("Please provide description for reply");
    }

    SupportQuery query = repository.getOne(request.getId());

    if (query == null) {
      throw new NotFoundException("Cannot locate original query!");
    }

    SupportQueryEntry reply = new SupportQueryEntry();
    reply.setDescription(request.getDescription());
    reply.setSupportQuery(query);
    query.setStatus(QueryStatus.Open);
    query.addReply(reply);
    return repository.saveAndFlush(query);
  }

  public SupportQuery closeQuery(SupportRequest request)
  {

    SupportQuery query = repository.getOne(request.getId());

    if (query == null) {
      throw new NotFoundException("Cannot locate original query!");
    }

    query.setStatus(QueryStatus.Closed);
    return repository.saveAndFlush(query);
  }
}
