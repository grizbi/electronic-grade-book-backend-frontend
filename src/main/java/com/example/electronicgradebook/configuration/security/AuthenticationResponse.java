package com.example.electronicgradebook.configuration.security;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationResponse {

    private String jwt;
    private String role;

    public AuthenticationResponse(String jwt, String role) {
        this.jwt = jwt;
        this.role = role;
    }

}
