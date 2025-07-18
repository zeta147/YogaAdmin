package com.example.yogaadmin;

import android.annotation.SuppressLint;

public class YogaCourse {
    private String _yogaCourseId;
    private String _name;
    private String _dayOfWeek;
    private String _time;
    private int _capacity;
    private String _duration;
    private float _price;
    private String _type;
    private String _description;


    public YogaCourse(String course_id, String name, String dayOfWeek, String time, int capacity, String duration, float price, String type, String description){
        if(course_id != null){this._yogaCourseId = course_id;}
        this._name = name;
        this._dayOfWeek = dayOfWeek;
        this._time = time;
        this._capacity = capacity;
        this._duration = duration;
        this._price = price;
//        this._price = Float.parseFloat(String.format("%.2f", price));
        this._type = type;
        if(description != null){this._description = description;}
    }

    public String getYogaCourseId(){return this._yogaCourseId;}
    public String getName(){return this._name;}
    public String getDayOfWeek(){return this._dayOfWeek;}
    public String getTime(){return this._time;}
    public int getCapacity(){return this._capacity;}
    public String getDuration(){return this._duration;}
    public float getPrice(){return this._price;}
    public String getType(){return this._type;}
    public String getDescription(){return this._description;}
}
