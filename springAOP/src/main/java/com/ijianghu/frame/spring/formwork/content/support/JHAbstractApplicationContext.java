package com.ijianghu.frame.spring.formwork.content.support;

/**
 * @author kai on
 * @date 2018/5/30 0:48
 */
public abstract  class JHAbstractApplicationContext {

    //提供给子类重写
    protected void onRefresh(){

    }

    protected abstract void refreshBeanFactory();

}
