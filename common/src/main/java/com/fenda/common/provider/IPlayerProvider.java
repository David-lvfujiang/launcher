package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 16:17
 * @Description
 */
public interface IPlayerProvider extends IProvider {

    /**上一页**/
    void prevPage();

    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 重新播放
     */
    void rePlay();
    /**
     * 上一个
     */
    void prev();
    /**
     * 下一个
     */
    void next();
    /**
     * 列表循环播放
     */
    void loopListPlay();

    /**
     * 单曲循环播放
     */
    void loopSinglePlay();

    /**
     * 随机播放
     */
    void randomPlay();

    /**
     * 快进
     * @param relativeTime
     * @param absoluteTime
     */
    void forward(int relativeTime,int absoluteTime);

    /**
     * 快退
     * @param relativeTime
     * @param absoluteTime
     */
    void backward(int relativeTime,int absoluteTime);


}
