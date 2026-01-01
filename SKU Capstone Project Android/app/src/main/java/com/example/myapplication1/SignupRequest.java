package com.example.myapplication1;

public class SignupRequest {
    private String email;
    private String password;
    private boolean consent;

    public SignupRequest(String email, String password, boolean consent) {
        this.email = email;
        this.password = password;
        this.consent = consent;
    }
}
