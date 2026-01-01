package com.example.myapplication1;

public class LoginResponse {
    private boolean ok;
    private String message;
    private String accessToken;
    private String refreshToken;

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
