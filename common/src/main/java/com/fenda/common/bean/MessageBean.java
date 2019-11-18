package com.fenda.common.bean;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/10/9 16:10
 * @Description 长连接消息实体
 */
public class MessageBean {

    private String deviceId;
    private String messageDetail;
    private UserInfoBean messageUserInfoDTO;
    private MessageDeviceInfo deviceInfoVO;


    public class MessageDeviceInfo {

        private String deviceId;
        private String icon;
        private long id;
        private String name;
        private String rongcloud_token;
        private int userType;

        public MessageDeviceInfo() {
        }

        public MessageDeviceInfo(String deviceId, String icon, long id, String name, String rongcloud_token, int userType) {
            this.deviceId = deviceId;
            this.icon = icon;
            this.id = id;
            this.name = name;
            this.rongcloud_token = rongcloud_token;
            this.userType = userType;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRongcloud_token() {
            return rongcloud_token;
        }

        public void setRongcloud_token(String rongcloud_token) {
            this.rongcloud_token = rongcloud_token;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }

    public MessageBean() {
    }

    public MessageBean(String deviceId, String messageDetail, UserInfoBean messageUserInfoDTO, MessageDeviceInfo deviceInfoVO) {
        this.deviceId = deviceId;
        this.messageDetail = messageDetail;
        this.messageUserInfoDTO = messageUserInfoDTO;
        this.deviceInfoVO = deviceInfoVO;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public UserInfoBean getMessageUserInfoDTO() {
        return messageUserInfoDTO;
    }

    public void setMessageUserInfoDTO(UserInfoBean messageUserInfoDTO) {
        this.messageUserInfoDTO = messageUserInfoDTO;
    }

    public MessageDeviceInfo getDeviceInfoVO() {
        return deviceInfoVO;
    }

    public void setDeviceInfoVO(MessageDeviceInfo deviceInfoVO) {
        this.deviceInfoVO = deviceInfoVO;
    }
}
