package com.ijianghu.frame.spring.formwork.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author kai on
 * @date 2018/5/30 1:02
 */
//默认使用JDK动态代理
public class JHAopProxy implements InvocationHandler{

    private JHAopConfig config;
    private Object target;

    //把原生的对象传进来
    public Object getProxy(Object instance){
        this.target = instance;
        Class<?> clazz = instance.getClass();
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        return proxy;
    }

    public void setConfig(JHAopConfig config){
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //原始方法调用以前执行的方法
        if(config.contains(method)){
            JHAopConfig.JHAspect jhAspect = config.get(method);
            jhAspect.getPoints()[0].invoke(jhAspect.getAspect());
        }

        Object obj = method.invoke(this.target, args);

        //原始方法调用以前执行的方法
        if(config.contains(method)){
            JHAopConfig.JHAspect jhAspect = config.get(method);
            jhAspect.getPoints()[1].invoke(jhAspect.getAspect());
        }

        //最原始的返回值
        return obj;
    }
}
