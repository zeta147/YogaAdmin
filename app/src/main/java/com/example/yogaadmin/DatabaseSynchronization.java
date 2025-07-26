package com.example.yogaadmin;

import android.content.Context;

import java.util.ArrayList;

public class DatabaseSynchronization {
    private DatabaseHelper _dbHelper;
    private FirebaseHelper _firebaseHelper;
    private Context _context;

    public DatabaseSynchronization(Context context) {
        _context = context;
        _dbHelper = new DatabaseHelper(_context);
        _firebaseHelper = new FirebaseHelper();
    }

    public void uploadToFireBase(){
        _firebaseHelper.deleteAllYogaCourses();
        _firebaseHelper.deleteAllSchedules();

        ArrayList<YogaCourse> yogaCourses = _dbHelper.getAllYogaCourses();
        for (YogaCourse yogaCourse : yogaCourses) {
            _firebaseHelper.insertYogaCourse(yogaCourse, _context);
        }

        ArrayList<Schedule> schedules = _dbHelper.getAllSchedules();
        for (Schedule schedule : schedules) {
            _firebaseHelper.insertSchedule(schedule, _context);
        }
    }


    public void downloadFromFireBase(){
        _dbHelper.deleteAllYogaCoursesAndSchedules();
        ArrayList<YogaCourse> yogaCourses = _firebaseHelper.getAllYogaCourses();
        for (YogaCourse yogaCourse : yogaCourses) {
            _dbHelper.insertYogaCourse(yogaCourse);
        }
        ArrayList<Schedule> schedules = _firebaseHelper.getAllSchedules();
        for (Schedule schedule : schedules) {
            _dbHelper.insertYogaCourseSchedule(schedule);
        }
    }
}
