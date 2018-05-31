package com.ijianghu.frame.spring.formwork.demo.mvc.action;



import com.ijianghu.frame.spring.formwork.Annotation.JHAutowired;
import com.ijianghu.frame.spring.formwork.Annotation.JHController;
import com.ijianghu.frame.spring.formwork.Annotation.JHRequestMapping;
import com.ijianghu.frame.spring.formwork.Annotation.JHRequestParam;
import com.ijianghu.frame.spring.formwork.demo.service.IDemoService;
import com.ijianghu.frame.spring.formwork.webmvc.servlet.JHModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author kai on
 * @date 2018/5/24 22:23
 */
@JHController
@JHRequestMapping("/demo")
public class DemoAction {

    @JHAutowired
    private IDemoService demoService;

    @JHRequestMapping("/main.shtml")
    public void main(HttpServletRequest req, HttpServletResponse resp,
                     @JHRequestParam("name") String name){
        System.out.println(name+"开始调用service方法");
        demoService.getNames();
    }

    @JHRequestMapping("/query.shtml")
    public JHModelAndView query(HttpServletRequest req, HttpServletResponse resp,
                                @JHRequestParam("name") String name){
        demoService.getNames();
        HashMap<String, String> model = new HashMap<String, String>();
        model.put("name",name);
        return new JHModelAndView("list.html",model);
    }

}
