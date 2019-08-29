package com.fenda.common.baserx;

import com.fenda.common.base.BaseActivity;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 11:03
 * @Description
 */
public class RxSchedulers {


    public static <T> ObservableTransformer<T, T> io_main(final LifecycleProvider provider) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(provider.<T>bindUntilEvent(ActivityEvent.DESTROY));

            }
        };
    }
}
