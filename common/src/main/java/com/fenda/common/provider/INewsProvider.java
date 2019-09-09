package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

import org.json.JSONObject;

/**
 * author : matt.Ljp
 * date : 2019/9/4 10:42
 * description :
 */
public interface INewsProvider extends IProvider {
    void news(JSONObject dataJsonObject);
}
