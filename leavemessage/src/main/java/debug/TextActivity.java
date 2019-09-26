package debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;


import com.fenda.leavemessage.LeaveMessageService;
import com.fenda.leavemessage.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


public class TextActivity extends FragmentActivity implements View.OnClickListener {
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leavemessage_test);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            LeaveMessageService service = new LeaveMessageService();
            service.initRongIMlistener();
              RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, "15977395823", "15977395823");
        }
    }
}
