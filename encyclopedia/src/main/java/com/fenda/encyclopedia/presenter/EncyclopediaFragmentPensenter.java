package com.fenda.encyclopedia.presenter;

import android.content.Context;
import android.content.Intent;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenda.common.router.RouterPath;
import com.fenda.encyclopedia.model.EncyclopediaShares;
import com.fenda.encyclopedia.view.EncyclopediaQuestiionActivity;
import com.fenda.encyclopedia.view.EncyclopediaSharesActivity;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.IEncyclopediaProvider;

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
    public void geTextMsg(String msg) {
        //JSONObject jsonObject = JSONObject.parseObject(msg);//json对象转字符串
        String content = "大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋大熊猫属于大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋大熊猫属于食肉目、熊科、大熊猫亚大大的黑眼圈，胖嘟嘟的身体大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋大熊猫属于大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋大熊猫属于食肉目、熊科、大熊猫亚大大的黑眼圈，胖嘟嘟的身体大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋大熊猫属于大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋大熊猫属于食肉目、熊科、大熊猫亚大大的黑眼圈，胖嘟嘟的身体";
        String title = "熊猫是什么动物";
        Intent intent = new Intent();
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        intent.setClass(BaseApplication.getInstance(), EncyclopediaQuestiionActivity.class);
        BaseApplication.getInstance().startActivity(intent);
    }

    /**
     * 处理股票
     * @param msg
     */
    @Override
    public void geSharesMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);//json对象转字符串
        JSONArray array = (JSONArray) jsonObject.getJSONObject("dm").getJSONObject("widget").get("content");
        String skill = jsonObject.getJSONObject("nlu").get("input").toString();
        JSONObject content = array.getJSONObject(0);//时间
        String date = content.get("date").toString();//时间
        String high = content.get("high").toString();//上证指数
        String change = content.get("change").toString();//上涨指数
        String percentage = content.get("percentage").toString();//涨幅百分比
        EncyclopediaShares shares = new EncyclopediaShares(skill, date, high, change, percentage);

        Intent intent = new Intent();
        intent.putExtra("shares", shares);
        intent.setClass(BaseApplication.getInstance(), EncyclopediaSharesActivity.class);
        BaseApplication.getInstance().startActivity(intent);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
