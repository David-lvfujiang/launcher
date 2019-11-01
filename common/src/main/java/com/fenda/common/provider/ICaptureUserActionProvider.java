package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/31
 * @Describe:
 */
public interface ICaptureUserActionProvider extends IProvider {
   public void captureUserAction(String name);
}
