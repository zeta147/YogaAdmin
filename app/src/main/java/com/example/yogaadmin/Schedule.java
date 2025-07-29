package com.example.yogaadmin;

public class Schedule {
    private String scheduleId;
    private String yogaCourseId;
    private String courseName; // for listing schedule
    private String dayOfWeek; // for checking correct day of week
    private String date;
    private String teacherName;
    private String comment;
    private int isUploaded;
    private int isDeleted;

    private static final DayOfWeekEnum[] _dayOfWeekList = DayOfWeekEnum.values();

    /// constructors for creating schedule with default value
    public Schedule(){
        yogaCourseId = null;
        date = null;
        teacherName = null;
        comment = null;
        isUploaded = 0;
        isDeleted = 0;
    }

    /// constructors for creating schedule
    public Schedule(String yogaCourseId, String date, String teacherName, String comment) {
        this.yogaCourseId = yogaCourseId;
        this.date = date;
        this.teacherName = teacherName;
        this.comment = comment;
        this.isUploaded = 0;
        this.isDeleted = 0;
    }

    /// constructors for updating schedule
    public Schedule(String yogaCourseScheduleId, String yogaCourseId, String date, String teacherName, String comment, int isUploaded, int isDeleted) {
        this.scheduleId = yogaCourseScheduleId;
        this.yogaCourseId = yogaCourseId;
        this.date = date;
        this.teacherName = teacherName;
        this.comment = comment;
        this.isUploaded = isUploaded;
        this.isDeleted = isDeleted;
    }

    /// constructors for listing schedule
    public Schedule(String yogaCourseScheduleId, String yogaCourseId, String courseName, String dayOfWeek, String date, String teacherName, String comment){
        this.scheduleId = yogaCourseScheduleId;
        this.yogaCourseId = yogaCourseId;
        this.courseName = courseName;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.teacherName = teacherName;
        this.comment = comment;
    }

    public String getScheduleId() {return this.scheduleId;}
    public String getYogaCourseId() {return this.yogaCourseId;}
    public String getCourseName() {return this.courseName;} // for listing schedule
    public String getDate() {return this.date;}
    public String getDayOfWeek() {return this.dayOfWeek;} // for listing schedule
    public String getTeacherName() {return this.teacherName;}
    public String getComment() {return comment;}
    public int getIsUploaded() {return isUploaded;}
    public int getIsDeleted() {return isDeleted;}

    public void setScheduleId(String yogaCourseScheduleId) {this.scheduleId = yogaCourseScheduleId;}
    public void setYogaCourseId(String yogaCourseId) {this.yogaCourseId = yogaCourseId;}
    public void setCourseName(String courseName) {this.courseName = courseName;} // for listing schedule
    public void setDate(String date) {this.date = date;}
    public void setDayOfWeek(String dayOfWeek) {this.dayOfWeek = dayOfWeek;} // for listing schedule
    public void setTeacherName(String teacherName) {this.teacherName = teacherName;}
    public void setComment(String comment) {this.comment = comment;}
    public void setIsUploaded(int isUploaded) {this.isUploaded = isUploaded;}
    public void setIsDeleted(int isDeleted) {this.isDeleted = isDeleted;}

}
