package com.fenda.common.network;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/12/5 9:59
 * @Description
 */
public class Network {


    private boolean isNetwork;

    public Network(boolean isNetwork) {
        this.isNetwork = isNetwork;
    }

    public Network() {
    }

    public boolean isNetwork() {
        return isNetwork;
    }

    public void setNetwork(boolean network) {
        isNetwork = network;
    }
}
