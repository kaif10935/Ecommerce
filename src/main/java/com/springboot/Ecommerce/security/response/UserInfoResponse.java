package com.springboot.Ecommerce.security.response;


import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private List<String> roles;

    public UserInfoResponse(String jwtToken, String username, List<String> roles) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String jwtToken, String username, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
