package com.ijianghu.frame.spring.formwork.demo.service.impl;


import com.ijianghu.frame.spring.formwork.Annotation.JHService;
import com.ijianghu.frame.spring.formwork.demo.service.IDemoService;

/**
 * @author kai on
 * @date 2018/5/24 22:25
 */
@JHService
public class DemoServiceImpl implements IDemoService {
    @Override
    public void getNames() {
        System.out.println("调用了service方法");
    }
}
