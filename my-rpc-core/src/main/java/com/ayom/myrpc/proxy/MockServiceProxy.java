package com.ayom.myrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock服务代理
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> type = method.getReturnType();
        log.info("mock invoke{}",method.getName());
        return getDefaultObject(type);
    }

    private Object getDefaultObject(Class<?> type) {
        //基本类型赋值
        if(type.isPrimitive()){
            if(type == boolean.class){
                return false;
            }else if (type == short.class){
                return(short) 0;
            } else if (type == int.class) {
                return 0;
            }else if(type == long.class){
                return 0L;
            }
        }
        //对象类型
        return null;
    }
}
