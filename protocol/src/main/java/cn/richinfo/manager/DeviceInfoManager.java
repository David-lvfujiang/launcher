package cn.richinfo.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/10/12 17:00
 */
public class DeviceInfoManager {
    private static DeviceInfoManager sInstance;
    private static Context mContext;

    /**
     * 返回值说明：对部分设备不支持的功能属性，返回unSupport
     * 设备支持但当前没有值，则返回unKnown
     */
    private String UNSUPPORT = "unSupport";
    private String UNKNOWN = "unKnown";

    public DeviceInfoManager(){

    }


    /**
     * longitude: 默认值: 0x1.fffffffffffffP+1023
     *            说明: 当前手机所在longitude，取值范围：-180 —— 180，东经为正
     * latitude: 默认值: 0x1.fffffffffffffP+1023
     *           说明: 当前手机所在latitude，取值范围：-90 —— 90，北纬为正
     * locationMode: 默认值: 1
     *               说明: 获取位置信息的来源
     *                      0: 不支持定位
     *                      1：GPS，
     *                      2：北斗
     *                      3: 伽利略
     *                      4：格洛纳斯
     *                      5：基站定位
     *                      6：WIFI定位
     *                      12：gps和北斗协同
     */
    private class LocationMsg{
        public double longitude;
        public double latitude;
        public int locationMode;
    }


    /**
     * 获取设备的ROM容量大小
     * 返回值：返回设备的存储总容量（ROM）大小，与工信部登记终端ROM信息一致
     */
    public String getRomStorageSize (){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        //long usedBlocks=totalBlocks-availableBlocks;

        String totalSize = Formatter.formatFileSize(mContext, totalBlocks*blockSize);
        String availableSize = Formatter.formatFileSize(mContext, availableBlocks*blockSize);

        return availableSize+"/"+totalSize;
    }

    /**
     * 函数说明：获取设备的RAM容量大小
     * 返回值：返回设备的内存总容量（RAM）大小，与工信部登记争端RAM信息一致
     */
    public String getRamStorageSize (){
        RandomAccessFile reader = null;
        String load = null;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }
        return load;
    }

    /**
     * 获取设备的WLAN MAC地址
     * 返回值：返回设备的WLAN MAC地址
     */
    public String getMacAddress (){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    /**
     * 获取设备的AP型号
     * 返回值：返回设备的应用处理器（AP）型号。
     * 备注：举例说明，比如小米8手机，这里返回骁龙845（SDM845）
     */
    public String getCPUModel (){
        return android.os.Build.CPU_ABI;
    }
    /**
     * 获取设备的网络类型
     * 返回值：返回的网络类型分wifi，4g,3g，2g这四种
     */
    public String getNetworkType (){
        return "wifi";
    }
    /**
     * 获取设备的操作系统版本号
     * 返回值：返回设备的操作系统版本号，指Android或YunOS大版本
     */
    public String getOSVersion (){
        return getProperty("ro.product.version", "unknown");
    }
    /**
     * 获取设备当前固件版本号
     * 	返回值：设备当前固件版本号，非基带版本或内核版本及Android版本
     */
    public String getDeviceSoftwareVersion(){
        return getVerName(mContext);
    }
    /**
     * 固件版本名称，非基带版本或内核版本及Android版本。
     * 备注：举例说明一下，如果是小米手机，这里返回MIUI。如果是华为手机，这里是指EMUI
     */
    public String getDeviceSoftwareName(){
        return UNKNOWN;
    }
    /**
     * volte开关状态，0返回开，1返回关。
     * 备注：不支持volte的设备返回-1
     */
    public int getVoLTEState(){
        return -1;
    }
    /**
     * 获取设备当前宽带账号
     * 	返回值：宽带账号，不存在则返回unKnown
     */
    public String getNetAccount(){
        return UNKNOWN;
    }
    /**
     * 获取设备当前手机号码
     * 	返回值：手机号码，不存在则返回unKnown
     */
    public String getPhoneNumber(){
        return UNKNOWN;
    }
    /**
     * 发起定位请求，并缓存定位到的位置信息备接口 getLocation() 来查询
     */
    public static void startLocation(){

    }
    /**
     * 获取接口 startLocation() 定位后缓存的位置信息，LocationMsg 为 DeviceInfoManager内部类，
     * 包含三个public的成员变量：longitude、latitude、locationMode,,变量含义及取值范围参见：4.1.1.3内部类
     * 返回包含经纬度信息的内部类实例
     */
//    public LocationMsg getLocation (){
//
//    }
    /**
     * 获取该类的一个实例，需要做成单例模式（single intance）
     * 返回值：该类的一个实例
     */
    public static DeviceInfoManager getInstance (Context context){
        return sInstance;
    }

    //获取系统属性
    public static  String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String)(get.invoke(c, key, "unknown" ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *获取版本号名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try{
            verName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        }catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return verName;
    }

}
