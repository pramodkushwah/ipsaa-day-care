package com.synlabs.ipsaa.view.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse implements Response
{

    private String token;
    private Set<String> privileges = new HashSet<>();
    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse(String token, Set<String> privileges)
    {
        this.token = token;
        this.privileges = privileges;
    }
    public String getToken() {
        return token;
    }

    public Set<String> getPrivileges()
    {
        return privileges;
    }
}
