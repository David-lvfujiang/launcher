package com.fenda.common.bean;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/11/1
 * @Describe:
 */
public class CaptureUserActionBean {
    private String functionType;
    private String location;
    private String usageMode;

    public CaptureUserActionBean(){}
    public CaptureUserActionBean(String functionType, String location, String usageMode) {
        this.functionType = functionType;
        this.location = location;
        this.usageMode = usageMode;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsageMode(String usageMode) {
        this.usageMode = usageMode;
    }

    public String getFunctionType() {
        return functionType;
    }

    public String getLocation() {
        return location;
    }

    public String getUsageMode() {
        return usageMode;
    }
}
