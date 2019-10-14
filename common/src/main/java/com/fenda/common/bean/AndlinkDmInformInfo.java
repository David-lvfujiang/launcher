package com.fenda.common.bean;

import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/10/14 14:05
 */
public class AndlinkDmInformInfo {
    public String deviceId;
    public String childDeviceId;
    public String eventType;
    public long timestamp;
    public dataParams data;
//    public List<evevtTypeParams> data;

    public static class dataParams{
        public List<dataParamValues> params;
    }

    public static class dataParamValues{
        public String paramCode;
        public String paramValue;
    }
}
