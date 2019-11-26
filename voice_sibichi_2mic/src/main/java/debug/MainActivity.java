package debug;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.fenda.ai.R;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.service.DDSService;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.util.ImageUtil;
import com.fenda.protocol.tcp.bean.EventMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/9/2 8:13
  * @Description
  *
  */
public class MainActivity extends BaseActivity {


    public static final int PERMISSION_REQ = 0x123456;

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;


    private String[] mPermission = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
    };

    private String [] models;

    private List<String> mRequestPermission = new ArrayList<String>();


    @Override
    public int onBindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (!hasPermission()) {
//                startActivityForResult(
//                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
//                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
//            }
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
            }else {
                init();

            }
        }


    }

    @Override
    public void initData() {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                Toast.makeText(this,"未能开启部分App权限！",Toast.LENGTH_SHORT).show();
            }else {

            }
        }
    }
    /**
     * 拷贝assets下的文件的方法
     */
    private void assetsDataToSD(String assertDir, String fileName,
                                String modelDir) throws IOException {

        String outputPath = modelDir + File.separator + fileName;
        File mFile = new File(outputPath);
        if (!mFile.exists()){
            mFile.createNewFile();
        }

        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(outputPath);

        String assertFilePath = assertDir + File.separator + fileName;
        myInput = this.getAssets().open(assertFilePath);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    private void init(){

        try {
            models = this.getAssets().list("voice");
            File mFile = new File(VoiceConstant.sVoiceDir);
            if (!mFile.exists()){
                mFile.mkdir();
            }
            for (int i = 0; i < models.length; i++) {
                assetsDataToSD("voice",models[i], VoiceConstant.sVoiceDir);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        startService(new Intent(this, DDSService.class));
    }


    @SuppressLint("NewApi")
    private void moveTaskToFront() {
        ActivityManager mAm = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.AppTask> taskList = mAm.getAppTasks();
        for (ActivityManager.AppTask appTask : taskList) {
            if (VoiceConstant.MUSIC_PKG.equals(appTask.getTaskInfo().topActivity.getPackageName())){
                mAm.moveTaskToFront(appTask.getTaskInfo().id,0);
                return;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
            init();
        }
    }



}
