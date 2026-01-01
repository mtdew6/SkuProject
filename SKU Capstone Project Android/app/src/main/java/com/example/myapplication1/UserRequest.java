package com.example.myapplication1;
public class UserRequest {
    private String id;       // 아이디
    private String password; // 비밀번호
    private String name;     // 이름

    // 생성자: 상자에 물건을 담는 과정
    public UserRequest(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }
}