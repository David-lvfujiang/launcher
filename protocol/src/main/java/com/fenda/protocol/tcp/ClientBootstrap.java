package com.fenda.protocol.tcp;

import android.content.Context;

import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.factory.ClientFactory;
import com.fenda.protocol.tcp.interf.IMSClientInterface;
import com.fenda.protocol.tcp.listener.CallEventListener;
import com.fenda.protocol.tcp.listener.ConnectStatusListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Vector;

public class ClientBootstrap {
    private static final ClientBootstrap INSTANCE = new ClientBootstrap();
    private IMSClientInterface imsClient;
    private boolean isActive;

    private ClientBootstrap() {

    }

    public static ClientBootstrap getInstance() {
        return INSTANCE;
    }


    public synchronized void init(Context context, String userId, String ip, int port, int appStatus) {
        if (!isActive()) {
            String hosts = "[{\"host\":\"" + ip + "\", \"port\":" + port + "}]";
            Vector<String> serverUrlList = convertHosts(hosts);
            if (serverUrlList == null || serverUrlList.size() == 0) {
                System.out.println("init IMLibClientBootstrap error,ims hosts is null");
                return;
            }

            isActive = true;
            System.out.println("init IMLibClientBootstrap, servers=" + hosts);
            if (null != imsClient) {
                imsClient.close();
            }
            imsClient = ClientFactory.getIMSClient(context);
            updateAppStatus(appStatus);
            imsClient.init(serverUrlList, new CallEventListener(userId), new ConnectStatusListener());
        }
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMessage(BaseTcpMessage msg) {
        if (isActive) {
            imsClient.sendMsg(msg);
        }
    }

    private Vector<String> convertHosts(String hosts) {
        if (hosts != null && hosts.length() > 0) {

            JsonParser parser = new JsonParser();
            JsonArray hostArray = parser.parse(hosts).getAsJsonArray();
            if (null != hostArray && hostArray.size() > 0) {
                Vector<String> serverUrlList = new Vector<String>();
                JsonObject host;
                for (int i = 0; i < hostArray.size(); i++) {
                    host = parser.parse(hostArray.get(i).toString()).getAsJsonObject();
                    serverUrlList.add(host.get("host").getAsString() + " "
                            + host.get("port").getAsInt());
                }
                return serverUrlList;
            }
        }
        return null;
    }

    /**
     * 设置APP状态
     *
     * @param appStatus
     */
    public void updateAppStatus(int appStatus) {
        if (imsClient == null) {
            return;
        }

        imsClient.setAppStatus(appStatus);
    }

}
