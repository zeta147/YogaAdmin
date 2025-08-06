package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Schedule _schedule;
    private String _scheduleId, _courseId, _courseName, _courseDayOfWeek, _courseDate, _courseTeacherName, _courseComment;
    private int _year, _month, _dayOfMonth, _dayOfWeek;
    private int _dayOfMonthCurrent, _monthCurrent, _yearCurrent;
    private TextView _textViewCourseName;
    private CalendarView _calendarView;
    private Calendar _calendar;
    private EditText _editTextTeacherName;
    private EditText _editTextComment;
    private TextView _textViewDateScheduleErrorMessage,
                    _textViewTeacherNameErrorMessage,
                    _textViewCommentErrorMessage;
    private Button _buttonEdit,
            _buttonSave,
            _buttonCancel,
            _buttonDelete;
    private Context _context;
    private final DayOfWeekEnum[] _dayOfWeekEnum = DayOfWeekEnum.values();
    private boolean _isEditing;

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

        initializeWidget();
        assignScheduleDetailValue();
        disableInputWidget();
        getCalendarCurrentDate();
        _isEditing = false;
        _context = this;
        _schedule = new Schedule(_scheduleId, _courseId, _year + "-" + _month + "-" + _dayOfMonth, _courseTeacherName, _courseComment, 0, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigate_homepage) {
            if(_context.getClass() == YogaCourseActivity.class)
                return true;
            Intent i = new Intent(this, YogaCourseActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.navigate_schedules) {
            if(_context.getClass() == ScheduleActivity.class)
                return true;
            Intent i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /// initialize widget input, button, and error message
    private void initializeWidget(){
        _textViewCourseName = findViewById(R.id.textViewCourseScheduleName);
        _calendarView = findViewById(R.id.calendarViewSchedule);
        _editTextTeacherName = findViewById(R.id.editTextTeacherName);
        _editTextComment = findViewById(R.id.editTextComment);

        _buttonEdit = findViewById(R.id.buttonEdit);
        _buttonSave = findViewById(R.id.buttonSave);
        _buttonCancel = findViewById(R.id.buttonCancel);
        _buttonDelete = findViewById(R.id.buttonDelete);

        _textViewDateScheduleErrorMessage = findViewById(R.id.textViewDateScheduleErrorMessage);
        _textViewTeacherNameErrorMessage = findViewById(R.id.textViewTeacherNameErrorMessage);
        _textViewCommentErrorMessage = findViewById(R.id.textViewCommentErrorMessage);

        _buttonSave.setVisibility(View.INVISIBLE);
        _buttonCancel.setVisibility(View.INVISIBLE);
    }



    /// assign schedule detail value from intent
    private void assignScheduleDetailValue(){
        _scheduleId = getIntent().getStringExtra("schedule_id");
        _courseId = getIntent().getStringExtra("course_id");
        _courseName = getIntent().getStringExtra("course_name");
        _courseDayOfWeek = getIntent().getStringExtra("course_dayOfWeek");
        _courseDate = getIntent().getStringExtra("course_date");
        _courseTeacherName = getIntent().getStringExtra("course_teacherName");
        _courseComment = getIntent().getStringExtra("course_comment");

        String[] date = _courseDate.split("-");  // split into dd, mm, yyyy
        _dayOfMonth = Integer.parseInt(date[0]); // index 0 is dd
        _month = Integer.parseInt(date[1]); // index 1 is mm
        _year = Integer.parseInt(date[2]); // index 2 is yyyy
        _calendar = Calendar.getInstance();
        _calendar.set(_year, _month-1, _dayOfMonth); //Calendar.MONTH is zero based
        _dayOfWeek = _calendar.get(Calendar.DAY_OF_WEEK); //Calendar.DAY_OF_WEEK is one based (Sunday = 1)


        _calendarView.setDate(_calendar.getTimeInMillis()); // set data for calender view
        _textViewCourseName.setText(_courseName);
        _editTextTeacherName.setText(_courseTeacherName);
        _editTextComment.setText(_courseComment);
    }

    /// disable input widget not allow to edit
    private void disableInputWidget(){
        _calendarView.setEnabled(false);
        _editTextTeacherName.setEnabled(false);
        _editTextComment.setEnabled(false);
    }

    /// enable input widget allow to edit
    private void enableInputWidget(){
        _calendarView.setEnabled(true);
        _editTextTeacherName.setEnabled(true);
        _editTextComment.setEnabled(true);
    }

    /// get current date
    private void getCalendarCurrentDate(){
        _calendar = Calendar.getInstance();
        _yearCurrent = _calendar.get(Calendar.YEAR);
        _monthCurrent = _calendar.get(Calendar.MONTH)+1; //Calendar.MONTH is zero based
        _dayOfMonthCurrent = _calendar.get(Calendar.DAY_OF_MONTH);
    }

    /// get date from calendar view
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

    /// call when click view button
    private void viewMode(){
        _buttonSave.setVisibility(View.GONE);
        _buttonCancel.setVisibility(View.GONE);
        _buttonEdit.setVisibility(View.VISIBLE);
        _buttonDelete.setVisibility(View.VISIBLE);
        disableInputWidget();
    }

    /// call when click edit button
    private void editMode(){
        _buttonSave.setVisibility(View.VISIBLE);
        _buttonCancel.setVisibility(View.VISIBLE);
        _buttonEdit.setVisibility(View.GONE);
        _buttonDelete.setVisibility(View.GONE);
        enableInputWidget();
    }

    /// call when click update schedule
    public void onClickUpdateSchedule(View view){
        if(!_isEditing) {
            Toast.makeText(_context, "Please click edit button to update _schedule", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean canUpdateSchedule;
        getCalendarViewDate();
        canUpdateSchedule = checkValidDate() && checkValidInput();
        if(!canUpdateSchedule){
            Toast.makeText(_context, "Please check your input", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);
        alertDialog.setTitle("Update Schedule");
        alertDialog.setMessage("Are you sure you want to update this _schedule?");
        alertDialog.setNegativeButton("No", (dialog, which) ->{});
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Thread t = new Thread(new UpdateScheduleThread());
            t.start();
            while(t.isAlive()){}
            Toast.makeText(this, "Update schedule successfully", Toast.LENGTH_SHORT).show();
        });
        alertDialog.create().show();
    }

    /// update schedule to database
    private void updateSchedule(){
        DatabaseHelper dbHelper = new DatabaseHelper(_context);
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        _schedule = new Schedule(_scheduleId,
                _courseId,
                _dayOfMonth + "-" + _month + "-" + _year,
                _courseTeacherName,
                _courseComment,
                0,
                0);

        if(NetworkConnection.isConnected(_context)){
            _schedule.setIsUploaded(1);
            firebaseHelper.updateSchedule(_schedule);
        }
        dbHelper.updateSchedule(_schedule);
        dbHelper.close();
    }

    /// this is a thread to update schedule to database
    private class UpdateScheduleThread extends Thread {
        @Override
        public void run() {
            try {
                updateSchedule();
            } catch (RuntimeException e) {
                Log.e("UpdateScheduleThread", "Error: " + e.getMessage());
            }
        }
    }

    /// call when click edit schedule
    public void onClickEditSchedule(View view){
        editMode();
        _isEditing = true;
    }

    /// call when click cancel schedule
    public void onClickCancelSchedule(View view){
        viewMode();
        _isEditing = false;
    }

    /// call when click delete schedule
    public void onClickDeleteSchedule(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);
        alertDialog.setTitle("Delete Schedule");
        alertDialog.setMessage("Are you sure you want to delete this _schedule?");
        alertDialog.setNegativeButton("No", (dialog, which) -> {});
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Thread t = new Thread(new DeleteScheduleThread());
            t.start();
            while(t.isAlive()){}
            Toast.makeText(_context, "Delete schedule successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
        alertDialog.create().show();
    }

    /// delete schedule from database
    private void deleteSchedule(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        FirebaseHelper firebaseHelper = new FirebaseHelper();

        _schedule.setIsDeleted(1);
        if(!NetworkConnection.isConnected(_context)){
            dbHelper.updateSchedule(_schedule);
        }
        else {
            dbHelper.deleteSchedule(_schedule);
            firebaseHelper.deleteSchedule(_schedule);
        }
        dbHelper.close();
    }

    /// this is a thread to delete schedule from database
    private class DeleteScheduleThread implements Runnable {
        @Override
        public void run() {
            try {
                deleteSchedule();
            } catch (RuntimeException e) {
                Log.e("DeleteScheduleThread", "Error: " + e.getMessage());
            }
        }
    }

    /// check valid date having a current or future date
    private boolean checkValidDate(){
        boolean isValid;
        isValid = checkScheduleYear()
//                && checkScheduleMonth()
//                && checkScheduleDayOfMonth()
                && checkScheduleDayOfWeek();
        return isValid;
    }

    /// check valid input having a teacher name and a comment
    private boolean checkValidInput(){
        boolean isValid;
        isValid = checkScheduleTeacherName() && checkScheduleComment();
        return isValid;
    }

    /// check schedule year having a current or future year
    private boolean checkScheduleYear(){
        if(_year < _yearCurrent){
            setErrorMessageVisible(_textViewDateScheduleErrorMessage, "Please select a current or future year");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewDateScheduleErrorMessage);
        }
        return true;
    }

    /// check schedule month having a current or future month
//    private boolean checkScheduleMonth(){
//        if(_month < _monthCurrent){
//            setErrorMessageVisible(_textViewDateScheduleErrorMessage, "Please select a current or future month");
//            return false;
//        }
//        else{
//            setErrorMessageInvisible(_textViewDateScheduleErrorMessage);
//        }
//        return true;
//    }

    /// check schedule day of month having a current or future day of month
//    private boolean checkScheduleDayOfMonth(){
//        if(_dayOfMonth < _dayOfMonthCurrent){
//            setErrorMessageVisible(_textViewDateScheduleErrorMessage, "Please select a current or future date");
//            return false;
//        }
//        else{
//            setErrorMessageInvisible(_textViewDateScheduleErrorMessage);
//        }
//        return true;
//    }

    /// check schedule day of week having a correct day of week
    private boolean checkScheduleDayOfWeek(){
        //compare method 1
//        if(DayOfWeekEnum.valueOf(_courseDayOfWeek).ordinal()+1 != _dayOfWeek){
//
//        }
        //_dayOfWeek is one based (Sunday = 1)
        //dayOfWeekEnum is zero based (Sunday = 0)
        if(_dayOfWeekEnum[_dayOfWeek - 1] != DayOfWeekEnum.valueOf(_courseDayOfWeek)){
            setErrorMessageVisible(_textViewDateScheduleErrorMessage, "Please select the correct day of week");
        }
        else{
            setErrorMessageInvisible(_textViewDateScheduleErrorMessage);
        }
        return true;
    }

    /// check schedule teacher name is not empty
    private boolean checkScheduleTeacherName(){
        String teacherNameTemp = _editTextTeacherName.getText().toString().trim();
        if(teacherNameTemp.isEmpty()){
            setErrorMessageVisible(_textViewTeacherNameErrorMessage, "Please enter teacher name");
            return false;
        }
        else if(teacherNameTemp.length() > 255){
            setErrorMessageVisible(_textViewTeacherNameErrorMessage, "Please enter a teacher name less than 255 characters");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewTeacherNameErrorMessage);
        }
        _courseTeacherName = teacherNameTemp;
        return true;
    }

    /// check schedule comment having less than 1000 characters
    private boolean checkScheduleComment(){
        String commentTemp = _editTextComment.getText().toString().trim();
        if(commentTemp.length() > 1000){
            setErrorMessageVisible(_textViewCommentErrorMessage, "Please enter a comment less than 1000 characters");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewCommentErrorMessage);
        }
        _courseComment = commentTemp;
        return true;
    }

    /// set error message invisible
    private void setErrorMessageInvisible(TextView textView){
        textView.setVisibility(View.INVISIBLE);
    }

    /// set error message visible and set error message
    private void setErrorMessageVisible(TextView textView, String message){
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }
}