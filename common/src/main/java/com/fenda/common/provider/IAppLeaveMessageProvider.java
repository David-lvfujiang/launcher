package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/24
 * @Describe:
 */
public interface IAppLeaveMessageProvider extends IProvider {
    public void initRongIMlistener();
    public void openConversationListActivity();

}
