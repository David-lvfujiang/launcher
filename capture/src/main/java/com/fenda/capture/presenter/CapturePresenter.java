package com.fenda.capture.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.capture.constant.CaptureContant;
import com.fenda.capture.http.CaptureApiService;
import com.fenda.capture.request.CaptureMessageRequest;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.provider.ICaptureUserActionProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.protocol.http.RxSchedulers;

import io.reactivex.functions.Consumer;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/31
 * @Describe:
 */
@Route(path = "/capture/MyCaptureProvider")
public class CapturePresenter implements ICaptureUserActionProvider {
    @Override
    public void captureUserAction(String name) {
        Log.e("TAG1", "onHandleIntent: ");
        CaptureMessageRequest captureRequest = new CaptureMessageRequest("天气","首页","0");
        RetrofitHelper.getInstance(CaptureContant.TEST_BASE_URL).getServer(CaptureApiService.class).commitUserAction("天气","首页","0")
                .compose(RxSchedulers.<BaseResponse>applySchedulers())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.getCode() == 200 || ("操作成功").equals(response.getMessage())) {  //注册成功
                            Log.e("TAG1",  "操作成功");
                        }  else {   //注册失败
                            Log.e("TAG1",  response.getMessage());
                            Log.e("TAG1",  response.getCode()+"失败");

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG1",  throwable.getMessage()+"异常");

                        // 异常处理
                    }
                });
    }

    @Override
    public void init(Context context) {

    }
}


