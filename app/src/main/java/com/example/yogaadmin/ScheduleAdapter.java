package com.example.yogaadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleAdapter extends BaseAdapter {
    private final ArrayList<Schedule> _scheduleList;
    private final Context _context;
    private final LayoutInflater _layoutInflater;

    public ScheduleAdapter(Context context, ArrayList<Schedule> scheduleList){

        this._context = context;
        this._layoutInflater = LayoutInflater.from(context);
        this._scheduleList = scheduleList;
    }

    @Override
    public int getCount() {return _scheduleList.size();}

    @Override
    public Object getItem(int position) {return null;}

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String dateString = "";
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        convertView = _layoutInflater.inflate(R.layout.schedule_item, parent, false);
        TextView textViewCourseScheduleNameItem = convertView.findViewById(R.id.textViewCourseScheduleNameItem);
        TextView textViewDayOfWeekScheduleItem = convertView.findViewById(R.id.textViewDayOfWeekScheduleItem);
        TextView textViewDateScheduleItem = convertView.findViewById(R.id.textViewDateScheduleItem);
        TextView textViewTeacherNameScheduleItem = convertView.findViewById(R.id.textViewTeacherNameScheduleItem);

        textViewCourseScheduleNameItem.setText(_scheduleList.get(position).getCourseName());
        textViewDayOfWeekScheduleItem.setText(_scheduleList.get(position).getDayOfWeek());
        textViewDateScheduleItem.setText(_scheduleList.get(position).getDateForListView());
        textViewTeacherNameScheduleItem.setText(_scheduleList.get(position).getTeacherName());
        return convertView;
    }


}
