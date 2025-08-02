package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private EditText _editTextScheduleSearchBar;
    private ListView _listViewSchedule;
    private Spinner _spinnerSearchType;
    private RelativeLayout _panelNoScheduleMessage, _panelLoading;
    private ScheduleAdapter _scheduleAdapter;
    private ArrayList<Schedule> _scheduleList;
    private DatabaseHelper _dbHelper;
    private Context _context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _editTextScheduleSearchBar = findViewById(R.id.editTextSearchBar);
        _listViewSchedule = findViewById(R.id.listViewSchedule);
        _spinnerSearchType = findViewById(R.id.spinnerSearchType);
        _panelNoScheduleMessage = findViewById(R.id.panelNoYogaCourseMessage);
        _panelLoading = findViewById(R.id.panelLoading);
        _scheduleList = new ArrayList<Schedule>();
        _context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllScheduleList(); // call this method to refresh the list
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

    /// get schedule list from database
    private void getAllScheduleList(){
        _panelLoading.setVisibility(View.VISIBLE);
        Thread getScheduleListThread = new Thread(new getScheduleListThread());
        getScheduleListThread.start();
        while(getScheduleListThread.isAlive()){}
        _panelLoading.setVisibility(View.GONE);
        if(_scheduleList.isEmpty()){
            _panelNoScheduleMessage.setVisibility(View.VISIBLE);
        }
        else{
            _panelNoScheduleMessage.setVisibility(View.GONE);
        }
        _scheduleAdapter = new ScheduleAdapter(this, _scheduleList);
        _listViewSchedule.setAdapter((ListAdapter) _scheduleAdapter);
        _listViewSchedule.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ScheduleDetailActivity.class);
                i.putExtra("schedule_id", _scheduleList.get(position).getScheduleId());
                i.putExtra("course_id", _scheduleList.get(position).getYogaCourseId());
                i.putExtra("course_name", _scheduleList.get(position).getCourseName());
                i.putExtra("course_dayOfWeek", _scheduleList.get(position).getDayOfWeek());
                i.putExtra("course_date", _scheduleList.get(position).getDate());
                i.putExtra("course_teacherName", _scheduleList.get(position).getTeacherName());
                i.putExtra("course_comment", _scheduleList.get(position).getComment());
                startActivity(i);
            }
        }));
    }

    /// this is a thread to get yoga course schedule list from database
    private class getScheduleListThread implements Runnable{
        @Override
        public void run() {
            try {
                _dbHelper = new DatabaseHelper(_context);
                _scheduleList =  _dbHelper.getYogaCourseJoinScheduleList();
            } catch (Exception e){
                e.printStackTrace();
            }
            _dbHelper.close();
        }
    }

    /// call when click search schedule to search teacher name of the schedule
    public void onClickSearchSchedule(View view){
        _panelLoading.setVisibility(View.VISIBLE);
        String searchType = _spinnerSearchType.getSelectedItem().toString();
        String keyWord = _editTextScheduleSearchBar.getText().toString();
        Thread searchScheduleThread = new Thread(new searchScheduleThread(keyWord, searchType));
        searchScheduleThread.start();
        while(searchScheduleThread.isAlive()){}
        _panelLoading.setVisibility(View.GONE);
        if(_scheduleList.isEmpty()){
            _panelNoScheduleMessage.setVisibility(View.VISIBLE);
        }
        else {
            _panelNoScheduleMessage.setVisibility(View.GONE);
        }
        _scheduleAdapter = new ScheduleAdapter(this, _scheduleList);
        _listViewSchedule.setAdapter((ListAdapter) _scheduleAdapter);
        _listViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ScheduleDetailActivity.class);
                i.putExtra("schedule_id", _scheduleList.get(position).getScheduleId());
                i.putExtra("course_id", _scheduleList.get(position).getYogaCourseId());
                i.putExtra("course_name", _scheduleList.get(position).getCourseName());
                i.putExtra("course_dayOfWeek", _scheduleList.get(position).getDayOfWeek());
                i.putExtra("course_date", _scheduleList.get(position).getDate());
                i.putExtra("course_teacherName", _scheduleList.get(position).getTeacherName());
                i.putExtra("course_comment", _scheduleList.get(position).getComment());
                startActivity(i);
            }
        });
    }


    /// this is a thread to get schedule list having a searching teacher name from database
    private class searchScheduleThread implements Runnable{
        private final String keyWord;
        private final String searchType;
        public searchScheduleThread(String searchText, String searchType){
            this.searchType = searchType;
            this.keyWord = searchText;
        }
        @Override
        public void run() {
            _scheduleList.clear();
            _dbHelper = new DatabaseHelper(_context);
            if(keyWord.isEmpty()){
                _scheduleList = _dbHelper.getYogaCourseJoinScheduleList();
                _dbHelper.close();
                return;
            }
            else {
                _scheduleList = _dbHelper.getScheduleSearchList(keyWord, searchType);
            }
            _dbHelper.close();
        }
    }
}