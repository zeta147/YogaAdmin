package com.example.yogaadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.HashMap;

public class ScheduleCreateActivity extends AppCompatActivity {
    private Schedule schedule;
    private String _courseId, _courseDayOfWeek;
    private CalendarView calendarView;
    private Calendar calendar;
    private int _year, _month, _dayOfMonth, _dayOfWeek;
    private int _dayOfMonthCurrent, _monthCurrent, _yearCurrent;
    private HashMap<String, Integer> _dayOfWeekMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        calendarView.setDate(calendar.getTimeInMillis());
        _dayOfWeekMap = new HashMap<>() {
            {
                put("Sunday", 1);
                put("Monday", 2);
                put("Tuesday", 3);
                put("Wednesday", 4);
                put("Thursday", 5);
                put("Friday", 6);
                put("Saturday", 7);
            }
        };
        getYogaCourseDetailsValue();
    }

    private void getYogaCourseDetailsValue() {
        _courseId = getIntent().getStringExtra("course_id");
        _courseDayOfWeek = getIntent().getStringExtra("course_dayOfWeek");
    }

    private void getCalendarViewDate(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
            }
        });
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH)+1; //Calendar.MONTH is zero based
        _dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        _dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)
    }

    private void getCalendarCurrentDate(){
        calendar = Calendar.getInstance();
        _yearCurrent = calendar.get(Calendar.YEAR);
        _monthCurrent = calendar.get(Calendar.MONTH)+1; //Calendar.MONTH is zero based
        _dayOfMonthCurrent = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void checkValidDate(){
        if(_year < _yearCurrent){
            return;
        }
        if(_month < _monthCurrent){
            return;
        }
        if(_dayOfMonth < _dayOfMonthCurrent){
            return;
        }
        if(_dayOfWeek != _dayOfWeekMap.get(_courseDayOfWeek)){
            return;
        }
        Toast.makeText(this, "Valid date", Toast.LENGTH_SHORT).show();
    }

    public void onClickAddSchedule(View view) {
        getCalendarViewDate();
        getCalendarCurrentDate();
        checkValidDate();
    }


    
}