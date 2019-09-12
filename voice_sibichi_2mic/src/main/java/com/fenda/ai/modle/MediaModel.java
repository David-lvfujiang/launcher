package com.fenda.ai.modle;


import android.text.TextUtils;

import com.aispeech.dui.plugin.music.MusicPlugin;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.skill.Util;
import com.fenda.common.BaseApplication;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.INewsProvider;
import com.fenda.common.router.RouterPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/3 10:22
 * @Description
 */
public class MediaModel {

    private INewsProvider provider;

    public void handleMusicMediaWidget(final JSONObject dataJsonObject, final String type) {
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
        int contentType = 0;
        //笑话 电台 曲艺  故事 新闻 电台
        final ArrayList<FDMusic> playlist = new ArrayList<>();
        if ("笑话".equals(type)){
            playlist.addAll(getjokes(contentArray));
            contentType = Constant.Player.JOKE;
        }else if ("直播电台".equals(type)){
            playlist.addAll(getNewConsult(contentArray));
            contentType = Constant.Player.FM;
        }else if ("曲艺".equals(type) || "故事".equals(type)){
            playlist.addAll(getCrossTallk(contentArray));
            contentType = Constant.Player.CROSS_TALLK;
        }else if ("新闻".equals(type)){
            if (Util.isTaskQQmusic(BaseApplication.getInstance())){
                MusicPlugin.get().getMusicApi().pause();
            }
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("");
                }
            }).compose(RxSchedulers.<String>io_main())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if (provider == null){
                                provider = ARouter.getInstance().navigation(INewsProvider.class);
                            }
                            provider.news(dataJsonObject);
                        }
                    });
            return;
        }else if ("诗词".equals(type)){
            playlist.addAll(getPoetrys(contentArray));
            contentType = Constant.Player.POETRY;
        }else {
            playlist.addAll(getNewConsult(contentArray));
        }

        if (playlist == null){
            return;
        }

        final MusicPlayBean bean = new MusicPlayBean();
        bean.setFdMusics(playlist);
        bean.setMsgName(type);
        bean.setMsgType(contentType);
        bean.setAidlMsgType(Constant.Player.keyBroadcastMusicList);

        if (Util.isTaskQQmusic(BaseApplication.getInstance())){
            MusicPlugin.get().getMusicApi().pause();
        }
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("");
            }
        }).compose(RxSchedulers.<String>io_main())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ARouter.getInstance().build(RouterPath.PLAYER.MUSIC).withParcelable(Constant.Player.keyDataMusicKey,bean).navigation();
                    }
                });



    }

    /**
     * 获取新闻数据
     * @param contentArray
     * @return
     */
    private ArrayList<FDMusic> getNewsMusics(JSONArray contentArray) {

        final ArrayList<FDMusic> playlist = new ArrayList<>();
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

    /**
     * 获取相声数据
     * @param contentArray
     * @return
     */
    private ArrayList<FDMusic> getCrossTallk(JSONArray contentArray){
        final ArrayList<FDMusic> playlist = new ArrayList<>();
        FDMusic fdmusic;
        for (int i = 0; i < contentArray.length(); i++) {
            try {
                JSONObject itemJsonObject = contentArray.getJSONObject(i);
                String album = itemJsonObject.optString("album");
                String title = itemJsonObject.optString("title", "");
                String subTitle = itemJsonObject.optString("subTitle", "");
                String imageUrl = itemJsonObject.optString("imageUrl", "");
                String linkUrl = itemJsonObject.optString("linkUrl", "");
                String mediaid = itemJsonObject.optString("mediaId", "");
                if(TextUtils.isEmpty(linkUrl)) {
                    break;
                }
                if (!TextUtils.isEmpty(album)){
                    album = "《"+album+"》";
                }
                subTitle = subTitle + " 一 "+title;
                fdmusic=new FDMusic(album,linkUrl,imageUrl,subTitle);

                playlist.add(fdmusic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playlist;
    }

    /**
     * 获取笑话数据
     * @param contentArray
     * @return
     */
    private ArrayList<FDMusic> getjokes(JSONArray contentArray){
        ArrayList<FDMusic> playList = new ArrayList<>();
        for (int i = 0; i < contentArray.length(); i ++){
            try {
                JSONObject itemJosnObJect = contentArray.getJSONObject(i);
                String title = itemJosnObJect.optString("title");
                String linkUrl = itemJosnObJect.optString("linkUrl");
                String imageUrl = itemJosnObJect.optString("imageUrl");
                FDMusic fdMusic = new FDMusic(title,linkUrl,imageUrl,"");
                playList.add(fdMusic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return playList;

    }

    /**
     * 获取最新资讯
     * @param contentArray
     * @return
     */
    private ArrayList<FDMusic> getNewConsult(JSONArray contentArray){
        ArrayList<FDMusic> playList = new ArrayList<>();
        for (int i = 0; i < contentArray.length(); i ++){
            try {
                JSONObject itemJsonObject = contentArray.getJSONObject(i);
                String title = itemJsonObject.optString("title", "");
                String subTitle = itemJsonObject.optString("subTitle", "");
                String imageUrl = itemJsonObject.optString("imageUrl", "");
                String linkUrl = itemJsonObject.optString("linkUrl", "");
                FDMusic fdMusic = new FDMusic(title,linkUrl,imageUrl,subTitle);
                playList.add(fdMusic);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return playList;
    }

    private ArrayList<FDMusic> getPoetrys(JSONArray contentArray){
        ArrayList<FDMusic> poetryList = new ArrayList<>();
        for (int i = 0; i < contentArray.length(); i ++){
            JSONObject items = contentArray.optJSONObject(i);
            String author = items.optString("author");
            String imageUrl = items.optString("imageUrl");
            String linkUrl = items.optString("linkUrl");
            String text = items.optString("text");
            String title = items.optString("title");
            FDMusic fdMusic = new FDMusic();
            fdMusic.setMusicUri(linkUrl);
            fdMusic.setMusicImage(imageUrl);
            fdMusic.setMusicArtist(author);
            fdMusic.setMusicTitle(title);
            fdMusic.setContent(text);
            poetryList.add(fdMusic);
        }
        return poetryList;
    }



}
