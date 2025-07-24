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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ScheduleCreateActivity extends AppCompatActivity {
    private Schedule _schedule;
    private String _courseId, _courseName, _courseDayOfWeek;
    private Date _date;
    private int _year, _month, _dayOfMonth, _dayOfWeek;

    private TextView _textViewCourseScheduleName;
    private CalendarView _calendarView;
    private Calendar _calendar;
    private EditText _editTextTeacherName, editTextComment;

    private TextView _textViewDateErrorMessage,
            _textViewTeacherNameErrorMessage,
            _textViewCommentErrorMessage;

    private int _dayOfMonthCurrent, _monthCurrent, _yearCurrent;
    private DayOfWeekEnum[] _dayOfWeekEnum;
    private DatabaseHelper _dbHelper;


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
        _dbHelper = new DatabaseHelper(this);
        _dbHelper.checkAvailableTable();
        _dayOfWeekEnum = DayOfWeekEnum.values();

    }


    private void getScheduleInputWidget() {
        _textViewCourseScheduleName = findViewById(R.id.textViewCourseScheduleName);
        _textViewCourseScheduleName.setText(_courseName);
        _calendarView = findViewById(R.id.calendarViewSchedule);
        _editTextTeacherName = findViewById(R.id.editTextTeacherName);
        editTextComment = findViewById(R.id.editTextComment);
    }

    private void getScheduleErrorMessageWidget() {
        _textViewDateErrorMessage = findViewById(R.id.textViewDateScheduleErrorMessage);
        _textViewTeacherNameErrorMessage = findViewById(R.id.textViewTeacherNameErrorMessage);
        _textViewCommentErrorMessage = findViewById(R.id.textViewCommentErrorMessage);
    }

    private void initializeSetErrorMessageInvisible() {
        _textViewDateErrorMessage.setVisibility(View.INVISIBLE);
        _textViewTeacherNameErrorMessage.setVisibility(View.INVISIBLE);
        _textViewCommentErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void getYogaCourseDetailsValue() {
        _courseId = getIntent().getStringExtra("course_id");
        _courseName = getIntent().getStringExtra("course_name");
        _courseDayOfWeek = getIntent().getStringExtra("course_dayOfWeek");
    }

    private void initializeCalendarView() {
        _calendar = Calendar.getInstance();
        _calendarView.setDate(_calendar.getTimeInMillis());
        _year = _calendar.get(Calendar.YEAR);
        _month = _calendar.get(Calendar.MONTH) + 1; //Calendar.MONTH is zero based
        _dayOfMonth = _calendar.get(Calendar.DAY_OF_MONTH);
        _dayOfWeek = _calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)
    }

    private void getCalendarViewDate(){
        _calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                _year = year;
                _month = month + 1; //Calendar.MONTH is zero based
                _dayOfMonth = dayOfMonth;
                _calendar.set(year, month, dayOfMonth);
                _dayOfWeek = _calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)

            }
        });
    }

    private void getCalendarCurrentDate(){
        _calendar = Calendar.getInstance();
        _yearCurrent = _calendar.get(Calendar.YEAR);
        _monthCurrent = _calendar.get(Calendar.MONTH)+1; //Calendar.MONTH is zero based
        _dayOfMonthCurrent = _calendar.get(Calendar.DAY_OF_MONTH);

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
        Thread thread = new Thread(new addScheduleThread());
        thread.start();
        while (thread.isAlive()){}
        Toast.makeText(this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
    }

    private class addScheduleThread implements Runnable {
        @Override
        public void run() {
            String dateString = _year +"-"+ _month +"-"+ _dayOfMonth;
            _schedule = new Schedule(
                    _courseId,
                    dateString,
                    _editTextTeacherName.getText().toString(),
                    editTextComment.getText().toString()
            );
            try {
                _dbHelper.insertYogaCourseSchedule(_schedule);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a current or future year");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewDateErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleMonth() {
        if (_month < _monthCurrent) {
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a current or future month");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewDateErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleDayOfMonth() {
        if (_dayOfMonth < _dayOfMonthCurrent) {
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a current or future _date");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewDateErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleDayOfWeek() {
        //compare method 1
//        if(DayOfWeekEnum.valueOf(_courseDayOfWeek).ordinal()+1 != _dayOfWeek){
//
//        }
        if (_dayOfWeekEnum[_dayOfWeek - 1] != DayOfWeekEnum.valueOf(_courseDayOfWeek)) {
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select the correct day of week");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewDateErrorMessage);
        }

        return true;
    }

    private boolean checkScheduleTeacherName() {
        String teacherNameTemp = _editTextTeacherName.getText().toString();
        if (teacherNameTemp.isEmpty()) {
            setErrorMessageVisible(_textViewTeacherNameErrorMessage, "Please enter teacher name");
            return false;
        } else {
            setErrorMessageInvisible(_textViewTeacherNameErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleComment() {
        String commentTemp = editTextComment.getText().toString();
        if (commentTemp.length() > 255) {
            setErrorMessageVisible(_textViewCommentErrorMessage, "Please enter a comment less than 255 characters");
            return false;
        } else {
            setErrorMessageInvisible(_textViewCommentErrorMessage);
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