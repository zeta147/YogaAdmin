package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Objects;

public class YogaCourseDetailsActivity extends AppCompatActivity {
    private YogaCourse _yogaCourse;
    private String _course_id, _name, _dayOfWeek, _time, _duration, _type, _description;
    private int _capacity;
    private float _price;
    TextView _textViewCourseName,
            _textViewCourseCapacity,
            _textViewCoursePrice,
            _textViewCourseDescription;

    TextView _textViewNameErrorMessage,
            _textViewDayOfWeekErrorMessage,
            _textViewTimeErrorMessage,
            _textViewCapacityErrorMessage,
            _textViewDurationErrorMessage,
            _textViewPriceErrorMessage,
            _textViewTypeErrorMessage,
            _textViewDescriptionErrorMessage;


    Spinner _textViewCourseDayOfWeek,
            _textViewCourseTime,
            _textViewCourseDuration,
            _textViewCourseType;

    Button _buttonEdit,
            _buttonSave,
            _buttonCancel,
            _buttonDelete,
            _buttonAddSchedule;

    ArrayAdapter<String> _dayOfWeekAdapter,
            _timeAdapter,
            _durationAdapter,
            _typeAdapter;

    private Context _context;
    private boolean _isEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_yoga_course_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        assignYogaCourseDetailsValue();
        InitializeWidget();
        initializeSetErrorMessageInvisible();
        _context = this;
        _isEditing = false;
        _yogaCourse = new YogaCourse(
                _course_id,
                _name,
                _dayOfWeek,
                _time,
                _capacity,
                _duration,
                _price,
                _type,
                _description,
                0,
                0);

        _dayOfWeekAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.day_of_week));
        _timeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.time));
        _durationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.duration));
        _typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.type));

        setYogaCourseDetailsValue();
        disableInputField();
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

    private void InitializeWidget() {
        _textViewCourseName = findViewById(R.id.textViewNameValue);
        _textViewCourseDayOfWeek = findViewById(R.id.textViewDayOfWeekValue);
        _textViewCourseTime = findViewById(R.id.textViewTimeValue);
        _textViewCourseCapacity = findViewById(R.id.textViewCapacityValue);
        _textViewCourseDuration = findViewById(R.id.textViewDurationValue);
        _textViewCoursePrice = findViewById(R.id.textViewPriceValue);
        _textViewCourseType = findViewById(R.id.textViewTypeValue);
        _textViewCourseDescription = findViewById(R.id.textViewDescriptionValue);

        _textViewNameErrorMessage = findViewById(R.id.textViewNameErrorMessage);
        _textViewDayOfWeekErrorMessage = findViewById(R.id.textViewDayOfWeekErrorMessage);
        _textViewTimeErrorMessage = findViewById(R.id.textViewTimeErrorMessage);
        _textViewCapacityErrorMessage = findViewById(R.id.textViewCapacityErrorMessage);
        _textViewDurationErrorMessage = findViewById(R.id.textViewDurationErrorMessage);
        _textViewPriceErrorMessage = findViewById(R.id.textViewPriceErrorMessage);
        _textViewTypeErrorMessage = findViewById(R.id.textViewTypeErrorMessage);
        _textViewDescriptionErrorMessage = findViewById(R.id.textViewDescriptionErrorMessage);

        _buttonEdit = findViewById(R.id.buttonEdit);
        _buttonSave = findViewById(R.id.buttonSave);
        _buttonCancel = findViewById(R.id.buttonCancel);
        _buttonDelete = findViewById(R.id.buttonDelete);
        _buttonAddSchedule = findViewById(R.id.buttonAddSchedule);
        _buttonSave.setVisibility(View.INVISIBLE);
        _buttonCancel.setVisibility(View.INVISIBLE);
    }

    private void initializeSetErrorMessageInvisible() {
        _textViewNameErrorMessage.setVisibility(View.INVISIBLE);
        _textViewDayOfWeekErrorMessage.setVisibility(View.INVISIBLE);
        _textViewTimeErrorMessage.setVisibility(View.INVISIBLE);
        _textViewCapacityErrorMessage.setVisibility(View.INVISIBLE);
        _textViewDurationErrorMessage.setVisibility(View.INVISIBLE);
        _textViewPriceErrorMessage.setVisibility(View.INVISIBLE);
        _textViewTypeErrorMessage.setVisibility(View.INVISIBLE);
        _textViewDescriptionErrorMessage.setVisibility(View.INVISIBLE);
    }


    private void assignYogaCourseDetailsValue() {
        _course_id = getIntent().getStringExtra("course_id");
        _name = getIntent().getStringExtra("course_name");
        _dayOfWeek = getIntent().getStringExtra("course_dayOfWeek");
        _time = getIntent().getStringExtra("course_time");
        _capacity = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("course_capacity")));
        _duration = getIntent().getStringExtra("course_duration");
        _price = Float.parseFloat(Objects.requireNonNull(getIntent().getStringExtra("course_price")));
        _type = getIntent().getStringExtra("course_type");
        _description = getIntent().getStringExtra("course_description");
    }

    private void setYogaCourseDetailsValue() {
        int position = 0;
        _textViewCourseName.setText(_name);
        position = _dayOfWeekAdapter.getPosition(_dayOfWeek);
        _textViewCourseDayOfWeek.setSelection(position);
        position = _timeAdapter.getPosition(_time);
        _textViewCourseTime.setSelection(position);
        _textViewCourseCapacity.setText(String.valueOf(_capacity));
        position = _durationAdapter.getPosition(_duration);
        _textViewCourseDuration.setSelection(position);
        _textViewCoursePrice.setText(String.valueOf(_price));
        position = _typeAdapter.getPosition(_type);
        _textViewCourseType.setSelection(position);
        _textViewCourseDescription.setText(_description);
    }

    private void getYogaCourseDetailsValue() {
        _name = _textViewCourseName.getText().toString();
        _dayOfWeek = _textViewCourseDayOfWeek.getSelectedItem().toString();
        _time = _textViewCourseTime.getSelectedItem().toString();
        _capacity = Integer.parseInt(_textViewCourseCapacity.getText().toString());
        _duration = _textViewCourseDuration.getSelectedItem().toString();
        _price = Float.parseFloat(_textViewCoursePrice.getText().toString());
        _type = _textViewCourseType.getSelectedItem().toString();
        _description = _textViewCourseDescription.getText().toString();
    }

    private void disableInputField() {
        _textViewCourseName.setEnabled(false);
        _textViewCourseDayOfWeek.setEnabled(false);
        _textViewCourseTime.setEnabled(false);
        _textViewCourseCapacity.setEnabled(false);
        _textViewCourseDuration.setEnabled(false);
        _textViewCoursePrice.setEnabled(false);
        _textViewCourseType.setEnabled(false);
        _textViewCourseDescription.setEnabled(false);
    }

    private void enableInputField() {
        _textViewCourseName.setEnabled(true);
        _textViewCourseDayOfWeek.setEnabled(true);
        _textViewCourseTime.setEnabled(true);
        _textViewCourseCapacity.setEnabled(true);
        _textViewCourseDuration.setEnabled(true);
        _textViewCoursePrice.setEnabled(true);
        _textViewCourseType.setEnabled(true);
        _textViewCourseDescription.setEnabled(true);
    }

    private void viewMode() {
        _buttonSave.setVisibility(View.INVISIBLE);
        _buttonCancel.setVisibility(View.INVISIBLE);
        _buttonEdit.setVisibility(View.VISIBLE);
        _buttonDelete.setVisibility(View.VISIBLE);
        disableInputField();
    }

    private void editMode(){
        _buttonSave.setVisibility(View.VISIBLE);
        _buttonCancel.setVisibility(View.VISIBLE);
        _buttonEdit.setVisibility(View.INVISIBLE);
        _buttonDelete.setVisibility(View.INVISIBLE);
        enableInputField();
    }

    public void onClickSaveYogaCourse(View view) {
        if(!_isEditing){
            Toast.makeText(this, "Please press edit button to edit course details first", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean canUpdate;
        canUpdate = checkYogaCourseDetailsName()
                && checkYogaCourseDetailsDayOfWeek()
                && checkYogaCourseDetailsTime()
                && checkYogaCourseDetailsCapacity()
                && checkYogaCourseDetailsDuration()
                && checkYogaCourseDetailsPrice()
                && checkYogaCourseDetailsType()
                && checkYogaCourseDetailsDescription();
        if (!canUpdate) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Save course changes");
        alertDialog.setMessage("Are you sure you want to update this course?");
        alertDialog.setNegativeButton("No", (dialog, which) -> {
        });
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Thread updateDatabaseYogaCourse = new Thread(new UpdateDatabaseYogaCourse());
            updateDatabaseYogaCourse.start();
            while (updateDatabaseYogaCourse.isAlive()) {}
            Toast.makeText(this, "Course with id: " + _course_id + " has been updated", Toast.LENGTH_SHORT).show();
            finish();
        });
        alertDialog.create().show();
    }

    private void updateYogaCourseDetails() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        getYogaCourseDetailsValue();
        _yogaCourse = new YogaCourse(
                _course_id,
                _name,
                _dayOfWeek,
                _time,
                _capacity,
                _duration,
                _price,
                _type,
                _description,
                0,
                0);

        if(NetworkConnection.isConnected(_context)){
            _yogaCourse.setIsUploaded(1);
            firebaseHelper.updateYogaCourse(_yogaCourse);
        }
        dbHelper.updateYogaCourse(_yogaCourse);
        dbHelper.close();
    }

    private class UpdateDatabaseYogaCourse implements Runnable {
        @Override
        public void run() {
            try {
                updateYogaCourseDetails();
            } catch (Exception e) {
                Log.e("UpdateDatabaseYogaCourse", "Error: " + e.getMessage());
            }
        }
    }

    /// call when click view edit yoga course to turn to edit mode
    public void onClickEditYogaCourse(View view) {
        editMode();
        _isEditing = true;
    }

    /// call when click view cancel yoga course to turn to view mode
    public void onClickCancelYogaCourse(View view) {
        viewMode();
        _isEditing = false;
    }

    /// call when click view delete yoga course to delete yoga course
    public void onClickDeleteYogaCourse(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete course");
        alertDialog.setMessage("Are you sure you want to delete this course?");
        alertDialog.setNegativeButton("No", (dialog, which) -> {
        });
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Thread deleteDatabaseYogaCourse = new Thread(new DeleteDatabaseYogaCourseThread());
            deleteDatabaseYogaCourse.start();
            while (deleteDatabaseYogaCourse.isAlive()) {}
            Toast.makeText(this, "Course with id: " + _course_id + " has been deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
        alertDialog.create().show();
    }


    /// delete yoga course method
    private void deleteYogaCourse() {
        Log.d("Check network connection: ", String.valueOf(NetworkConnection.isConnected(_context)));
        DatabaseHelper dbHelper = new DatabaseHelper(_context);
        FirebaseHelper firebaseHelper = new FirebaseHelper();

        _yogaCourse.setIsDeleted(1);
        if(!NetworkConnection.isConnected(_context)){
            _yogaCourse.setIsUploaded(0);
            dbHelper.updateYogaCourse(_yogaCourse);
            ArrayList<Schedule> schedules = dbHelper.getSchedulesByCourseId(_yogaCourse.getYogaCourseId());
            for (Schedule schedule : schedules) {
                schedule.setIsDeleted(1);
                dbHelper.updateSchedule(schedule);
            }
        }
        else {
            dbHelper.deleteYogaCourse(_yogaCourse);
            firebaseHelper.deleteYogaCourse(_yogaCourse);
        }
        dbHelper.close();
    }

    /// this is a thread to delete yoga course
    private class DeleteDatabaseYogaCourseThread implements Runnable {
        @Override
        public void run() {
            try {
                deleteYogaCourse();
            } catch (Exception e) {
                Log.e("DeleteDatabaseYogaCourseThread", "Error:" + e.getMessage());
            }
        }
    }

    /// call when click view add schedule to navigate to add schedule activity
    public void onClickAddSchedule(View view){
        Intent i = new Intent(this, ScheduleCreateActivity.class);
        i.putExtra("course_id", _course_id);
        i.putExtra("course_name", _name);
        i.putExtra("course_dayOfWeek", _dayOfWeek);
        startActivity(i);
    }

    /// set error message invisible when this activity is created
    private void setErrorMessageInvisible(TextView textView) {
        textView.setVisibility(View.INVISIBLE);
    }

    /// set invisible error message and set error message to the text view error message
    private void setErrorMessageVisible(TextView textView, String message) {
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    /// check input name is empty and return false, return true if name is not empty
    private boolean checkYogaCourseDetailsName() {
        String nameTemp = _textViewCourseName.getText().toString();
        if (nameTemp.isEmpty()) {
            setErrorMessageVisible(_textViewNameErrorMessage, "Please enter course name");
            return false;
        } else {
            setErrorMessageInvisible(_textViewNameErrorMessage);
        }
        _name = nameTemp;
        return true;
    }

    /// check input day of week is empty and return false, return true if day of week is not empty
    private boolean checkYogaCourseDetailsDayOfWeek() {
        String dayOfWeekTemp = _textViewCourseDayOfWeek.getSelectedItem().toString();
        if (dayOfWeekTemp.isEmpty()) {
            setErrorMessageVisible(_textViewDayOfWeekErrorMessage, "Please select day of week");
            return false;
        } else {
            setErrorMessageInvisible(_textViewDayOfWeekErrorMessage);
        }
        _dayOfWeek = dayOfWeekTemp;
        return true;
    }

    /// check input time is empty and return false, return true if time is not empty
    private boolean checkYogaCourseDetailsTime() {
        String timeTemp = _textViewCourseTime.getSelectedItem().toString();
        if (timeTemp.isEmpty()) {
            setErrorMessageVisible(_textViewTimeErrorMessage, "Please select time");
            return false;
        } else {
            setErrorMessageInvisible(_textViewTimeErrorMessage);
            _time = timeTemp;
        }
        return true;
    }

    /// check input capacity is empty and return false, return true if capacity is not empty
    private boolean checkYogaCourseDetailsCapacity() {
        String capacityTemp = _textViewCourseCapacity.getText().toString();
        if (capacityTemp.isEmpty()) {
            setErrorMessageVisible(_textViewCapacityErrorMessage, "Please enter capacity");
            return false;
        } else {
            setErrorMessageInvisible(_textViewCapacityErrorMessage);
        }
        _capacity = Integer.parseInt(capacityTemp);
        return true;

    }

    /// check input duration is empty and return false, return true if duration is not empty
    private boolean checkYogaCourseDetailsDuration() {
        String durationTemp = _textViewCourseDuration.getSelectedItem().toString();
        if (durationTemp.isEmpty()) {
            setErrorMessageVisible(_textViewDurationErrorMessage, "Please select duration");
            return false;
        } else {
            setErrorMessageInvisible(_textViewDurationErrorMessage);
        }
        _duration = durationTemp;
        return true;
    }

    /// check input price is empty and return false, return true if price is not empty
    private boolean checkYogaCourseDetailsPrice() {
        String priceTemp = _textViewCoursePrice.getText().toString();
        if (priceTemp.isEmpty()) {
            setErrorMessageVisible(_textViewPriceErrorMessage, "Please enter price");
            return false;
        } else {
            setErrorMessageInvisible(_textViewPriceErrorMessage);
        }
        _price = Float.parseFloat(priceTemp);
        return true;
    }

    /// check input type is empty and return false, return true if type is not empty
    private boolean checkYogaCourseDetailsType() {
        String typeTemp = _textViewCourseType.getSelectedItem().toString();
        if (typeTemp.isEmpty()) {
            setErrorMessageVisible(_textViewTypeErrorMessage, "Please select type");
            return false;
        } else {
            setErrorMessageInvisible(_textViewTypeErrorMessage);
        }
        _type = typeTemp;
        return true;
    }

    /// check input description has more than 255 characters and return false, return true if description has less than 255 characters
    private boolean checkYogaCourseDetailsDescription() {
        String descriptionTemp = _textViewCourseDescription.getText().toString();
        if (descriptionTemp.length() > 255) {
            setErrorMessageVisible(_textViewDescriptionErrorMessage, "Description cannot be more than 255 characters");
            return false;
        } else {
            setErrorMessageInvisible(_textViewDescriptionErrorMessage);
        }
        _description = descriptionTemp;
        return true;
    }
}

