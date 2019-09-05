package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 业务处理接口
 */
public interface IEncyclopediaProvider extends IProvider {

    public void geTextMsg(String msg);
    public void geSharesMsg(String msg);

}
