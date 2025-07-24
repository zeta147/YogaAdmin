package com.example.yogaadmin;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<YogaCourse> yogaCourses;


    public FirebaseHelper() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    public void insertYogaCourse(YogaCourse yogaCourse, Context context) {
        try{
            databaseReference
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

    public void getYogaCourse(YogaCourse yogaCourse) {

    }
    public void updateYogaCourse(YogaCourse yogaCourse) {
        databaseReference.child("yogaCourses").push().setValue(yogaCourse);
    }

    public void deleteYogaCourse(YogaCourse yogaCourse) {
        databaseReference.child("yogaCourses").push().setValue(yogaCourse);
    }



}
