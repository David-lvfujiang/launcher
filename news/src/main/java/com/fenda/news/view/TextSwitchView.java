package com.fenda.news.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.fenda.news.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author matt.Ljp
 * @time 2019/9/24 9:30
 * @description
 */

public class TextSwitchView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private int index= -1;
    private Context context;
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next(); //取得下标值
                    updateText();  //更新TextSwitcherd显示内容;
                    break;
            }
        };
    };
    private String [] resources={
            "可以试试“你好小乐，返回”",
            "可以试试“你好小乐，播放下一条”",
            "可以试试“你好小乐，播放上一条”",
            "可以试试“你好小乐，暂停播放”"
    };
    private Timer timer; //
    public TextSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init() {
        if(timer==null) {
            timer = new Timer();
        }
        this.setFactory(this);
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.textswitch_in_animation));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.textswitch_out_animation));

    }
    public void setResources(String[] res){
        this.resources = res;
    }

    public void setTextStillTime(long time){
        if(timer==null){
            timer = new Timer();
        }else{
            timer.scheduleAtFixedRate(new MyTask(), 5, time);//每3秒更新
        }
    }
    private class MyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }
    private int next(){
        int flag = index+1;
        if(flag>resources.length-1){
            flag=flag-resources.length;
        }
        return flag;
    }
    private void updateText(){
        this.setText(resources[index]);
    }
    @Override
    public View makeView() {
        TextView tv =new TextView(context);
        tv.setTextSize(20);
        tv.setTextColor(Color.WHITE);
        return tv;
    }
}