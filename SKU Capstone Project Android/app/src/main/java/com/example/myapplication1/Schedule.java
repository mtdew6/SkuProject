package com.example.myapplication1;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Schedule extends AppCompatActivity {

    // 화면에 있는 뷰들을 담을 변수 선언
    private CalendarView calendarView;
    private RecyclerView rvScheduleList;
    private FloatingActionButton fabAdd;

    // 선택된 날짜를 저장할 변수
    private String selectedDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        // 1. 보내주신 EdgeToEdge 설정 코드 (그대로 유지)
        // 주의: activity_schedule.xml 최상위 레이아웃에 android:id="@+id/main"이 있어야 에러가 안 납니다.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ==========================================
        // 여기서부터 기능 구현 코드입니다
        // ==========================================

        // 2. XML에 있는 뷰들과 연결 (findViewById)
        calendarView = findViewById(R.id.calendarView);
        rvScheduleList = findViewById(R.id.rvScheduleList);
        fabAdd = findViewById(R.id.fabAdd);

        // 3. 날짜 초기화 (앱 켜자마자 오늘 날짜 저장)
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault());
        selectedDateString = sdf.format(new Date(currentTimeMillis));

        // 4. 달력 날짜를 클릭했을 때의 동작
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDateString = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일";
                Toast.makeText(Schedule.this, selectedDateString + " 선택됨", Toast.LENGTH_SHORT).show();
            }
        });

        fabAdd.setOnClickListener(v -> {
            // 현재 선택된 날짜를 확인
            Toast.makeText(Schedule.this,
                    "현재 선택 날짜: " + selectedDateString + "\n일정 추가 화면으로 이동 기능을 넣으세요.",
                    Toast.LENGTH_LONG).show();
        });

        // 6. 리스트 기본 설정
        rvScheduleList.setLayoutManager(new LinearLayoutManager(this));
    }
}