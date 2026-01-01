package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        CheckBox cbAgree = findViewById(R.id.cb_agree);
        AppCompatButton btnNext = findViewById(R.id.btn_next);

        // 초기 비활성화
        btnNext.setEnabled(false);
        btnNext.setAlpha(0.5f);

        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnNext.setEnabled(isChecked);
                btnNext.setAlpha(isChecked ? 1.0f : 0.5f);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                intent.putExtra("consent", cbAgree.isChecked());
                startActivity(intent);
                finish();
            }
        });
    }
}
