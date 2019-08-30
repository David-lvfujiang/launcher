package com.fenda.gallery.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneCameraBean implements Parcelable {

    private String id;
    private String userid;
    private String deviceid;
    private String photos;
    private String tenant;
    private String deleted;
    private String version;
    private String createBy;
    private String updateBy;
    private String dateCreate;
    private String dateUpdate;
    private int selectStatus;
    private String thumbnail;
    private long time;
    private int size;

    public PhoneCameraBean() {
    }

    public PhoneCameraBean(String id, String userid, String deviceid, String photos, String tenant, String deleted, String version, String createBy, String updateBy, String dateCreate, String dateUpdate, int selectStatus, String thumbnail, long time, int size) {
        this.id = id;
        this.userid = userid;
        this.deviceid = deviceid;
        this.photos = photos;
        this.tenant = tenant;
        this.deleted = deleted;
        this.version = version;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.selectStatus = selectStatus;
        this.thumbnail = thumbnail;
        this.time = time;
        this.size = size;
    }

    protected PhoneCameraBean(Parcel in) {
        id = in.readString();
        userid = in.readString();
        deviceid = in.readString();
        photos = in.readString();
        tenant = in.readString();
        deleted = in.readString();
        version = in.readString();
        createBy = in.readString();
        updateBy = in.readString();
        dateCreate = in.readString();
        dateUpdate = in.readString();
        selectStatus = in.readInt();
        thumbnail = in.readString();
        time = in.readLong();
        size = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userid);
        dest.writeString(deviceid);
        dest.writeString(photos);
        dest.writeString(tenant);
        dest.writeString(deleted);
        dest.writeString(version);
        dest.writeString(createBy);
        dest.writeString(updateBy);
        dest.writeString(dateCreate);
        dest.writeString(dateUpdate);
        dest.writeInt(selectStatus);
        dest.writeString(thumbnail);
        dest.writeLong(time);
        dest.writeInt(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhoneCameraBean> CREATOR = new Creator<PhoneCameraBean>() {
        @Override
        public PhoneCameraBean createFromParcel(Parcel in) {
            return new PhoneCameraBean(in);
        }

        @Override
        public PhoneCameraBean[] newArray(int size) {
            return new PhoneCameraBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(int selectStatus) {
        this.selectStatus = selectStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "PhoneCameraBean{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", photos='" + photos + '\'' +
                ", tenant='" + tenant + '\'' +
                ", deleted='" + deleted + '\'' +
                ", version='" + version + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", dateCreate='" + dateCreate + '\'' +
                ", dateUpdate='" + dateUpdate + '\'' +
                ", selectStatus=" + selectStatus +
                ", time=" + time +
                ", size=" + size +
                '}';
    }
}
