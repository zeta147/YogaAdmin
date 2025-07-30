package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView _textViewNameErrorMessage,
            _textViewDayOfWeekErrorMessage,
            _textViewTimeErrorMessage,
            _textViewCapacityErrorMessage,
            _textViewDurationErrorMessage,
            _textViewPriceErrorMessage,
            _textViewTypeErrorMessage;
    private Context _context;

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
        _context = this;
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

    /// set error message invisible when this activity is created
    private void InitializeSetErrorMessageInvisible(){
        _textViewNameErrorMessage = findViewById(R.id.textViewNameErrorMessage);
        _textViewDayOfWeekErrorMessage = findViewById(R.id.textViewDayOfWeekErrorMessage);
        _textViewTimeErrorMessage = findViewById(R.id.textViewTimeErrorMessage);
        _textViewCapacityErrorMessage = findViewById(R.id.textViewCapacityErrorMessage);
        _textViewDurationErrorMessage = findViewById(R.id.textViewDurationErrorMessage);
        _textViewPriceErrorMessage = findViewById(R.id.textViewPriceErrorMessage);
        _textViewTypeErrorMessage = findViewById(R.id.textViewTypeErrorMessage);

        _textViewNameErrorMessage.setVisibility(View.INVISIBLE);
        _textViewDayOfWeekErrorMessage.setVisibility(View.INVISIBLE);
        _textViewTimeErrorMessage.setVisibility(View.INVISIBLE);
        _textViewCapacityErrorMessage.setVisibility(View.INVISIBLE);
        _textViewDurationErrorMessage.setVisibility(View.INVISIBLE);
        _textViewPriceErrorMessage.setVisibility(View.INVISIBLE);
        _textViewTypeErrorMessage.setVisibility(View.INVISIBLE);
    }

    /// call when click view add yoga course to insert new course to database
    public void onCLickAddCourse(View view){
        boolean canAdd;
        // check all input value, return false if any input value is empty
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
        Thread t = new Thread(new AddYogaCourseThread());
        t.start();
        while(t.isAlive()){} // wait for thread to finish
        Toast.makeText(this, "Course create successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    /// insert new course to database method
    private void addNewCourse(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        YogaCourse newCourse = new YogaCourse(null,
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

        if(!NetworkConnection.isConnected(_context)) {
            dbHelper.insertYogaCourse(newCourse);
            dbHelper.close();
            return;
        }
        newCourse.setIsUploaded(1);
        _course_id = String.valueOf(dbHelper.insertYogaCourse(newCourse));
        newCourse.setYogaCourseId(_course_id);
        firebaseHelper.insertYogaCourse(newCourse);
        dbHelper.close();
    }

    /// this is a thread to insert new course to database
    private class AddYogaCourseThread extends Thread {
        @Override
        public void run() {
            try {
                addNewCourse();
            }catch (Exception e){
                Log.e("AddYogaCourseThread", "Error: " + e.getMessage());
            }
        }
    }


    /// call when click view clear to clear all value on input fields
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


    /// set invisible error message
    private void setErrorMessageInvisible(TextView textView){
        textView.setVisibility(View.INVISIBLE);
    }

    /// set visible error message and set error message to the text view error message
    private void setErrorMessageVisible(TextView textView, String message){
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    /// check input name is empty and return false, return true if name is not empty
    private boolean checkInputName(){
        EditText editTextName = findViewById(R.id.editTextCourseName);
        String nameTemp = editTextName.getText().toString();
        if(nameTemp.isEmpty()){
            setErrorMessageVisible(_textViewNameErrorMessage, "Name cannot be empty");
            return false;
        }
        else if(nameTemp.length() > 255){
            setErrorMessageVisible(_textViewNameErrorMessage, "Name cannot be more than 255 characters");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewNameErrorMessage);
        }
        _name = nameTemp;
        return true;
    }

    /// check input day of week is empty and return false, return true if day of week is not empty
    private boolean checkInputDayOfWeek() {
        Spinner spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        String dayOfWeekTemp = spinnerDayOfWeek.getSelectedItem().toString();
        if (dayOfWeekTemp.isEmpty()) {
            setErrorMessageVisible(_textViewDayOfWeekErrorMessage, "Day of week cannot be empty");
            return false;
        }
        else{
            setErrorMessageInvisible(_textViewDayOfWeekErrorMessage);
        }
        _dayOfWeek = dayOfWeekTemp;
        return true;
    }

    /// check input time is empty and return false, return true if time is not empty
    private boolean checkInputTime() {
        Spinner spinnerTime = findViewById(R.id.spinnerTime);
        String timeTemp = spinnerTime.getSelectedItem().toString();
        if (timeTemp.isEmpty()) {
            setErrorMessageVisible(_textViewTimeErrorMessage, "Time cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewTimeErrorMessage);
        }
        _time = timeTemp;
        return true;
    }

    /// check input capacity is empty and return false, return true if capacity is not empty
    private boolean checkInputCapacity(){
        EditText editTextCapacity = findViewById(R.id.editTextCapacity);
        String capacityTemp = editTextCapacity.getText().toString();
        if(capacityTemp.isEmpty()){
            setErrorMessageVisible(_textViewCapacityErrorMessage, "Capacity cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewCapacityErrorMessage);
        }
        try {
            _capacity = Integer.parseInt(capacityTemp);
        } catch (NumberFormatException e) {
            setErrorMessageVisible(_textViewCapacityErrorMessage, "Capacity is not valid (or is too large)");
            return false;
        }
        return true;
    }

    /// check input duration is empty and return false, return true if duration is not empty
    private boolean checkInputDuration() {
        Spinner spinnerDuration = findViewById(R.id.spinnerDuration);
        String durationTemp = spinnerDuration.getSelectedItem().toString();
        if (durationTemp.isEmpty()) {
            setErrorMessageVisible(_textViewDurationErrorMessage, "Duration cannot be empty");
            return false;
        } else {
            setErrorMessageInvisible(_textViewDurationErrorMessage);
        }
        _duration = durationTemp;
        return true;
    }

    /// check input price is empty and return false, return true if price is not empty
    private boolean checkInputPrice(){
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        String priceTemp = editTextPrice.getText().toString();
        if(priceTemp.isEmpty()){
            setErrorMessageVisible(_textViewPriceErrorMessage, "Price cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewPriceErrorMessage);
        }
        try {
            _price = Float.parseFloat(priceTemp);
        } catch (NumberFormatException e) {
            setErrorMessageVisible(_textViewPriceErrorMessage, "Price is not valid (or is too large)");
            return false;
        }
        return true;
    }

    /// check input type is empty and return false, return true if type is not empty
    private boolean checkInputType() {
        Spinner spinnerType = findViewById(R.id.spinnerType);
        String typeTemp = spinnerType.getSelectedItem().toString();
        if (typeTemp.isEmpty()) {
            setErrorMessageVisible(_textViewTypeErrorMessage, "Type cannot be empty");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewTypeErrorMessage);
        }
        _type = typeTemp;
        return true;
    }

    /// check input description has more than 255 characters and return false, return true if description has less than 255 characters
    private boolean checkInputDescription(){
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        String descriptionTemp = editTextDescription.getText().toString();
        if(descriptionTemp.length() > 255){
            setErrorMessageVisible(_textViewTypeErrorMessage, "Description cannot be more than 255 characters");
            return false;
        }
        else {
            setErrorMessageInvisible(_textViewTypeErrorMessage);
        }
        _description = descriptionTemp;
        return true;
    }

}