package com.fenda.common.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.R;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;


public class SpeechView {


    private WindowManager wManager;
    private TextView txInput;
    private ImageView imgGif;
    private String inputText;

    private boolean isShow;
    private WindowManager.LayoutParams wmParams;
    private View view;


    public SpeechView(Context mContext){
        getWindowManager(mContext);
    }


    /**
     * @category 实例化WindowManager 初次模拟位置时候使用
     * @param context
     */
    private void getWindowManager(final Context context) {
        wManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        wmParams.format = PixelFormat.TRANSPARENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
               ;
        wmParams.gravity = Gravity.BOTTOM;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.windowAnimations = R.style.view_anim;


        view = LayoutInflater.from(context).inflate(R.layout.popuplayout,null);
        txInput = view.findViewById(R.id.tv_input);
        imgGif  = view.findViewById(R.id.img_voice_dialog);
        ImageUtil.loadGIFImage(R.mipmap.common_voice_dialog,imgGif,R.mipmap.common_voice_dialog);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        wManager.removeView(view);
                        isShow = false;
//                        try {
//                            DDS.getInstance().getAgent().stopDialog();
//                        } catch (DDSNotInitCompleteException e) {
//                            e.printStackTrace();
//                        }
                        break;
                        default:
                }
                return false;
            }
        });

    }

    public void showView(final String msg){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!isShow){
                    wManager.addView(view,wmParams);
                    txInput.setText(msg);
                    isShow = true;
                }else {
                    txInput.setText(msg);
                }
            }
        });

    }

    public void closeView(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (isShow){
                    wManager.removeView(view);
                }
                isShow = false;
            }
        });

    }




}
