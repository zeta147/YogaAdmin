package com.example.yogaadmin;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseSynchronization {
    private Context _context;

    public DatabaseSynchronization(Context context) {
        _context = context;
    }

    public void syncSQLiteFirebase() {
        Log.d("Sync check network connection", "Network status: " + NetworkConnection.isConnected(_context));
        if(!NetworkConnection.isConnected(_context))
            return;

        Thread insertYogaCourseToFirebaseThread = new Thread(new insertUpdateYogaCourseThread());
        insertYogaCourseToFirebaseThread.start();

        Thread insertScheduleToFirebaseThread = new Thread(new insertUpdateScheduleThread());
        insertScheduleToFirebaseThread.start();

        Thread deleteYogaCourseThread = new Thread(new deleteYogaCourseThread());
        deleteYogaCourseThread.start();

        Thread deleteScheduleThread = new Thread(new deleteScheduleThread());
        deleteScheduleThread.start();
    }



    /// upload yoga course to firebase and update yoga course in local database
    private class insertUpdateYogaCourseThread implements Runnable {
        @Override
        public void run() {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(_context);
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                ArrayList<YogaCourse> notUploadYogaCoursesList = dbHelper.getYogaCoursesNotUpload();
                Log.d("insertUpdateYogaCourseThread", "notUploadYogaCoursesList: " + notUploadYogaCoursesList.size());

                if(notUploadYogaCoursesList.isEmpty())
                    return;

                for (YogaCourse yogaCourse : notUploadYogaCoursesList) {
                    yogaCourse.setIsUploaded(1);
                    firebaseHelper.updateYogaCourse(yogaCourse);
                    dbHelper.updateYogaCourse(yogaCourse);
                }
                Log.d("insertUpdateYogaCourseThread", "notUploadYogaCoursesList success");
                dbHelper.close();
            }
            catch (Exception e){
                Log.e("DatabaseSynchronization", "Error getting not upload yoga course list:" + e.getMessage());
            }

        }
    }

    /// upload yoga course to firebase and update yoga course in local database
    private class insertUpdateScheduleThread implements Runnable {

        @Override
        public void run() {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(_context);
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                ArrayList<Schedule> _notUploadSchedulesList = dbHelper.getSchedulesNotUpload();
                Log.d("insertUpdateScheduleThread", "notUploadSchedulesList: " + _notUploadSchedulesList.size());

                if(_notUploadSchedulesList.isEmpty())
                    return;

                for (Schedule schedule : _notUploadSchedulesList) {
                    schedule.setIsUploaded(1);
                    firebaseHelper.updateSchedule(schedule);
                    dbHelper.updateSchedule(schedule);
                }
                Log.d("insertUpdateScheduleThread", "notUploadSchedulesList success");
                dbHelper.close();
            }
            catch (Exception e){
                Log.e("DatabaseSynchronization", "Error getting not upload schedule list:" + e.getMessage());
            }
        }
    }

    /// delete yoga course from firebase and update yoga course in local database
    private class deleteYogaCourseThread implements Runnable {
        @Override
        public void run() {
            try{
                DatabaseHelper dbHelper = new DatabaseHelper(_context);
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                ArrayList<YogaCourse> deletedYogaCoursesList = dbHelper.getYogaCoursesDeleted();
                Log.d("deleteYogaCourseThread", "deletedYogaCoursesList: " + deletedYogaCoursesList.size());

                if(deletedYogaCoursesList.isEmpty())
                    return;

                for (YogaCourse yogaCourse : deletedYogaCoursesList) {
                    firebaseHelper.deleteYogaCourse(yogaCourse);
                    dbHelper.deleteYogaCourse(yogaCourse);
                }
                Log.d("deleteYogaCourseThread", "deletedYogaCoursesList success");
                dbHelper.close();
            }
            catch (Exception e){
                Log.e("DatabaseSynchronization", "Error getting deleted yoga course list:" + e.getMessage());
            }
        }
    }

    /// delete yoga course from firebase and update yoga course in local database
    private class deleteScheduleThread implements Runnable {
        @Override
        public void run() {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(_context);
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                ArrayList<Schedule> deletedSchedulesList = dbHelper.getSchedulesDeleted();
                Log.d("deleteScheduleThread", "deletedSchedulesList: " + deletedSchedulesList.size());
                if(deletedSchedulesList.isEmpty())
                    return;

                for (Schedule schedule : deletedSchedulesList) {
                    firebaseHelper.deleteSchedule(schedule);
                    dbHelper.deleteSchedule(schedule);
                }
                Log.d("deleteScheduleThread", "deletedSchedulesList success");
                dbHelper.close();
            }
            catch (Exception e){
                Log.e("DatabaseSynchronization", "Error getting deleted schedule list:" + e.getMessage());
            }
        }
    }
}
