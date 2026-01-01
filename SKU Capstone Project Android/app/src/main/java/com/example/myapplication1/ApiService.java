package com.example.myapplication1;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // 회원가입(+메일 발송): server /auth/signup
    @POST("/auth/signup")
    Call<GenericResponse> signup(@Body SignupRequest request);

    // 이메일 인증 확인: server /auth/verify
    @POST("/auth/verify")
    Call<GenericResponse> verify(@Body VerifyRequest request);

    // 로그인: server /auth/login
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/health")
    Call<HealthResponse> health();
}
