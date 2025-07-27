package com.example.yogaadmin;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class FirebaseHelper {
    private final DatabaseReference _databaseReference;

    public FirebaseHelper() {
        FirebaseDatabase _firebaseDatabase = FirebaseDatabase.getInstance();
        _databaseReference = _firebaseDatabase.getReference();
    }

    public void insertYogaCourse(YogaCourse yogaCourse) {
        try{
            _databaseReference
            .child("yogaCourses")
            .child(String.valueOf(yogaCourse.getYogaCourseId()))
            .setValue(yogaCourse)
            .addOnSuccessListener(taskSnapshot -> {
                // Handle success
                Log.d("Firebase", "Yoga course added to firebase successfully");
            })
            .addOnFailureListener(e -> {
                // Handle failure
                Log.w("Firebase", "Failed to add yoga course to firebase");
            });
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateYogaCourse(YogaCourse yogaCourse) {
        _databaseReference.child("yogaCourses").child(yogaCourse.getYogaCourseId()).setValue(yogaCourse);
    }

    public void deleteYogaCourse(YogaCourse yogaCourse) {
        _databaseReference.child("yogaCourses").child(yogaCourse.getYogaCourseId()).removeValue();
    }

    public void deleteAllYogaCourses() {
        _databaseReference.child("yogaCourses").removeValue();
    }

    public long getYogaCourseCount(){
        final long[] count = {0};
        _databaseReference.child("yogaCourses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count[0] = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return count[0];
    }

    public void insertSchedule(Schedule schedule) {
        try {
            _databaseReference.child("schedules")
            .child(String.valueOf(schedule.getScheduleId()))
            .setValue(schedule).addOnSuccessListener(taskSnapshot -> {
                // Handle success
                Log.d("Firebase", "Schedule added to firebase successfully");
            })
            .addOnFailureListener(e -> {
                // Handle failure
                Log.w("Firebase", "Failed to add schedule to firebase");
            });
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateSchedule(Schedule schedule) {
        _databaseReference.child("schedules").child(schedule.getScheduleId()).setValue(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        _databaseReference.child("schedules").child(schedule.getScheduleId()).removeValue();
    }

    public void deleteAllSchedules() {
        _databaseReference.child("schedules").removeValue();
    }

    public long getScheduleCount() {
        final long[] count = {0};
        _databaseReference.child("schedules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count[0] = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return count[0];
    }

}
