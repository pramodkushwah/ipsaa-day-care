package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.view.common.Request;

public class StateRequest implements Request {

    private Long id;
    private String city;
    private String name;

    public StateRequest() { }

    public Long getId() { return unmask(this.id); }

    public void setId(Long id) { this.id = id; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public State toEntity(){
        State state= new State();
        state.setName(this.name.toUpperCase());
        return state;
    }

    @Override
    public String toString() {
        return "StateRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
