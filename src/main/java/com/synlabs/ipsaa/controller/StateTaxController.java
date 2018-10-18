package com.synlabs.ipsaa.controller;


import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.entity.center.StateTax;
import com.synlabs.ipsaa.service.StateTaxService;
import com.synlabs.ipsaa.view.center.StateResponse;
import com.synlabs.ipsaa.view.center.StateTaxRequest;
import com.synlabs.ipsaa.view.center.StateTaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tax/")
public class StateTaxController {
    //TODO - Add,update,Get;

    @Autowired
    StateTaxService stateTaxService;

    @GetMapping("{stateId}")
    public List<StateTaxResponse> list(@PathVariable("stateId") Long id){
        return  stateTaxService.get(id).stream().map(StateTaxResponse::new).collect(Collectors.toList());
    }

    @PostMapping()
    public void addTax(@RequestBody StateTaxRequest request){
        stateTaxService.add(request);
    }
}
