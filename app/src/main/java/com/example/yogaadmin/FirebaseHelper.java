package com.example.yogaadmin;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


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
            .push()
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
            Log.e("FirebaseHelper", "Error: " + e.getMessage());
        }
    }

    public void updateYogaCourse(YogaCourse yogaCourse) {
        HashMap<String, Object> yogaCourseValues = new HashMap<>();
        yogaCourseValues.put("name", yogaCourse.getName());
        yogaCourseValues.put("dayOfWeek", yogaCourse.getDayOfWeek());
        yogaCourseValues.put("time", yogaCourse.getTime());
        yogaCourseValues.put("capacity", yogaCourse.getCapacity());
        yogaCourseValues.put("duration", yogaCourse.getDuration());
        yogaCourseValues.put("price", yogaCourse.getPrice());
        yogaCourseValues.put("type", yogaCourse.getType());
        yogaCourseValues.put("description", yogaCourse.getDescription());
        yogaCourseValues.put("isUploaded", yogaCourse.getIsUploaded());
        yogaCourseValues.put("isDeleted", yogaCourse.getIsDeleted());

        _databaseReference.child("yogaCourses")
                .orderByChild("yogaCourseId")
                .equalTo(yogaCourse.getYogaCourseId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot yogaCourseSnapshot : snapshot.getChildren()) {
                            yogaCourseSnapshot.getRef().updateChildren(yogaCourseValues);
                        }
                        Log.d("Firebase", "Yoga course updated in firebase successfully");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Firebase", "Failed to update yoga course in firebase");
                    }
        });
    }

    public void deleteYogaCourse(YogaCourse yogaCourse) {
        _databaseReference.child("yogaCourses")
        .orderByChild("yogaCourseId")
        .equalTo(yogaCourse.getYogaCourseId())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot yogaCourseSnapshot : snapshot.getChildren()) {
                    yogaCourseSnapshot.getRef().removeValue();
                }
                Log.d("Firebase", "Yoga course deleted from firebase successfully");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to delete yoga course from firebase");
            }
        });
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
            .push()
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
        HashMap<String, Object> scheduleValues = new HashMap<>();
        scheduleValues.put("courseId", schedule.getYogaCourseId());
        scheduleValues.put("date", schedule.getDate());
        scheduleValues.put("teacherName", schedule.getTeacherName());
        scheduleValues.put("comment", schedule.getComment());
        scheduleValues.put("isUploaded", schedule.getIsUploaded());
        scheduleValues.put("isDeleted", schedule.getIsDeleted());

        _databaseReference.child("schedules")
        .orderByChild("scheduleId")
        .equalTo(schedule.getScheduleId())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot scheduleSnapshot : snapshot.getChildren()) {
                    scheduleSnapshot.getRef().updateChildren(scheduleValues);
                }
                Log.d("Firebase", "Schedule updated in firebase successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to update schedule in firebase");
            }
        });
    }

    public void deleteSchedule(Schedule schedule) {
        _databaseReference.child("schedules")
        .orderByChild("scheduleId")
        .equalTo(schedule.getScheduleId())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot scheduleSnapshot : snapshot.getChildren()) {
                    scheduleSnapshot.getRef().removeValue();
                    Log.d("Firebase", "Schedule deleted from firebase successfully");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to delete schedule from firebase");
            }
        });
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
