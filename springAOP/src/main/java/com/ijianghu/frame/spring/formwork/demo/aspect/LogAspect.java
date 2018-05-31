package com.ijianghu.frame.spring.formwork.demo.aspect;

/**
 * @author kai on
 * @date 2018/5/31 6:55
 */
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(){
        //方法中的逻辑我们自己编辑
        System.out.println("方法调用前");
    }

    //在调用一个方法之后，执行after方法
    public void after(){
        System.out.println("方法调用之后");
    }
}
