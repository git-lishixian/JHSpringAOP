package com.ijianghu.frame.spring.formwork.content.support;

import com.ijianghu.frame.spring.formwork.Annotation.JHAutowired;
import com.ijianghu.frame.spring.formwork.Annotation.JHController;
import com.ijianghu.frame.spring.formwork.Annotation.JHService;
import com.ijianghu.frame.spring.formwork.beans.JHBeanDefinition;
import com.ijianghu.frame.spring.formwork.beans.JHBeanPostProcessor;
import com.ijianghu.frame.spring.formwork.beans.JHBeanWrapper;
import com.ijianghu.frame.spring.formwork.core.JHBeanFactory;
import com.ijianghu.frame.spring.formwork.demo.mvc.action.DemoAction;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kai on
 * @date 2018/5/25 0:04
 */
public class JHApplicationContext extends  JHAbstractApplicationContext implements JHBeanFactory {

    private String[] configLocations;

    //定义一个reader加载器
    private JHBeanDefinitionReader reader;

    //beanDefinitionMapp用来保存配置信息的
    private Map<String, JHBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, JHBeanDefinition>();

    private Map<String, Object> beanCacheMap = new HashMap<String, Object>();

    //beanWrapperMap用来存储所有被代理的对象
    private Map<String, JHBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, JHBeanWrapper>();

    //构造方法
    public JHApplicationContext(String... locations) {
        this.configLocations = locations;
        this.refresh();
    }


    //通过读取BeanDefinition中的信息
    //然后，通过反射机制创建一个实例并返回
    //Sprign浊法是，不会吧最原始的对象放出去，会用一个BeanWrappr来进行一次包装
    //装饰器模式：
    //1、保留原来的OOP关系
    //2、对他进行扩展，增强（为了以后AOP打基础）
    @Override
    public Object getBean(String beanName) {

        JHBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();

        //生成通知事件
        JHBeanPostProcessor beanPostProcessor = new JHBeanPostProcessor();

        Object instantce = instantionBean(beanDefinition);
        if(null == instantce){return null;}

        //在实例初始化以前调用一次
        beanPostProcessor.postProcessBeforeInitialization(instantce,beanName);

        JHBeanWrapper beanWrapper = new JHBeanWrapper(instantce);
        this.beanWrapperMap.put(beanName,beanWrapper);

        //在实例初始化以后调用一次
        beanPostProcessor.postProcessAfterInitialization(instantce,beanName);

        //populateBean(beanName,instantce);
        //通过这样调用一次，相当于给我们留下了操作的空间
        return this.beanWrapperMap.get(beanName).getWrapperInstance();
    }

    //传一个BeanDefinition,返回一个实例Bean
    private Object instantionBean(JHBeanDefinition beanDefinition) {

        Object instance = null;
        String className = beanDefinition.getBeanClassName();

        try {


            //因为根据Class才能确定一个类是否有实例
            if (this.beanCacheMap.containsKey(className)) {
                instance = beanCacheMap.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.beanCacheMap.put(className, instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void refresh() {

        //定位
        this.reader = new JHBeanDefinitionReader(configLocations);

        //加载
        List<String> beanDefinitions = reader.loadBeanDefinitions();

        //注册
        doRegisty(beanDefinitions);

        //依赖注入（lazy-init = false）,要是执行依赖注入
        //在这里自动调用getBean方法
        
        doAutowired();

        DemoAction demoAction = (DemoAction) getBean("demoAction");
        demoAction.main(null,null,"kai");


    }

    //开始执行自动化的依赖注入
    private void doAutowired() {

        for (Map.Entry<String,JHBeanDefinition> beanDefinitionEntry: this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();

            if(!beanDefinitionEntry.getValue().getLazyInit()){
                getBean(beanName);
            }
        }

        for (Map.Entry<String,JHBeanWrapper> beanWrapperEntry:this.beanWrapperMap.entrySet()) {
            populateBean(beanWrapperEntry.getKey(),beanWrapperEntry.getValue().getWrapperInstance());

        }
    }

    public void populateBean(String beanName,Object instance){

        Class<?> clazz = instance.getClass();

        if(!(clazz.isAnnotationPresent(JHController.class))||
                (clazz.isAnnotationPresent(JHService.class))){
            return;
        }

        //如果是的话，字段全部读出来
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            if(!field.isAnnotationPresent(JHAutowired.class)){continue;}

            JHAutowired autowired = field.getAnnotation(JHAutowired.class);

            String autowiredBeanName = autowired.value().trim();

            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);

           try {

                field.set(instance,this.beanWrapperMap.get(autowiredBeanName).getWrapperInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //真正的将BeanDefinition注册到beanDefinitionMap中
    private void doRegisty(List<String> beanDefinitions) {

        try {
            for (String className : beanDefinitions) {
                Class<?> beanClass = Class.forName(className);
                //beanClass
                //用它实现类来实例化
                if (beanClass.isInterface()) {
                    continue;
                }

                JHBeanDefinition beanDefinition = reader.rigisterBean(className);
                if (beanDefinition != null) {
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
                }

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //或自定义名字
                    //或报错
                    this.beanDefinitionMap.put(i.getName(), beanDefinition);

                }


                //到这里容器的初始化完毕

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] getBeanDefinitionNames(){

        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){

        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }


    //实现AbstractApplicationContext的方法
    @Override
    protected void refreshBeanFactory() {

    }
}
