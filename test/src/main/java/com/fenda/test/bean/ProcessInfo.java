package com.fenda.test.bean;

import android.graphics.drawable.Drawable;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/2 9:52
 * @Description
 */
public class ProcessInfo {


   private String name = null;
    private Drawable icon = null;
    private long memSize = 0;
   private boolean isSys = false;
   private String processName;


    public ProcessInfo(String name, Drawable icon, long memSize, boolean isSys, String processName) {
        this.name = name;
        this.icon = icon;
        this.memSize = memSize;
        this.isSys = isSys;
        this.processName = processName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public boolean isSys() {
        return isSys;
    }

    public void setSys(boolean sys) {
        isSys = sys;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", memSize=" + memSize +
                ", isSys=" + isSys +
                ", processName='" + processName + '\'' +
                '}';
    }
}
