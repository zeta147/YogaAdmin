package com.example.yogaadmin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ScheduleCreateActivity extends AppCompatActivity {
    private Schedule schedule;
    private String _courseId, _courseName, _courseDayOfWeek;
    private Date date;
    private int _year, _month, _dayOfMonth, _dayOfWeek;


    private TextView textViewCourseScheduleName;
    private CalendarView calendarView;
    private Calendar calendar;
    private EditText editTextTeacherName, editTextComment;

    private TextView textViewDateErrorMessage,
            textViewTeacherNameErrorMessage,
            textViewCommentErrorMessage;

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

        getYogaCourseDetailsValue();
        getScheduleInputWidget();
        getScheduleErrorMessageWidget();
        initializeSetErrorMessageInvisible();
        initializeCalendarView();
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

    }


    private void getScheduleInputWidget() {
        textViewCourseScheduleName = findViewById(R.id.textViewCourseScheduleName);
        textViewCourseScheduleName.setText(_courseName);
        calendarView = findViewById(R.id.calendarViewSchedule);
        editTextTeacherName = findViewById(R.id.editTextTeacherName);
        editTextComment = findViewById(R.id.editTextComment);
    }

    private void getScheduleErrorMessageWidget() {
        textViewDateErrorMessage = findViewById(R.id.textViewDateScheduleErrorMessage);
        textViewTeacherNameErrorMessage = findViewById(R.id.textViewTeacherNameErrorMessage);
        textViewCommentErrorMessage = findViewById(R.id.textViewCommentErrorMessage);
    }

    private void initializeSetErrorMessageInvisible() {
        textViewDateErrorMessage.setVisibility(View.INVISIBLE);
        textViewTeacherNameErrorMessage.setVisibility(View.INVISIBLE);
        textViewCommentErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void getYogaCourseDetailsValue() {
        _courseId = getIntent().getStringExtra("course_id");
        _courseName = getIntent().getStringExtra("course_name");
        _courseDayOfWeek = getIntent().getStringExtra("course_dayOfWeek");
    }

    private void initializeCalendarView() {
        calendar = Calendar.getInstance();
        calendarView.setDate(calendar.getTimeInMillis());
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH is zero based
        _dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        _dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)
    }

    private void getCalendarViewDate(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                _year = year;
                _month = month + 1; //Calendar.MONTH is zero based
                _dayOfMonth = dayOfMonth;
                calendar.set(year, month, dayOfMonth);
                _dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)
            }
        });
    }

    private void getCalendarCurrentDate(){
        calendar = Calendar.getInstance();
        _yearCurrent = calendar.get(Calendar.YEAR);
        _monthCurrent = calendar.get(Calendar.MONTH)+1; //Calendar.MONTH is zero based
        _dayOfMonthCurrent = calendar.get(Calendar.DAY_OF_MONTH);

    }


    @SuppressLint("SimpleDateFormat")
    public void onClickAddSchedule(View view) {
        boolean canAddSchedule;
        getCalendarViewDate();
        getCalendarCurrentDate();
        canAddSchedule = checkValidDate() && checkValidInput();
        if(!canAddSchedule){
            return;
        }
        String dateString = _year +"-"+ _month +"-"+ _dayOfMonth;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        schedule = new Schedule(
                _courseId,
                date,
                editTextTeacherName.getText().toString(),
                editTextComment.getText().toString()
        );
        Log.d("Selected day of week", String.valueOf(date));
        Toast.makeText(this, "Date: " + String.valueOf(date), Toast.LENGTH_SHORT).show();
    }

    private boolean checkValidDate(){
        boolean isValid;
        isValid = checkScheduleYear()
                && checkScheduleMonth()
                && checkScheduleDayOfMonth()
                && checkScheduleDayOfWeek();
        return isValid;
    }

    private boolean checkValidInput(){
        boolean isValid;
        isValid = checkScheduleTeacherName()
                && checkScheduleComment();
        return isValid;
    }

    private boolean checkScheduleYear() {
        if(_year < _yearCurrent){
            setErrorMessageVisible(textViewDateErrorMessage, "Please select a current or future year");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDateErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleMonth() {
        if (_month < _monthCurrent) {
            setErrorMessageVisible(textViewDateErrorMessage, "Please select a current or future month");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDateErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleDayOfMonth() {
        if (_dayOfMonth < _dayOfMonthCurrent) {
            setErrorMessageVisible(textViewDateErrorMessage, "Please select a current or future date");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDateErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleDayOfWeek() {
        if (_dayOfWeek != _dayOfWeekMap.get(_courseDayOfWeek)) {
            setErrorMessageVisible(textViewDateErrorMessage, "Please select the correct day of week");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewDateErrorMessage);
        }

        return true;
    }

    private boolean checkScheduleTeacherName() {
        String teacherNameTemp = editTextTeacherName.getText().toString();
        if (teacherNameTemp.isEmpty()) {
            setErrorMessageVisible(textViewTeacherNameErrorMessage, "Please enter teacher name");
            return false;
        } else {
            setErrorMessageInvisible(textViewTeacherNameErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleComment() {
        String commentTemp = editTextComment.getText().toString();
        if (commentTemp.isEmpty()) {
            setErrorMessageVisible(textViewCommentErrorMessage, "Please enter comment");
            return false;
        } else {
            setErrorMessageInvisible(textViewCommentErrorMessage);
        }
        return true;
    }

    private void setErrorMessageVisible(TextView textViewErrorMessage, String message){
        textViewErrorMessage.setText(message);
        textViewErrorMessage.setVisibility(View.VISIBLE);
    }

    private void setErrorMessageInvisible(TextView textViewErrorMessage){
        textViewErrorMessage.setVisibility(View.INVISIBLE);
    }
    
}