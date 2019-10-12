package com.fenda.encyclopedia.presenter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenda.common.provider.IEncyclopediaProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.encyclopedia.model.EncyclopediaSharesBean;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 百科业务处理实现类
 */
@Route(path = RouterPath.Encyclopedia.ENCYCLOPEDIA_PROVIDER)
public class EncyclopediaFragmentPensenter implements IEncyclopediaProvider {
    private Context context;

    /**
     * 百科问答、闲聊
     *
     * @param msg
     */
    @Override
    public void processQuestionTextMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String content = jsonObject.getJSONObject("dm").getJSONObject("widget").get("text").toString();
        String title = jsonObject.getJSONObject("dm").get("input").toString();
        ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY).withString("content", content).withString("title", title).navigation();

    }

    /**
     * 历史上的今日
     *
     * @param msg
     */
    @Override
    public void processHistoryTodayMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //标题
        String title = jsonObject.getJSONObject("dm").getJSONObject("widget").get("title").toString();
        //文字内容
        String content = jsonObject.getJSONObject("dm").getJSONObject("widget").getJSONObject("extra").get("content").toString();
        ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY).withString("content", content).withString("title", title).navigation();
    }

    /**
     * 处理股票
     *
     * @param msg
     */
    @Override
    public void processSharesMsg(String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            JSONArray array = (JSONArray) jsonObject.getJSONObject("dm").getJSONObject("widget").get("content");
            String skill = jsonObject.getJSONObject("nlu").get("input").toString();
            JSONObject content = array.getJSONObject(0);
            //时间
            String date = content.get("date").toString();
            //上证指数
            String current = content.get("current").toString();
            //上涨指数
            String change = content.get("change").toString();
            //涨幅百分比
            String percentage = content.get("percentage").toString();
            EncyclopediaSharesBean shares = new EncyclopediaSharesBean(skill, date, current, change, percentage);
            ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_SHARES_ACTIVITY).withParcelable("shares", shares).navigation();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }

    /**
     * 成语解释
     *
     * @param msg
     */
    @Override
    public void processIdiomTextMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //标题
        String title = jsonObject.getJSONObject("dm").get("input").toString();
        //文字内容
        String content = jsonObject.getJSONObject("dm").get("nlg").toString();
        ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY).withString("content", content).withString("title", title).navigation();
    }

    /**
     * 星座运势
     *
     * @param msg
     */
    @Override
    public void processConstellationTextMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //标题
        String title = jsonObject.getJSONObject("dm").get("input").toString();
        //文字内容
        String content = jsonObject.getJSONObject("dm").get("nlg").toString();
        ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY).withString("content", content).withString("title", title).navigation();
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
