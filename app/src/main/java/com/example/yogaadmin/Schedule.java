package com.example.yogaadmin;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Schedule {
    private String _yogaCourseScheduleId;
    private String _yogaCourseId;
    private String _courseName;
    private String _dayOfWeek;
    private String _date;
    private String _teacherName;
    private String _comment;
    private static final DayOfWeekEnum[] dayOfWeekList = DayOfWeekEnum.values();

    /// constructors for creating schedule
    public Schedule(String yogaCourseId, String date, String teacherName, String comment) {
        _yogaCourseId = yogaCourseId;
        _date = date;
        _teacherName = teacherName;
        _comment = comment;
    }

    /// constructors for updating schedule
    public Schedule(String yogaCourseScheduleId, String yogaCourseId, String date, String teacherName, String comment) {
        if(yogaCourseScheduleId != null){this._yogaCourseScheduleId = yogaCourseScheduleId;}
        _yogaCourseId = yogaCourseId;
        _date = date;
        _teacherName = teacherName;
        _comment = comment;
    }

    /// constructors for listing schedule
    public Schedule(String yogaCourseScheduleId, String yogaCourseId, String courseName, String dayOfWeek, String date, String teacherName, String comment){
        _yogaCourseScheduleId = yogaCourseScheduleId;
        _yogaCourseId = yogaCourseId;
        _courseName = courseName;
        _dayOfWeek = dayOfWeek;
        _date = date;
        _teacherName = teacherName;
        _comment = comment;
    }

    public String getYogaCourseScheduleId() {return _yogaCourseScheduleId;}
    public String getYogaCourseId() {return _yogaCourseId;}
    public String getCourseName() {return _courseName;} // for listing schedule
    public String getDate() {return _date;}
    public String getDayOfWeek() {return _dayOfWeek;} // for listing schedule
    public String getTeacherName() {return _teacherName;}
    public String getComment() {return _comment;}

    public String getDateForListView(){
        String dateString = _date;
        String[] dateStringArray = dateString.split("-");
        int year = Integer.parseInt(dateStringArray[0]);
        int month = Integer.parseInt(dateStringArray[1]);
        int day = Integer.parseInt(dateStringArray[2]);
        return day + "-" + month + "-" + year;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseDateStringToDate(String dateString){
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static String parseDateToString(Date date){
        if(date == null){return null;}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    public static String getDayOfWeek(int dayOfWeekInt){
        return dayOfWeekList[dayOfWeekInt-1].toString();
    }
}
