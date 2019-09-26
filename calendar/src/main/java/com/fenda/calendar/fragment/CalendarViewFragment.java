package com.fenda.calendar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fenda.calendar.R;
import com.fenda.calendar.adapter.CalendarGridViewAdapter;
import com.fenda.calendar.controller.CalendarDateController;
import com.fenda.calendar.data.CalendarDate;
import com.fenda.calendar.utils.DateUtils;
import com.fenda.common.util.LogUtil;

import java.util.List;


/**
 * Created by joybar on 4/27/16.
 */
public class CalendarViewFragment extends Fragment {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String CHOICE_MODE_SINGLE = "choice_mode_single";
    private boolean isChoiceModelSingle;
    private int mYear;
    private int mMonth;
    private GridView mGridView;
    private TextView tvDate;
    private TextView tvJiaziYear;
    private OnDateClickListener onDateClickListener;
    private OnDateCancelListener onDateCancelListener;

    public CalendarViewFragment() {
    }

    public static CalendarViewFragment newInstance(int year, int month) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        fragment.setArguments(args);
        return fragment;
    }

    public static CalendarViewFragment newInstance(int year, int month, boolean isChoiceModelSingle) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putBoolean(CHOICE_MODE_SINGLE, isChoiceModelSingle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onDateClickListener = (OnDateClickListener) context;
            if(!isChoiceModelSingle){
                //多选
                onDateCancelListener = (OnDateCancelListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnDateClickListener or OnDateCancelListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mYear = getArguments().getInt(YEAR);
            mMonth = getArguments().getInt(MONTH);
            isChoiceModelSingle = getArguments().getBoolean(CHOICE_MODE_SINGLE, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mGridView = view.findViewById(R.id.gv_calendar);
        tvDate = view.findViewById(R.id.tv_perpetual_calendar_year_month);
        tvJiaziYear = view.findViewById(R.id.tv_perpetual_calendar_jiazi_year);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<CalendarDate> mListDataCalendar;//日历数据
        mListDataCalendar = CalendarDateController.getCalendarDate(mYear, mMonth);
        tvDate.setText(mYear + "年" + mMonth + "月");
        tvJiaziYear.setText(mListDataCalendar.get(0).getLunar().lunarYearToJiaZi);
        mGridView.setAdapter(new CalendarGridViewAdapter(mListDataCalendar));
        final List<CalendarDate> finalMListDataCalendar = mListDataCalendar;
        if (isChoiceModelSingle) {
            mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        } else {
            mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarDate calendarDate = ((CalendarGridViewAdapter) mGridView.getAdapter()).getListData().get(position);
                if (isChoiceModelSingle) {
                    //单选
                    if (finalMListDataCalendar.get(position).isInThisMonth()) {
                        onDateClickListener.onDateClick(calendarDate);
                    } else {
                        mGridView.setItemChecked(position, false);
                    }
                } else {
                    //多选
                    if (finalMListDataCalendar.get(position).isInThisMonth()) {
                       // mGridView.getCheckedItemIds()
                        if(!mGridView.isItemChecked(position)){
//                            onDateCancelListener.onDateCancel(calendarDate);
                        } else {
                            onDateClickListener.onDateClick(calendarDate);
                        }

                    } else {
                        mGridView.setItemChecked(position, false);
                    }

                }
            }
        });
        mGridView.post(new Runnable() {
            @Override
            public void run() {
                //需要默认选中当天
                List<CalendarDate> mListData = ((CalendarGridViewAdapter) mGridView.getAdapter()).getListData();
                int count = mListData.size();
                for (int i = 0; i < count; i++) {
                    if (mListData.get(i).getSolar().solarDay == DateUtils.getDay()
                            && mListData.get(i).getSolar().solarMonth == DateUtils.getMonth()
                            && mListData.get(i).getSolar().solarYear == DateUtils.getYear()) {
                        if (null != mGridView.getChildAt(i) && mListData.get(i).isInThisMonth()) {
                            // mListData.get(i).setIsSelect(true);
                            onDateClickListener.onDateClick(mListData.get(i));
                            mGridView.setItemChecked(i, true);
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (null != mGridView) {
                // mGridView.setItemChecked(mCurrentPosition, false);
                mGridView.clearChoices();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnDateClickListener {
        void onDateClick(CalendarDate calendarDate);
    }
    public interface OnDateCancelListener {
        void onDateCancel(CalendarDate calendarDate);
    }
}
