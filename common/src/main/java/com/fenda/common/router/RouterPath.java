package com.fenda.common.router;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/27 16:01
 * @Description Arouter框架的注解的path 路径 统一在这个类里管理
 */
public interface RouterPath {
    /**
     * 都用这种方式 Voice表示自己的组件名称
     */
    interface PLAYER{
        String MusiceActivity = "/player/MusicActivity";
        String FDMusicListActivity = "/player/FDMusicListActivity";
    }


    public interface Gallery {
        String GALLERY_CATOGORY = "/gallery/GalleryCategoryActivity";
        String GALLERY_LIST_LOCAL = "/gallery/PhotosActivity";
        String GALLERY_LIST_SERVER = "/gallery/FamilyGalleryActivity";
        String GALLERY_DETAIL = "/gallery/PhotoDetailActivity";
    }

    public interface Call {
        String CALL_SERVICE = "/call/CallService";
        String MAIN_ACTIVITY = "/call/MainActivity";
    }
    interface SETTINGS {
        String FDSettingsActivity = "/settings/FDSettingsActivity";
        String FDSettingsVolumeActivity = "/settings/FDSettingsVolumeActivity";
        String FDSetttingsBrightnessActivity = "/settings/FDSetttingsBrightnessActivity";
        String FDSettingsWifiActivity = "/settings/FDSettingsWifiActivity";
        String FDSettingsWifiInputPswActivity = "/settings/FDSettingsWifiInputPswActivity";
        String FDSettingsWifiConnectedInfoActivity = "/settings/FDSettingsWifiConnectedInfoActivity";
        String FDSettingsDeviceInfoActivity = "/settings/FDSettingsDeviceInfoActivity";
        String FDSettingsBluetoothActivity = "/settings/FDSettingsBluetoothActivity";

    }
}
