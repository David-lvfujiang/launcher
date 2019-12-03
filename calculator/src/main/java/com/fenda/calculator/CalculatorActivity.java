package com.fenda.calculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;

@Route(path = RouterPath.Calculator.CALCULATOR_ACTIVITY)
public class CalculatorActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 输入和输出共用框
     */
    private TextView tvIo;
    /**
     * 表达式显示框
     */
    private TextView tvExpressionOutput;

    private LinearLayout layout;
    // 用于计算
    private StringBuilder mStringBuilder = new StringBuilder();
    //用于显示
    private StringBuilder mExpression = new StringBuilder();
    private CountDownTimer timer;


    @Override
    public int onBindLayout() {
        return R.layout.activity_calculator;
    }

    @Override
    public void initView() {
        initTitle("计算器");
        Intent mIntent = getIntent();
        String answer = mIntent.getStringExtra("answer");
        String exp = mIntent.getStringExtra("exp");



        tvIo = findViewById(R.id.input_outputView);
        layout = findViewById(R.id.lin_layout);
        tvExpressionOutput = findViewById(R.id.expressionOutputView);
        tvIo.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvExpressionOutput.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!TextUtils.isEmpty(answer) && !TextUtils.isEmpty(exp)){
            tvExpressionOutput.setText(exp + " = ");
            tvIo.setText(answer);
            countDownTime();
        }
        //数字0-9
        findViewById(R.id.calculator_bt0).setOnClickListener(this);
        findViewById(R.id.calculator_bt1).setOnClickListener(this);
        findViewById(R.id.calculator_bt2).setOnClickListener(this);
        findViewById(R.id.calculator_bt3).setOnClickListener(this);
        findViewById(R.id.calculator_bt4).setOnClickListener(this);
        findViewById(R.id.calculator_bt5).setOnClickListener(this);
        findViewById(R.id.calculator_bt6).setOnClickListener(this);
        findViewById(R.id.calculator_bt7).setOnClickListener(this);
        findViewById(R.id.calculator_bt8).setOnClickListener(this);
        findViewById(R.id.calculator_bt9).setOnClickListener(this);
        //小数点
        findViewById(R.id.calculator_bt_dot).setOnClickListener(this);
        //等于号
        findViewById(R.id.calculator_bt_equal).setOnClickListener(this);
        //加法
        findViewById(R.id.calculator_bt_plus).setOnClickListener(this);
        //减法
        findViewById(R.id.calculator_bt_sub).setOnClickListener(this);
        //乘法
        findViewById(R.id.calculator_bt_mul).setOnClickListener(this);
        //除法
        findViewById(R.id.calculator_bt_div).setOnClickListener(this);
        //左小括号
        findViewById(R.id.calculator_bt_lpar).setOnClickListener(this);
        //右小括号
        findViewById(R.id.calculator_bt_rpar).setOnClickListener(this);
        //del
        findViewById(R.id.calculator_bt_del).setOnClickListener(this);
        //clear键
        findViewById(R.id.calculator_bt_clr).setOnClickListener(this);
        //退出
//        findViewById(R.id.calculator_iv_back).setOnClickListener(this);
    }

    @Override
    public void initData() {
    }
    private void focusable(){
        tvIo.post(new Runnable() {
            @Override
            public void run() {
                int scrollAmount = tvIo.getLayout().getLineTop(tvIo.getLineCount())
                        - tvIo.getHeight();
                if (scrollAmount > 0) {
                    tvIo.scrollTo(0, scrollAmount);
                } else {
                    tvIo.scrollTo(0, 0);
                }
            }
        });
    }

    //负数标记
    private int count_negative = 0;
    //结果为负数标记
    private boolean equals_negative = false;
    private boolean equals = false;
    private int count_bracket_left = 0;
    private int count_bracket_right = 0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String answer = intent.getStringExtra("answer");
        String exp = intent.getStringExtra("exp");
        if (!TextUtils.isEmpty(answer) && !TextUtils.isEmpty(exp)){
            tvExpressionOutput.setText(exp + " = ");
            tvIo.setText(answer);
            closeTimer();
            countDownTime();
        }

    }


    private void countDownTime() {
        if (timer ==  null){
            timer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    CalculatorActivity.this.finish();
                }
            };
            timer.start();
        }
    }

    private void closeTimer(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.calculator_bt0) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            mStringBuilder = mStringBuilder.append("0");
            mExpression = mExpression.append("0");
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt1) {
            if (equals) {
                //当equals为true，输入数字，清空字符串，在把标志变为false
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*1");
                mExpression = mExpression.append("×1");
            } else {
                mStringBuilder = mStringBuilder.append("1");
                mExpression = mExpression.append("1");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt2) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*2");
                mExpression = mExpression.append("×2");

            } else {
                mStringBuilder = mStringBuilder.append("2");
                mExpression = mExpression.append("2");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt3) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*3");
                mExpression = mExpression.append("×3");
            } else {
                mStringBuilder = mStringBuilder.append("3");
                mExpression = mExpression.append("3");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt4) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*4");
                mExpression = mExpression.append("×4");
            } else {
                mStringBuilder = mStringBuilder.append("4");
                mExpression = mExpression.append("4");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt5) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*5");
                mExpression = mExpression.append("×5");
            } else {
                mStringBuilder = mStringBuilder.append("5");
                mExpression = mExpression.append("5");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt6) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*6");
                mExpression = mExpression.append("×6");
            } else {
                mStringBuilder = mStringBuilder.append("6");
                mExpression = mExpression.append("6");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt7) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*7");
                mExpression = mExpression.append("×7");
            } else {
                mStringBuilder = mStringBuilder.append("7");
                mExpression = mExpression.append("7");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt8) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*8");
                mExpression = mExpression.append("×8");
            } else {
                mStringBuilder = mStringBuilder.append("8");
                mExpression = mExpression.append("8");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt9) {
            if (equals) {
                mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                mExpression = mExpression.delete(0, mExpression.length());
                equals = false;
            }
            if (mStringBuilder.length() > 0 && mStringBuilder.charAt(mStringBuilder.length() - 1) == ')') {
                mStringBuilder = mStringBuilder.append("*9");
                mExpression = mExpression.append("×9");
            } else {
                mStringBuilder = mStringBuilder.append("9");
                mExpression = mExpression.append("9");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_del) {
            //删除
            if (equals) {
                equals = false;
            }
            if (mStringBuilder.length() != 0) {
                mStringBuilder = mStringBuilder.deleteCharAt(mStringBuilder.length() - 1);
                mExpression = mExpression.deleteCharAt(mExpression.length() - 1);
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_clr) {
            //清空
            if (equals) {
                equals = false;
            }
            mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
            mExpression = mExpression.delete(0, mExpression.length());
            tvIo.setText("0");
            focusable();
            tvExpressionOutput.setText("");
        } else if (id == R.id.calculator_bt_lpar) {
            //左括号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            if (mStringBuilder.length() > 0 && (mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9')) { //当前面是数字是，自动添加为'*('
                mStringBuilder = mStringBuilder.append("*(");
                mExpression = mExpression.append("×(");
            }
            if (mStringBuilder.length() == 0) {
                //如果此时字符串是空的，也就是说想在式子最前面添加括号，就添加括号
                mStringBuilder = mStringBuilder.append("(");
                mExpression = mExpression.append("(");
            }
            if (mStringBuilder.length() > 0 && (mStringBuilder.charAt(mStringBuilder.length() - 1) == '*' || mStringBuilder.charAt(mStringBuilder.length() - 1) == '/' || mStringBuilder.charAt(mStringBuilder.length() - 1) == '+' || mStringBuilder.charAt(mStringBuilder.length() - 1) == '-')) { //如果当括号前面是符号时添加括号
                mStringBuilder = mStringBuilder.append("(");
                mExpression = mExpression.append("(");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_rpar) {
            //右括号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            //数字的数目
            int count_num = 0;
            count_bracket_left = count_bracket_right = 0;
            if (mStringBuilder.length() != 0) {
                for (int i = mStringBuilder.length() - 1; i >= 0; i--) {
                    //对字符串进行遍历，如果存在左括号且括号中有数字，标记转为真,
                    if (count_bracket_left == 0 && (mStringBuilder.charAt(i) >= '0' && mStringBuilder.charAt(i) <= '9')) {
                        count_num++;
                    }
                    if (mStringBuilder.charAt(i) == '(') {
                        count_bracket_left++;
                    }
                    if (mStringBuilder.charAt(i) == ')') {
                        count_bracket_right++;
                    }
                }
                Log.d("count_bracket", String.valueOf(count_bracket_left + " " + count_bracket_right));
                if ((count_bracket_left > count_bracket_right) && count_num > 0) {
                    //当标记均为真，也就是存在左括号时且在左括号前面有数字，才让添加括号
                    mStringBuilder = mStringBuilder.append(")");
                    mExpression = mExpression.append(")");
                }
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_div) {
            //除号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            if (mStringBuilder.length() != 0) {
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9') || mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                    if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9')) {
                        //如果前一位是数字，就直接添加
                        // if (count_negative > 0){ //如果前面是负数，就加上括号
                        //     mStringBuilder = mStringBuilder.append(")/");
                        //     count_negative = 0;
                        // } else {
                        mStringBuilder = mStringBuilder.append("/");
                        mExpression = mExpression.append("÷");
                        // }
                    }
                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                        //如果前一位是'.',就先为前一位数字补0
                        mStringBuilder = mStringBuilder.append("0/");
                        mExpression = mExpression.append("0÷");
                    }
                }
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) == ')')) {
                    //如果前一位是')'也让加上/
                    mStringBuilder = mStringBuilder.append("/");
                    mExpression = mExpression.append("÷");
                }
            } else{
                mStringBuilder = mStringBuilder.append("0/");
                mExpression = mExpression.append("0÷");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_mul) {
            //乘号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            if (mStringBuilder.length() != 0) {
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9') || mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                    if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9')) {
                        //如果前一位是数字，就直接添加
                        mStringBuilder = mStringBuilder.append("*");
                        mExpression = mExpression.append("×");
                    }
                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                        //如果前一位是'.',就先为前一位数字补0
                        mStringBuilder = mStringBuilder.append("0*");
                        mExpression = mExpression.append("0×");
                    }
                }
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) == ')')) {
                    mStringBuilder = mStringBuilder.append("*");
                    mExpression = mExpression.append("×");
                }
            }
            else{
                mStringBuilder = mStringBuilder.append("0*");
                mExpression = mExpression.append("0×");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_sub) {
            // 减号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            if (mStringBuilder.length() != 0) {
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9') || mStringBuilder.charAt(mStringBuilder.length() - 1) == '.' ){
//                        || mStringBuilder.charAt(mStringBuilder.length() - 1) == '(') {
                    //如果前一位是数字，就直接添加
                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9') {
                        mStringBuilder = mStringBuilder.append("-");
                        mExpression = mExpression.append("-");
                    }
                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                        //如果前一位是'.',就先为前一位数字补0
                        mStringBuilder = mStringBuilder.append("0.");
                        mExpression = mExpression.append("0.");
                    }
//                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) == '(') {
//                        mStringBuilder = mStringBuilder.append("-");
//                        mExpression = mExpression.append("-");
//                        count_negative++;
//                    }

                }
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) == ')')) {
                    mStringBuilder = mStringBuilder.append("-");
                    mExpression = mExpression.append("-");
                }
//                else {
//                    mStringBuilder = mStringBuilder.append("(-");
//                    mExpression = mExpression.append("(-");
//                }

            } else{
                mStringBuilder = mStringBuilder.append("0-");
                mExpression = mExpression.append("0-");
            }
//            else { //负号
//                mStringBuilder = mStringBuilder.append("(-");
//                mExpression = mExpression.append("(-");
//                count_negative++;
//            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_plus) {
            //加号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            if (mStringBuilder.length() != 0) {
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9') || mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                    if ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9')) {
                        //如果前一位是数字，就直接添加
                        // if (count_negative > 0) { //如果前面是负数，就加上括号
                        //     mStringBuilder = mStringBuilder.append(")+");
                        //     count_negative = 0;
                        // } else {
                        mStringBuilder = mStringBuilder.append("+");
                        mExpression = mExpression.append("+");
                        // }
                    }
                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) == '.') {
                        //如果前一位是'.',就先为前一位数字补0
                        mStringBuilder = mStringBuilder.append("0+");
                        mExpression = mExpression.append("0+");
                    }

                }
                if ((mStringBuilder.charAt(mStringBuilder.length() - 1) == ')')) {
                    mStringBuilder = mStringBuilder.append("+");
                    mExpression = mExpression.append("+");
                }
            } else{
                mStringBuilder = mStringBuilder.append("0+");
                mExpression = mExpression.append("0+");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_dot) {
            //小数点
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                }
            }
            if (mStringBuilder.length() != 0) {
                int count_dot = 0;
                for (int i = mStringBuilder.length() - 1; i >= 0; i--) {
                    if (mStringBuilder.charAt(i) == '.') {
                        count_dot++;
                    }
                    if (!(mStringBuilder.charAt(i) >= '0' && mStringBuilder.charAt(i) <= '9')) {
                        break;
                    }
                }
                if (count_dot == 0) {
                    if (mStringBuilder.charAt(mStringBuilder.length() - 1) == '*' || mStringBuilder.charAt(mStringBuilder.length() - 1) == '/' || mStringBuilder.charAt(mStringBuilder.length() - 1) == '+' || mStringBuilder.charAt(mStringBuilder.length() - 1) == '-') {
                        // 如果最后一位是符号时，直接输小数点会自动补'0'，形成'0.'
                        mStringBuilder = mStringBuilder.append("0.");
                        mExpression = mExpression.append("0.");
                    } else {
                        mStringBuilder = mStringBuilder.append(".");
                        mExpression = mExpression.append(".");
                    }
                }
            } else{
                mStringBuilder = mStringBuilder.append("0.");
                mExpression = mExpression.append("0.");
            }
            tvIo.setText(mExpression.toString());
            focusable();
        } else if (id == R.id.calculator_bt_equal) {
            //等号
            if (equals) {
                equals = false;
                if (equals_negative) {
                    equals_negative = false;
                    mStringBuilder = mStringBuilder.insert(0, "(");
                    mExpression = mExpression.insert(0, "(");
                    mStringBuilder = mStringBuilder.append(")");
                    mExpression = mExpression.append(")");
                }
            }
            count_bracket_right = count_bracket_left = 0;
            if (mStringBuilder.length() != 0) {
                for (int i = 0; i < mStringBuilder.length(); i++) {
                    if (mStringBuilder.charAt(i) == '(') {
                        count_bracket_left++;
                    }
                    if (mStringBuilder.charAt(i) == ')') {
                        count_bracket_right++;
                    }
                }
                if (count_bracket_left != count_bracket_right) {
                    ToastUtils.show( "请注意括号匹配");
                }
                if (count_bracket_left == count_bracket_right &&
                        ((mStringBuilder.charAt(mStringBuilder.length() - 1) >= '0' && mStringBuilder.charAt(mStringBuilder.length() - 1) <= '9') || mStringBuilder.charAt(mStringBuilder.length() - 1) == ')')) {
                    equals = true;
                    count_negative = 0;
                    try {

                        String str = InfixToSuffix.Cal(InfixToSuffix.Suffix(mStringBuilder));
                        int equals = 0;
                        tvIo.setText(str);
                        focusable();
                        tvExpressionOutput.setText(mExpression.toString() + "=");
                        mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                        mExpression = mExpression.delete(0, mExpression.length());
                        try {
                            equals = Integer.valueOf(str).intValue();
                            if (equals < 0) {
                                equals_negative = true;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        mStringBuilder.append(str);
                        mExpression.append(str);
                    } catch (Exception e) {
                        tvIo.setText("Error");
                        focusable();
                        mStringBuilder = mStringBuilder.delete(0, mStringBuilder.length());
                        mExpression = mExpression.delete(0, mExpression.length());
                    }
                }
            }
        }

        closeTimer();
    }

}