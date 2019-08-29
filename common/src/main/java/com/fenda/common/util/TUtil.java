package com.fenda.common.util;

import java.lang.reflect.ParameterizedType;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 14:07
  * @Description 
  *
  */
public class TUtil {

    /**
     * 获取泛型对象
     * @param o
     * @param index
     * @param <T>
     * @return
     */
    public static <T> T getT(Object o,int index){
        try {
            return ((Class<T>) ((ParameterizedType)o.getClass().getGenericSuperclass()).getActualTypeArguments()[index]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }


}
