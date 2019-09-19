package debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fenda.weather.R;
import com.fenda.weather.WeatherHelper;


public class TextActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
     button = findViewById(R.id.button);
     button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            String json = "{\n" +
                    "\t\"widgetName\": \"weather\",\n" +
                    "\t\"type\": \"custom\",\n" +
                    "\t\"extra\": {\n" +
                    "\t\t\"nlg_message\": \"深圳今天全天多云转晴，气温23~32℃，有北风1级\"\n" +
                    "\t},\n" +
                    "\t\"webhookResp\": {\n" +
                    "\t\t\"extra\": {\n" +
                    "\t\t\t\"future\": [{\n" +
                    "\t\t\t\t\"week\": \"周四\",\n" +
                    "\t\t\t\t\"temperature\": \"23~32℃\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-19\",\n" +
                    "\t\t\t\t\"weather\": \"多云转晴\",\n" +
                    "\t\t\t\t\"windLevel\": \"1\",\n" +
                    "\t\t\t\t\"wind\": \"北风\"\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"week\": \"周五\",\n" +
                    "\t\t\t\t\"temperature\": \"23~32℃\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-20\",\n" +
                    "\t\t\t\t\"weather\": \"晴\",\n" +
                    "\t\t\t\t\"windLevel\": \"1\",\n" +
                    "\t\t\t\t\"wind\": \"北风\"\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"week\": \"周六\",\n" +
                    "\t\t\t\t\"temperature\": \"24~32℃\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-21\",\n" +
                    "\t\t\t\t\"weather\": \"多云转晴\",\n" +
                    "\t\t\t\t\"windLevel\": \"1\",\n" +
                    "\t\t\t\t\"wind\": \"北风\"\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"week\": \"周日\",\n" +
                    "\t\t\t\t\"temperature\": \"25~30℃\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-22\",\n" +
                    "\t\t\t\t\"weather\": \"多云\",\n" +
                    "\t\t\t\t\"windLevel\": \"1~4\",\n" +
                    "\t\t\t\t\"wind\": \"东北风转北风\"\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"week\": \"周一\",\n" +
                    "\t\t\t\t\"temperature\": \"26~30℃\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-23\",\n" +
                    "\t\t\t\t\"weather\": \"多云转阵雨\",\n" +
                    "\t\t\t\t\"windLevel\": \"1\",\n" +
                    "\t\t\t\t\"wind\": \"北风\"\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t\"week\": \"周二\",\n" +
                    "\t\t\t\t\"temperature\": \"26~30℃\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-24\",\n" +
                    "\t\t\t\t\"weather\": \"阵雨\",\n" +
                    "\t\t\t\t\"windLevel\": \"1\",\n" +
                    "\t\t\t\t\"wind\": \"北风\"\n" +
                    "\t\t\t}],\n" +
                    "\t\t\t\"Index\": {\n" +
                    "\t\t\t\t\"humidity\": \"61\",\n" +
                    "\t\t\t\t\"aqi\": {\n" +
                    "\t\t\t\t\t\"AQI\": \"53\",\n" +
                    "\t\t\t\t\t\"pm25\": \"50\",\n" +
                    "\t\t\t\t\t\"AQL\": \"良\",\n" +
                    "\t\t\t\t\t\"AQIdesc\": \"敏感人群可以适当减少出行。\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"liveIndex\": [{\n" +
                    "\t\t\t\t\t\"status\": \"较适宜\",\n" +
                    "\t\t\t\t\t\"name\": \"运动指数\",\n" +
                    "\t\t\t\t\t\"desc\": \"天气还不错，只是气温有点高，户外运动千万别中暑。\"\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"status\": \"较适宜\",\n" +
                    "\t\t\t\t\t\"name\": \"钓鱼指数\",\n" +
                    "\t\t\t\t\t\"desc\": \"气压小幅波动，可能会影响鱼儿的进食。\"\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"status\": \"中等\",\n" +
                    "\t\t\t\t\t\"name\": \"紫外线指数\",\n" +
                    "\t\t\t\t\t\"desc\": \"涂擦SPF大于15、PA+防晒护肤品。\"\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"status\": \"适宜\",\n" +
                    "\t\t\t\t\t\"name\": \"洗车指数\",\n" +
                    "\t\t\t\t\t\"desc\": \"洗车后，可至少保持3天车辆清洁，适宜洗车。\"\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"status\": \"闷热\",\n" +
                    "\t\t\t\t\t\"name\": \"穿衣指数\",\n" +
                    "\t\t\t\t\t\"desc\": \"潮湿闷热，衣物排汗透气，手帕擦汗环保时尚，不建议在露天场所逛街。\"\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"status\": \"易发\",\n" +
                    "\t\t\t\t\t\"name\": \"感冒指数\",\n" +
                    "\t\t\t\t\t\"desc\": \"感冒容易发生，少去人群密集的场所有利于降低感冒的几率。\"\n" +
                    "\t\t\t\t}]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"forecast\": [{\n" +
                    "\t\t\t\t\"wind\": \"北风\",\n" +
                    "\t\t\t\t\"date\": \"2019-09-19\",\n" +
                    "\t\t\t\t\"sunset\": \"18:24:00\",\n" +
                    "\t\t\t\t\"weather\": \"多云转晴\",\n" +
                    "\t\t\t\t\"highTemp\": \"32\",\n" +
                    "\t\t\t\t\"temperature\": \"23~32℃\",\n" +
                    "\t\t\t\t\"week\": \"周四\",\n" +
                    "\t\t\t\t\"sunrise\": \"06:12:00\",\n" +
                    "\t\t\t\t\"tempInteval\": \"9\",\n" +
                    "\t\t\t\t\"lowTemp\": \"23\",\n" +
                    "\t\t\t\t\"windLevel\": \"1\",\n" +
                    "\t\t\t\t\"tip\": \"有点热，记得多喝水\"\n" +
                    "\t\t\t}]\n" +
                    "\t\t},\n" +
                    "\t\t\"cityName\": \"深圳\",\n" +
                    "\t\t\"errorcode\": 0\n" +
                    "\t},\n" +
                    "\t\"cityName\": \"深圳\",\n" +
                    "\t\"name\": \"weather\",\n" +
                    "\t\"intentName\": \"查询天气\",\n" +
                    "\t\"taskName\": \"天气\",\n" +
                    "\t\"duiWidget\": \"custom\",\n" +
                    "\t\"skillName\": \"天气\",\n" +
                    "\t\"skillId\": \"2019042500000544\"\n" +
                    "}";
            WeatherHelper weatherHelper =  new WeatherHelper();
            weatherHelper.weatherFromVoiceControl(json);
        }
    }
}
