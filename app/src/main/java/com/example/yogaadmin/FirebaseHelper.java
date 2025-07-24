package com.example.yogaadmin;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseHelper {
    private FirebaseDatabase _firebaseDatabase;
    private DatabaseReference _databaseReference;
    private ArrayList<YogaCourse> _yogaCourses;
    private ArrayList<Schedule> _schedules;

    public FirebaseHelper() {
        this._firebaseDatabase = FirebaseDatabase.getInstance();
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



}
