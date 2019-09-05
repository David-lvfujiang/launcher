package com.fenda.player.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.constant.Constant;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.view.CircleImageView;
import com.fenda.player.R;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import java.util.ArrayList;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/9/3 10:48
  * @Description 
  *
  */
public class FDMusicAdpater extends RecyclerView.Adapter<FDMusicAdpater.MusicItemHolder> {

    private Context mContext;
    private ArrayList<FDMusic> mMusicArr;
    private int mCurrentIndex;
    private int mContextType;
    private OnItemClickListener mListener;
    private ValueAnimator mObjectAnimator;
    private long mCurrentPlayTime;


    public FDMusicAdpater(Context context, ArrayList<FDMusic> musicArr, int mCurrentIndex, int contextType){
        mContext = context;
        mMusicArr = musicArr;
        this.mCurrentIndex = mCurrentIndex;
        this.mContextType = contextType;
    }

    public void  setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }



    @NonNull
    @Override
    public MusicItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View tView = LayoutInflater.from(
                mContext).inflate(R.layout.player_item_musicinfo, viewGroup,
                false);
        MusicItemHolder tHolder = new MusicItemHolder(tView);

        return tHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicItemHolder viewHolder, int i) {
        FDMusic tMusic = mMusicArr.get(i);
        String artist = tMusic.getMusicArtist();
        if (i == mCurrentIndex){
            initAnimation(viewHolder.musicPlayingIconIv);
            startAnimation();
            viewHolder.musicPlayingIconIv.setVisibility(View.VISIBLE);
            viewHolder.musicListBg.setBackgroundColor(ContextCompat.getColor(mContext,R.color.player_color_4cffffff));



            if (mContextType == Constant.Player.JOKE){
                ImageUtil.loadDefaultcImg(viewHolder.musicPlayingIconIv,tMusic.getMusicImage(),R.mipmap.player_joke_small);
            }else if (mContextType == Constant.Player.CROSS_TALLK){
                ImageUtil.loadDefaultcImg(viewHolder.musicPlayingIconIv,tMusic.getMusicImage(),R.mipmap.player_cover_pic_default);
            }else if (mContextType == Constant.Player.POETRY){
                ImageUtil.loadDefaultcImg(viewHolder.musicPlayingIconIv,tMusic.getMusicImage(),R.mipmap.player_poetry_small_pic);
            }
            if (TextUtils.isEmpty(artist)){
                viewHolder.musicAuthorTv.setText(mContext.getResources().getString(R.string.player_play_music));
            }else {
                viewHolder.musicAuthorTv.setText(artist);
            }

        }else{
            viewHolder.musicAuthorTv.setText(artist);
            viewHolder.musicPlayingIconIv.setVisibility(View.INVISIBLE);
            viewHolder.musicListBg.setBackgroundColor(ContextCompat.getColor(mContext,R.color.player_color_bc000));
        }
        String title = tMusic.getMusicTitle();
        if (title.contains("《")){
            title = title.replace("《","");
        }
        if (title.contains("》")){
            title = title.replace("》","");
        }
        viewHolder.musicNameTv.setText(title);


    }

    @Override
    public int getItemCount() {
        return mMusicArr.size();
    }

    public void updateData(ArrayList musicList, int currentIndex){
        mMusicArr = musicList;
        mCurrentIndex = currentIndex;

        notifyDataSetChanged();
    }

    public void updateCurrentIndex(int currentIndex){
        mCurrentIndex = currentIndex;
        notifyDataSetChanged();
    }



    public void startAnimation(){
        if (mObjectAnimator != null){
            mObjectAnimator.start();
//            objectAnimator.setCurrentPlayTime(mCurrentPlayTime);
        }
    }


    private void initAnimation(CircleImageView imgMusicPlay){
        if (mObjectAnimator == null){
            mObjectAnimator = ObjectAnimator.ofFloat(imgMusicPlay,"rotation",0.0f,360f);
            mObjectAnimator.setDuration(20000);
            //循环模式
            mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
            //无线循环
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimator.setInterpolator(new LinearInterpolator());
        }

    }

    public void  cancelAnimation(){
        if (mObjectAnimator != null){
//            mCurrentPlayTime = objectAnimator.getCurrentPlayTime();
            mObjectAnimator.cancel();
            mObjectAnimator = null;
        }
    }

    class MusicItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView musicPlayingIconIv;
        private TextView musicNameTv;
        private TextView musicAuthorTv;
        private TextView musicTimeTv;
        private ConstraintLayout musicListBg;

        public MusicItemHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            musicPlayingIconIv = itemView.findViewById(R.id.iv_item_playicon);
            musicNameTv = itemView.findViewById(R.id.tv_item_name);
            musicAuthorTv = itemView.findViewById(R.id.tv_item_authorname);
            musicTimeTv = itemView.findViewById(R.id.tv_item_time);
            musicListBg = itemView.findViewById(R.id.cl_musiclist_bg);

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.cl_musiclist_bg){
                mCurrentIndex = getAdapterPosition();

                MusicPlayBean bean = new MusicPlayBean();
                bean.setAidlMsgType(Constant.Player.keyBroadcastSelectItem);
                bean.setCurrentItem(mCurrentIndex);
                EventBusUtils.post(bean);
                notifyDataSetChanged();
                if (mListener != null){
                    mListener.itemClickListener(mCurrentIndex);
                }
                cancelAnimation();
            }
        }
    }

    public interface OnItemClickListener{
        /**
         * 点击监听
         * @param position
         */
        void itemClickListener(int position);
    }

}
