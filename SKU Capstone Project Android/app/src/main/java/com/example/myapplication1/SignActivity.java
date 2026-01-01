package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignActivity extends AppCompatActivity {

    private ApiService apiService;

    private EditText etEmail;
    private EditText etPw;
    private EditText etPwCheck;
    private EditText etVerifyCode;

    private AppCompatButton btnVerifyRequest;
    private AppCompatButton btnVerifyCheck;
    private AppCompatButton btnSignupFinish;

    private boolean consent = false;
    private boolean emailVerified = false;
    private String pendingEmail = null; // 인증 대상 이메일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        consent = getIntent().getBooleanExtra("consent", false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        ImageView btnBack = findViewById(R.id.btn_back);

        etEmail = findViewById(R.id.et_sign_email);
        etPw = findViewById(R.id.et_sign_pw);
        etPwCheck = findViewById(R.id.et_sign_pw_check);
        etVerifyCode = findViewById(R.id.et_verify_code);

        btnVerifyRequest = findViewById(R.id.btn_verify_request);
        btnVerifyCheck = findViewById(R.id.btn_verify_check);
        btnSignupFinish = findViewById(R.id.btn_signup_finish);

        // 초기에는 인증 확인 버튼/입력칸 숨김
        etVerifyCode.setVisibility(View.GONE);
        btnVerifyCheck.setVisibility(View.GONE);

        btnBack.setOnClickListener(v -> finish());

        // ✅ 인증요청 버튼: /auth/signup 호출 (서버가 가입 + 인증코드 메일 발송)
        btnVerifyRequest.setOnClickListener(v -> requestSignupAndSendCode());

        // ✅ 인증번호 확인 버튼: /auth/verify 호출
        btnVerifyCheck.setOnClickListener(v -> verifyCode());

        // ✅ 회원가입 완료: 인증이 끝났을 때만 완료 처리 (여기서는 로그인 화면으로 이동)
        btnSignupFinish.setOnClickListener(v -> {
            if (!emailVerified) {
                Toast.makeText(SignActivity.this, "이메일 인증을 먼저 완료해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(SignActivity.this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();
            // 로그인 화면으로 이동 (원하면 메뉴로 바로 보내도 됨)
            Intent intent = new Intent(SignActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void requestSignupAndSendCode() {
        final String email = etEmail.getText().toString().trim();
        final String pw = etPw.getText().toString();
        final String pw2 = etPwCheck.getText().toString();

        if (!consent) {
            Toast.makeText(this, "약관에 동의해야 회원가입이 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pw.equals(pw2)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 상태 초기화
        emailVerified = false;
        pendingEmail = email;

        btnVerifyRequest.setEnabled(false);

        SignupRequest req = new SignupRequest(email, pw, true);
        apiService.signup(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                btnVerifyRequest.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Toast.makeText(SignActivity.this, "인증번호가 이메일로 발송되었습니다. (지연될 수 있어요)", Toast.LENGTH_SHORT).show();
                    etVerifyCode.setVisibility(View.VISIBLE);
                    btnVerifyCheck.setVisibility(View.VISIBLE);
                    return;
                }

                // 409: 이미 가입된 이메일
                if (response.code() == 409) {
                    Toast.makeText(SignActivity.this, "이미 가입된 이메일입니다. 다른 이메일로 시도하거나 DB에서 삭제 후 재시도하세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                // 그 외 에러
                Toast.makeText(SignActivity.this, "회원가입 실패 (" + response.code() + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                btnVerifyRequest.setEnabled(true);
                Toast.makeText(SignActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyCode() {
        final String code = etVerifyCode.getText().toString().trim();
        if (pendingEmail == null || pendingEmail.isEmpty()) {
            Toast.makeText(this, "먼저 인증요청을 눌러주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code.length() != 6) {
            Toast.makeText(this, "인증번호 6자리를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnVerifyCheck.setEnabled(false);

        VerifyRequest req = new VerifyRequest(pendingEmail, code);
        apiService.verify(req).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                btnVerifyCheck.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    emailVerified = true;
                    Toast.makeText(SignActivity.this, "인증되었습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignActivity.this, "인증 실패(코드 틀림/만료). 다시 요청해보세요.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                btnVerifyCheck.setEnabled(true);
                Toast.makeText(SignActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
