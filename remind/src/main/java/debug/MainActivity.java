package debug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fenda.common.constant.Constant;
import com.fenda.remind.AlarmActivity;
import com.fenda.remind.AlarmListActivity;
import com.fenda.remind.R;
import com.fenda.remind.bean.AlarmBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/29 16:25
  * @Description 
  *
  */
public class MainActivity extends AppCompatActivity {

//    [{"date":"20190830","id":131,"object":"闹钟","operation":"设置","period":"下午","recent_tsp":1567155600,"time":"17:00:00","timestamp":1567155600,"vid":"7596346627167086667"}]
    private TextView tvTest;
    private TextView tvTest2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tv_test);
        tvTest2 = findViewById(R.id.textView);

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final ArrayList<AlarmBean> mList = new ArrayList<>();
                for (int i = 0; i < 5; i ++){
                    AlarmBean bean = new AlarmBean();
                    bean.setDate("20190830");
                    bean.setId(131);
                    bean.setObject("闹钟");
                    bean.setOperation("设置");
                    bean.setPeriod("下午");
                    bean.setRecent_tsp(1567155600);
                    bean.setTime("1"+i+":00:00");
                    bean.setTimestamp(1567155600);
                    bean.setVid("7596346627167086667");

                    mList.add(bean);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                                    Bundle mBundle = new Bundle();
                                    mBundle.putString("alarmType", Constant.Remind.CREATE_REMIND);
                                    mBundle.putParcelableArrayList("alarmList",mList);
                                    intent.putExtras(mBundle);
                                    startActivity(intent);
                                }
                            });




                        }
                    }).start();


                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mList.clear();

                }



            }
        });

        tvTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlarmListActivity.class);
                startActivity(intent);
            }
        });
    }
}
