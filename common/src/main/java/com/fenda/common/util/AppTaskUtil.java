package com.fenda.common.util;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.fenda.common.BaseApplication;

import java.util.Calendar;
import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/2 11:33
 * @Description
 */
public class AppTaskUtil {

    /**
     * 获取最顶部APP的包名
     * @return
     */
    public static String getAppTopPackage(){
        UsageStatsManager mAm = (UsageStatsManager) BaseApplication.getInstance().getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        //结束时间
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        //开始时间
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> taskList = mAm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime, endTime);
        int j = 0;
        for (int i = 0; i < taskList.size(); i++) {
            LogUtil.e("stats name = "+taskList.get(i).getPackageName() +" LastTimeUsed = "+taskList.get(i).getLastTimeUsed()+taskList.get(i).getLastTimeStamp());
            if (taskList.get(i).getLastTimeUsed() > taskList.get(j).getLastTimeUsed()) {
                j = i;
                UsageStats stats = taskList.get(j);
            }
        }
        if (taskList.size() > 0){
            String pkg = taskList.get(j).getPackageName();
            return pkg;
        }

        return null;
    }
    /**
     * 判断launcher是否在前台
     * @return
     */
    public static boolean isLauncherForeground(){
        if (getAppTopPackage().equals(BaseApplication.getInstance().getPackageName()) ){
            return true;
        }
        return false;
    }




}
