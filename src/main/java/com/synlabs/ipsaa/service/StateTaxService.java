package com.synlabs.ipsaa.service;


import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.entity.center.StateTax;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.StateRepository;
import com.synlabs.ipsaa.jpa.StateTaxRepository;
import com.synlabs.ipsaa.util.BigDecimalUtils;
import com.synlabs.ipsaa.view.center.StateTaxRequest;
import com.synlabs.ipsaa.view.center.StateTaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StateTaxService extends BaseService{

    @Autowired
    StateTaxRepository stateTaxRepository;

    @Autowired
    StateRepository stateRepository;

    public List<StateTax> get(Long id){

        State state= stateRepository.findOne(unmask(id));

        if(state == null){
            throw new NotFoundException(String.format("No such state found"));
        }
        List<StateTax> list=stateTaxRepository.findByStateId(unmask(id));
        return stateTaxRepository.findByStateId(unmask(id));
    }

    public void add(StateTaxRequest request){

        if(request.getState() == null){
            throw new ValidationException("Cannot Locate the State" );
        }
        State state=stateRepository.findOneByName(request.getState());
        List<StateTax> stateTaxList=stateTaxRepository.findByStateId(unmask(state.getId()));

        StateTax tax;
        if(stateTaxList== null){
            save(request,state);
        }else{
            RangeSet<BigDecimal> range =TreeRangeSet.create();

            for(StateTax stateTax: stateTaxList){
                range.add(Range.closed(stateTax.getMin(),stateTax.getMax()));
            }

            if(range.contains(request.getMin()) && range.contains(request.getMax())){
                throw new ValidationException("The current range values are already defined!!!");
            }
            else{
               save(request,state);

            }
        }
    }

    public void save(StateTaxRequest request,State state){

        StateTax tax=new StateTax();

        tax.setMin(request.getMin());
        if(BigDecimalUtils.lessThen(request.getMax(),request.getMin())){
            throw new ValidationException("Upper limit should be greater than lower limit");
        }
        else{
            tax.setMax(request.getMax());
        }
        tax.setStates(state);
        tax.setProfessionalTax(request.getTax());


        stateTaxRepository.saveAndFlush(tax);
    }


}
