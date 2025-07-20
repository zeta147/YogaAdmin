package com.example.yogaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listViewYogaCourse;
    Adapter yogaCourseAdapter;
    ArrayList<YogaCourse> yogaCoursesList;
    DatabaseHelper db;
    private Context context;

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

        listViewYogaCourse = findViewById(R.id.listViewYogaCourse);
        yogaCoursesList = new ArrayList<YogaCourse>();
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getYogaCoursesList();
    }



    private void getYogaCoursesList(){
        Thread getDatabaseYogaCourseListThread = new Thread(new GetDatabaseYogaCourseListThread());
        getDatabaseYogaCourseListThread.start();
        while(getDatabaseYogaCourseListThread.isAlive()){}
        yogaCourseAdapter = new YogaCourseAdapter(this, yogaCoursesList);
        listViewYogaCourse.setAdapter((ListAdapter) yogaCourseAdapter);
        listViewYogaCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), YogaCourseDetailsActivity.class);
                i.putExtra("course_id", yogaCoursesList.get(position).getYogaCourseId());
                i.putExtra("course_name", yogaCoursesList.get(position).getName());
                i.putExtra("course_dayOfWeek", yogaCoursesList.get(position).getDayOfWeek());
                i.putExtra("course_time", yogaCoursesList.get(position).getTime());
                i.putExtra("course_capacity", String.valueOf(yogaCoursesList.get(position).getCapacity()));
                i.putExtra("course_duration", yogaCoursesList.get(position).getDuration());
                i.putExtra("course_price", String.valueOf(yogaCoursesList.get(position).getPrice()));
                i.putExtra("course_type", yogaCoursesList.get(position).getType());
                i.putExtra("course_description", yogaCoursesList.get(position).getDescription());
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
            db = new DatabaseHelper(context);
            yogaCoursesList = db.getAllYogaCourses();
        }
    }



}