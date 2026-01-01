package com.example.myapplication1;

public class GenericResponse {
    private boolean ok;
    private String message;
    private String userId;
    private String email;

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
}
