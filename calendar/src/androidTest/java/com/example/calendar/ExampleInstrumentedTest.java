package com.example.calendar;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.fenda.calendar.presenter.CalendarPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.calendar", appContext.getPackageName());
    }

    @Test
    public void testAdd() throws Exception {
        String json="{\"recordId\":\"1156e8febca04740b844af840bedd581\",\"skillId\":\"2019031800001161\",\"requestId\":\"1156e8febca04740b844af840bedd581\",\"nlu\":{\"skillId\":\"2019031800001161\",\"res\":\"5d637bc0d5fdd7000d0fc2db\",\"input\":\"今天星期几\",\"inittime\":16.837158203125,\"loadtime\":25.77587890625,\"skill\":\"日历\",\"skillVersion\":\"30\",\"source\":\"dui\",\"systime\":87.666748046875,\"semantics\":{\"request\":{\"slots\":[{\"pos\":[0,1],\"rawvalue\":\"今天\",\"name\":\"阳历日期\",\"rawpinyin\":\"jin tian\",\"value\":\"20190829\"},{\"name\":\"intent\",\"value\":\"查询星期\"},{\"name\":\"查询对象\",\"value\":\"星期\"}],\"task\":\"日历\",\"confidence\":1,\"slotcount\":3,\"rules\":{\"5d637bc0d5fdd7000d0fc3f3\":{\"阳历日期\":[\"20190829\",0,1],\"intent\":[\"查询星期\",-1,-1]},\"5d637bc0d5fdd7000d0fc3f1\":{\"查询对象\":[\"星期\",-1,-1]}}}},\"version\":\"2019.1.15.20:40:58\",\"timestamp\":1567063151},\"dm\":{\"input\":\"今天星期几\",\"shouldEndSession\":true,\"task\":\"日历\",\"widget\":{\"widgetName\":\"default\",\"duiWidget\":\"text\",\"extra\":{\"month\":8,\"nlyear\":\"己亥\",\"year\":2019,\"weekday\":\"星期四\",\"day\":29,\"nlday\":\"二十九\",\"nlmonth\":\"七月\"},\"name\":\"default\",\"text\":\"星期四\",\"type\":\"text\"},\"intentName\":\"查询星期\",\"runSequence\":\"nlgFirst\",\"nlg\":\"今天是星期四\",\"intentId\":\"5d637bc0d5fdd7000d0fc2f2\",\"speak\":{\"text\":\"今天是星期四\",\"type\":\"text\"},\"command\":{\"api\":\"\"},\"taskId\":\"5d637bc0d5fdd7000d0fc2db\",\"status\":1},\"contextId\":\"e6c083aa549d46dc8df26a32684cc6a5\",\"sessionId\":\"e6c083aa549d46dc8df26a32684cc6a5\"}";
        CalendarPresenter calculator = new CalendarPresenter();

    }

}
