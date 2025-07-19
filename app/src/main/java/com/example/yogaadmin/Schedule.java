package com.example.yogaadmin;

import java.util.Date;

public class Schedule {
    private String _yogaCourseScheduleId;
    private String _yogaCourseId;
    private Date _date;
    private String _teacherName;
    private String _comment;


    public Schedule(String yogaCourseId, Date date, String teacherName, String comment) {
        _date = date;
        _teacherName = teacherName;
        _comment = comment;
    }

    public Schedule(String yogaCourseScheduleId, String yogaCourseId, Date date, String teacherName, String comment) {
        if(yogaCourseScheduleId != null){this._yogaCourseScheduleId = yogaCourseScheduleId;}
        _yogaCourseId = yogaCourseId;
        _date = date;
        _teacherName = teacherName;
        _comment = comment;
    }



    public String getYogaCourseScheduleId() {return _yogaCourseScheduleId;}
    public String getYogaCourseId() {return _yogaCourseId;}
    public Date getDate() {return _date;}
    public String getTeacherName() {return _teacherName;}

}
