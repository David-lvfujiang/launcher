package com.fenda.protocol.tcp;

public class TCPConfig {

    // 默认重连一个周期失败间隔时长
    public static final int DEFAULT_RECONNECT_INTERVAL = 3 * 1000;
    // 连接超时时长
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    // 默认一个周期重连次数
    public static final int DEFAULT_RECONNECT_COUNT = 3;
    // 默认重连起始延时时长，重连规则：最大n次，每次延时n * 起始延时时长，重连次数达到n次后，重置
    public static final int DEFAULT_RECONNECT_BASE_DELAY_TIME = 3 * 1000;
    // 默认消息发送失败重发次数
    public static final int DEFAULT_RESEND_COUNT = 3;
    // 默认消息重发间隔时长
    public static final int DEFAULT_RESEND_INTERVAL = 8 * 1000;
    // 默认应用在前台时心跳消息间隔时长
    public static final int DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND = 50 * 1000;
    // 默认应用在后台时心跳消息间隔时长
    public static final int DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND = 60 * 1000;
    // 应用在前台标识
    public static final int APP_STATUS_FOREGROUND = 0;
    // 应用在后台标识
    public static final int APP_STATUS_BACKGROUND = -1;
    public static final String KEY_APP_STATUS = "key_app_status";
    // 默认服务端返回的消息发送成功状态报告
    public static final int DEFAULT_REPORT_SERVER_SEND_MSG_SUCCESSFUL = 1;
    // 默认服务端返回的消息发送失败状态报告
    public static final int DEFAULT_REPORT_SERVER_SEND_MSG_FAILURE = 0;
    // ims连接状态：连接中
    public static final int CONNECT_STATE_CONNECTING = 0;
    // ims连接状态：连接成功
    public static final int CONNECT_STATE_SUCCESSFUL = 1;
    // ims连接状态：连接失败
    public static final int CONNECT_STATE_FAILURE = -1;

    public static final int HEAD = 0x1a2b3c4d;

    public interface MessageType {
        int CHANGE_NICK_NAME = 1007;//修改家庭圈昵称通知
        int ALBUM_CHANGE = 1008; //相册变化通知
        int CHANGE_DEVICE_NAME = 1009; //设备名修改通知
        int USER_ICON_CHANGE = 1010; //用户头像修改通知

        int MESSAGE_REPLEY = 1110;//客户端消息确认
        int HEAD_REQ = 1111;//客户端心跳包
        int HEAD_RES = 1112;//心跳响应包
        int SERVICE_HEAD_REQ = 1113;//服务器心跳包
        int MANGER_SUCCESS = 1000;//管理员绑定成功通知
        int FAMILY_APPLY_ADD = 1001;//普通成员申请加入家庭确认通知
        int FAMILY_ADD = 1002;//普通成员加入家庭通知 1: 1  本人申请管理员同意通知
        int MANGER_INVITE_FAMILY = 1003;//管理员邀请加入家庭通知
        int NEW_USER_ADD = 1004;//新人加入家庭通知 1 : n  新人加入群发消息
        int FAMILY_DISSOLVE = 1005;//家庭解散通知
        int USER_EXIT_FAMILY = 1006;//普通成员退出家庭通知
        int USER_REPAIR_HEAD = 1010;//用户修改头像

        int ANDLINK_NETWORK_OK = 1011; //中国移动网络配置成功通知
    }




}
