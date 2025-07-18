package com.example.yogaadmin;

public class Schedule {
    private String _yogaCourseScheduleId;
    private String _yogaCourseId;
    private String _date;
    private String _teacherName;
    private String _comment;

    public Schedule(String yogaCourseScheduleId, String yogaCourseId, String date, String teacherName, String comment) {
        if(yogaCourseScheduleId != null){this._yogaCourseScheduleId = yogaCourseScheduleId;}
        _yogaCourseId = yogaCourseId;
        _date = date;
        _teacherName = teacherName;
        _comment = comment;
    }

    public String getYogaCourseScheduleId() {return _yogaCourseScheduleId;}
    public String getYogaCourseId() {return _yogaCourseId;}
    public String getDate() {return _date;}
    public String getTeacherName() {return _teacherName;}

}
