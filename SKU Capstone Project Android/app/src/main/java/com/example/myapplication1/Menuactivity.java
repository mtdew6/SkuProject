package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Menuactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuactivity);

        TextView btnSchedule = findViewById(R.id.btnSchedule);

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(Menuactivity.this, Schedule.class);
            startActivity(intent);
        });
    }
}
