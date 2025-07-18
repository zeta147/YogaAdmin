package com.example.yogaadmin;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class YogaCourseAdapter extends BaseAdapter {
    private final ArrayList<YogaCourse> _yogaCoursesList;
    private final Context _context;
    private final LayoutInflater _layoutInflater;

    public YogaCourseAdapter(Context context, ArrayList<YogaCourse> yogaCoursesList) {
        this._context = context;
        this._layoutInflater = LayoutInflater.from(context);
        this._yogaCoursesList = yogaCoursesList;
    }

    @Override
    public int getCount() {
        return _yogaCoursesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "DefaultLocale"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = _layoutInflater.inflate(R.layout.yoga_course_item, parent, false);
        TextView textViewCourseName = convertView.findViewById(R.id.textViewCourseName);
        TextView textViewCourseTime = convertView.findViewById(R.id.textViewCourseTime);
        TextView textViewCourseDayOfWeek = convertView.findViewById(R.id.textViewCourseDayOfWeek);
        TextView textViewCoursePrice = convertView.findViewById(R.id.textViewCoursePrice);
        TextView textViewCourseType = convertView.findViewById(R.id.textViewCourseType);

        textViewCourseName.setText(_yogaCoursesList.get(position).getName());
        textViewCourseTime.setText("Time: " + _yogaCoursesList.get(position).getTime());
        textViewCourseDayOfWeek.setText("On: " + _yogaCoursesList.get(position).getDayOfWeek());
        textViewCoursePrice.setText(String.valueOf(String.format("%.2f", _yogaCoursesList.get(position).getPrice()) + "$"));
        textViewCourseType.setText("Type: " + _yogaCoursesList.get(position).getType());
        return convertView;
    }

}
