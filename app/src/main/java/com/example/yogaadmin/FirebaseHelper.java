package com.example.yogaadmin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {
    private final DatabaseReference _databaseReference;

    public FirebaseHelper() {
        FirebaseDatabase _firebaseDatabase = FirebaseDatabase.getInstance();
        _databaseReference = _firebaseDatabase.getReference();
    }


    public void insertYogaCourse(YogaCourse yogaCourse, Context context) {
        try{
            _databaseReference
            .child("yogaCourses")
            .child(String.valueOf(yogaCourse.getYogaCourseId()))
            .setValue(yogaCourse)
            .addOnSuccessListener(taskSnapshot -> {
                // Handle success
                Toast.makeText(context, "Yoga course added to firebase successfully", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                // Handle failure
                Toast.makeText(context, "Failed to add yoga course to firebase", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateYogaCourse(YogaCourse yogaCourse) {
        _databaseReference.child("yogaCourses").push().setValue(yogaCourse);
    }

    public void deleteYogaCourse(YogaCourse yogaCourse) {
        _databaseReference.child("yogaCourses").push().setValue(yogaCourse);
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

    public ArrayList<YogaCourse> getAllYogaCourses() {
        ArrayList<YogaCourse> yogaCourses = new ArrayList<>();
        _databaseReference.child("yogaCourses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                yogaCourses.clear();
                for (DataSnapshot yogaCourseSnapshot : dataSnapshot.getChildren()) {
                    YogaCourse yogaCourse = yogaCourseSnapshot.getValue(YogaCourse.class);
                    yogaCourses.add(yogaCourse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Failed to read value.", error.toException());
            }
        });
        return yogaCourses;
    }

    public void insertSchedule(Schedule schedule, Context context) {
        try {
            _databaseReference.child("schedules")
            .child(String.valueOf(schedule.getScheduleId()))
            .setValue(schedule).addOnSuccessListener(taskSnapshot -> {
                // Handle success
                Toast.makeText(context, "Schedule added to firebase successfully", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                // Handle failure
                Toast.makeText(context, "Failed to add schedule to firebase", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateSchedule(Schedule schedule) {
        _databaseReference.child("schedules").push().setValue(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        _databaseReference.child("schedules").push().setValue(schedule);
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

    public ArrayList<Schedule> getAllSchedules() {
        ArrayList<Schedule> schedules = new ArrayList<>();
        _databaseReference.child("schedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                schedules.clear();
                for (DataSnapshot scheduleSnapshot : snapshot.getChildren()) {
                    Schedule schedule = scheduleSnapshot.getValue(Schedule.class);
                    schedules.add(schedule);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Failed to read value.", error.toException());
            }
        });
        return schedules;
    }
}
