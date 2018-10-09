package com.synlabs.ipsaa.controller;


import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.view.center.StateRequest;
import com.synlabs.ipsaa.view.center.StateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;

@RestController
@RequestMapping("api/state/")
public class StateController {
    //TODO-1. list all states
    //     2. create new state - for new center in new city
    //     3. list user states
    //     4. Add @Secured annotation

    @Autowired
    CenterService centerService;

    @GetMapping("all")
    @Secured(CENTER_WRITE)
    public List<StateResponse> allStates(){
        return centerService.listOfStates().stream().map(StateResponse::new).collect(Collectors.toList());
    }

    @PostMapping()
    @Secured(CENTER_WRITE)
    public StateResponse save(@RequestBody StateRequest request){
        return new StateResponse(centerService.saveState(request));
    }

    @PutMapping()
    @Secured(CENTER_WRITE)
    public StateResponse update(@RequestBody StateRequest request){
        return new StateResponse(centerService.updateState(request));
    }

    @GetMapping("{cityId}")
    @Secured(CENTER_WRITE)
    public StateResponse getState(@PathVariable("cityId")Long id) {
        return new StateResponse(centerService.getState(id));
    }

    @DeleteMapping("{stateId}")
    @Secured(CENTER_WRITE)
    public void delete(@PathVariable("stateId") Long id){
        StateRequest request=new StateRequest();
        request.setId(id);
        centerService.deleteState(request);
    }
}
