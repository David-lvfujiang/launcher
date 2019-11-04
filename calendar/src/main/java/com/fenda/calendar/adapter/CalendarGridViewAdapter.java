package com.fenda.calendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fenda.calendar.R;
import com.fenda.calendar.data.CalendarDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by joybar on 2/24/16.
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private List<CalendarDate> mListData = new ArrayList<>();
    //24节气或者节假日标志
    private boolean festivalFlag = false;


    public CalendarGridViewAdapter(List<CalendarDate> mListData) {
        this.mListData = mListData;
    }

    public List<CalendarDate> getListData() {
        return mListData;
    }


    @Override
    public int getCount() {
        return mListData.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        CalendarDate calendarDate = mListData.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_calendar, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_day.setText(calendarDate.getSolar().solarDay+"");
        String str;
        if(!TextUtils.isEmpty(calendarDate.getSolar().solar24Term)){
            str =  calendarDate.getSolar().solar24Term;
            festivalFlag = true;
        }else if(!TextUtils.isEmpty(calendarDate.getLunar().lunarFestivalName)){
            str = calendarDate.getLunar().lunarFestivalName;
            festivalFlag= true;
        } else if(!TextUtils.isEmpty(calendarDate.getSolar().solarFestivalName)){
            str = calendarDate.getSolar().solarFestivalName;
            festivalFlag= true;
        }else{
            if (calendarDate.getLunar().isFristDay(mListData.get(position).getLunar().lunarDay)){
                str = calendarDate.getLunar().getLunarMonthString(mListData.get(position).getLunar().lunarMonth);
            }else {
                str = calendarDate.getLunar().getChinaDayString(mListData.get(position).getLunar().lunarDay);
            }
            festivalFlag = false;
        }
        viewHolder.tv_lunar_day.setText(str);
        if (festivalFlag){
            viewHolder.tv_lunar_day.setTextColor(Color.parseColor("#E0C297"));
        }
        if(mListData.get(position).isInThisMonth()){
            if (mListData.get(position).getSolar().solarWeek==6||mListData.get(position).getSolar().solarWeek==0){
                viewHolder.tv_day.setTextColor(Color.parseColor("#1170FF"));
            }
        }else{
            viewHolder.tv_day.setTextColor(Color.parseColor("#30C4C4C4"));
            viewHolder.tv_lunar_day.setTextColor(Color.parseColor("#30C4C4C4"));
        }
        return convertView;
    }


    public static class ViewHolder {
        private TextView tv_day;
        private TextView tv_lunar_day;
        private TextView tvDate;
        public ViewHolder(View itemView) {
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_lunar_day = itemView.findViewById(R.id.tv_lunar_day);
            tvDate = itemView.findViewById(R.id.tv_perpetual_calendar_year_month);
        }
    }

}

