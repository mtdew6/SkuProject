package com.example.myapplication1;

// 👇 public이 꼭 붙어 있어야 합니다! (이게 없으면 Cannot access 에러 남)
public class UserResponse {
    private String message;
    private String name;

    // 생성자가 없어도 되지만, Getter는 필수
    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}