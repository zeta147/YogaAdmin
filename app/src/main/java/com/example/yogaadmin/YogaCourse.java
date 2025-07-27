package com.example.yogaadmin;
public class YogaCourse {
    private String yogaCourseId;
    private String name;
    private String dayOfWeek;
    private String time;
    private int capacity;
    private String duration;
    private float price;
    private String type;
    private String description;
    private int isUploaded;


    public YogaCourse(){
        this.yogaCourseId = null;
        this.name = null;
        this.dayOfWeek = null;
        this.time = null;
        this.capacity = 0;
        this.duration = null;
        this.price = 0;
        this.type = null;
        this.description = null;
        this.isUploaded = 0;
    }

    public YogaCourse(String course_id, String name, String dayOfWeek, String time, int capacity, String duration, float price, String type, String description){
        if(course_id != null){this.yogaCourseId = course_id;}
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
//        this._price = Float.parseFloat(String.format("%.2f", price));
        this.type = type;
        if(description != null){this.description = description;}
        this.isUploaded = 0;
    }

    public String getYogaCourseId(){return this.yogaCourseId;}
    public String getName(){return this.name;}
    public String getDayOfWeek(){return this.dayOfWeek;}
    public String getTime(){return this.time;}
    public int getCapacity(){return this.capacity;}
    public String getDuration(){return this.duration;}
    public float getPrice(){return this.price;}
    public String getType(){return this.type;}
    public String getDescription(){return this.description;}
    public int getIsUploaded(){return this.isUploaded;}


    public void setYogaCourseId(String course_id){this.yogaCourseId = course_id;}
    public void setName(String name){this.name = name;}
    public void setDayOfWeek(String dayOfWeek){this.dayOfWeek = dayOfWeek;}
    public void setTime(String time){this.time = time;}
    public void setCapacity(int capacity){this.capacity = capacity;}
    public void setDuration(String duration){this.duration = duration;}
    public void setPrice(float price){this.price = price;}
    public void setType(String type){this.type = type;}
    public void setDescription(String description){this.description = description;}
    public void setIsUploaded(int isUploaded){this.isUploaded = isUploaded;}
}
