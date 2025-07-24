package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class YogaCourseActivity extends AppCompatActivity {
    private ListView _listViewYogaCourse;
    private RelativeLayout _panelNoYogaCourseMessage,
                            _panelLoading;
    private Adapter _yogaCourseAdapter;
    private ArrayList<YogaCourse> _yogaCoursesList;
    private DatabaseHelper _db;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _listViewYogaCourse = findViewById(R.id.listViewYogaCourse);
        _panelNoYogaCourseMessage = findViewById(R.id.panelNoYogaCourseMessage);
        _panelLoading = findViewById(R.id.panelLoading);
        _yogaCoursesList = new ArrayList<YogaCourse>();
        _context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getYogaCoursesList();
    }



    private void getYogaCoursesList(){
        _panelNoYogaCourseMessage.setVisibility(View.GONE);
        _panelLoading.setVisibility(View.VISIBLE);
        Thread getDatabaseYogaCourseListThread = new Thread(new GetDatabaseYogaCourseListThread());
        getDatabaseYogaCourseListThread.start();
        while(getDatabaseYogaCourseListThread.isAlive()){}
        _panelLoading.setVisibility(View.GONE);
        if(_yogaCoursesList.isEmpty()){
            _panelNoYogaCourseMessage.setVisibility(View.VISIBLE);
        }
        _yogaCourseAdapter = new YogaCourseAdapter(this, _yogaCoursesList);
        _listViewYogaCourse.setAdapter((ListAdapter) _yogaCourseAdapter);
        _listViewYogaCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), YogaCourseDetailsActivity.class);
                i.putExtra("course_id", _yogaCoursesList.get(position).getYogaCourseId());
                i.putExtra("course_name", _yogaCoursesList.get(position).getName());
                i.putExtra("course_dayOfWeek", _yogaCoursesList.get(position).getDayOfWeek());
                i.putExtra("course_time", _yogaCoursesList.get(position).getTime());
                i.putExtra("course_capacity", String.valueOf(_yogaCoursesList.get(position).getCapacity()));
                i.putExtra("course_duration", _yogaCoursesList.get(position).getDuration());
                i.putExtra("course_price", String.valueOf(_yogaCoursesList.get(position).getPrice()));
                i.putExtra("course_type", _yogaCoursesList.get(position).getType());
                i.putExtra("course_description", _yogaCoursesList.get(position).getDescription());
                startActivity(i);
            }
        });

    }

    public void onClickViewAddYogaCourse(View view){
        Intent i = new Intent(this, YogaCourseCreateActivity.class);
        startActivity(i);
    }

    public void onClickViewYogaCourseSchedule(View view){
        Intent i = new Intent(this, ScheduleActivity.class);
        startActivity(i);
    }

    private class GetDatabaseYogaCourseListThread implements Runnable {
        @Override
        public void run() {
            _db = new DatabaseHelper(_context);
            _yogaCoursesList = _db.getAllYogaCourses();
        }
    }



}