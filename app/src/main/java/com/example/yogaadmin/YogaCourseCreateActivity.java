package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class YogaCourseCreateActivity extends AppCompatActivity {
    private String _course_id, _name, _dayOfWeek, _time, _duration, _type, _description;
    private int _capacity;
    private float _price;
    TextView textViewNameErrorMessage,
            textViewDayOfWeekErrorMessage,
            textViewTimeErrorMessage,
            textViewCapacityErrorMessage,
            textViewDurationErrorMessage,
            textViewPriceErrorMessage,
            textViewTypeErrorMessage;
    EditText editText;
    Spinner spinner;
    DatabaseHelper DB;

    private boolean _isSuccessfulAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_yoga_course_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitializeSetErrorMessageInvisible();
    }


    public void onCLickAddCourse(View view){
        boolean canAdd;
        canAdd = checkInputName()
                && checkInputDayOfWeek()
                && checkInputTime()
                && checkInputCapacity()
                && checkInputDuration()
                && checkInputPrice()
                && checkInputType()
                && checkInputDescription();
        if(!canAdd){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Thread t = new Thread(new AddDatabaseYogaCourseThread());
        t.start();
        while(t.isAlive()){}
        if(!_isSuccessfulAdded){
            Toast.makeText(this, "Course create failed", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(this, "Course with id: " + _course_id + " create successfully", Toast.LENGTH_SHORT).show();
        }
        Intent i = new Intent(this, YogaCourseDetailsActivity.class);
        YogaCourse newCourse = new YogaCourse(_course_id,
                                                _name,
                                                _dayOfWeek,
                                                _time,
                                                _capacity,
                                                _duration,
                                                _price,
                                                _type,
                                                _description);
        i.putExtra("course_id", newCourse.getYogaCourseId());
        i.putExtra("course_name", newCourse.getName());
        i.putExtra("course_dayOfWeek", newCourse.getDayOfWeek());
        i.putExtra("course_time", newCourse.getTime());
        i.putExtra("course_capacity", String.valueOf(newCourse.getCapacity()));
        i.putExtra("course_duration", newCourse.getDuration());
        i.putExtra("course_price", String.valueOf(newCourse.getPrice()));
        i.putExtra("course_type", newCourse.getType());
        i.putExtra("course_description", newCourse.getDescription());
        startActivity(i);
    }

    private void addNewCourse(){
        YogaCourse newCourse = new YogaCourse(null,
                _name,
                _dayOfWeek,
                _time,
                _capacity,
                _duration,
                _price,
                _type,
                _description);
        DB = new DatabaseHelper(getApplicationContext());
        try {
            _course_id = String.valueOf(DB.insertYogaCourse(newCourse));
        } catch (RuntimeException e) {
            _isSuccessfulAdded = false;
            return;
        }
        _isSuccessfulAdded = true;
    }

    private class AddDatabaseYogaCourseThread extends Thread {
        @Override
        public void run() {
            try {
                addNewCourse();
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }
    }

    public void onClickClear(View view){
        EditText editTextName = findViewById(R.id.editTextCourseName);
        editTextName.setText("");
        Spinner spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        spinnerDayOfWeek.setSelection(0);
        Spinner spinnerTime = findViewById(R.id.spinnerTime);
        spinnerTime.setSelection(0);
        EditText editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextCapacity.setText("");
        Spinner spinnerDuration = findViewById(R.id.spinnerDuration);
        spinnerDuration.setSelection(0);
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        editTextPrice.setText("");
        Spinner spinnerType = findViewById(R.id.spinnerType);
        spinnerType.setSelection(0);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        editTextDescription.setText("");
        InitializeSetErrorMessageInvisible();
    }


    private void InitializeSetErrorMessageInvisible(){
        textViewNameErrorMessage = findViewById(R.id.textViewNameErrorMessage);
        textViewDayOfWeekErrorMessage = findViewById(R.id.textViewDayOfWeekErrorMessage);
        textViewTimeErrorMessage = findViewById(R.id.textViewTimeErrorMessage);
        textViewCapacityErrorMessage = findViewById(R.id.textViewCapacityErrorMessage);
        textViewDurationErrorMessage = findViewById(R.id.textViewDurationErrorMessage);
        textViewPriceErrorMessage = findViewById(R.id.textViewPriceErrorMessage);
        textViewTypeErrorMessage = findViewById(R.id.textViewTypeErrorMessage);

        textViewNameErrorMessage.setVisibility(View.INVISIBLE);
        textViewDayOfWeekErrorMessage.setVisibility(View.INVISIBLE);
        textViewTimeErrorMessage.setVisibility(View.INVISIBLE);
        textViewCapacityErrorMessage.setVisibility(View.INVISIBLE);
        textViewDurationErrorMessage.setVisibility(View.INVISIBLE);
        textViewPriceErrorMessage.setVisibility(View.INVISIBLE);
        textViewTypeErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessageInvisible(TextView textView){
        textView.setVisibility(View.INVISIBLE);
    }

    private void setErrorMessageVisible(TextView textView, String message){
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    private boolean checkInputName(){
        EditText editTextName = findViewById(R.id.editTextCourseName);
        String nameTemp = editTextName.getText().toString();
        if(nameTemp.isEmpty()){
            setErrorMessageVisible(textViewNameErrorMessage, "Name cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewNameErrorMessage);
        }
        _name = nameTemp;
        return true;
    }

    private boolean checkInputDayOfWeek() {
        Spinner spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        String dayOfWeekTemp = spinnerDayOfWeek.getSelectedItem().toString();
        if (dayOfWeekTemp.isEmpty()) {
            setErrorMessageVisible(textViewDayOfWeekErrorMessage, "Day of week cannot be empty");
            return false;
        }
        else{
            setErrorMessageInvisible(textViewDayOfWeekErrorMessage);
        }
        _dayOfWeek = dayOfWeekTemp;
        return true;
    }

    private boolean checkInputTime() {
        Spinner spinnerTime = findViewById(R.id.spinnerTime);
        String timeTemp = spinnerTime.getSelectedItem().toString();
        if (timeTemp.isEmpty()) {
            setErrorMessageVisible(textViewTimeErrorMessage, "Time cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewTimeErrorMessage);
        }
        _time = timeTemp;
        return true;
    }

    private boolean checkInputCapacity(){
        EditText editTextCapacity = findViewById(R.id.editTextCapacity);
        String capacityTemp = editTextCapacity.getText().toString();
        if(capacityTemp.isEmpty()){
            setErrorMessageVisible(textViewCapacityErrorMessage, "Capacity cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewCapacityErrorMessage);
        }
        try {
            _capacity = Integer.parseInt(capacityTemp);
        } catch (NumberFormatException e) {
            setErrorMessageVisible(textViewCapacityErrorMessage, "Capacity must be a number");
            return false;
        }
        return true;
    }

    private boolean checkInputDuration() {
        Spinner spinnerDuration = findViewById(R.id.spinnerDuration);
        String durationTemp = spinnerDuration.getSelectedItem().toString();
        if (durationTemp.isEmpty()) {
            setErrorMessageVisible(textViewDurationErrorMessage, "Duration cannot be empty");
            return false;
        } else {
            setErrorMessageInvisible(textViewDurationErrorMessage);
        }
        _duration = durationTemp;
        return true;
    }

    private boolean checkInputPrice(){
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        String priceTemp = editTextPrice.getText().toString();
        if(priceTemp.isEmpty()){
            setErrorMessageVisible(textViewPriceErrorMessage, "Price cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewPriceErrorMessage);
        }
        try {
            _price = Float.parseFloat(priceTemp);
        } catch (NumberFormatException e) {
            setErrorMessageVisible(textViewPriceErrorMessage, "Price must be a number");
            return false;
        }
        return true;
    }

    private boolean checkInputType() {
        Spinner spinnerType = findViewById(R.id.spinnerType);
        String typeTemp = spinnerType.getSelectedItem().toString();
        if (typeTemp.isEmpty()) {
            setErrorMessageVisible(textViewTypeErrorMessage, "Type cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewTypeErrorMessage);
        }
        _type = typeTemp;
        return true;
    }

    private boolean checkInputDescription(){
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        String descriptionTemp = editTextDescription.getText().toString();
        if(descriptionTemp.length() > 255){
            setErrorMessageVisible(textViewTypeErrorMessage, "Description cannot be more than 255 characters");
            return false;
        }
        else {
            setErrorMessageInvisible(textViewTypeErrorMessage);
        }
        _description = descriptionTemp;
        return true;
    }

}