package debug;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fenda.leavemessage.R;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

public class MainActivity extends Activity  {

    private EditText ed;
    // public static final String TOKEN ="AzDJwMVt2nRAZOFVsKTbKq+YsUIoF3ojin3K277sfOlvDwWFPGN4jClKhWxl7gE0N9pVf/zbHE5GfOaV2jK4tatdpZUyLdaH";
    //悟空  18673668974
    private static final String token1 = "ltIQGd2PMHtQNquy9FauVzp/dPyTEiStB6FwuVPqjyR5TkWat++/Q25vRZTFYmoEb1VTznHv/JTQ50r8fSDdA1UwGxWcxjW+";
    //贝吉塔 18673668975
    private static final String token2 = "G/qH1cxU/Hw8pz3twKeJJ2nHe5cSzxRbkjgC90ByirqNkqwMSCymPL2Fya7fgoQXOfVinsxpjAO5svmVLWlRnw==";
    //希特 18673668976
    private static final String token3 = "AlVE6YaATpAFTZy31SUtzFQqrKMgifCALlpd4GquzNtBgztK0k5Y5rkNZQDEAEsdCDVIkoazt1Ud/ku2O9etLORMCknkewf2";
    //海马客服 18673668977
    private static final String token4 = "vZFFeKnyF6add3wwV4N6JFQqrKMgifCALlpd4GquzNtBgztK0k5Y5ueWghQVNO4AXDyypJgOTEwd/ku2O9etLLdG2sJN6loq";

    private String string = "";
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String packageName = this.getPackageName();
        log("----------->" + packageName);
        initView();
        connectRongServer(token2);
        initData();
//haha
//hoho
//hchc
    }



    @Override
    protected void onDestroy() {
        RongIM.getInstance().disconnect();
        super.onDestroy();
    }

    private void initData() {
        findViewById(R.id.btn_chat_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //启动会话界面
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                    RongIM.getInstance().startPrivateChat(MainActivity.this, "18332767375", "留言");
                }
            }
        });

        findViewById(R.id.btn_chatlist_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRongServer(token2);
                //启动会话列表
                RongIM.getInstance().startConversation(MainActivity.this, Conversation.ConversationType.PRIVATE, "159773958223", "");
            }
        });
        findViewById(R.id.btn_createGroup_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRongServer(token1);
                //创建群聊
                final List<String> testList = new ArrayList<String>();
                testList.add("18673668975");
                testList.add("18673668976");
                testList.add("18673668977");
                RongIM.getInstance().createDiscussion("123", testList, new RongIMClient.CreateDiscussionCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        ed = (EditText) findViewById(R.id.ed_id_main);
                        ed.setText(s);
                        roomId = s;


                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        //  Toast.makeText(MainActivity.this,errorCode.getValue()+"",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        findViewById(R.id.btn_joingroup_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRongServer(token1);
                RongIM.getInstance().startDiscussionChat(MainActivity.this, ed.getText().toString(), "456");
            }
        });


        //添加讨论组成员
        findViewById(R.id.btn_addgroup_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRongServer(token1);
                final ArrayList<String> userIds = new ArrayList<String>();
                userIds.add("10");//增加 userId。


                RongIM.getInstance().addMemberToDiscussion(roomId, userIds, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(MainActivity.this, errorCode.getValue() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //移除讨论组成员
        findViewById(R.id.btn_delgroup_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RongIM.getInstance().getRongIMClient().removeMemberFromDiscussion("bac45e36-3ef3-45b5-9047-dd25054c27ae", "10", new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(MainActivity.this, errorCode.getValue() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //发送消息
        findViewById(R.id.btn_sendmessage_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 构造 TextMessage 实例
                TextMessage myTextMessage = TextMessage.obtain("发送消息");
                /* 生成 Message 对象。
                 * "7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
                 * Conversation.ConversationType.PRIVATE 为私聊会话类型，根据需要，也可以传入其它会话类型，如群组，讨论组等。
                 */
                Message myMessage = Message.obtain("18332767375", Conversation.ConversationType.PRIVATE, myTextMessage);

                RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        //消息本地数据库存储成功的回调
                        Toast.makeText(MainActivity.this, "本地数据库存储成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Message message) {
                        //消息通过网络发送成功的回调
                        Toast.makeText(MainActivity.this, "网络发送成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        //消息发送失败的回调
                        Toast.makeText(MainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initView() {

    }

    private void connectRongServer(String token1) {
        String str1= "5mfF3df8TuD9AitlCumV7c8DXz56D//Dqf6XntLmLhdl6rPnq3v13OhVw3Nkc4/tuHgRHDdOOnj8FdLkXxoRoZ9JdYds3ZTG\n";
        String str2 = "EhgsrWiVKbCpQ3fMm7uPas8DXz56D//Dqf6XntLmLhdl6rPnq3v13Gmm0VjBcCgziqatGsNlPQUXWPtguHhvumke6RLFBXCw";

        RongIM.connect(str2, new RongIMClient.ConnectCallback() {
            //token1参数报错
            @Override
            public void onTokenIncorrect() {
                Log.e("TAG", "参数错误");
                Toast.makeText(MainActivity.this, "token1参数报错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s) {
                Log.e("TAG", "成功");
                Toast.makeText(MainActivity.this, "连接成功 ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("TAG", "失败1111");
                Log.e("TAG", errorCode.getMessage());

            }
        });
    }



    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void log(String text) {
        Log.e("TAG->Result", text);
    }











}
