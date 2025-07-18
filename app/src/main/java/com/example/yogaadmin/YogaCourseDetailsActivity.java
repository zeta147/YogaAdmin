package com.example.yogaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class YogaCourseDetailsActivity extends AppCompatActivity {
    private YogaCourse yogaCourseDetails;
    private String _course_id, _name, _dayOfWeek, _time, _duration, _type, _description;
    private int _capacity;
    private float _price;
    TextView textViewCourseName,
            textViewCourseCapacity,
            textViewCoursePrice,
            textViewCourseDescription;

    TextView textViewNameErrorMessage,
            textViewDayOfWeekErrorMessage,
            textViewTimeErrorMessage,
            textViewCapacityErrorMessage,
            textViewDurationErrorMessage,
            textViewPriceErrorMessage,
            textViewTypeErrorMessage;

    Spinner textViewCourseDayOfWeek,
            textViewCourseTime,
            textViewCourseDuration,
            textViewCourseType;

    ArrayAdapter<String> adapterDayOfWeek,
            adapterTime,
            adapterDuration,
            adapterType;

    private boolean _isSuccessfulUpdate,
            _isSuccessfulDelete;

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
        getYogaCourseDetailsInputWidget();
        getYogaCourseDetailsErrorMessageWidget();
        yogaCourseDetails = new YogaCourse(
                _course_id,
                _name,
                _dayOfWeek,
                _time,
                _capacity,
                _duration,
                _price,
                _type,
                _description);

        adapterDayOfWeek = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.day_of_week));
        adapterTime = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.time));
        adapterDuration = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.duration));
        adapterType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.type));

        setYogaCourseDetailsValue();
        disableCourseDetailsInputField();
    }

    private void getYogaCourseDetailsInputWidget() {
        textViewCourseName = findViewById(R.id.textViewCourseDetailNameValue);
        textViewCourseDayOfWeek = findViewById(R.id.textViewCourseDetailDayOfWeekValue);
        textViewCourseTime = findViewById(R.id.textViewCourseDetailTimeValue);
        textViewCourseCapacity = findViewById(R.id.textViewCourseDetailCapacityValue);
        textViewCourseDuration = findViewById(R.id.textViewCourseDetailDurationValue);
        textViewCoursePrice = findViewById(R.id.textViewCourseDetailPriceValue);
        textViewCourseType = findViewById(R.id.textViewCourseDetailTypeValue);
        textViewCourseDescription = findViewById(R.id.textViewCourseDetailDescriptionValue);
    }

    private void getYogaCourseDetailsErrorMessageWidget() {
        textViewNameErrorMessage = findViewById(R.id.textViewCourseDetailNameErrorMessage);
        textViewDayOfWeekErrorMessage = findViewById(R.id.textViewCourseDetailDayOfWeekErrorMessage);
        textViewTimeErrorMessage = findViewById(R.id.textViewCourseDetailTimeErrorMessage);
        textViewCapacityErrorMessage = findViewById(R.id.textViewCourseDetailCapacityErrorMessage);
        textViewDurationErrorMessage = findViewById(R.id.textViewCourseDetailDurationErrorMessage);
        textViewPriceErrorMessage = findViewById(R.id.textViewCourseDetailPriceErrorMessage);
        textViewTypeErrorMessage = findViewById(R.id.textViewCourseDetailTypeErrorMessage);
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
        textViewCourseName.setText(_name);
        position = adapterDayOfWeek.getPosition(_dayOfWeek);
        textViewCourseDayOfWeek.setSelection(position);
        position = adapterTime.getPosition(_time);
        textViewCourseTime.setSelection(position);
        textViewCourseCapacity.setText(String.valueOf(_capacity));
        position = adapterDuration.getPosition(_duration);
        textViewCourseDuration.setSelection(position);
        textViewCoursePrice.setText(String.valueOf(_price));
        position = adapterType.getPosition(_type);
        textViewCourseType.setSelection(position);
        textViewCourseDescription.setText(_description);
    }

    private void getYogaCourseDetailsValue() {
        _name = textViewCourseName.getText().toString();
        _dayOfWeek = textViewCourseDayOfWeek.getSelectedItem().toString();
        _time = textViewCourseTime.getSelectedItem().toString();
        _capacity = Integer.parseInt(textViewCourseCapacity.getText().toString());
        _duration = textViewCourseDuration.getSelectedItem().toString();
        _price = Float.parseFloat(textViewCoursePrice.getText().toString());
        _type = textViewCourseType.getSelectedItem().toString();
        _description = textViewCourseDescription.getText().toString();
    }

    private void disableCourseDetailsInputField() {
        textViewCourseName.setEnabled(false);
        textViewCourseDayOfWeek.setEnabled(false);
        textViewCourseTime.setEnabled(false);
        textViewCourseCapacity.setEnabled(false);
        textViewCourseDuration.setEnabled(false);
        textViewCoursePrice.setEnabled(false);
        textViewCourseType.setEnabled(false);
        textViewCourseDescription.setEnabled(false);
    }

    private void enableCourseDetailsInputField() {
        textViewCourseName.setEnabled(true);
        textViewCourseDayOfWeek.setEnabled(true);
        textViewCourseTime.setEnabled(true);
        textViewCourseCapacity.setEnabled(true);
        textViewCourseDuration.setEnabled(true);
        textViewCoursePrice.setEnabled(true);
        textViewCourseType.setEnabled(true);
        textViewCourseDescription.setEnabled(true);
    }

    public void onClickSaveYogaCourse(View view) {
        boolean canUpdate = true;
        canUpdate = checkYogaCourseDetailsName();
        canUpdate = canUpdate && checkYogaCourseDetailsDayOfWeek();
        canUpdate = canUpdate && checkYogaCourseDetailsTime();
        canUpdate = canUpdate && checkYogaCourseDetailsCapacity();
        canUpdate = canUpdate && checkYogaCourseDetailsDuration();
        canUpdate = canUpdate && checkYogaCourseDetailsPrice();
        canUpdate = canUpdate && checkYogaCourseDetailsType();
        canUpdate = canUpdate && checkYogaCourseDetailsDescription();
        if (!canUpdate) {
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
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        getYogaCourseDetailsValue();
        yogaCourseDetails = new YogaCourse(
                _course_id,
                _name,
                _dayOfWeek,
                _time,
                _capacity,
                _duration,
                _price,
                _type,
                _description);
        db.updateYogaCourse(yogaCourseDetails);
    }

    private class UpdateDatabaseYogaCourse implements Runnable {
        @Override
        public void run() {
            try {
                updateYogaCourseDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickEditYogaCourse(View view) {
        enableCourseDetailsInputField();
    }

    public void onClickCancelYogaCourse(View view) {
        setYogaCourseDetailsValue();
        disableCourseDetailsInputField();
    }

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

    private void deleteYogaCourse() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteYogaCourse(yogaCourseDetails);
    }

    private class DeleteDatabaseYogaCourseThread implements Runnable {
        @Override
        public void run() {
            try {
                deleteYogaCourse();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickAddSchedule(View view){
        Intent i = new Intent(this, ScheduleCreateActivity.class);
        i.putExtra("course_id", _course_id);
        startActivity(i);
    }

    private void initializeSetErrorMessageInvisible() {
        textViewNameErrorMessage.setVisibility(View.INVISIBLE);
        textViewDayOfWeekErrorMessage.setVisibility(View.INVISIBLE);
        textViewTimeErrorMessage.setVisibility(View.INVISIBLE);
        textViewCapacityErrorMessage.setVisibility(View.INVISIBLE);
        textViewDurationErrorMessage.setVisibility(View.INVISIBLE);
        textViewPriceErrorMessage.setVisibility(View.INVISIBLE);
        textViewTypeErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessageInvisible(TextView textView) {
        textView.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessageVisible(TextView textView, String message) {
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    private boolean checkYogaCourseDetailsName() {
        String nameTemp = textViewCourseName.getText().toString();
        if (nameTemp.isEmpty()) {
            setErrorMessageVisible(textViewNameErrorMessage, "Please enter course name");
            return false;
        } else {
            setErrorMessageInvisible(textViewNameErrorMessage);
        }
        _name = nameTemp;
        return true;
    }

    private boolean checkYogaCourseDetailsDayOfWeek() {
        String dayOfWeekTemp = textViewCourseDayOfWeek.getSelectedItem().toString();
        if (dayOfWeekTemp.isEmpty()) {
            setErrorMessageVisible(textViewDayOfWeekErrorMessage, "Please select day of week");
            return false;
        } else {
            setErrorMessageInvisible(textViewDayOfWeekErrorMessage);
        }
        _dayOfWeek = dayOfWeekTemp;
        return true;
    }

    private boolean checkYogaCourseDetailsTime() {
        String timeTemp = textViewCourseTime.getSelectedItem().toString();
        if (timeTemp.isEmpty()) {
            setErrorMessageVisible(textViewTimeErrorMessage, "Please select time");
            return false;
        } else {
            setErrorMessageInvisible(textViewTimeErrorMessage);
            _time = timeTemp;
        }
        return true;
    }

    private boolean checkYogaCourseDetailsCapacity() {
        String capacityTemp = textViewCourseCapacity.getText().toString();
        if (capacityTemp.isEmpty()) {
            setErrorMessageVisible(textViewCapacityErrorMessage, "Please enter capacity");
            return false;
        } else {
            setErrorMessageInvisible(textViewCapacityErrorMessage);
        }
        _capacity = Integer.parseInt(capacityTemp);
        return true;

    }

    private boolean checkYogaCourseDetailsDuration() {
        String durationTemp = textViewCourseDuration.getSelectedItem().toString();
        if (durationTemp.isEmpty()) {
            setErrorMessageVisible(textViewDurationErrorMessage, "Please select duration");
            return false;
        } else {
            setErrorMessageInvisible(textViewDurationErrorMessage);
        }
        _duration = durationTemp;
        return true;
    }

    private boolean checkYogaCourseDetailsPrice() {
        String priceTemp = textViewCoursePrice.getText().toString();
        if (priceTemp.isEmpty()) {
            setErrorMessageVisible(textViewPriceErrorMessage, "Please enter price");
            return false;
        } else {
            setErrorMessageInvisible(textViewPriceErrorMessage);
        }
        _price = Float.parseFloat(priceTemp);
        return true;
    }

    private boolean checkYogaCourseDetailsType() {
        String typeTemp = textViewCourseType.getSelectedItem().toString();
        if (typeTemp.isEmpty()) {
            setErrorMessageVisible(textViewTypeErrorMessage, "Please select type");
            return false;
        } else {
            setErrorMessageInvisible(textViewTypeErrorMessage);
        }
        _type = typeTemp;
        return true;
    }

    private boolean checkYogaCourseDetailsDescription() {
        String descriptionTemp = textViewCourseDescription.getText().toString();
        if (descriptionTemp.length() > 255) {
            setErrorMessageVisible(textViewTypeErrorMessage, "Description cannot be more than 255 characters");
            return false;
        } else {
            setErrorMessageInvisible(textViewTypeErrorMessage);
        }
        _description = descriptionTemp;
        return true;
    }




}

