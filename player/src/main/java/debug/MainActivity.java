package debug;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.constant.Constant;
import com.fenda.common.util.ToastUtils;
import com.fenda.player.MusicActivity;
import com.fenda.player.R;

import java.util.ArrayList;
import java.util.List;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/29 10:31
  * @Description
  *
  */
public class MainActivity extends AppCompatActivity {

    private TextView tvTest;
    private boolean hasRecordPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tv_test);

        hasRecordPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;

        if (!hasRecordPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, 1);
        }

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayBean bean = new MusicPlayBean();
                List<FDMusic> musicList = new ArrayList<>();
                for (int i = 0 ; i < 10 ; i ++){
                    FDMusic music = new FDMusic();
                    music.setMusicArtist("李白");
                    music.setMusicUri("http://oss.iot.aispeech.com/dcmp/CBF3532970CF927A663B7C1E1ECAF2BE.mp3");
                    music.setMusicImage("http://oss.iot.aispeech.com/dcmp/5566C6925BE7F17659754D9DD742CF55.png");
                    music.setMusicTitle("送友人");
                    music.setContent("水国蒹葭夜有霜，月寒山色共苍苍。谁言千里自今夕，离梦杳如关塞长。水国蒹葭夜有霜，月寒山色共苍苍。谁言千里自今夕，离梦杳如关塞长。水国蒹葭夜有霜，月寒山色共苍苍。谁言千里自今夕，离梦杳如关塞长。水国蒹葭夜有霜，月寒山色共苍苍。谁言千里自今夕，离梦杳如关塞长。水国蒹葭夜有霜，月寒山色共苍苍。谁言千里自今夕，离梦杳如关塞长。水国蒹葭夜有霜，月寒山色共苍苍。谁言千里自今夕，离梦杳如关塞长。");
                    musicList.add(music);
                }
                bean.setFdMusics(musicList);
                bean.setMsgType(Constant.Player.POETRY);


                Intent mIntent = new Intent(MainActivity.this, MusicActivity.class);
                Bundle mBundel = new Bundle();
                mBundel.putParcelable(Constant.Player.keyDataMusicKey,bean);
                mIntent.putExtras(mBundel);
                startActivity(mIntent);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (1 == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.show("请同意权限");
            }
        }
    }






}
