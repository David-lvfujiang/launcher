package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 业务处理接口
 */
public interface IEncyclopediaProvider extends IProvider {

    public void processQuestionTextMsg(String msg);
    public void processSharesMsg(String msg);
    public void processHistoryTodayMsg(String msg);
    public void processIdiomTextMsg(String msg);
    public void processConstellationTextMsg(String msg);


}
