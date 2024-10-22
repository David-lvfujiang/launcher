package com.fenda.common.router;

import android.app.Activity;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/27 16:01
 * @Description Arouter框架的注解的path 路径 统一在这个类里管理
 */
public interface RouterPath {
    /**
     * 都用这种方式 Voice表示自己的组件名称
     */
    interface PLAYER {
        String MUSIC = "/player/MusicActivity";
        String MUSIC_LIST = "/player/FDMusicListActivity";
        String MUSIC_PROVIDER = "/player/PlayerProviderImpl";

    }

    /**
     * 提醒
     */
    interface REMIND {
        String ALARM = "/remind/AlarmActivity";
        String ALARM_LIST = "/remind/AlarmListActivity";
        String ALARM_SERVICE = "/remind/AlarmService";
        String ALARM_RUERY = "/remind/AlarmQuery";
    }

    interface COMMON {
        String JSON_SERVICE = "/common/JsonServiceImpl";
    }

    public interface Gallery {
        String GALLERY_CATOGORY = "/gallery/GalleryCategoryActivity";
        String GALLERY_LIST_LOCAL = "/gallery/PhotosActivity";
        String GALLERY_LIST_SERVER = "/gallery/FamilyGalleryActivity";
        String GALLERY_DETAIL = "/gallery/PhotoDetailActivity";
    }

    interface Call {
        String CALL_SERVICE = "/call/CallService";
        String MAIN_ACTIVITY = "/call/MainActivity";
    }

    interface Calendar {
        String CALENDAR_ACTIVITY = "/calendar/CalendarActivity";
        String CALENDAR_PROVIDER = "/calendar/CalendarPresenTer";
        String HOLIDAY_ACTIVITY = "/calendar/HolidayActivity";
        String Perpetual_CALENDAR_ACTIVITY = "/calendar/PerpetualCalendarActivity";
        String CALENDAR_QUERY_LASTDAY_ACTIVITY = "/calendar/CalendarQueryLastDayActivity";
    }

    interface Capture {
        String CAPTURE_ACTIVITY = "/capture/CaptureActivity";
        String CAPTURE_PROVIDER = "/capture/CaptureService";
        String CAPTURE_SERVICE = "/capture/CaptureService";
        String CAPTURE_MYPROVIDER = "/capture/MyCaptureProvider";
    }

    interface Encyclopedia {
        String ENCYCLOPEDIA_PROVIDER = "/encyclopedia/EncyclopediaFragmentPensenter";
        String ENCYCLOPEDIA_SHARES_ACTIVITY = "/encyclopedia/EncyclopediaSharesActivity";
        String ENCYCLOPEDIA_QUESTIION_ACTIVITY = "/encyclopedia/EncyclopediaQuestiionActivity";
    }

    interface Leavemessage {
        String LEAVEMESSAGE_SERVICE = "/leavemessage/LeaveMessageService";
        String LEAVEMESSAGE_DIALOG_ACTIVITY = "/leavemessage/LeavemessageDialogActivity";


    }

    interface SETTINGS {
        String SettingsService = "/settings/SettingsService";

        String SettingsActivity = "/settings/SettingsActivity";
        String SettingsVolumeActivity = "/settings/SettingsVolumeActivity";
        String SetttingsBrightnessActivity = "/settings/SetttingsBrightnessActivity";
        String SettingsWifiActivity = "/settings/SettingsWifiActivity";
        String SettingsWifiInputPswActivity = "/settings/SettingsWifiInputPswActivity";
        String SettingsWifiConnectedInfoActivity = "/settings/SettingsWifiConnectedInfoActivity";
        String SettingsDeviceInfoActivity = "/settings/SettingsDeviceInfoActivity";
        String SettingsBluetoothActivity = "/settings/SettingsBluetoothActivity";
        String SettingsChangeDeviceNameActivity = "/settings/SettingsChangeDeviceNameActivity";
        String SettingsDeviceCenterActivity = "/settings/SettingsDeviceCenterActivity";
        String SettingsDeviceContractsActivity = "/settings/SettingsDeviceContractsActivity";
        String SettingsBindDeviceActivity = "/settings/SettingsBindDeviceActivity";
        String SettingsLoadWebviewActivity = "/settings/SettingsLoadWebviewActivity";
        String SettingsContractsNickNameEditActivity = "/settings/SettingsContractsNickNameEditActivity";
        String SettingsDeviceAddContractsQRActivity = "/settings/SettingsDeviceAddContractsQRActivity";
        String SettingsDeviceContractsNickNameActivity = "/settings/SettingsDeviceContractsNickNameActivity";
    }

    interface VOICE {
        String DDSService = "/voice/DDSService";
        String INIT_PROVIDER = "/voice/initService";
        String REQUEST_PROVIDER = "/voice/requestService";

    }

    interface NEWS {
        String NEWS_PLAY = "/news/NewsPlay";
        String NEWS_ACTIVITY = "/news/NewsActivity";
    }


    interface HomePage {
        String HOMEPAGE_MAIN = "/homepage/HomePageActivity";
        String HOMEPAGE_WIFI = "/homepage/StartWifiConfigureActivity";
    }

    interface Weather {
        String WEATHER_MAIN = "/weather/WeatherActivity";
        String WEATHER_SERVICE = "/weather/WeatherService";
    }

    interface Recommend {
        String RECOMMEND = "/recommend/recommend";
    }

    interface Calculator {
        String CALCULATOR_ACTIVITY = "/calculator/calculator";
    }
}
