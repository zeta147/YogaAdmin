package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ScheduleDetailActivity extends AppCompatActivity {
    private Schedule schedule;
    private String _scheduleId, _courseId, _courseName, _courseDayOfWeek, _courseDate, _courseTeacherName, _courseComment;
    private int _year, _month, _dayOfMonth, _dayOfWeek;
    private int _dayOfMonthCurrent, _monthCurrent, _yearCurrent;
    private TextView textViewYogaCourseName;
    private CalendarView calendarView;
    private Calendar calendar;
    private EditText editTextTeacherName;
    private EditText editTextComment;
    private TextView textViewDateScheduleErrorMessage;
    private TextView textViewTeacherNameErrorMessage;
    private TextView textViewCommentErrorMessage;
    private DatabaseHelper DB;
    private Context context;
    private DayOfWeekEnum[] dayOfWeekEnum = DayOfWeekEnum.values();
    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        getScheduleDetailWidget();
        assignScheduleDetailValue();
        setScheduleDetailValue();
        initializeSetErrorMessageInvisible();
        disableInputWidget();
        getCalendarCurrentDate();
        isEditing = false;
        context = this;
        schedule = new Schedule(_scheduleId, _courseId, _year + "-" + _month + "-" + _dayOfMonth, _courseTeacherName, _courseComment);
    }

    private void getScheduleDetailWidget(){
        textViewYogaCourseName = findViewById(R.id.textViewCourseScheduleName);
        calendarView = findViewById(R.id.calendarViewSchedule);
        editTextTeacherName = findViewById(R.id.editTextTeacherName);
        editTextComment = findViewById(R.id.editTextComment);
    }

    private void assignScheduleDetailValue(){
        _scheduleId = getIntent().getStringExtra("schedule_id");
        _courseId = getIntent().getStringExtra("course_id");
        _courseName = getIntent().getStringExtra("course_name");
        _courseDayOfWeek = getIntent().getStringExtra("course_dayOfWeek");
        _courseDate = getIntent().getStringExtra("course_date");
        _courseTeacherName = getIntent().getStringExtra("course_teacherName");
        _courseComment = getIntent().getStringExtra("course_comment");

        String[] date = _courseDate.split("-");
        _year = Integer.parseInt(date[0]);
        _month = Integer.parseInt(date[1]);
        _dayOfMonth = Integer.parseInt(date[2]);
        calendar = Calendar.getInstance();
        calendar.set(_year, _month-1, _dayOfMonth); //Calendar.MONTH is zero based
        _dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)
    }

    private void setScheduleDetailValue(){
        textViewYogaCourseName.setText(_courseName);
        calendar = Calendar.getInstance();
        calendar.set(_year, _month-1, _dayOfMonth); //Calendar.MONTH is zero based
        calendarView.setDate(calendar.getTimeInMillis());
        editTextTeacherName.setText(_courseTeacherName);
        editTextComment.setText(_courseComment);
    }

    private void initializeSetErrorMessageInvisible(){
        textViewDateScheduleErrorMessage = findViewById(R.id.textViewDateScheduleErrorMessage);
        textViewTeacherNameErrorMessage = findViewById(R.id.textViewTeacherNameErrorMessage);
        textViewCommentErrorMessage = findViewById(R.id.textViewCommentErrorMessage);
    }

    private void disableInputWidget(){
        calendarView.setEnabled(false);
        editTextTeacherName.setEnabled(false);
        editTextComment.setEnabled(false);
    }

    private void enableInputWidget(){
        calendarView.setEnabled(true);
        editTextTeacherName.setEnabled(true);
        editTextComment.setEnabled(true);
    }

    private void getCalendarCurrentDate(){
        calendar = Calendar.getInstance();
        _yearCurrent = calendar.get(Calendar.YEAR);
        _monthCurrent = calendar.get(Calendar.MONTH)+1; //Calendar.MONTH is zero based
        _dayOfMonthCurrent = calendar.get(Calendar.DAY_OF_MONTH);
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

    public void onClickUpdateSchedule(View view){
        if(!isEditing) {
            Toast.makeText(context, "Please click edit button to update schedule", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean canUpdateSchedule;
        getCalendarViewDate();
        canUpdateSchedule = checkValidDate() && checkValidInput();
        if(!canUpdateSchedule){
            Toast.makeText(context, "Please check your input", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Update Schedule");
        alertDialog.setMessage("Are you sure you want to update this schedule?");
        alertDialog.setNegativeButton("No", (dialog, which) ->{});
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Thread t = new Thread(new UpdateScheduleThread());
            t.start();
            while(t.isAlive()){}
            Toast.makeText(this, "Update schedule successfully", Toast.LENGTH_SHORT).show();
        });
        alertDialog.create().show();
    }

    private void updateSchedule(){
        schedule = new Schedule(_scheduleId, _courseId, _year + "-" + _month + "-" + _dayOfMonth, _courseTeacherName, _courseComment);
        DB = new DatabaseHelper(getApplicationContext());
        DB.updateSchedule(schedule);
    }

    private class UpdateScheduleThread extends Thread {
        @Override
        public void run() {
            try {
                updateSchedule();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickEditSchedule(View view){
        enableInputWidget();
        isEditing = true;
    }

    public void onClickCancelSchedule(View view){
        disableInputWidget();
        isEditing = false;
    }

    public void onClickDeleteSchedule(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete Schedule");
        alertDialog.setMessage("Are you sure you want to delete this schedule?");
        alertDialog.setNegativeButton("No", (dialog, which) -> {});
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            DB = new DatabaseHelper(getApplicationContext());
            try {
                DB.deleteSchedule(schedule);
            } catch (RuntimeException e) {
                e.printStackTrace();
                Toast.makeText(this, "Delete schedule failed", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Delete schedule successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        alertDialog.create().show();
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

    private boolean checkScheduleYear(){
        if(_year < _yearCurrent){
            setErrorMessageVisible(textViewDateScheduleErrorMessage, "Please select a current or future year");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDateScheduleErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleMonth(){
        if(_month < _monthCurrent){
            setErrorMessageVisible(textViewDateScheduleErrorMessage, "Please select a current or future month");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDateScheduleErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleDayOfMonth(){
        if(_dayOfMonth < _dayOfMonthCurrent){
            setErrorMessageVisible(textViewDateScheduleErrorMessage, "Please select a current or future date");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDateScheduleErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleDayOfWeek(){
        //compare method 1
//        if(DayOfWeekEnum.valueOf(_courseDayOfWeek).ordinal()+1 != _dayOfWeek){
//
//        }
        //_dayOfWeek is one based (Sunday = 1)
        //dayOfWeekEnum is zero based (Sunday = 0)
        if(dayOfWeekEnum[_dayOfWeek - 1] != DayOfWeekEnum.valueOf(_courseDayOfWeek)){
            setErrorMessageVisible(textViewDateScheduleErrorMessage, "Please select the correct day of week");
        }
        else{
            setErrorMessageInvisible(textViewDateScheduleErrorMessage);
        }
        return true;
    }

    private boolean checkScheduleTeacherName(){
        String teacherNameTemp = editTextTeacherName.getText().toString();
        if(teacherNameTemp.isEmpty()){
            setErrorMessageVisible(textViewTeacherNameErrorMessage, "Please enter teacher name");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewTeacherNameErrorMessage);
        }
        _courseTeacherName = teacherNameTemp;
        return true;
    }

    private boolean checkScheduleComment(){
        String commentTemp = editTextComment.getText().toString();
        if(commentTemp.length() > 255){
            setErrorMessageVisible(textViewCommentErrorMessage, "Please enter a comment less than 255 characters");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewCommentErrorMessage);
        }
        _courseComment = commentTemp;
        return true;
    }


    private void setErrorMessageInvisible(TextView textView){
        textView.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessageVisible(TextView textView, String message){
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }
}