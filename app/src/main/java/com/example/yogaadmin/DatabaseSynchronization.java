package com.example.yogaadmin;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseSynchronization {
    private DatabaseHelper _dbHelper;
    private FirebaseHelper _firebaseHelper;
    private Context _context;
    private ArrayList<YogaCourse> _yogaCoursesList;
    private ArrayList<Schedule> _schedulesList;

    public DatabaseSynchronization(Context context) {
        _context = context;
        _dbHelper = new DatabaseHelper(_context);
        _firebaseHelper = new FirebaseHelper();
    }

    public void uploadToFireBase() {
        _yogaCoursesList = _dbHelper.getYogaCourseNotUpload();
        if (_yogaCoursesList.isEmpty()) {
            Thread insertYogaCourseToFirebaseThread = new Thread(new insertUpdateYogaCourseThread());
            insertYogaCourseToFirebaseThread.start();
        }

        _schedulesList = _dbHelper.getScheduleNotUpload();
        if (_schedulesList.isEmpty()) {
            Thread insertScheduleToFirebaseThread = new Thread(new insertUpdateScheduleThread());
            insertScheduleToFirebaseThread.start();
        }
    }


    /// upload yoga course to firebase and update yoga course in local database
    private class insertUpdateYogaCourseThread implements Runnable {

        @Override
        public void run() {
            for (YogaCourse yogaCourse : _yogaCoursesList) {
                yogaCourse.setIsUploaded(1);
                _firebaseHelper.insertYogaCourse(yogaCourse);
                _dbHelper.updateYogaCourse(yogaCourse);
            }
        }
    }

    /// upload yoga course to firebase and update yoga course in local database
    private class insertUpdateScheduleThread implements Runnable {

        @Override
        public void run() {
            for (Schedule schedule : _schedulesList) {
                schedule.setIsUploaded(1);
                _firebaseHelper.insertSchedule(schedule);
                _dbHelper.updateSchedule(schedule);
            }
        }
    }
}
