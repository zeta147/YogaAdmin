package com.example.yogaadmin;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    /// Database name
    private static final String DATABASE_NAME = "yoga_app";

    /// Table yoga course
    private static final String YOGA_COURSE_TABLE_NAME = "yoga_courses";
    private static final String COURSE_ID_COLUMN_NAME = "course_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DAY_OF_WEEK_COLUMN_NAME = "day_of_week";
    private static final String TIME_COLUMN_NAME = "time";
    private static final String CAPACITY_COLUMN_NAME = "capacity";
    private static final String DURATION_COLUMN_NAME = "duration";
    private static final String PRICE_COLUMN_NAME = "price";
    private static final String TYPE_COLUMN_NAME = "type";
    private static final String DESCRIPTION_COLUMN_NAME = "description";

    /// Table yoga course schedule
    private static final String COURSE_SCHEDULE_TABLE_NAME = "yoga_course_schedules";
    private static final String COURSE_SCHEDULE_ID_COLUMN_NAME = "yoga_course_schedule_id";
    private static final String YOGA_COURSE_ID_COLUMN_NAME = "yoga_course_id";
    private static final String DATE_COLUMN_NAME = "date";
    private static final String TEACHER_NAME_COLUMN_NAME = "teacher_name";
    private static final String COMMENT_COLUMN_NAME = "comment";

    private final SQLiteDatabase database;

    private static final String DATABASE_YOGA_COURSE_CREATE_QUERY = String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +                     //YOGA_COURSE_TABLE_NAME
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +      //COURSE_ID_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //NAME_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //DAY_OF_WEEK_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //TIME_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //CAPACITY_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //DURATION_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //PRICE_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //TYPE_COLUMN_NAME
                    "%s TEXT) ",                                    //DESCRIPTION_COLUMN_NAME
            YOGA_COURSE_TABLE_NAME, COURSE_ID_COLUMN_NAME, NAME_COLUMN_NAME,
            DAY_OF_WEEK_COLUMN_NAME, TIME_COLUMN_NAME, CAPACITY_COLUMN_NAME,
            DURATION_COLUMN_NAME, PRICE_COLUMN_NAME, TYPE_COLUMN_NAME,
            DESCRIPTION_COLUMN_NAME
    );

    private static final String DATABASE_YOGA_COURSE_SCHEDULE_CREATE_QUERY = String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +                     //YOGA_COURSE_SCHEDULE_TABLE_NAME
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +      //YOGA_COURSE_SCHEDULE_ID_COLUMN_NAME
                    "%s INTEGER NOT NULL, " +                       //YOGA_COURSE_ID_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //DATE_COLUMN_NAME
                    "%s TEXT NOT NULL, " +                          //TEACHER_NAME_COLUMN_NAME
                    "%s TEXT, " +                                   //COMMENT_COLUMN_NAME
                    "FOREIGN KEY(%s) REFERENCES %s(%s))",           //YOGA_COURSE_ID_COLUMN_NAME, YOGA_COURSE_TABLE_NAME, COURSE_ID_COLUMN_NAME
            COURSE_SCHEDULE_TABLE_NAME, COURSE_SCHEDULE_ID_COLUMN_NAME, YOGA_COURSE_ID_COLUMN_NAME,
            DATE_COLUMN_NAME, TEACHER_NAME_COLUMN_NAME, COMMENT_COLUMN_NAME,
            YOGA_COURSE_ID_COLUMN_NAME, YOGA_COURSE_TABLE_NAME, COURSE_ID_COLUMN_NAME
    );

    private static final String COURSE_JOIN_SCHEDULE_QUERY =
            "SELECT " + "y" + "." + COURSE_ID_COLUMN_NAME + ", " +
                        "y" + "." + NAME_COLUMN_NAME + ", " +
                        "y" + "." + DAY_OF_WEEK_COLUMN_NAME + ", " +
                        "s" + "." + COURSE_SCHEDULE_ID_COLUMN_NAME+ ", " +
                        "s" + "." + DATE_COLUMN_NAME + ", " +
                        "s" + "." + TEACHER_NAME_COLUMN_NAME + ", " +
                        "s" + "." + COMMENT_COLUMN_NAME + " " +
            "FROM "  +  YOGA_COURSE_TABLE_NAME + " y " +
            "JOIN "  +  COURSE_SCHEDULE_TABLE_NAME + " s " +
            "ON "    +  "y" + "." + COURSE_ID_COLUMN_NAME + " = " + "s" + "." + YOGA_COURSE_ID_COLUMN_NAME + " "+
            "ORDER BY " + "s" + "." + DATE_COLUMN_NAME + " ASC";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1 );
        this.database = getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON");
    }

    @SuppressLint("Recycle")
    public void checkAvailableTable(){
        Cursor cursor = this.database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while (cursor.moveToNext()) {
            Log.d("Tables", "Table Name: " + cursor.getString(0));
        }
        cursor.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_YOGA_COURSE_CREATE_QUERY);
        db.execSQL(DATABASE_YOGA_COURSE_SCHEDULE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + YOGA_COURSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_SCHEDULE_TABLE_NAME);
        Log.w(this.getClass().getName(), YOGA_COURSE_TABLE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        Log.w(this.getClass().getName(), COURSE_SCHEDULE_TABLE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertYogaCourse(YogaCourse course){
        ContentValues rowValues = new ContentValues();
        rowValues.put(COURSE_ID_COLUMN_NAME,course.getYogaCourseId());
        rowValues.put(NAME_COLUMN_NAME,course.getName());
        rowValues.put(DAY_OF_WEEK_COLUMN_NAME,course.getDayOfWeek());
        rowValues.put(TIME_COLUMN_NAME,course.getTime());
        rowValues.put(CAPACITY_COLUMN_NAME,course.getCapacity());
        rowValues.put(DURATION_COLUMN_NAME,course.getDuration());
        rowValues.put(PRICE_COLUMN_NAME,course.getPrice());
        rowValues.put(TYPE_COLUMN_NAME,course.getType());
        rowValues.put(DESCRIPTION_COLUMN_NAME,course.getDescription());
        return database.insertOrThrow(YOGA_COURSE_TABLE_NAME, null, rowValues);
    }

    public void updateYogaCourse(YogaCourse course){
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN_NAME,course.getName());
        rowValues.put(DAY_OF_WEEK_COLUMN_NAME,course.getDayOfWeek());
        rowValues.put(TIME_COLUMN_NAME,course.getTime());
        rowValues.put(CAPACITY_COLUMN_NAME,course.getCapacity());
        rowValues.put(DURATION_COLUMN_NAME,course.getDuration());
        rowValues.put(PRICE_COLUMN_NAME,course.getPrice());
        rowValues.put(TYPE_COLUMN_NAME,course.getType());
        rowValues.put(DESCRIPTION_COLUMN_NAME,course.getDescription());
        database.update(YOGA_COURSE_TABLE_NAME, rowValues, COURSE_ID_COLUMN_NAME + "=" + course.getYogaCourseId(), null);
    }

    public void deleteYogaCourse(YogaCourse course){
        database.execSQL("PRAGMA foreign_keys=OFF");
        database.delete(YOGA_COURSE_TABLE_NAME, COURSE_ID_COLUMN_NAME + "=" + course.getYogaCourseId(), null);
        database.delete(COURSE_SCHEDULE_TABLE_NAME, YOGA_COURSE_ID_COLUMN_NAME + "=" + course.getYogaCourseId(), null);
    }

    public void deleteAllYogaCoursesAndSchedules(){
        database.execSQL("PRAGMA foreign_keys=OFF");
        database.delete(YOGA_COURSE_TABLE_NAME, null, null);
        database.delete(COURSE_SCHEDULE_TABLE_NAME, null, null);
    }

    public long getYogaCourseCount(){
        return database.query(YOGA_COURSE_TABLE_NAME, null, null, null, null, null, null).getCount();
    }

    public ArrayList<YogaCourse> getAllYogaCourses(){
        Cursor results = database.query("yoga_courses",
                new String[]{"course_id",
                        "name",
                        "day_of_week",
                        "time",
                        "capacity",
                        "duration",
                        "price",
                        "type",
                        "description"},
                null, null, null, null, "name");

        ArrayList<YogaCourse> arrayListCourse = new ArrayList<YogaCourse>();

        results.moveToFirst();
        while(!results.isAfterLast()){
            int course_id = results.getInt(0);
            String name = results.getString(1);
            String day_of_week = results.getString(2);
            String time = results.getString(3);
            int capacity = results.getInt(4);
            String duration = results.getString(5);
            float price = results.getFloat(6);
            String type = results.getString(7);
            String description = results.getString(8);

            String idStr = String.valueOf(course_id);
            YogaCourse course = new YogaCourse(idStr, name, day_of_week, time, capacity, duration, price, type, description);
            arrayListCourse.add(course);

            results.moveToNext();
        }
        results.close();
        return arrayListCourse;
    }


    public void insertYogaCourseSchedule(Schedule schedule){
        ContentValues rowValues = new ContentValues();
        rowValues.put(YOGA_COURSE_ID_COLUMN_NAME,schedule.getYogaCourseId());
        rowValues.put(DATE_COLUMN_NAME,schedule.getDate());
        rowValues.put(TEACHER_NAME_COLUMN_NAME,schedule.getTeacherName());
        rowValues.put(COMMENT_COLUMN_NAME,schedule.getComment());
        database.insertOrThrow(COURSE_SCHEDULE_TABLE_NAME, null, rowValues);
    }

    public void updateSchedule(Schedule schedule){
        ContentValues rowValues = new ContentValues();
        rowValues.put(YOGA_COURSE_ID_COLUMN_NAME,Integer.parseInt(schedule.getYogaCourseId()));
        rowValues.put(DATE_COLUMN_NAME,schedule.getDate());
        rowValues.put(TEACHER_NAME_COLUMN_NAME,schedule.getTeacherName());
        rowValues.put(COMMENT_COLUMN_NAME,schedule.getComment());
        database.update(COURSE_SCHEDULE_TABLE_NAME, rowValues, COURSE_SCHEDULE_ID_COLUMN_NAME + "=" + schedule.getScheduleId(), null);
    }

    public void deleteSchedule(Schedule schedule){
        database.delete(COURSE_SCHEDULE_TABLE_NAME, COURSE_SCHEDULE_ID_COLUMN_NAME + "=" + schedule.getScheduleId(), null);
    }



    public long getScheduleCount(){
        return database.query(COURSE_SCHEDULE_TABLE_NAME, null, null, null, null, null, null).getCount();
    }

    public ArrayList<Schedule> getAllSchedules() {
        Cursor results = database.query("yoga_course_schedules",
                new String[]{"yoga_course_schedule_id",
                        "yoga_course_id",
                        "date",
                        "teacher_name",
                        "comment"},
                null, null, null, null, "date");
        ArrayList<Schedule> arrayListSchedule = new ArrayList<Schedule>();
        results.moveToFirst();
        while (!results.isAfterLast()) {
            int yoga_course_schedule_id = results.getInt(0);
            int yoga_course_id = results.getInt(1);
            String date = results.getString(2);
            String teacher_name = results.getString(3);
            String comment = results.getString(4);
            String idStr = String.valueOf(yoga_course_schedule_id);
            String yoga_course_idStr = String.valueOf(yoga_course_id);
            Schedule schedule = new Schedule(idStr, yoga_course_idStr, date, teacher_name, comment);
            arrayListSchedule.add(schedule);
            results.moveToNext();
        }
        results.close();
        return arrayListSchedule;
    }

    @SuppressLint("Recycle")
    public ArrayList<Schedule> getYogaCourseJoinScheduleList(){
        Cursor results = database.rawQuery(COURSE_JOIN_SCHEDULE_QUERY, null, null);
        ArrayList<Schedule> arrayListSchedule = new ArrayList<Schedule>();
        results.moveToFirst();
        while (!results.isAfterLast()) {
            int yoga_course_id = results.getInt(0);
            String name = results.getString(1);
            String day_of_week = results.getString(2);
            int yoga_course_schedule_id = results.getInt(3);
            String date = results.getString(4);
            String teacher_name = results.getString(5);
            String comment = results.getString(6);
            String idStr = String.valueOf(yoga_course_schedule_id);
            String yoga_course_idStr = String.valueOf(yoga_course_id);
            Schedule schedule = new Schedule(idStr, yoga_course_idStr, name, day_of_week, date, teacher_name, comment);
            arrayListSchedule.add(schedule);
            results.moveToNext();
        }
        results.close();
        return arrayListSchedule;
    }

    public ArrayList<Schedule> getYogaCourseJoinScheduleFilteredTeacherList(String keyWord){
         String COURSE_JOIN_SCHEDULE_FILTERED_QUERY =
                "SELECT " + "y" + "." + COURSE_ID_COLUMN_NAME + ", " +
                        "y" + "." + NAME_COLUMN_NAME + ", " +
                        "y" + "." + DAY_OF_WEEK_COLUMN_NAME + ", " +
                        "s" + "." + COURSE_SCHEDULE_ID_COLUMN_NAME+ ", " +
                        "s" + "." + DATE_COLUMN_NAME + ", " +
                        "s" + "." + TEACHER_NAME_COLUMN_NAME + ", " +
                        "s" + "." + COMMENT_COLUMN_NAME + " " +
                        "FROM "  +  YOGA_COURSE_TABLE_NAME + " y " +
                        "JOIN "  +  COURSE_SCHEDULE_TABLE_NAME + " s " +
                        "ON " + "y" + "." + COURSE_ID_COLUMN_NAME + " = " + " s" + "." + YOGA_COURSE_ID_COLUMN_NAME + " "+
                        "WHERE " + "s" + "." + TEACHER_NAME_COLUMN_NAME + " LIKE '%" + keyWord + "%' " +
                        "ORDER BY " + "s" + "." + DATE_COLUMN_NAME + " ASC " ;
        Cursor results = database.rawQuery(COURSE_JOIN_SCHEDULE_FILTERED_QUERY, null, null);
        ArrayList<Schedule> arrayListSchedule = new ArrayList<Schedule>();
        results.moveToFirst();
        while (!results.isAfterLast()) {
            int yoga_course_id = results.getInt(0);
            String name = results.getString(1);
            String day_of_week = results.getString(2);
            int yoga_course_schedule_id = results.getInt(3);
            String date = results.getString(4);
            String teacher_name = results.getString(5);
            String comment = results.getString(6);
            String idStr = String.valueOf(yoga_course_schedule_id);
            String yoga_course_idStr = String.valueOf(yoga_course_id);
            Schedule schedule = new Schedule(idStr, yoga_course_idStr, name, day_of_week, date, teacher_name, comment);
            arrayListSchedule.add(schedule);
            results.moveToNext();
        }
        results.close();
        return arrayListSchedule;
    }
}
