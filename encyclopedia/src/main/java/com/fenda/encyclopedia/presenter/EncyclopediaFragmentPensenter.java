package com.fenda.encyclopedia.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenda.common.router.RouterPath;
import com.fenda.encyclopedia.model.EncyclopediaSharesBean;
import com.fenda.encyclopedia.view.EncyclopediaQuestiionActivity;
import com.fenda.encyclopedia.view.EncyclopediaSharesActivity;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.IEncyclopediaProvider;

import static android.support.constraint.Constraints.TAG;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 百科业务处理实现类
 */
@Route(path = RouterPath.Encyclopedia.ENCYCLOPEDIA_PROVIDER)
public class EncyclopediaFragmentPensenter implements IEncyclopediaProvider {
    private Context context;

    /**
     * 处理问答
     * @param msg
     */
    @Override
    public void processQuestionTextMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);//json对象转字符串
        String content = jsonObject.getString("text");
        String title = jsonObject.getString("input");
        ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY).withString("content", content).withString("title", title).navigation();

    }

    /**
     * 处理股票
     * @param msg
     */
    @Override
    public void processSharesMsg(String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);//json对象转字符串
            JSONArray array = (JSONArray) jsonObject.getJSONObject("dm").getJSONObject("widget").get("content");
            String skill = jsonObject.getJSONObject("nlu").get("input").toString();
            JSONObject content = array.getJSONObject(0);//时间
            String date = content.get("date").toString();//时间
            String high = content.get("high").toString();//上证指数
            String change = content.get("change").toString();//上涨指数
            String percentage = content.get("percentage").toString();//涨幅百分比
            EncyclopediaSharesBean shares = new EncyclopediaSharesBean(skill, date, high, change, percentage);
            ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_SHARES_ACTIVITY).withObject("shares", shares).navigation();
        } catch (Exception e) {
            Log.e(TAG, "processSharesMsg: "+e.getMessage());
        }
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
