package com.fenda.player.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.fenda.common.base.BaseFragment;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.player.R;
import com.fenda.player.bean.PlayerMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class LyricFragment extends BaseFragment {

    TextView tvTitle;
    TextView tvAuthor;
    LinearLayout linContent;


    public static LyricFragment getInstance(FDMusic music) {
        LyricFragment fragment = new LyricFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("poetry",music);
        fragment.setArguments(mBundle);
        return fragment;
    }


    @Override
    public int onBindLayout() {
        return R.layout.player_fragment_lyric;
    }

    @Override
    public void initView(){
        tvTitle = mRootView.findViewById(R.id.tv_title);
        tvAuthor = mRootView.findViewById(R.id.tv_author);
        linContent = mRootView.findViewById(R.id.lin_content);

    }

    @Override
    public void initData() {
        FDMusic music = getArguments().getParcelable("poetry");
        initLyricData(music.getMusicTitle(),music.getMusicArtist(),music.getContent());

    }

    private void initLyricData(String title,String author,String text) {
        tvTitle.setText(title);
        tvAuthor.setText(author);
        linContent.removeAllViews();
        if (!TextUtils.isEmpty(text) && text.indexOf("。") != -1){
            String[] contents = text.split("。");
            for (int i = 0; i < contents.length; i++) {
                String item = contents[i];
                if (item.indexOf("！") != -1){
                    String[] mItems = item.split("！");
                    for (int i1 = 0; i1 < mItems.length; i1++) {
                        String mText = mItems[i1];
                        TextView tv = new TextView(getActivity());
                        tv.setTextSize(30);
                        tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.player_white));
                        if (i1 == mItems.length -1){
                            tv.setText(mText+"。");
                        }else {
                            tv.setText(mText+"！");

                        }
                        tv.setGravity(Gravity.CENTER);
                        linContent.addView(tv);
                    }
                }else {
                    TextView tv = new TextView(getActivity());
                    tv.setTextSize(30);
                    tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.player_white));
                    tv.setText(item+"。");
                    tv.setGravity(Gravity.CENTER);
                    linContent.addView(tv);
                }

            }
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvnet(PlayerMessage message){
        initLyricData(message.getMusicTitle(),message.getMusicName(),message.getContent());

    }


    @Override
    public void onDestroyView() {
        EventBusUtils.unregister(this);
        super.onDestroyView();
    }
}
