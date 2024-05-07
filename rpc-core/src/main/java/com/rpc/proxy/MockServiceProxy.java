package com.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *  TODO:完善 Mock 的逻辑，支持更多返回类型的默认值生成!
 *      参考思路:使用 Faker 之类的伪造数据生成库，来生成默认值。
 **/
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("MOCK invoke {}", method.getName());
        return getDefaultObject(returnType);
    }

    /**
     * 根据代理接口的class返回不同的默认值
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type){
        //基本类型
        if(type.isPrimitive()){
            if(type == boolean.class){
                return false;
            }
            else if(type == short.class){
                return (short)0;
            }
            else if(type == int.class){
                return(int) 0;
            }
            else if(type == long.class){
                return 0L;
            }
        }
        return null;
    }
}
