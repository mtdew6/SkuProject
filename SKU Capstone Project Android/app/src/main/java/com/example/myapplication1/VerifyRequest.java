package com.example.myapplication1;

public class VerifyRequest {
    private String email;
    private String code;

    public VerifyRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
