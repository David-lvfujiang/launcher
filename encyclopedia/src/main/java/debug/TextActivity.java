package debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fenda.encyclopedia.R;
import com.fenda.encyclopedia.presenter.EncyclopediaFragmentPensenter;


public class TextActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
     button = findViewById(R.id.button);
     button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            EncyclopediaFragmentPensenter pensenter = new EncyclopediaFragmentPensenter();

            String baike ="{\n" +
                    "\t\"skillId\": \"2018050400000027\",\n" +
                    "\t\"contextId\": \"25b32734b5cf78d7c307a69c0d05be1d\",\n" +
                    "\t\"speakUrl\": \"https:\\/\\/dds.dui.ai\\/runtime\\/v1\\/cache\\/011693a1b4222bfd956a69fd1c33356a?productId=278584846\",\n" +
                    "\t\"error\": {\n" +
                    "\t\t\"errMsg\": \"ba skill respond ok.\",\n" +
                    "\t\t\"errId\": \"010507\"\n" +
                    "\t},\n" +
                    "\t\"dm\": {\n" +
                    "\t\t\"custom\": {\n" +
                    "\t\t\t\"intent\": {\n" +
                    "\t\t\t\t\"type\": \"\",\n" +
                    "\t\t\t\t\"sub_type\": \"\"\n" +
                    "\t\t\t}\n" +
                    "\t\t},\n" +
                    "\t\t\"inspire\": {},\n" +
                    "\t\t\"nlg\": \"玫瑰（学名：RosarugosaThunb.）：原产地中国。属蔷薇目，蔷薇科落叶灌木，枝杆多针刺，奇数羽状复叶，小叶5-9片，椭圆形，有边刺。花瓣倒卵形，重瓣至半重瓣，花有紫红色、白色，果期8-9月，扁球形。枝条较为柔弱软垂且多密刺，每年花期只有一次，因此较少用于育种，近来其主要被重视的特性为抗病性与耐寒性。玫瑰作为经济作物时，其花朵主要用于食品及提炼香精玫瑰油，玫瑰油应用于化妆品、食品、精细化工等工业。在欧洲诸语言中，蔷薇、玫瑰、月季都是使用同一个词，如英语是rose，德语是DieRose。玫瑰是英国的国花。通俗意义中的“玫瑰”已成为多种蔷薇属植物的通称。且事实上杂交玫瑰也是由蔷薇属下各物种杂交选育所产生。此内容请看“玫瑰花”一词。\",\n" +
                    "\t\t\"widget\": {\n" +
                    "\t\t\t\"type\": \"text\",\n" +
                    "\t\t\t\"text\": \"玫瑰（学名：RosarugosaThunb.）：原产地中国原产地中国。属蔷薇目原产地中国。属蔷薇目原产地中国原产地中国原产地中国。属蔷薇目原产地中国。属蔷薇目原产地中国。属蔷薇目原产地中国。属蔷薇目，蔷薇科落叶灌木原产地中国。属蔷薇目，蔷薇科落叶灌木，枝杆多针刺，奇数羽状复叶，小叶5-9片，椭圆形，有边刺。\"\n" +
                    "\t\t},\n" +
                    "\t\t\"input\": \"玫瑰是什么\",\n" +
                    "\t\t\"status\": 0,\n" +
                    "\t\t\"shouldEndSession\": false\n" +
                    "\t},\n" +
                    "\t\"recordId\": \"011693a1b4222bfd956a69fd1c33356a\",\n" +
                    "\t\"sessionId\": \"25b32734b5cf78d7c307a69c0d05be1d\",\n" +
                    "\t\"requestId\": \"011693a1b4222bfd956a69fd1c33356a\",\n" +
                    "\t\"skill\": \"百科\",\n" +
                    "\t\"nlu\": {\n" +
                    "\t\t\"timestamp\": 1569399205,\n" +
                    "\t\t\"skillId\": \"2018050400000027\",\n" +
                    "\t\t\"baSemantic\": {\n" +
                    "\t\t\t\"answerEntity\": [{\n" +
                    "\t\t\t\t\"type\": \"\",\n" +
                    "\t\t\t\t\"name\": \"\"\n" +
                    "\t\t\t}],\n" +
                    "\t\t\t\"skillId\": \"2018050400000027\",\n" +
                    "\t\t\t\"skill\": \"\",\n" +
                    "\t\t\t\"resultType\": \"insideBaike\",\n" +
                    "\t\t\t\"confidence\": {\n" +
                    "\t\t\t\t\"level\": \"a\",\n" +
                    "\t\t\t\t\"score\": \"1.0\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"virtualSkillId\": \"2018050400000027\",\n" +
                    "\t\t\t\"queryEntity\": [{\n" +
                    "\t\t\t\t\"type\": \"其他\",\n" +
                    "\t\t\t\t\"name\": \"玫瑰\"\n" +
                    "\t\t\t}],\n" +
                    "\t\t\t\"context\": {\n" +
                    "\t\t\t\t\"first_turn\": \"true\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"intent\": {\n" +
                    "\t\t\t\t\"type\": \"\",\n" +
                    "\t\t\t\t\"sub_type\": \"\"\n" +
                    "\t\t\t}\n" +
                    "\t\t},\n" +
                    "\t\t\"skill\": \"百科\",\n" +
                    "\t\t\"input\": \"玫瑰是什么\"\n" +
                    "\t}\n" +
                    "}";
            pensenter.processQuestionTextMsg(baike);
        }
    }
}
