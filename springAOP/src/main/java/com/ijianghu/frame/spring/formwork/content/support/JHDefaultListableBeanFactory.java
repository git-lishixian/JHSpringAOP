package com.ijianghu.frame.spring.formwork.content.support;

import com.ijianghu.frame.spring.formwork.beans.JHBeanDefinition;
import com.ijianghu.frame.spring.formwork.core.JHBeanFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kai on
 * @date 2018/5/25 0:09
 */
public class JHDefaultListableBeanFactory extends JHAbstractApplicationContext  {


//    new ConcurrentHashMap<String,JHBeanDefinition>

    //beanDefinitionMap用来保存配置信息

    private Map<String,JHBeanDefinition> beanDefinitionMap = new HashMap<String, JHBeanDefinition>();

    @Override
    protected void onRefresh() {
        super.onRefresh();
    }
    protected void refreshBeanFactory() {

    }
}
