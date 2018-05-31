package com.ijianghu.frame.spring.formwork.beans;

import com.ijianghu.frame.spring.formwork.aop.JHAopProxy;
import com.ijianghu.frame.spring.formwork.core.JHFactoryBean;

/**
 * @author kai on
 * @date 2018/5/27 12:34
 */
public class JHBeanWrapper extends JHFactoryBean{

    private JHAopProxy aopProxy = new JHAopProxy();

    //还会用到 观察者 模式
    //1、支持事件响应，还有一个监听
    private JHBeanPostProcessor beanPostProcessor;
    private Object wrapperInstance;

   //原始的通过反射new出来，要把包装起来，存下来
    private Object originalInstance;

    public JHBeanWrapper(Object instance){

        //从这里开始，我们要把动态的代码添加进来
        Object proxy = aopProxy.getProxy(instance);
        this.wrapperInstance = proxy;
        this.originalInstance = instance;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    //返回代理以后的Class
    //k可能会是这个$Proxy@###
    public Object getOriginalInstance() {
        return originalInstance;
    }

    public JHBeanPostProcessor getBeanPostProcessor() {
        return beanPostProcessor;
    }

    public void setBeanPostProcessor(JHBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessor = beanPostProcessor;
    }
}
