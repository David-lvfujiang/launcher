package com.fenda.encyclopedia.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fenda.encyclopedia.R;
import com.fenda.encyclopedia.presenter.EncyclopediaFragmentPensenter;

public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "{\"recordId\":\"6b8342486c2b4e7ea84a70a0a0f27258\",\"skillId\":\"2019031900001251\",\"requestId\":\"6b8342486c2b4e7ea84a70a0a0f27258\",\"nlu\":{\"skillId\":\"2019031900001251\",\"res\":\"5d148ef54c2b63000d49a532\",\"input\":\"我想知道腾讯股票现在什么价格\",\"inittime\":29.18896484375,\"loadtime\":33.68896484375,\"skill\":\"股票\",\"skillVersion\":\"11\",\"source\":\"aidui\",\"systime\":113.27001953125,\"semantics\":{\"request\":{\"slots\":[{\"pos\":[4,5],\"rawvalue\":\"腾讯\",\"name\":\"名称\",\"rawpinyin\":\"teng xun\",\"value\":\"腾讯控股\"},{\"name\":\"intent\",\"value\":\"查询股票\"},{\"pos\":[12,13],\"rawvalue\":\"价格\",\"name\":\"技术指标\",\"rawpinyin\":\"jia ge\",\"value\":\"股价\"}],\"task\":\"股票\",\"confidence\":1,\"slotcount\":3,\"rules\":{\"股票199\":{\"对象\":[\"股票\",6,7]},\"股票15\":{\"名称\":[\"腾讯控股\",4,5],\"intent\":[\"查询股票\",-1,-1]}}}},\"version\":\"2019.1.15.20:40:58\",\"timestamp\":1567577344},\"dm\":{\"input\":\"我想知道腾讯股票现在什么价格\",\"shouldEndSession\":true,\"task\":\"股票\",\"widget\":{\"widgetName\":\"default\",\"duiWidget\":\"text\",\"extra\":{\"date\":\"2019-09-04 13:47:04\",\"symbol\":\"0700.HK\",\"amount\":2674756800,\"np\":3626638,\"code\":\"0700\",\"change\":6.4,\"absChange\":6.4,\"absPercentage\":1.94,\"lastClose\":329.2,\"volume\":7963660,\"tradeTime\":[\"09:30-12:00\",\"13:00-16:00\"],\"current\":335.6,\"high\":338,\"avg\":335.87,\"low\":333,\"percentage\":1.94,\"name\":\"腾讯控股\",\"wp\":3793167,\"exchange\":\"HK\",\"is_found\":1,\"state\":\"\",\"chart\":{\"list\":{}},\"open\":333.2},\"count\":1,\"name\":\"default\",\"currentPage\":1,\"type\":\"text\",\"content\":[{\"date\":\"2019-09-04 13:47:04\",\"symbol\":\"0700.HK\",\"amount\":2674756800,\"np\":3626638,\"code\":\"0700\",\"change\":6.4,\"absChange\":6.4,\"absPercentage\":1.94,\"lastClose\":329.2,\"volume\":7963660,\"tradeTime\":[\"09:30-12:00\",\"13:00-16:00\"],\"current\":335.6,\"high\":338,\"avg\":335.87,\"low\":333,\"percentage\":1.94,\"name\":\"腾讯控股\",\"wp\":3793167,\"exchange\":\"HK\",\"is_found\":1,\"state\":\"\",\"chart\":{\"list\":{}},\"open\":333.2}],\"errorcode\":0},\"intentName\":\"查询股票\",\"runSequence\":\"nlgFirst\",\"nlg\":\"当前腾讯控股报335.6点，上涨6.4点，涨幅1.94%\",\"intentId\":\"5d148ef54c2b63000d49a546\",\"speak\":{\"text\":\"当前腾讯控股报335.6点，上涨6.4点，涨幅1.94%\",\"type\":\"text\"},\"command\":{\"api\":\"\"},\"taskId\":\"5d148ef54c2b63000d49a532\",\"status\":1},\"contextId\":\"cae0b55301484d458bf7dbbfe6c74ae3\",\"sessionId\":\"cae0b55301484d458bf7dbbfe6c74ae3\"}";
                EncyclopediaFragmentPensenter pensenTer = new EncyclopediaFragmentPensenter();
                pensenTer.processSharesMsg(json);
                // pensenTer.geTextMsg("");
            }
        });
    }
}
