package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.hygiene.Hygiene;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.HygieneRepository;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.center.HolidayRequest;
import com.synlabs.ipsaa.view.hygiene.HygieneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by itrs on 7/7/2017.
 */
@Service
public class HygieneService extends BaseService
{
  @Autowired
  private HygieneRepository hygieneRepository;

  @Autowired
  private CenterRepository centerRepository;

  public List<Hygiene> list(CenterRequest request){
    if(request.getId()==null){
      throw new NotFoundException("Center Id is null");
    }
    Center center = centerRepository.findOne(request.getId());
    if(center==null){
      throw new NotFoundException(String.format("Cannot locate center[%s]",request.getMaskId()));
    }
    return hygieneRepository.findByCenter(center);
  }

  public Hygiene save(HygieneRequest request){
    if(request.getCenterId()==null){
      throw new NotFoundException("Center Id is null");
    }
    Center center = centerRepository.findOne(request.getCenterId());
    if(center==null){
      throw new NotFoundException(String.format("Cannot locate center[%s]",request.getMaskedCenterId()));
    }
    Hygiene hygiene=new Hygiene();
    hygiene.setName(request.getName());
    hygiene.setCenter(center);
    return hygieneRepository.saveAndFlush(hygiene);
  }

  public Hygiene update(HygieneRequest request){
    Hygiene hygiene = hygieneRepository.findOne(request.getId());
    if(hygiene==null){
      throw new NotFoundException(String.format("Cannot locate Hygiene[%s]",request.getMaskedId()));
    }
    hygiene.setName(request.getName());
    return hygieneRepository.saveAndFlush(hygiene);
  }

  public void delete(HygieneRequest request){
    Hygiene hygiene = hygieneRepository.findOne(request.getId());
    if(hygiene==null){
      throw new NotFoundException(String.format("Cannot locate Hygiene[%s]",request.getMaskedId()));
    }
    hygieneRepository.delete(hygiene.getId());
  }
}
