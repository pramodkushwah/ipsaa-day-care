package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.view.common.Response;

public class StateResponse implements Response {

    private Long id;
    private String name;

    public StateResponse(State state) {
        this.id=mask(state.getId());
        this.name=state.getName();
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }


}
