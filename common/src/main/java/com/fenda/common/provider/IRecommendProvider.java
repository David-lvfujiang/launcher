package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

import org.json.JSONObject;

/**
 * @author matt.Ljp
 * @time 2019/9/18 10:14
 * @description
 */

public interface IRecommendProvider extends IProvider {
    void requestRecommend(JSONObject dataRecommend);
}
