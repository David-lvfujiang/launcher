package com.fenda.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.INewsProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.Md5Utils;
import com.fenda.news.bean.FDMusic;
import com.fenda.news.bean.MusicPlayBean;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * author : matt.Ljp
 * date : 2019/9/4 10:29
 * description :
 */
@Route(path = RouterPath.NEWS.NEWS_PLAY)
public class NewsPlay implements INewsProvider {
    public static boolean isNewsAcitivytOpen = false;
    public static final String keyNews = "news";
    private ArrayList<FDMusic> playlist = new ArrayList<>();
    private Context mContext;

    /**
     * 获取新闻数据
     * @param contentArray
     * @return
     */
    private ArrayList<FDMusic> getNewsMusics(JSONArray contentArray) {

        FDMusic fdmusic;
        for (int i = 0; i < contentArray.length(); i++) {
            try {
                JSONObject itemJsonObject = contentArray.getJSONObject(i);
                JSONObject extraObject = itemJsonObject.optJSONObject("extra");
                String title = extraObject.optString("title", "");
                String subTitle = extraObject.optString("catalog_name", "");
                String imageUrl = extraObject.optString("image", "");
                String linkUrl = extraObject.optString("audio", "");
                String mediaid = extraObject.optString("news_id", "");
                String humanTime = extraObject.optString("human_time");
                String source = extraObject.optString("source");
                if (TextUtils.isEmpty(mediaid)) {
                    mediaid = Md5Utils.md5(linkUrl);
                }
                if(linkUrl.equals("")) {
                    return null;
                }
                fdmusic = new FDMusic(title,linkUrl,imageUrl,subTitle);
                fdmusic.setMusicTime(humanTime);
                fdmusic.setMusicArtist(source);
                playlist.add(fdmusic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playlist;
    }

    @Override
    public void news(JSONObject dataJsonObject) {
        JSONArray contentArray = null;
        try {
            if (dataJsonObject.get("content") instanceof JSONArray) {
                contentArray = dataJsonObject.getJSONArray("content");
                if (contentArray == null || contentArray.length() <= 0) {
                    return;
                }
            } else {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
            playlist.addAll(getNewsMusics(contentArray));
            final MusicPlayBean bean = new MusicPlayBean();
            bean.setFdMusics(playlist);
            bean.setMsgName("新闻");
            bean.setMsgType(Constant.Player.NEW_CONSULT);
            bean.setAidlMsgType(Constant.Player.keyBroadcastMusicList);
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent mIntent = new Intent(mContext, NewsActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(NewsPlay.keyNews,bean);
                    mIntent.putExtras(mBundle);
                    mContext.startActivity(mIntent);
                    EventBusUtils.post(bean);
                }
            });

    }
    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
