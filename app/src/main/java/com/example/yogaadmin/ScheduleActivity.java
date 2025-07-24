package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private EditText _editTextScheduleSearchBar;
    private ListView _listViewSchedule;
    private RelativeLayout _panelNoScheduleMessage, _panelLoading;
    private ScheduleAdapter _scheduleAdapter;
    private ArrayList<Schedule> _scheduleList, _filteredScheduleList;
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
        _panelNoScheduleMessage = findViewById(R.id.panelNoYogaCourseMessage);
        _panelLoading = findViewById(R.id.panelLoading);
        _scheduleList = new ArrayList<>();
        _filteredScheduleList = new ArrayList<>();
        _context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllScheduleList();
    }

    private void getAllScheduleList(){
        _panelLoading.setVisibility(View.VISIBLE);
        Thread getScheduleListThread = new Thread(new getScheduleListThread());
        getScheduleListThread.start();
        while(getScheduleListThread.isAlive()){}
        _panelLoading.setVisibility(View.GONE);
        if(_scheduleList.isEmpty()){
            _panelNoScheduleMessage.setVisibility(View.VISIBLE);
        }else{
            _panelNoScheduleMessage.setVisibility(View.GONE);
        }
        _scheduleAdapter = new ScheduleAdapter(this, _scheduleList);
        _listViewSchedule.setAdapter((ListAdapter) _scheduleAdapter);
        _listViewSchedule.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ScheduleDetailActivity.class);
                i.putExtra("schedule_id", _scheduleList.get(position).getYogaCourseScheduleId());
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

    public void onClickSearchSchedule(View view){
        String keyWord = _editTextScheduleSearchBar.getText().toString();

        Thread searchScheduleThread = new Thread(new searchScheduleThread(keyWord));
        searchScheduleThread.start();
        while(searchScheduleThread.isAlive()){}
        _scheduleAdapter = new ScheduleAdapter(this, _filteredScheduleList);
        _listViewSchedule.setAdapter((ListAdapter) _scheduleAdapter);
        _listViewSchedule.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ScheduleDetailActivity.class);
                i.putExtra("schedule_id", _filteredScheduleList.get(position).getYogaCourseScheduleId());
                i.putExtra("course_id", _filteredScheduleList.get(position).getYogaCourseId());
                i.putExtra("course_name", _filteredScheduleList.get(position).getCourseName());
                i.putExtra("course_dayOfWeek", _filteredScheduleList.get(position).getDayOfWeek());
                i.putExtra("course_date", _filteredScheduleList.get(position).getDate());
                i.putExtra("course_teacherName", _filteredScheduleList.get(position).getTeacherName());
                i.putExtra("course_comment", _filteredScheduleList.get(position).getComment());
                startActivity(i);
            }
        }));
    }


    /// Search method 1
//    private class searchScheduleThread1 implements Runnable{
//        private final String keyWord;
//        public searchScheduleThread(String searchText){
//            this.keyWord = searchText;
//        }
//
//        @Override
//        public void run(){
//            _filteredScheduleList.clear();
//            if(keyWord.isEmpty()){
//                _filteredScheduleList.addAll(_scheduleList);
//                return;
//            }
//            for(Schedule schedule : _scheduleList){
//                if(schedule.getTeacherName().toLowerCase().contains(keyWord.toLowerCase())){
//                    _filteredScheduleList.add(schedule);
//                }
//            }
//        }
//    }

    private class searchScheduleThread implements Runnable{
        private final String keyWord;
        public searchScheduleThread(String searchText){
            this.keyWord = searchText;
        }
        @Override
        public void run() {
            _filteredScheduleList.clear();
            _dbHelper = new DatabaseHelper(_context);
            if(keyWord.isEmpty()){
                _filteredScheduleList = _dbHelper.getYogaCourseJoinScheduleList();
                return;
            }
            _filteredScheduleList = _dbHelper.getYogaCourseJoinScheduleFilteredTeacherList(keyWord);
            _dbHelper.close();
        }
    }
}