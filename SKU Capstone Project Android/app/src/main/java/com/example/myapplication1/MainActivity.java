package com.example.myapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.et_id);
        etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        View btnSignup = findViewById(R.id.btn_signup);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        final String email = etEmail.getText().toString().trim();
        final String pw = etPassword.getText().toString();

        if (email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "이메일/비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest req = new LoginRequest(email, pw);

        apiService.login(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {

                    // 토큰 저장 (나중에 Authorization 헤더로 사용)
                    SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                    prefs.edit()
                            .putString("accessToken", response.body().getAccessToken())
                            .putString("refreshToken", response.body().getRefreshToken())
                            .apply();

                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Menuactivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                // 서버는 인증 안 된 계정도 401로 떨어지게 되어있음(우리가 만든 로직 기준)
                Toast.makeText(MainActivity.this, "로그인 실패(이메일 인증 여부/비밀번호 확인)", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
